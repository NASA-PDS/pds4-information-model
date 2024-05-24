# Simple bash script to clone a couple dLDDs and run LDDTool
#
# NOTE: Requires execution from top-level of repo, and the
# pds4-information-model package has been built already

if [ ! -f model-lddtool/target/lddtool*.tar.gz ]; then
    echo "ERROR: LDDTool tar.gz does not exist. Please build LDDTool first."
    echo
    echo "       mvn package"
    exit 1
fi

# Cleanup
rm -fr ldd-repo/ lddtool-pkg/

# Unpack lddtool
mkdir lddtool-pkg
tar -xvzf model-lddtool/target/lddtool*.tar.gz -C lddtool-pkg/

echo "Test 1: ldd-template"
git clone https://github.com/pds-data-dictionaries/ldd-template.git ldd-repo/

# Run lddtool
lddtool-pkg/lddtool*/bin/lddtool -lpJ ldd-repo/src/*IngestLDD.xml

if [ $? -ne 0 ]; then
    echo "Test 1: FAILED"
    exit $?
fi
echo "Test 1: SUCCESS"

echo "Test 2: ldd-cart"
rm -fr ldd-repo
git clone https://github.com/pds-data-dictionaries/ldd-cart.git ldd-repo/

cd ldd-repo/; git submodule update --init --force --remote; cd ..

# Run lddtool
lddtool-pkg/lddtool*/bin/lddtool -lpJ ldd-repo/src/dependencies/ldd-*/src/*IngestLDD.xml ldd-repo/src/*IngestLDD.xml

if [ $? -ne 0 ]; then
    echo "Test 2: FAILED"
    exit $?
fi
echo "Test 2: SUCCESS"

echo "Test 3: ldd-nucspec (#771)"
# Run lddtool with downloaded data
lddtool-pkg/lddtool*/bin/lddtool -lpJ test/data/771/PDS4_NUCSPEC_IngestLDD.xml
diff PDS4_NUCSPEC_*_1100.sch test/data/771/expected/PDS4_NUCSPEC_1M00_1100.sch | egrep -v "PDS4 Schematron for Name Space Id:nucspec" | egrep -v "Generated from the PDS4 Information Model" > tmp.txt
lines=$(wc -l tmp.txt | awk '{print $1}')

if [ $lines -ne 2 ]; then
    echo "Test 3: FAILED"
    echo $lines
    exit $?
fi
echo "Test 3: SUCCESS"

# File count check
echo "Test 4: Check file counts from files generated"
count=$(ls -al PDS* | wc -l)
if [ $count -ne 18 ]; then
    echo "Test 3: FAILED"
    exit 1
fi
echo "Test 4: SUCCESS"

exit $?
