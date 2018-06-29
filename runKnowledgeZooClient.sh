#! /bin/sh

APK=$1

SHA256=`shasum -a 256 $APK | awk '{print $1}' | tr '[a-z]' '[A-Z]'`

echo "{" > $SHA256.json
echo "$SHA256:" >> $SHA256.json
./extract_metadata.py $APK >> $SHA256.json
echo "}" >> $SHA256.json

java -Xmx40G -jar KnowledgeZooClient.jar -csv $SHA256.json

rm -rf $SHA256.json
