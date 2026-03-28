# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

The PDS4 Information Model repository generates the NASA Planetary Data System's PDS4 Information Model, including XML schemas, Schematron files, data dictionaries, and ontologies. The repository consists of three Maven modules that work together to process ontology data and generate various output artifacts.

## Core Architecture

### Multi-Module Maven Structure

The project is organized as a parent POM (`pom.xml`) with three child modules:

1. **model-dmdocument** - Maven plugin that serves as the core engine
   - Main entry point: `DMDocument.java` (can be run as standalone or Maven plugin)
   - Processes ontology `.pins` and `.pont` files from `model-ontology`
   - Generates data dictionaries, XML schemas, Schematron rules, JSON, OWL/RDF outputs
   - Packaged as an executable JAR that is bundled into `model-lddtool`

2. **model-lddtool** - Local Data Dictionary Tool (LDDTool)
   - Command-line tool for processing Ingest_LDD XML files
   - Embeds `model-dmdocument` as `DMDocument.jar` in its resources
   - Validates and integrates LDDs (Local Data Dictionaries) into the master model
   - Generates namespace-specific schemas and documentation

3. **model-ontology** - Ontology data container
   - Contains Protégé ontology instances in `src/ontology/Data/`
   - Stores master ontology files: `dd11179.pins`, `DMDocument.pins`, `Glossary.pins`, `UpperModel.pont`
   - Supports multiple IM versions in versioned subdirectories (1B00, 1C00, ... 1P00, etc.)
   - Currently on IM version 1.25.0.0 (folder 1P00 as of Build 16.0)

### Key Data Flow

1. Ontology data in `.pins`/`.pont` format (stored in `model-ontology/src/ontology/Data/`)
2. `DMDocument` reads and parses these files, building an in-memory DOM model
3. Various writer classes generate output artifacts (XSD, SCH, JSON, RDF, etc.)
4. For LDDTool runs, Ingest_LDD XML files are merged with the master model
5. Output is written to configured directories (typically `target/` for builds)

### Information Model Versions

The system supports multiple PDS4 Information Model versions. The current build version is defined in `DMDocument.java`:
- `buildIMVersionId = "1.25.0.0"`
- `buildIMVersionFolderId = "1P00"`

Alternate IM versions (1B00 through 1P00) can be specified using the `-V` flag in LDDTool. Each version has its own data files in `model-ontology/src/ontology/Data/<version>/`.

## Build Commands

### Standard Build
```bash
# Clean and package all modules
mvn clean package

# This will:
# - Compile all three modules
# - Run Cucumber tests in model-lddtool
# - Create executable JARs and distribution archives
```

### Generate Documentation Site
```bash
# Must run package first
mvn clean package
mvn site:site site:stage

# View generated docs
open target/staging/model/index.html
```

### Skip Tests During Build
```bash
mvn clean package -DskipTests
```

## Testing

### Test Framework
Tests use Cucumber (BDD framework) with JUnit integration. Test definitions are in Gherkin feature files.

### Running All Tests
```bash
mvn clean test
```

Always complete a `mvn clean test` or `mvn clean compile` after making code changes. Which one you run depends upon the significance of the code change. Use your best judgement.

### Test Structure
- Feature files: `model-lddtool/src/test/resources/features/`
  - `integration.feature` - Main integration test scenarios
  - `validate.feature` - Validation test scenarios
- Test data: `model-lddtool/src/test/resources/data/`
- Test step definitions: `model-lddtool/src/test/java/`

### Running Specific Tests
Cucumber tests are tagged (e.g., `@NASA-PDS/pds4-information-model#738`). To run specific tests, modify the test runner or use Maven profiles.

### Important Test Configuration
The `model-lddtool/pom.xml` configures Surefire with:
- System property: `data.home=${project.basedir}/../model-ontology/src/ontology/Data`
- Memory settings: `-Xms512m -Xmx2048m -Xss1024m`

### Diagnosing Test Failures
When `mvn test` fails, detailed validate output for each test case is written to:
```
model-lddtool/target/test/report_<testDir>.json
```
For example, a failure in the `github894` test case → `model-lddtool/target/test/report_github894.json`.

These JSON reports contain the full validate output including `totalErrors`, `totalWarnings`, and individual error messages — useful for understanding whether a count mismatch is due to missing test data, a validate behavior change, or a genuine regression.

### SNAPSHOT Dependency Caution
The `validate` dependency in `model-lddtool/pom.xml` uses a SNAPSHOT version. This means tests may pass in CI but fail locally (or vice versa) if the SNAPSHOT was updated between runs. If you see unexpected count mismatches, check whether the validate SNAPSHOT changed recently by comparing the JAR timestamp in `~/.m2/repository/gov/nasa/pds/validate/`.

## LDDTool Usage

LDDTool is the primary command-line interface for working with Local Data Dictionaries.

### Basic Syntax
```bash
# After building, the tool is in model-lddtool/target/
java -jar model-lddtool/target/model-lddtool-<version>.jar [options] <Ingest_LDD_files>
```

### Common Options
- `-p` - Set context to PDS4 (required for all operations)
- `-l` - Process local data dictionary input files
- `-V <version>` - Specify IM version (e.g., `-V 1P00`, `-V 1O00`)
- `-J` - Generate JSON output
- `-a` - Include all namespaces in JSON output (use with `-J`)
- `-W` - Generate modern web application with split JSON-LD files
- `-D` - Write Data Dictionary DocBook file
- `-n` - Write nuance property maps to LDD schema annotation
- `-v` - Display version information
- `-d` - Omit "mission" from namespace (for discipline dictionaries)

### Example Commands
```bash
# Generate LDD with JSON output for current IM version
java -jar lddtool.jar -lpJ PDS4_EXAMPLE_IngestLDD.xml

# Generate LDD for alternate IM version
java -jar lddtool.jar -lpJ -V 1O00 PDS4_EXAMPLE_IngestLDD.xml

# Generate master PDS4 schemas for specific IM version
java -jar lddtool.jar -p -V 1P00
```

## Updating for New IM Versions

When a new Information Model version is released:

1. Update test XML files with new IM version:
```bash
cd bin
python3.9 update_version.py <new-version> ../model-lddtool/src/test/resources/data/update_version/
```

2. Update `DMDocument.java` constants:
   - `buildIMVersionId`
   - `buildIMVersionFolderId`
   - Add new version to `alternateIMVersionArr` in the `init()` method

3. Ensure new version data exists in `model-ontology/src/ontology/Data/<new-version>/`

4. Update the `-V` argument choices in `DMDocument.getArgumentParserNamespace()`

## Code Organization Patterns

### Configuration Loading
Configuration is loaded from `config.properties` files located in:
- `model-ontology/src/ontology/Data/config.properties` (namespace configuration)
- `model-ontology/src/ontology/Data/<version>/config.properties` (version-specific)

Properties include IM version, namespace definitions, steward information, and URL patterns.

### Namespace Management
Namespaces are configured in `config.properties` with entries like:
- `lSchemaFileDefn.<namespace>.identifier` - Namespace ID
- `lSchemaFileDefn.<namespace>.isMaster` - Whether it's the master (Common) dictionary
- `lSchemaFileDefn.<namespace>.stewardArr` - Steward organizations
- `lSchemaFileDefn.<namespace>.nameSpaceURL` - Base URL for namespace

The `SchemaFileDefn` class represents each namespace configuration.

### State Management
The `DMDocument` class contains extensive static state variables that control processing:
- Flags for different export formats (`exportJSONFileFlag`, `exportDDFileFlag`, etc.)
- IM version information (`infoModelVersionId`, `schemaLabelVersionId`, `pds4BuildId`)
- Processing context (`LDDToolFlag`, `PDSOptionalFlag`)
- Master data structures (`masterDOMInfoModel`, `masterAllSchemaFileSortMap`)

**Important**: The `DMDocument.run()` method calls `reset()` at the end to clean up static state, allowing the tool to be run multiple times in the same JVM.

## Directory Structure Notes

- `bin/` - Utility scripts (update_version.py, generate-detect-secrets.sh)
- `test/` - Integration test data and smoke test scripts
- `tools/` - Additional tooling
- `out/` - Generated output (not tracked in git)
- Each module has standard Maven structure: `src/main/java`, `src/test/`, `src/site/`

## Web Application for Data Dictionary

### Overview

The `-W` flag generates a modern, client-side web application that provides an interactive interface for browsing the Information Model. This replaces the legacy DocBook/WebHelp system that was crashing due to file size.

### Directory Structure

The web application consists of two parts:

**1. Source Code** (in `webapp-src/` - version controlled):
```
webapp-src/                        # Static web application source (version controlled)
├── index.html                     # Main HTML entry point
├── start-server.sh               # Helper script for local testing
├── assets/
│   ├── main.js                   # Application JavaScript
│   └── styles.css                # Custom styles
└── README.md                      # Web app documentation
```

**2. Complete Deployable Package** (in `export/webapp/` - generated with `-W` flag):
```
export/webapp/                     # Complete, self-contained deployable package
├── index.html                     # Copied from webapp-src/
├── assets/                        # Copied from webapp-src/
│   ├── main.js
│   └── styles.css
├── README.md                      # Copied from webapp-src/
└── data/                          # Generated JSON files
    ├── context.jsonld             # JSON-LD vocabulary mappings
    ├── metadata.json              # IM version, namespaces, statistics
    ├── search-index.json          # Pre-built search index
    ├── classes/
    │   ├── index.json            # List of all classes
    │   ├── Product_Observational.json
    │   └── ... (one file per class)
    ├── attributes/
    │   ├── index.json            # List of all attributes
    │   └── ... (one file per attribute)
    ├── datatypes/
    │   └── index.json
    └── units/
        └── index.json
```

**Key Point**: When you run with `-W` flag, LDDTool automatically copies the webapp source files from `webapp-src/` to `export/webapp/` AND generates the data files. The result is a complete, ready-to-deploy package in `export/webapp/`.

### Key Features

- **Dynamic Data Loading**: Web app reads JSON files at runtime - no hardcoded data
- **Lazy Loading**: Individual class/attribute files loaded on-demand for fast initial load
- **Fuzzy Search**: Real-time search using Fuse.js across all classes and attributes
- **JSON-LD Compatible**: All data files use semantic web standards
- **Responsive Design**: Works on desktop, tablet, and mobile
- **Zero Dependencies**: No build step, no server required - pure static files
- **Offline Capable**: Once loaded, can work offline with cached data

### Usage

```bash
# Generate the Information Model with web application
mvn clean package
java -jar model-lddtool/target/lddtool-*.jar -p -W

# For LDDTool with local dictionary
java -jar lddtool.jar -lpW PDS4_EXAMPLE_IngestLDD.xml
```

### Important: Update and Deployment Workflow

The PDS4 Information Model is updated every 6 months. Here's the complete workflow:

1. **Update Model**: Make changes to the PDS4 Information Model
2. **Update Webapp Source** (optional): Make any UI/feature changes in `webapp-src/`
3. **Run Build with `-W` flag**: This automatically:
   - Copies webapp source files from `webapp-src/` to `export/webapp/`
   - Generates JSON data files in `export/webapp/data/`
   - Creates a complete, self-contained deployable package
4. **Deploy**: Upload the entire `export/webapp/` directory to your web server

**Key Benefits**:
- **Single Command**: One `-W` flag does everything
- **No Manual Copying**: Source files are automatically copied
- **Self-Contained**: `export/webapp/` has everything needed to deploy
- **No Hardcoded Paths**: Web app uses relative paths that work anywhere

### Local Testing

After running LDDTool with `-W` flag, test the complete deployable package:

```bash
# Navigate to the generated webapp package
cd export/webapp

# Start Python HTTP server
python3 -m http.server 8000

# Open in browser
open http://localhost:8000
```

This tests the actual package that will be deployed to production.

**For Development**: To test webapp source changes before building, you can run from `webapp-src/` but you'll need to temporarily update `DATA_PATH` in `main.js` to point to `'../export/webapp/data/'`.

### Deployment Options

The entire `export/webapp/` directory is a complete, self-contained package. Just upload it:

#### GitHub Pages (Recommended)
```bash
# Copy the complete package to docs/
cp -r export/webapp/* docs/

# Commit and push
git add docs/
git commit -m "Deploy PDS4 Data Dictionary v1.25.0"
git push
```

Then enable GitHub Pages pointing to `docs/` directory.

#### AWS S3 + CloudFront
```bash
# Sync the complete package to S3
aws s3 sync export/webapp/ s3://your-bucket/ --delete
```

#### Netlify or Vercel
```bash
# Deploy the complete package
netlify deploy --dir=export/webapp --prod

# Or with Vercel
vercel deploy export/webapp --prod
```

#### Any Static Host (Apache, Nginx, etc.)
```bash
# Copy to web server
scp -r export/webapp/* user@server:/var/www/html/pds4-dictionary/
```

### Implementation Details

**Java Backend** (`WriteWebAppFiles.java`):
- Reads in-memory DOM model
- Splits data into individual JSON files (one per class/attribute)
- Generates index files for navigation
- Creates pre-built search index
- Outputs JSON-LD with semantic web vocabulary mappings

**Web Frontend** (`main.js`):
- Loads metadata and search index on startup
- Initializes Fuse.js for fuzzy search
- Dynamically fetches detail files when user clicks on items
- Client-side routing with browser history API
- No framework dependencies - vanilla JavaScript

### Technology Stack

- **Backend**: Java (WriteWebAppFiles.java in model-dmdocument)
- **Frontend**: Vanilla JavaScript (ES6+), Tailwind CSS, Fuse.js
- **Data Format**: JSON-LD (compatible with RDF/semantic web tools)
- **Deployment**: Static files only - no server required

### Advantages Over DocBook/WebHelp

| Aspect | DocBook/WebHelp | Modern Web App |
|--------|-----------------|----------------|
| Build time | Slow (Oxygen processing) | Fast (direct JSON export) |
| File count | 2000+ HTML files | ~200 JSON files + 1 app |
| Search | Basic WebHelp search | Modern fuzzy search |
| Mobile | Poor | Responsive design |
| Loading | Load all upfront | Lazy load as needed |
| Scalability | Crashes at current size | Scales to any size |
| Maintenance | Complex DocBook→HTML | Simple JSON→Web |

### Troubleshooting

**"Failed to load application" or 404 errors**:
- Ensure `export/webapp/data/` directory exists with JSON files
- Run LDDTool with `-W` flag if data files don't exist
- Check that you're accessing via HTTP server (not file://)
- Verify you're running the server from `webapp-src/` directory
- Verify JSON files are valid (check browser console)

**Search not working**:
- Check that `export/webapp/data/search-index.json` exists
- Verify Fuse.js is loading from CDN (Network tab in DevTools)

**CORS errors**:
- You must serve via HTTP server, not open `index.html` directly in browser
- Use `./start-server.sh` or `python3 -m http.server 8000` from `webapp-src/`

For detailed documentation, see `webapp-src/README.md`.

## MkDocs Documentation Generation

### Overview

In addition to the JavaScript web application (above), LDDTool can generate Markdown documentation structured for MkDocs with the Material theme. This provides a modern, searchable, static site documentation system.

### Key Benefits Over JavaScript Web App

- **Static Site Generator** - Standard documentation tooling with large community
- **Easier Maintenance** - Markdown is simpler than JavaScript/JSON for future maintainers
- **Better for Handoff** - When team members leave, Markdown + MkDocs is easier to transfer
- **Professional Theme** - Material for MkDocs is widely used and well-maintained

### How It Works

When you run LDDTool with the `-W` flag:

1. **Generates JSON Web App** (WriteWebAppFiles.java):
   - Creates `export/webapp/` with JSON files and JavaScript app

2. **Generates MkDocs Markdown** (WriteMkDocsFiles.java):
   - Creates `export/docs/` with Markdown files and `mkdocs.yml` configuration

Both outputs are generated simultaneously from the same in-memory DOM model.

### Directory Structure

```
export/docs/                       # MkDocs documentation (generated with -W)
├── mkdocs.yml                    # MkDocs configuration (auto-generated)
├── docs/                         # Markdown source files (auto-generated)
│   ├── index.md                 # Home page with statistics
│   ├── classes/                 # Individual class pages (~350 files)
│   │   ├── Product_Observational.md
│   │   └── ...
│   ├── attributes/              # Individual attribute pages (~1200 files)
│   │   ├── name.md
│   │   └── ...
│   ├── datatypes/               # Data type reference
│   │   └── index.md
│   └── namespaces/              # Namespace index pages
│       ├── pds.md
│       ├── cart.md
│       └── ...
└── site/                        # Built HTML (after running mkdocs build)
    └── ...
```

### Namespace Organization

The documentation is automatically organized into three categories based on flags in `SchemaFileDefn`:

1. **Common** - `isMaster = true` (the core `pds` namespace)
2. **Discipline LDDs** - `isDiscipline = true` (cart, geo, img, spectral, etc.)
3. **Mission LDDs** - `isMission = true` (insight, orex, etc.)

This categorization appears in:
- Navigation sidebar (collapsible sections)
- Home page namespace listing
- Namespaces tab

### Usage

```bash
# Generate documentation
mvn clean package
java -jar model-lddtool/target/lddtool-*.jar -pW

# Install MkDocs (first time only)
pip install mkdocs-material

# Build and serve
cd export/docs
mkdocs serve  # Opens http://localhost:8000
```

For complete instructions, see [MKDOCS_DOCUMENTATION.md](MKDOCS_DOCUMENTATION.md).

### Key Features

- **Collapsible Navigation** - Namespace sections collapsed by default for clean sidebar
- **Full-Text Search** - Fast client-side search with suggestions
- **Cross-References** - Automatic links between classes, attributes, and namespaces
- **Visual Badges** - Shields.io badges for Abstract, Deprecated, Enumerated, Nillable
- **Responsive Design** - Material theme works on all devices
- **Markdown Tables** - Clean presentation of associations, constraints, permissible values

### Implementation Details

**Java Class**: `WriteMkDocsFiles.java` in model-dmdocument
- Reads in-memory DOM model (same as WriteWebAppFiles)
- Generates one Markdown file per class/attribute
- Creates namespace index pages
- Generates `mkdocs.yml` with hierarchical navigation
- Uses badges from shields.io for visual indicators

**Generated Files**:
- ~1500 Markdown files (classes + attributes + namespaces)
- `mkdocs.yml` with complete navigation structure
- Home page with statistics and namespace breakdown

**Navigation Structure**:
```yaml
nav:
  - Home: index.md
  - Classes:
    - Common:
      - pds:
        - Product_Observational: classes/Product_Observational.md
        - ...
    - Discipline LDDs:
      - cart:
        - ...
    - Mission LDDs:
      - insight:
        - ...
  - Attributes:
    - Common:
      - ...
  - Data Types: datatypes/index.md
  - Namespaces:
    - Common:
      - pds: namespaces/pds.md
    - Discipline LDDs:
      - cart: namespaces/cart.md
    - Mission LDDs:
      - insight: namespaces/insight.md
```

### Deployment

After generating documentation, you can deploy the built HTML:

```bash
cd export/docs

# Option 1: GitHub Pages (one command)
mkdocs gh-deploy

# Option 2: Build and deploy manually
mkdocs build
# Then upload the site/ directory to any static host
```

### Comparison with JavaScript Web App

| Feature | JavaScript Web App | MkDocs |
|---------|-------------------|---------|
| Build time | ~30 seconds | ~30 seconds (+ ~30s for mkdocs build) |
| Technology | Vanilla JS + JSON | Markdown + Python |
| Search | Fuse.js (fuzzy) | MkDocs search (exact) |
| Maintenance | Requires JS knowledge | Just Markdown |
| Deployment | Copy files | Copy files or `mkdocs gh-deploy` |
| Customization | Edit JS/HTML/CSS | Edit Markdown + mkdocs.yml |
| Team handoff | Harder | Easier |

**Recommendation**: Use MkDocs for long-term sustainability. The JavaScript web app is archived in `webapp-src/` for future reference but MkDocs is the recommended approach for documentation maintenance.

## Secret Detection

The repository uses slim-detect-secrets (NASA-AMMOS fork) to prevent committing sensitive information. A custom script generates the `.secrets.baseline` file:
```bash
./bin/generate-detect-secrets.sh
```

This is necessary because many files need to be ignored for this specific repository.
