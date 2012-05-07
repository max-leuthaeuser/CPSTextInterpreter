mkdir dist
mkdir temp
cp builds/*.jar temp/
cp -r config temp/config
cp -r examples temp/examples
cp LICENSE temp/LICENSE
cp README.rst temp/README.rst
cp -r target/site temp/site

now=$(date +"%m_%d_%Y")
cd temp
tar cvfz ../dist/CPSTextInterpreter-$now.tar.gz *
cd ..
rm -rf temp