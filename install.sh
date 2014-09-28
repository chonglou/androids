#/bin/sh

apk="./out/production/$1/$1.apk"
if [ -e $apk ]
then
	adb install -r $apk
	echo "Done!"
else
	echo "File $1.apk is not exists! "
fi
