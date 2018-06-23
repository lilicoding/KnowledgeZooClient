#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import os
import sys
import argparse
import json
import ast
from subprocess import Popen, PIPE, TimeoutExpired

CMD = './runtools.sh'


def simplify(output):
    result = dict()

    def get_pk_info(line):
        name = None
        vcode = None
        vname = None
        parts = line.split(' ')
        for p in parts:
            pieces = p.split('=')
            if len(pieces) > 1:
                if pieces[0] == 'name':
                    name = pieces[1].strip('\'')
                elif pieces[0] == 'versionCode':
                    vcode = pieces[1].strip('\'')
                elif pieces[0] == 'versionName':
                    vname = pieces[1].strip('\'')
        return name, vcode, vname

    def get_boolean(line):
        value = None
        parts = line.split(':')
        if len(parts) > 1:
            val = parts[1].strip()
            if val.lower() == 'true':
                value = True
            elif val.lower() == 'false':
                value = False
        return value

    def get_package(line):
        raw = line.split(';')[0].split('$')[0].lstrip('L')
        parts = raw.split('/')
        if len(parts) > 3:
            return '.'.join(parts[:3])
        else:
            return '.'.join(parts)

    def remove_u(line):
        if not line.startswith('\w'):
            return None
        parts = line.split(' ', 1)
        if len(parts) == 1:
            return line
        elif len(parts) == 2:
            p1 = str(ast.literal_eval(parts[1]))
            return '{} {}'.format(parts[0], p1)

    # with open(file_name) as f:
    state = None
    for line in output.splitlines():
        # line = line.encode('utf-8', errors='ignore')
        # line = line.rstrip()
        if state is not None and not line.startswith('\t'):
            state = None
        if line.startswith('package:'):
            pk_name, pk_ver_code, pk_ver_name = get_pk_info(line)
            result.update(
                packageName=pk_name,
                versionCode=pk_ver_code,
                versionName=pk_ver_name
            )
        elif line.startswith('sdkVersion:'):
            parts = line.split(':')
            if len(parts) > 1:
                result.update(
                    sdkVersion=parts[1].strip('\'')
                )
        elif line.startswith('uses-permission:'):
            permissions = result.get('permissions', list())
            parts = line.split(':')
            if len(parts) > 1:
                pieces = parts[1].split('=')
                if len(pieces) > 1:
                    segments = pieces[1].split(' ', 1)
                    if len(segments) == 1:
                        permissions.append(pieces[1].strip('\''))
                    elif len(segments) == 2:
                        permissions.append(segments[0].strip('\''))
                    result.update(permissions=permissions)
        elif line.startswith('MAIN ACTIVITY:'):
            parts = line.split(':')
            if len(parts) > 1:
                result.update(
                    mainActivity=parts[1].strip()
                )
        elif line.startswith('ACTIVITIES:'):
            state = 'activities'
        elif line.startswith('SERVICES:'):
            state = 'services'
        elif line.startswith('RECEIVERS:'):
            state = 'receivers'
        elif line.startswith('\t') and state == 'activities':
            activities = result.get('activities', list())
            the_activity = remove_u(line.strip())
            if the_activity is not None:
                activities.append(the_activity)
            result.update(activities=activities)
        elif line.startswith('\t') and state == 'services':
            services = result.get('services', list())
            the_service = remove_u(line.strip())
            if the_service is not None:
                services.append(the_service)
            result.update(services=services)
        elif line.startswith('\t') and state == 'receivers':
            receivers = result.get('receivers', list())
            the_receiver = remove_u(line.strip())
            if the_receiver is not None:
                receivers.append(the_receiver)
            result.update(receivers=receivers)
        elif line.startswith('PROVIDERS:'):
            parts = line.split(':')
            if len(parts) > 1:
                providers = parts[1].strip().strip('[]')
                if providers is not '':
                    result.update(providers=providers)
        elif line.startswith('Native code:'):
            result.update(useNativeCode=get_boolean(line))
        elif line.startswith('Dynamic code:'):
            result.update(useDynamicCode=get_boolean(line))
        elif line.startswith('Reflection code:'):
            result.update(useReflection=get_boolean(line))
        elif line.startswith('Ascii Obfuscation:'):
            result.update(useObfuscation=get_boolean(line))
        elif line.startswith('L'):
            packages = result.get('packages', set())
            packages.add(get_package(line))
            result.update(packages=packages)
        elif line.startswith('Owner:'):
            parts = line.split(':')
            if len(parts) > 1:
                result.update(certOwner=parts[1].strip())
        elif line.startswith('Certificate fingerprints:'):
            state = 'fingerprints'
        elif line.startswith('\t') and state == 'fingerprints':
            item = line.lstrip()
            if item.startswith('SHA256:'):
                parts = item.split(':', 1)
                if len(parts) > 1:
                    result.update(certFingerprint=parts[1].strip())
    packages = result.get('packages')
    if packages is not None:
        result.update(packages=list(packages))
    return result


def run(apk, aapt, timeout):
    istimeout = False
    arg = [
        CMD,
        apk,
        aapt
    ]
    with Popen(arg, stdout=PIPE, stderr=PIPE) as p:
        try:
            out, err = p.communicate(timeout=timeout)
        except TimeoutExpired:
            istimeout = True
            p.kill()
            out, err = p.communicate()
    if istimeout:
        print('Process was killed because of timeout.' +
              'The result could be incomplete!')
    print(err.decode('utf-8', errors='ignore'), file=sys.stderr)
    return simplify(out.decode('utf-8', errors='ignore'))



def main():
    parser = argparse.ArgumentParser(description='Collect APK knowledge \
                                     and write to a file named "result"')
    parser.add_argument('apk', type=str,
                        help='path to the APK')
    parser.add_argument('aapt', type=str,
                        help='path to aapt excutable')
    parser.add_argument('-t', '--timeout', type=int, metavar='seconds',
                        default=None, help='max time to wait for the \
                        analysis. If not given, it will wait until \
                        the analysis finish.')
    parser.add_argument('-d', '--dir', type=str, default='./',
                        help='write the result file to the given directory. \
                        If not given, it will be written locally.')
    args = parser.parse_args()
    if not os.path.isfile(args.apk):
        sys.exit('APK file does not exist!')
    if not os.path.isfile(args.aapt) and os.access(args.aapt, os.X_OK):
        sys.exit('aapt tool are needed and required to be excutable!')
    if not os.path.isdir(args.dir):
        sys.exit('The givern directory does not exist!')
    result = run(args.apk, args.aapt, args.timeout)
    with open(os.path.join(args.dir, 'result'), 'w') as file:
        json.dump(result, file, indent=2)


if __name__ == '__main__':
    main()
