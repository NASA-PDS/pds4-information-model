#!/bin/bash
#
# Copyright 2025, California Institute of Technology ("Caltech").
# U.S. Government sponsorship acknowledged.
#
# All rights reserved.
#
# This script generates the core PDS4 Information Model artifacts
# and packages them into a ZIP file for release distribution.
#
# Usage: ./bin/generate-core-ldd.sh
#
# Prerequisites:
#   - Maven build must have completed successfully (mvn clean package)
#   - Must be run from the root directory of the repository

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "=================================================="
echo "  PDS4 Core LDD Artifact Generation"
echo "=================================================="
echo

# Step 1: Check that lddtool tar.gz exists
echo "Step 1: Checking for lddtool package..."
LDDTOOL_TAR=$(find model-lddtool/target -name "lddtool*.tar.gz" -type f 2>/dev/null | head -n 1)

if [ -z "$LDDTOOL_TAR" ]; then
    echo -e "${RED}ERROR: lddtool tar.gz not found in model-lddtool/target/${NC}"
    echo "Please run 'mvn clean package' first to build lddtool."
    exit 1
fi

echo -e "${GREEN}✓ Found: $LDDTOOL_TAR${NC}"
echo

# Step 2: Extract lddtool to temporary directory
echo "Step 2: Extracting lddtool..."
WORK_DIR=$(mktemp -d)
trap "rm -rf $WORK_DIR" EXIT  # Cleanup on exit

tar -xzf "$LDDTOOL_TAR" -C "$WORK_DIR"
LDDTOOL_DIR=$(find "$WORK_DIR" -type d -name "lddtool*" | head -n 1)

if [ ! -d "$LDDTOOL_DIR" ]; then
    echo -e "${RED}ERROR: Failed to extract lddtool${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Extracted to: $LDDTOOL_DIR${NC}"
echo

# Step 3: Run lddtool to generate core IM artifacts
echo "Step 3: Generating core Information Model artifacts..."
cd "$LDDTOOL_DIR"

# Run lddtool with flags:
# -p: PDS4 context
# -D: Data Dictionary DocBook file
# -J: JSON output
# -O: TTL output
# -o: RDF output
./bin/lddtool -p -D -J -O -o

if [ $? -ne 0 ]; then
    echo -e "${RED}ERROR: lddtool execution failed${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Core IM artifacts generated${NC}"
echo

# Step 4: Identify IM version from generated files
echo "Step 4: Identifying IM version..."
IM_VERSION=$(ls export/PDS4_PDS_*.xsd 2>/dev/null | head -n 1 | sed -n 's/.*PDS4_PDS_\([0-9A-Z]*\)\.xsd/\1/p')

if [ -z "$IM_VERSION" ]; then
    echo -e "${RED}ERROR: Could not determine IM version from generated files${NC}"
    exit 1
fi

echo -e "${GREEN}✓ IM Version: $IM_VERSION${NC}"
echo

# Step 5: Package artifacts into ZIP file
echo "Step 5: Packaging artifacts..."
ZIP_NAME="PDS4_PDS_${IM_VERSION}.zip"

# Check if required files exist
REQUIRED_FILES=(
    "index_${IM_VERSION}.html"
    "export/PDS4_PDS_${IM_VERSION}.csv"
    "export/PDS4_PDS_${IM_VERSION}.JSON"
    "export/PDS4_PDS_${IM_VERSION}.sch"
    "export/PDS4_PDS_${IM_VERSION}.xml"
    "export/PDS4_PDS_${IM_VERSION}.xsd"
    "export/PDS4_PDS_DD_${IM_VERSION}.xml"
    "export/PDS4_PDS_OWL_${IM_VERSION}_IKG.ttl"
    "export/PDS4_PDS_OWL_${IM_VERSION}.rdf"
    "export/PDS4_PDS_RDF_${IM_VERSION}.rdf"
)

echo "Checking for required files..."
MISSING_FILES=()
for file in "${REQUIRED_FILES[@]}"; do
    if [ ! -f "$file" ]; then
        MISSING_FILES+=("$file")
        echo -e "${YELLOW}  ⚠ Missing: $file${NC}"
    else
        echo -e "${GREEN}  ✓ Found: $file${NC}"
    fi
done

if [ ${#MISSING_FILES[@]} -gt 0 ]; then
    echo -e "${YELLOW}WARNING: ${#MISSING_FILES[@]} file(s) missing but continuing...${NC}"
fi

# Create ZIP with available files
zip -q "$ZIP_NAME" "index_${IM_VERSION}.html" 2>/dev/null || echo -e "${YELLOW}  ⚠ Skipping index_${IM_VERSION}.html${NC}"

for file in "${REQUIRED_FILES[@]}"; do
    if [[ $file == export/* ]] && [ -f "$file" ]; then
        zip -q -j "$ZIP_NAME" "$file"
    fi
done

if [ ! -f "$ZIP_NAME" ]; then
    echo -e "${RED}ERROR: Failed to create ZIP file${NC}"
    exit 1
fi

# Move ZIP to original working directory
cd - > /dev/null
mv "$LDDTOOL_DIR/$ZIP_NAME" .

echo -e "${GREEN}✓ Created: $ZIP_NAME${NC}"
echo

# Display summary
echo "=================================================="
echo "  Summary"
echo "=================================================="
echo "IM Version:    $IM_VERSION"
echo "ZIP File:      $ZIP_NAME"
echo "Location:      $(pwd)/$ZIP_NAME"
echo "Size:          $(du -h "$ZIP_NAME" | cut -f1)"
echo
echo -e "${GREEN}✓ Core LDD artifact generation complete!${NC}"
