# 🪐 NASA PDS4 Information Model Repo

[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.5755795.svg)](https://doi.org/10.5281/zenodo.5755795) [![🤪 Unstable integration & delivery](https://github.com/NASA-PDS/pds4-information-model/actions/workflows/unstable-cicd.yaml/badge.svg)](https://github.com/NASA-PDS/pds4-information-model/actions/workflows/unstable-cicd.yaml) [![😌 Stable integration & delivery](https://github.com/NASA-PDS/pds4-information-model/actions/workflows/stable-cicd.yaml/badge.svg)](https://github.com/NASA-PDS/pds4-information-model/actions/workflows/stable-cicd.yaml) 

The software tools and data necessary for generating the Information Model including PDS4 ontology, data, and information model.

Please visit our website at https://nasa-pds.github.io/pds4-information-model/ for information about the modules that comrpise the information model.


## 👥 Contributing

Within the NASA Planetary Data System, we value the health of our community as much as the code. Towards that end, we ask that you read and practice what's described in these documents:

-   Our [contributor's guide](https://github.com/NASA-PDS/.github/blob/main/CONTRIBUTING.md) delineates the kinds of contributions we accept.
-   Our [code of conduct](https://github.com/NASA-PDS/.github/blob/main/CODE_OF_CONDUCT.md) outlines the standards of behavior we practice and expect by everyone who participates with our software.


## 📀 Installation

Installation instructions are broken into sections for _operational_ and _development_ installations.


### Operational

For operational installation of LDDTool, see the deployed documentation for installation instructions: https://nasa-pds.github.io/pds4-information-model/model-lddtool/install/index.html


### Development
For development installation, use maven:

```console
$ # Package the software
$ mvn clean package

$ # Generate the site (must run package first)
$ mvn site:site site:stage

$ # To view generated docs
$ open target/staging/model/index.html
```

To update the site documentation, see each components `src/site` directory (this is common for all Java Maven software):
| Component | Site Directory | Online Documentation |
|-----------|----------------|----------------------|
| Top-level Page | `src/site` | https://nasa-pds.github.io/pds4-information-model/ |
| LDDTool | `model-lddtool/src/site` | https://nasa-pds.github.io/pds4-information-model/model-lddtool/ |
| DMDocument | `model-dmdocument/src/site` | https://nasa-pds.github.io/pds4-information-model/model-dmdocument/ |
| Ontology | `model-ontology/src/site` | https://nasa-pds.github.io/pds4-information-model/model-ontology/ |


## 💁‍♀️ Usage

See the deployed documentation for usage details for the various components: https://nasa-pds.github.io/pds4-information-model/


## 👥 Contributing

Please see the [contribution guidelines](https://github.com/NASA-PDS/.github/blob/main/CONTRIBUTING.md) but also be sure to see our [code of conduct](https://github.com/NASA-PDS/.github/blob/main/CODE_OF_CONDUCT.md).


### 🔢 Versioning

We use the [SemVer](https://semver.org/) philosophy for versioning this software.


## 📃 License

The project is licensed under the [Apache version 2](LICENSE.md) license.

