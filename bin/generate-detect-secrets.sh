#!/bin/bash

detect-secrets scan . \
               --all-files \
               --disable-plugin AbsolutePathDetectorExperimental \
               --exclude-files '\.secrets..*' \
               --exclude-files '\.git.*' \
               --exclude-files '\.pre-commit-config\.yaml' \
               --exclude-files 'target' \
               --exclude-files 'docs/release/.*' \
               --exclude-files 'model-ontology/src/ontology/Data/.*' \
               --exclude-files 'model-dmdocument/src/main/java/gov/nasa/pds/model/plugin/.*' \
               --exclude-files 'docs/namespace-registry/.*' > .secrets.baseline
