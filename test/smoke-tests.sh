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
lddtool-pkg/lddtool*/bin/lddtool -lpJ -V 1L00 ldd-repo/src/*IngestLDD.xml

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
lddtool-pkg/lddtool*/bin/lddtool -lpJ -V 1L00 ldd-repo/src/dependencies/ldd-*/src/*IngestLDD.xml ldd-repo/src/*IngestLDD.xml

if [ $? -ne 0 ]; then
    echo "Test 2: FAILED"
    exit $?
fi
echo "Test 2: SUCCESS"

# File count check
echo "Test 3: Check file counts from files generated"
count=$(ls -al PDS* | wc -l)
if [ $count -ne 12 ]; then
    echo "Test 3: FAILED"
    exit 1
fi
echo "Test 3: SUCCESS"

exit $?
