#! /bin/sh
APK=$1
AAPT=$2

ANDROINFO="androguard/androapkinfo.py"

# run aapt badging
$AAPT dump badging $APK

# run androapkinfo.py
python2 $ANDROINFO -i $APK

# certificate
CERTS=`unzip -l $APK | grep META-INF | grep "\.RSA" | awk '{print $4}'`
for CERT in `echo $CERTS`;
do
    echo "====>"$CERT
    unzip -p $APK $CERT | keytool -printcert
done
