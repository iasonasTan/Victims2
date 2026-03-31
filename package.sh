#!/bin/bash
set -e

echo "[NOTE] You have to run './gradlew run' before running this script."

echo "Removing last outputs..."
rm -rf Victims2/ || true
rm Victims2.zip  || true

echo "Packaging jar into appimage..."
jpackage --main-jar ./app/build/libs/app.jar \
	--input . \
	--main-class main.Main \
	--type app-image \
	--name Victims2

echo "Zipping generated files..."
zip -r Victims2.zip Victims2/ >> /dev/null

rm -rf Victims2/

echo "Done!"
