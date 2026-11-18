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

**Requirements:**
- OpenJDK 17 or higher

```console
$ # Package the software
$ mvn clean package

$ # Generate the site (must run package first)
$ mvn site:site site:stage

$ # To view generated docs
$ open target/staging/model/index.html
```

### Testing

Tests are ran using Cucumber and Maven.

#### Running Tests

```bash
# Package the software
mvn clean package

# Run Cucumber tests
mvn clean test
```

#### Adding New Tests

To add additional tests:

- Create a directory within the [resources](model-lddtool/src/test/resources) directory and place your test data there
- In either the [developer](model-lddtool/src/test/resources/features/developer.feature) or [integration](model-lddtool/src/test/resources/features/integration.feature) feature file, add your test case to the table under `Examples`. For example,

```feature
| testName | resourceDirectory    | testDirectory | commandArgs | expectedResponse |
| "NASA-PDS/pds4-information-model#738 LDDTool is not creating valid URL for KPLO" | "src/test/resources" | "github738"   | "-lpJ {resourceDirectory}/{testDirectory}/PDS4_KPLO_IngestLDD.xml" | "0 error(s)"     |
```

#### Update For New IM Version
See https://github.com/NASA-PDS/pds4-information-model/tree/main/bin#update-for-new-im

### Update Site Documentation

To update the site documentation, see each components `src/site` directory (this is common for all Java Maven software):
| Component | Site Directory | Online Documentation |
|-----------|----------------|----------------------|
| Top-level Page | `src/site` | https://nasa-pds.github.io/pds4-information-model/ |
| LDDTool | `model-lddtool/src/site` | https://nasa-pds.github.io/pds4-information-model/model-lddtool/ |
| DMDocument | `model-dmdocument/src/site` | https://nasa-pds.github.io/pds4-information-model/model-dmdocument/ |
| Ontology | `model-ontology/src/site` | https://nasa-pds.github.io/pds4-information-model/model-ontology/ |

### Detect Secrets

The Planetary Data System's Engineering Node uses [detect-secrets](https://github.com/Yelp/detect-secrets), specifically a version forked by the [SLIM Team](https://nasa-ammos.github.io/slim/), called [slim-detect-secrets](https://github.com/NASA-AMMOS/slim-detect-secrets/tree/exp). This tool replaces Git Secrets but serves the same purpose: it helps prevent committing information to a repository that should remain secret. Unlike Git Secrets, though, the kinds of secrets detect-secrets finds includes not just passwords or API keys, but also hostnames and email addresses. It also uses entropy analysis to detect randomized strings that could be passwords.

To install, [see the wiki](https://github.com/NASA-PDS/nasa-pds.github.io/wiki/Git-and-Github-Guide#detect-secrets). To update a new `.secrets.baseline` file, run the custom script in the repo to generate the secrets. https://github.com/NASA-PDS/pds4-information-model/tree/main/bin. This is needed because there are numerous additional files that should be ignored in this repo.

### Tagging a Release

Stable releases are built by [Roundup](https://github.com/NASA-PDS/roundup-action) whenever a tag beginning with `release/` is pushed. The workflow is defined in `.github/workflows/stable-cicd.yaml`.

1. **Determine the next version.** Follow [SemVer](https://semver.org/) (`MAJOR.MINOR.PATCH`) and increment from the latest release:
   - `MAJOR` for incompatible changes
   - `MINOR` for backward-compatible new functionality
   - `PATCH` for backward-compatible bug fixes

   The latest release is shown on the [releases page](https://github.com/NASA-PDS/pds4-information-model/releases), or list the existing tags:

   ```bash
   git fetch --tags
   git tag --list 'release/*' --sort=-v:refname | head -5
   ```

2. **Create and push the tag** from an up-to-date `main`. The tag must start with `release/`:

   ```bash
   VERSION="16.1.2"
   git checkout main
   git pull
   git tag "release/${VERSION}"
   git push origin "release/${VERSION}"
   ```

3. **Monitor the build.** Open the [Actions tab](https://github.com/NASA-PDS/pds4-information-model/actions) and watch the "Stable integration & delivery" workflow run for your tag. Roundup deletes the `release/<version>` tag and creates a new `v<version>` tag in its place, so `release/<version>` will no longer exist after the build.

4. **Add release notes.** Once the workflow completes, Roundup creates a GitHub release named `v<version>` (for example, `v16.1.2`) on the [releases page](https://github.com/NASA-PDS/pds4-information-model/releases). Open it and either:
   - write the release notes by hand, or
   - generate them with the `generating-release-notes` skill from [pds-agent-skills](https://github.com/NASA-PDS/pds-agent-skills).

## 💁‍♀️ Usage

See the deployed documentation for usage details for the various components: https://nasa-pds.github.io/pds4-information-model/


## 👥 Contributing

Please see the [contribution guidelines](https://github.com/NASA-PDS/.github/blob/main/CONTRIBUTING.md) but also be sure to see our [code of conduct](https://github.com/NASA-PDS/.github/blob/main/CODE_OF_CONDUCT.md).


### 🔢 Versioning

We use the [SemVer](https://semver.org/) philosophy for versioning this software.


## 📃 License

The project is licensed under the [Apache version 2](LICENSE.md) license.

