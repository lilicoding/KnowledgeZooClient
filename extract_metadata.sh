#! /bin/sh
APK=$1

ANDROINFO="androapkinfo.py"

# run aapt badging
aapt dump badging $APK

# run androapkinfo.py
python $ANDROINFO -i $APK

# certificate
CERTS=`unzip -l $APK | grep META-INF | grep "\.RSA" | awk '{print $4}'`
for CERT in `echo $CERTS`;
do
    unzip -p $APK $CERT | keytool -printcert
done
