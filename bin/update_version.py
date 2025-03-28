#!/usr/bin/env python3
"""
This script replaces the <information_model_version> value in XML files
with a new version provided as a command-line argument. It accepts either
a file or a directory; if a directory is provided, it will recursively
traverse and process all files with a .xml or .lblx suffix.

Usage:
    python update_version.py NEW_VERSION path
"""
import os
import re
import sys


def update_information_model_version(file_path, new_version):
    """Update the information_model_version value in the XML file."""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
    except IOError as e:
        sys.stderr.write(f"Error reading {file_path}: {e}\n")
        return

    # Pattern explanation:
    # (<(?:\w+:)?information_model_version>) matches the opening tag with an optional namespace.
    # (.*?) captures the current version text.
    # (</(?:\w+:)?information_model_version>) matches the closing tag.
    pattern = r'(<(?:\w+:)?information_model_version>)(.*?)(</(?:\w+:)?information_model_version>)'

    # Use a lambda replacement function to avoid issues with group references in new_version.
    new_content, count = re.subn(
        pattern,
        lambda m: m.group(1) + new_version + m.group(3),
        content,
        flags=re.DOTALL
    )

    if count == 0:
        sys.stderr.write(f"No <information_model_version> element found in {file_path}\n")
        return

    try:
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(new_content)
        print(f"Updated {file_path}")
    except IOError as e:
        sys.stderr.write(f"Error writing {file_path}: {e}\n")


def process_path(path, new_version):
    """
    If path is a file, update it.
    If path is a directory, recursively traverse to update .xml and .lblx files.
    """
    if os.path.isfile(path):
        if path.lower().endswith(('.xml', '.lblx')):
            update_information_model_version(path, new_version)
        else:
            sys.stderr.write(f"Skipping non-XML file: {path}\n")
    elif os.path.isdir(path):
        for root, _, files in os.walk(path):
            for name in files:
                if name.lower().endswith(('.xml', '.lblx')):
                    file_path = os.path.join(root, name)
                    update_information_model_version(file_path, new_version)
    else:
        sys.stderr.write(f"Path does not exist: {path}\n")


def main():
    """Parse command-line arguments and process the file or directory."""
    if len(sys.argv) != 3:
        sys.stderr.write("Usage: python update_version.py NEW_VERSION path\n")
        sys.exit(1)

    new_version = sys.argv[1]
    path = sys.argv[2]

    process_path(path, new_version)


if __name__ == '__main__':
    main()
