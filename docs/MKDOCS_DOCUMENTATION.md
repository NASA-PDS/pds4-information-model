# MkDocs Documentation Generation

This document explains how to generate and deploy the MkDocs-based documentation for the PDS4 Information Model.

## Overview

LDDTool can generate Markdown documentation that is structured for MkDocs with the Material theme. This provides a modern, searchable, web-based data dictionary as an alternative to the DocBook/WebHelp output.

## Quick Start

### 1. Generate Documentation

Run LDDTool with the `-W` flag to generate both JSON and Markdown documentation:

```bash
# Build LDDTool (from repository root)
mvn clean package

# Extract the built lddtool package
tar -xzf model-lddtool/target/lddtool-*-bin.tar.gz

# Generate documentation
./lddtool-*/bin/lddtool -pW

# or 
./lddtool-*/bin/lddtool -lpW PDS4_RINGS_IngestLDD.xml PDS4_GEOM_IngestLDD.xml etc...
```

This creates:
- `export/docs/` - **Markdown files structured for MkDocs**
- `export/webapp/` - JSON files and JavaScript web app (legacy)


### 2. Set Up Python Virtual Environment (First Time Only)

It's recommended to use a Python virtual environment to isolate dependencies:

```bash
# Create a virtual environment in the export/docs directory
python3 -m venv venv

# Activate the virtual environment
# On macOS/Linux:
source venv/bin/activate
# On Windows:
# venv\Scripts\activate

# Install mkdocs-material
pip install mkdocs-material

# To deactivate the virtual environment when done:
# deactivate
```

**Note**: You'll need to activate the virtual environment (step 2) each time you want to build or serve the documentation in a new terminal session.

### 3. Build and Preview

```bash
# Activate virtual environment (if not already activated)
source venv/bin/activate  # macOS/Linux
# venv\Scripts\activate   # Windows

# Navigate to the generated docs
cd export/docs

# Option A: Build static HTML
mkdocs build
# Output will be in export/docs/site/

# Option B: Serve locally with live reload
mkdocs serve

# Then open http://localhost:8000 in your browser (usually http://127.0.0.1:8000)
# Ctrl+C to exit

# When done, deactivate the virtual environment
deactivate
```

## Documentation Structure

The generated documentation is organized into three main categories:

### Common
Core PDS4 namespace (`pds`) containing the base Information Model

### Discipline LDDs
Domain-specific Local Data Dictionaries:
- `cart` - Cartography
- `geo` - Geosciences
- `img` - Imaging
- `spectral` - Spectroscopy
- And more...

### Mission LDDs
Mission-specific Local Data Dictionaries:
- `insight` - InSight Mission
- `orex` - OSIRIS-REx Mission
- And more...

Each namespace includes:
- Individual pages for each class with associations and inheritance
- Individual pages for each attribute with constraints and permissible values
- Namespace index pages listing all classes and attributes

## Features

- **Collapsible Navigation** - Namespaces grouped by type, collapsed by default for easy navigation
- **Full-Text Search** - Fast client-side search with suggestions and highlighting
- **Cross-References** - Automatic links between related classes, attributes, and namespaces
- **Visual Badges** - Clear indicators for Abstract, Deprecated, Enumerated, and Nillable items
- **Responsive Design** - Works seamlessly on desktop, tablet, and mobile devices
- **Material Theme** - Professional, clean design following Material Design principles

## Deployment

All deployment methods require activating the virtual environment first.

### GitHub Pages

Deploy directly to GitHub Pages from the `export/docs` directory:

```bash
cd export/docs
source venv/bin/activate
mkdocs gh-deploy
deactivate
```

This will build the site and push it to the `gh-pages` branch.

### AWS S3 + CloudFront

```bash
cd export/docs
source venv/bin/activate
mkdocs build
deactivate
aws s3 sync site/ s3://your-bucket/pds4-docs/ --delete
aws cloudfront create-invalidation --distribution-id YOUR_DIST_ID --paths "/*"
```

### Netlify

```bash
cd export/docs
source venv/bin/activate
mkdocs build
deactivate
netlify deploy --dir=site --prod
```

### Traditional Web Server

```bash
cd export/docs
source venv/bin/activate
mkdocs build
deactivate
scp -r site/* user@server:/var/www/html/pds4-dictionary/
```

## Directory Structure

```
export/docs/
├── mkdocs.yml              # MkDocs configuration (auto-generated)
├── docs/                   # Markdown source files (auto-generated)
│   ├── index.md           # Home page with statistics and namespace links
│   ├── classes/           # Individual class pages (~350 files)
│   ├── attributes/        # Individual attribute pages (~1200 files)
│   ├── datatypes/         # Data type reference
│   └── namespaces/        # Namespace index pages (one per LDD)
└── site/                  # Built HTML (created by mkdocs build)
    └── ...                # Static HTML, CSS, JS ready to deploy
```

## Configuration

The `mkdocs.yml` file is automatically generated and includes:

- **Site metadata** - Title, description, author, URL
- **Material theme** - With indigo color scheme
- **Navigation** - Auto-generated hierarchical structure organized by namespace type
- **Plugins** - Search with custom separators
- **Markdown extensions** - Tables, code highlighting, admonitions, etc.

You generally don't need to modify this file, but you can customize it after generation if needed.

## Workflow for Updates

Every 6 months when the PDS4 Information Model is updated:

```bash
# 1. Update the Information Model source files
# (your normal IM update process)

# 2. Regenerate documentation
mvn clean package
tar -xzf model-lddtool/target/lddtool-*-bin.tar.gz
./lddtool-*/bin/lddtool -pW

# 3. Build and deploy
cd export/docs
source venv/bin/activate  # Activate virtual environment
mkdocs build
deactivate

# 4. Deploy to your hosting platform
# (use one of the deployment methods above)
```

That's it! The documentation is regenerated from the latest Information Model data.

## Troubleshooting

### MkDocs not found

**Problem**: Command `mkdocs` not found

**Solution**:
```bash
# Make sure you've activated the virtual environment
cd export/docs
source venv/bin/activate  # macOS/Linux
# venv\Scripts\activate   # Windows

# If venv doesn't exist, create it:
python3 -m venv venv
source venv/bin/activate
pip install mkdocs-material
```

### Build warnings about navigation

**Problem**: MkDocs shows warnings about pages not in navigation

**Solution**: These warnings are safe to ignore. MkDocs generates warnings for pages that aren't explicitly listed in the navigation tree, but they're still accessible via search and direct links.

### Search not working

**Problem**: Search bar doesn't return results

**Solution**:
- Search requires JavaScript to be enabled
- Search only works when served via HTTP (use `mkdocs serve` or deploy to a web server)
- Opening `index.html` directly from the filesystem won't enable search due to browser security restrictions

### Slow build times

**Problem**: `mkdocs build` takes a long time

**Solution**: This is normal with ~1500+ pages. The build typically takes 30-60 seconds. Use `mkdocs serve` during development for faster incremental builds.

### Port already in use

**Problem**: `mkdocs serve` fails with "Address already in use"

**Solution**:
```bash
# Use a different port
mkdocs serve -a localhost:8001

# Or find and kill the process using port 8000
lsof -ti:8000 | xargs kill -9  # macOS/Linux
```

## Comparison with DocBook

| Feature | DocBook/WebHelp | MkDocs |
|---------|----------------|---------|
| Build time | 5-10 minutes | 30-60 seconds |
| Crashes | Frequent with large LDDs | Never |
| Search | Basic | Fuzzy, with suggestions |
| Navigation | Flat hierarchy | Grouped by namespace type |
| Mobile support | Poor | Excellent |
| Maintenance | Complex XSLT | Simple Markdown |
| Customization | Difficult | Easy (edit .md files) |

## For More Information

- [MkDocs Documentation](https://www.mkdocs.org/)
- [Material for MkDocs](https://squidfunk.github.io/mkdocs-material/)
- [PDS4 Information Model](https://pds.nasa.gov/datastandards/about/)
- [LDDTool Documentation](https://nasa-pds.github.io/pds4-information-model/)
