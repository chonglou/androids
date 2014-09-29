#!/bin/sh

convert -resize 36x36 $1 $2/res/drawable-ldpi/ic_launcher.png
convert -resize 48x48 $1 $2/res/drawable-mdpi/ic_launcher.png
convert -resize 72x72 $1 $2/res/drawable-hdpi/ic_launcher.png
convert -resize 96x96 $1 $2/res/drawable-xhdpi/ic_launcher.png
