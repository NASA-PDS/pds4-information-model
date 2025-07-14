Python script to update the `information_model_version` for all test XML that require it.

## System Requirements

* Python 3.9

## Update for new IM

1. Update all the XML for a new IM build:

```
# update_version.py <new-information-model-version> model-lddtool/src/test/resources/data/update_version/

python3.9 update_version.py 1.24.0.0 ../model-lddtool/src/test/resources/data/update_version/
```

2. Add and commit updated files, add to IM update PR.
