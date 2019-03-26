#!/bin/sh
# Copyright 2010-2018, by the California Institute of Technology. 
# ALL RIGHTS RESERVED. United States Government sponsorship acknowledged. 
# Any commercial use must be negotiated with the Office of Technology Transfer 
# at the California Institute of Technology. 
#
# This software is subject to U. S. export control laws and regulations 
# (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software 
# is subject to U.S. export control laws and regulations, the recipient has 
# the responsibility to obtain export licenses or other export authority as 
# may be required before exporting such information to foreign countries or 
# providing access to foreign nationals.
#
# $Id$

# This script traverses the module directories to build and deploy 
# the associated sites with the EN skin to a deployment directory.

if [ $# != 1 ] ; then
  echo "Usage $0 <deployment-directory>"
  exit 1
fi

cd ..
mvn clean

mvn --non-recursive install clean
cd model-dmdocument
mvn install clean
cd ..

# Build each site (recursive).
mvn --file pom-en.xml site

echo "Deploying Model component sites to the deployment directory."
mkdir -p $1/model
cp -r target/site/* $1/model
mkdir -p $1/model-dmdocument
cp -r model-dmdocument/target/site/* $1/model/model-dmdocument
mkdir -p $1/model/model-lddtool
cp -r model-lddtool/target/site/* $1/model/model-lddtool
mkdir -p $1/model/model-ontology
cp -r model-ontology/target/site/* $1/model/model-ontology