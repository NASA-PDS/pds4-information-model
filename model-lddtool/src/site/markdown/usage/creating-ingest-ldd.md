# Creating the Ingest LDD Dictionary Input File

## Overview

The Ingest LDD Dictionary Input File serves as the configuration input for LDDTool, which generates XSD Schema and Schematron files for PDS4 local dictionaries. This guide helps you create well-structured, maintainable local data dictionaries.

## Prerequisites

Before beginning, ensure you have:

- Familiarity with a validating or schema-aware XML editor (e.g., Oxygen XML Editor, IntelliJ IDEA)
- Experience reading or writing PDS4 XML labels
- Knowledge of how to create XML files from schema definitions
- Understanding of PDS4 namespace schemas
- Access to validation tools like the PDS4 Validate Tool

### Recommended Tools

- **XML Editor**: Oxygen XML Editor, IntelliJ IDEA, VS Code with XML extensions
- **Validation**: PDS4 Validate Tool (available from https://nasa-pds.github.io/validate/)
- **LDDTool**: Latest version of LDDTool (see installation guide)

## Basic Structure and Strategies

### File Organization

The Ingest_LDD file must follow a specific ordering:

1. **Ingest_LDD header** - Dictionary metadata (namespace, version, steward)
2. **DD_Attribute definitions** - All attributes must be defined first
3. **DD_Class definitions** - Classes defined after all attributes
4. **DD_Rule definitions** (optional) - Schematron validation rules
5. **Property_Maps** (optional) - Terminological mappings

**Important ordering rules:**
- All attributes must be defined before any class definitions
- No enforced ordering within the attribute or class sections
- Classes can reference other classes that haven't been defined yet
- Attributes and classes can be in any order within their respective sections

**Best practices for maintainability:**
- Organize attributes alphabetically or by functional grouping
- Define lower-level subclasses before their containing classes
- Group related attributes and classes together
- Add XML comments to mark major sections

### Naming Conventions

PDS discipline namespaces follow these conventions:

**Attributes:**
- Use lowercase with underscores separating words
- Follow English grammar word order
- Examples: `horizontal_display_axis`, `instrument_compression_ratio`

**Classes:**
- Use initial capitalization (PascalCase) with underscores separating words
- Follow English grammar word order
- Examples: `Display_Settings`, `Instrument_Compression_Parameters`

**General rules:**
- ASCII characters only (no accented characters, emoji, etc.)
- Abbreviations should be avoided unless widely recognized
- Be consistent with PDS4 common dictionary naming patterns
- Use descriptive, self-documenting names

While not strictly mandatory, adhering to these conventions improves readability, consistency, and acceptance during peer review.

### Grouping Attributes into Classes

You have flexibility in how you organize attributes into classes and subclasses. Consider these approaches:

**Functional grouping** - Group attributes by their purpose or use:
```xml
<DD_Class>
  <name>Display_Settings</name>
  <definition>Settings for displaying array data on a device.</definition>
  <!-- Groups all display-related attributes -->
</DD_Class>
```

**Hierarchical grouping** - Create subclasses for logical containment:
```xml
<DD_Class>
  <name>Observation_Parameters</name>
  <DD_Association>
    <local_identifier>Timing_Parameters</local_identifier>
    <reference_type>component_of</reference_type>
  </DD_Association>
  <DD_Association>
    <local_identifier>Instrument_Settings</local_identifier>
    <reference_type>component_of</reference_type>
  </DD_Association>
</DD_Class>
```

**Best practices:**
- Reuse functional groupings from PDS3 labels when available
- Make all attributes belong to some dictionary class rather than standalone elements
- Keep class hierarchies shallow (typically 2-3 levels maximum)
- Group attributes that are logically related or always used together

### Documentation Requirements

All attribute definitions must be substantive and complete:

**Critical requirements:**
- Provide clear, complete definitions that explain the purpose and usage
- If an attribute has a unit of measure, you **must** include the `unit_of_measure_type` in the definition
- Avoid "TBD" placeholders - these cause problems during peer review
- All definitions are subject to external peer review by domain experts

**Good definition example:**
```xml
<DD_Attribute>
  <name>instrument_compression_ratio</name>
  <definition>The instrument_compression_ratio attribute specifies the ratio
  of the size of uncompressed image data to the size of compressed image data
  for on-board compression. A value of 1.0 indicates no compression.</definition>
  <DD_Value_Domain>
    <enumeration_flag>false</enumeration_flag>
    <value_data_type>ASCII_Real</value_data_type>
    <unit_of_measure_type>Units_of_None</unit_of_measure_type>
  </DD_Value_Domain>
</DD_Attribute>
```

**Poor definition example (avoid this):**
```xml
<DD_Attribute>
  <name>compression_ratio</name>
  <definition>TBD</definition>  <!-- NOT ACCEPTABLE -->
</DD_Attribute>
```

## Complexity Considerations

### Keep It Simple

Excessive internal complexity in local dictionaries should be avoided:

**Problems with complex dictionaries:**
- Increases user confusion and implementation difficulty
- Creates dependencies between different dictionary stewards
- May become unsupportable after mission end
- Makes validation and testing harder

**Types of complexity to avoid:**
- Deep class hierarchies (more than 3-4 levels)
- Excessive cross-namespace references
- Complicated choice lists with many alternatives
- Overly restrictive Schematron rules

**When complexity seems necessary:**
- Consult your PDS node consultant before implementation
- Consider whether the complexity is truly required
- Document the rationale for complex structures
- Provide clear usage examples

### Cross-Namespace Dependencies

When your dictionary references classes or attributes from other namespaces:

**Considerations:**
- Creates dependency on external stewards
- May require coordination for changes
- Can break if referenced namespace changes
- Adds complexity for users

**Best practices:**
- Minimize cross-namespace references when possible
- Document all external dependencies clearly
- Coordinate with external namespace stewards
- Test with multiple IM versions

## Filling Out the Ingest_LDD Class

The `Ingest_LDD` class serves as the XML document root and defines your dictionary's metadata.

### Required Attributes

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?xml-model href="https://pds.nasa.gov/pds4/pds/v1/PDS4_PDS_1P00.sch"
  schematypens="http://purl.oclc.org/dsdl/schematron"?>
<Ingest_LDD xmlns="http://pds.nasa.gov/pds4/pds/v1"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://pds.nasa.gov/pds4/pds/v1
    https://pds.nasa.gov/pds4/pds/v1/PDS4_PDS_1P00.xsd">

  <name>Example Dictionary</name>
  <ldd_version_id>1.0.0.0</ldd_version_id>
  <dictionary_type>Discipline</dictionary_type>
  <full_name>Your Name</full_name>
  <steward_id>img</steward_id>
  <namespace_id>example</namespace_id>

  <comment>Brief description of the dictionary's purpose and scope.</comment>

  <last_modification_date_time>2025-01-15T12:00:00Z</last_modification_date_time>

  <!-- DD_Attribute definitions go here -->

  <!-- DD_Class definitions go here -->

  <!-- DD_Rule definitions go here (optional) -->

</Ingest_LDD>
```

### Key Attributes Explained

| Attribute | Description | Example |
|-----------|-------------|---------|
| `name` | Human-readable dictionary name | `Imaging` |
| `ldd_version_id` | Version in W.X.Y.Z format | `1.0.0.0` |
| `dictionary_type` | Either `Discipline` or `Mission` | `Discipline` |
| `full_name` | Dictionary steward's full name | `Jane Doe` |
| `steward_id` | PDS node or steward abbreviation | `img`, `geo`, `atm` |
| `namespace_id` | Short namespace prefix (2-8 chars) | `img`, `geom`, `msn` |
| `comment` | Brief description of dictionary | See below |
| `last_modification_date_time` | ISO 8601 timestamp | `2025-01-15T12:00:00Z` |

**Namespace ID rules:**
- Must be unique across all PDS4 dictionaries
- Should be 2-8 characters
- Use lowercase letters only
- Should be mnemonic (related to dictionary purpose)

**Dictionary type:**
- `Discipline` - For discipline-wide dictionaries (imaging, geometry, spectroscopy)
- `Mission` - For mission-specific dictionaries (InSight, OSIRIS-REx)

## Filling Out the DD_Attribute Class

Define each attribute with complete metadata:

```xml
<DD_Attribute>
  <name>horizontal_display_axis</name>
  <version_id>1.0</version_id>
  <local_identifier>example.horizontal_display_axis</local_identifier>
  <nillable_flag>false</nillable_flag>
  <submitter_name>Jane Doe</submitter_name>

  <definition>The horizontal_display_axis attribute identifies, by name,
  the axis of an Array that is intended to be displayed in the horizontal
  dimension on a display device.</definition>

  <DD_Value_Domain>
    <enumeration_flag>false</enumeration_flag>
    <value_data_type>ASCII_Short_String_Collapsed</value_data_type>
    <unit_of_measure_type>Units_of_None</unit_of_measure_type>
  </DD_Value_Domain>
</DD_Attribute>
```

### Enumerated Attributes

For attributes with restricted value lists:

```xml
<DD_Attribute>
  <name>horizontal_display_direction</name>
  <version_id>1.0</version_id>
  <local_identifier>example.horizontal_display_direction</local_identifier>
  <nillable_flag>false</nillable_flag>
  <submitter_name>Jane Doe</submitter_name>

  <definition>The horizontal_display_direction attribute specifies the
  direction across the screen that data should be displayed.</definition>

  <DD_Value_Domain>
    <enumeration_flag>true</enumeration_flag>
    <value_data_type>ASCII_Short_String_Collapsed</value_data_type>
    <unit_of_measure_type>Units_of_None</unit_of_measure_type>

    <DD_Permissible_Value>
      <value>Left to Right</value>
      <value_meaning>Display elements from left to right, with lowest
      indices at the left edge.</value_meaning>
    </DD_Permissible_Value>

    <DD_Permissible_Value>
      <value>Right to Left</value>
      <value_meaning>Display elements from right to left, with lowest
      indices at the right edge.</value_meaning>
    </DD_Permissible_Value>
  </DD_Value_Domain>
</DD_Attribute>
```

**Key points about enumerated attributes:**
- Set `enumeration_flag` to `true`
- Define each permissible value with `DD_Permissible_Value`
- Provide `value_meaning` for each value
- LDDTool automatically generates Schematron validation for these

### Value Data Types

Common PDS4 data types:

| Data Type | Use Case |
|-----------|----------|
| `ASCII_Short_String_Collapsed` | Short text, whitespace normalized |
| `ASCII_Text_Preserved` | Longer text with preserved formatting |
| `ASCII_Integer` | Integer numbers |
| `ASCII_NonNegative_Integer` | Zero or positive integers |
| `ASCII_Real` | Real numbers (floating point) |
| `ASCII_Boolean` | true/false values |
| `ASCII_Date_Time_YMD` | ISO 8601 date/time |

### Units of Measure

If your attribute has units, specify `unit_of_measure_type`:

```xml
<DD_Value_Domain>
  <enumeration_flag>false</enumeration_flag>
  <value_data_type>ASCII_Real</value_data_type>
  <minimum_value>0.0</minimum_value>
  <maximum_value>360.0</maximum_value>
  <unit_of_measure_type>Units_of_Angle</unit_of_measure_type>
</DD_Value_Domain>
```

Common unit types:
- `Units_of_None` - Dimensionless
- `Units_of_Length` - Meters, kilometers, etc.
- `Units_of_Time` - Seconds, days, etc.
- `Units_of_Angle` - Degrees, radians
- `Units_of_Mass` - Kilograms, grams
- See the PDS4 Data Dictionary for complete list

## Filling Out the DD_Class Class

Define classes to group attributes:

```xml
<DD_Class>
  <name>Display_Settings</name>
  <version_id>1.0</version_id>
  <local_identifier>example.Display_Settings</local_identifier>
  <submitter_name>Jane Doe</submitter_name>

  <definition>The Display_Settings class contains attributes that describe
  how Array data should be displayed on a display device.</definition>

  <element_flag>true</element_flag>

  <!-- Associate attributes with this class -->
  <DD_Association>
    <local_identifier>example.horizontal_display_axis</local_identifier>
    <reference_type>attribute_of</reference_type>
    <minimum_occurrences>1</minimum_occurrences>
    <maximum_occurrences>1</maximum_occurrences>
  </DD_Association>

  <DD_Association>
    <local_identifier>example.horizontal_display_direction</local_identifier>
    <reference_type>attribute_of</reference_type>
    <minimum_occurrences>1</minimum_occurrences>
    <maximum_occurrences>1</maximum_occurrences>
  </DD_Association>

  <DD_Association>
    <local_identifier>pds.comment</local_identifier>
    <reference_type>attribute_of</reference_type>
    <minimum_occurrences>0</minimum_occurrences>
    <maximum_occurrences>1</maximum_occurrences>
  </DD_Association>
</DD_Class>
```

### DD_Association Cardinality

Control whether components are required or optional:

| Min | Max | Meaning |
|-----|-----|---------|
| 0 | 1 | Optional, at most one occurrence |
| 1 | 1 | Required, exactly one occurrence |
| 0 | unbounded | Optional, any number of occurrences |
| 1 | unbounded | Required, one or more occurrences |

### Referencing External Namespaces

Reference attributes/classes from other namespaces:

```xml
<DD_Association>
  <!-- Reference attribute from pds namespace -->
  <local_identifier>pds.comment</local_identifier>
  <reference_type>attribute_of</reference_type>
  <minimum_occurrences>0</minimum_occurrences>
  <maximum_occurrences>1</maximum_occurrences>
</DD_Association>

<DD_Association>
  <!-- Reference class from geom namespace -->
  <local_identifier>geom.Coordinate_Space_Definition</local_identifier>
  <reference_type>component_of</reference_type>
  <minimum_occurrences>0</minimum_occurrences>
  <maximum_occurrences>1</maximum_occurrences>
</DD_Association>
```

Format: `namespace_id.element_name`

## Advanced Techniques

### Schematron Rules (DD_Rule)

Define custom validation constraints beyond basic structure:

```xml
<DD_Rule>
  <local_identifier>rule_display_settings_required</local_identifier>
  <rule_context>//example:Display_Settings</rule_context>
  <rule_type>Assert</rule_type>

  <DD_Rule_Statement>
    <rule_type>Assert</rule_type>
    <rule_test>example:horizontal_display_axis and example:horizontal_display_direction</rule_test>
    <rule_message>Both horizontal_display_axis and horizontal_display_direction
    are required when Display_Settings is present.</rule_message>
  </DD_Rule_Statement>
</DD_Rule>
```

**Rule types:**
- `Assert` - Condition must be true
- `Report` - Report when condition is true (for warnings)

**Use cases:**
- Co-occurrence constraints (if A present, B must be present)
- Value range validation (attribute A > attribute B)
- Cross-element validation
- Conditional requirements

### Choice Lists

Allow users to choose between different elements:

```xml
<DD_Association>
  <local_identifier>XSChoice#1</local_identifier>
  <reference_type>attribute_of</reference_type>
  <minimum_occurrences>1</minimum_occurrences>
  <maximum_occurrences>1</maximum_occurrences>
</DD_Association>

<!-- Following associations are part of the choice -->
<DD_Association>
  <local_identifier>example.option_a</local_identifier>
  <reference_type>attribute_of</reference_type>
  <minimum_occurrences>1</minimum_occurrences>
  <maximum_occurrences>1</maximum_occurrences>
</DD_Association>

<DD_Association>
  <local_identifier>example.option_b</local_identifier>
  <reference_type>attribute_of</reference_type>
  <minimum_occurrences>1</minimum_occurrences>
  <maximum_occurrences>1</maximum_occurrences>
</DD_Association>
```

This generates `<xs:choice>` in the schema, allowing either `option_a` or `option_b`.

### Restricting External Attributes

Override an attribute from another namespace with tighter constraints:

```xml
<DD_Attribute>
  <name>comment</name>
  <version_id>1.0</version_id>
  <!-- This is the full unique identifier of the external attribute -->
  <local_identifier>0001_NASA_PDS_1.pds.Discipline_Area.pds.comment</local_identifier>
  <nillable_flag>false</nillable_flag>
  <submitter_name>Jane Doe</submitter_name>

  <definition>In this context, comment must follow specific format rules...</definition>

  <DD_Value_Domain>
    <enumeration_flag>false</enumeration_flag>
    <value_data_type>ASCII_Short_String_Collapsed</value_data_type>
    <minimum_characters>10</minimum_characters>
    <maximum_characters>255</maximum_characters>
  </DD_Value_Domain>
</DD_Attribute>
```

**Use this technique to:**
- Add tighter constraints to generic attributes
- Restrict value ranges
- Change cardinality
- Add enumerated values

## Best Practices Summary

1. **Plan before coding** - Sketch out your class hierarchy on paper
2. **Use clear names** - Follow PDS4 naming conventions consistently
3. **Write complete definitions** - Avoid TBD, explain units, provide context
4. **Keep it simple** - Avoid unnecessary complexity and deep hierarchies
5. **Validate early, validate often** - Use XML validation before running LDDTool
6. **Test thoroughly** - Create example labels using your dictionary
7. **Document assumptions** - Add XML comments explaining design decisions
8. **Coordinate dependencies** - Contact stewards before referencing external namespaces
9. **Version carefully** - Follow semantic versioning for `ldd_version_id`
10. **Seek review** - Get feedback from PDS consultants before finalizing

## Common Mistakes to Avoid

1. Defining classes before attributes
2. Missing `unit_of_measure_type` for attributes with units
3. Using TBD in definitions
4. Inconsistent naming conventions
5. Deep class hierarchies (>3-4 levels)
6. Excessive cross-namespace dependencies
7. Overly complex Schematron rules
8. Not testing with actual labels
9. Forgetting to update `last_modification_date_time`
10. Not coordinating with PDS node consultants

## Getting Help

If you need assistance:

- **PDS Node Consultants** - Contact your discipline or mission node
- **PDS Engineering Node** - Email pds-operator@jpl.nasa.gov
- **GitHub Issues** - https://github.com/NASA-PDS/pds4-information-model/issues
- **PDS4 Documentation** - https://pds.nasa.gov/datastandards/documents/

## Additional Resources

- **PDS4 Information Model Specification** - https://pds.nasa.gov/datastandards/documents/im/
- **PDS4 Data Dictionary** - https://pds.nasa.gov/datastandards/dictionaries/
- **LDDTool Documentation** - See the Operation section of this documentation
- **Example Dictionaries** - https://github.com/pds-data-dictionaries/
