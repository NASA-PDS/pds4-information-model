// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// * Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// * Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// * Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package gov.nasa.pds.model.plugin;

import java.util.ArrayList;
import java.util.Iterator;

public class DOMDataType extends ISOClassOAIS11179 {
  String type;
  String character_constraint;
  String formation_rule;
  String maximum_characters;
  String maximum_value;
  String minimum_characters;
  String minimum_value;
  String xml_schema_base_type;
  String character_encoding;

  ArrayList<String> pattern;

  public DOMDataType() {
    type = "TBD_type";
    character_constraint = "TBD_character_constraint";
    formation_rule = "TBD_formation_rule";
    maximum_characters = "TBD_maximum_characters";
    maximum_value = "TBD_maximum_value";
    minimum_characters = "TBD_minimum_characters";
    minimum_value = "TBD_minimum_value";
    xml_schema_base_type = "TBD_xml_schema_base_type";
    character_encoding = "TBD_character_encoding";

    pattern = new ArrayList<>();
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCharacter_constraint() {
    return character_constraint;
  }

  public void setCharacter_constraint(String character_constraint) {
    this.character_constraint = character_constraint;
  }

  public String getFormation_rule() {
    return formation_rule;
  }

  public void setFormation_rule(String formation_rule) {
    this.formation_rule = formation_rule;
  }

  public String getMaximumCharacters(boolean forceBound) {
    String lValue = this.maximum_characters;
    if (forceBound) {
      if (lValue.indexOf("TBD") == 0 || lValue.compareTo("") == 0
          || lValue.compareTo("2147483647") == 0) {
        return "Unbounded";
      }
    }
    if (lValue.compareTo("") == 0) {
      return "TBD_maximum_characters";
    }
    return lValue;
  }

  public void setMaximum_characters(String maximum_characters) {
    this.maximum_characters = maximum_characters;
  }

  public String getMaximumValue(boolean forceBound) {
    String lValue = this.maximum_value;
    if (forceBound) {
      if (lValue.indexOf("TBD") == 0 || lValue.compareTo("") == 0
          || lValue.compareTo("2147483647") == 0 || lValue.compareTo("4294967295") == 0
          || lValue.compareTo("INF") == 0) {
        return "Unbounded";
      }
    }
    if (lValue.compareTo("") == 0) {
      return "TBD_maximum_value";
    }
    return lValue;
  }

  public void setMaximum_value(String maximum_value) {
    this.maximum_value = maximum_value;
  }

  public String getMinimumCharacters(boolean forceBound) {
    String lValue = this.minimum_characters;
    if (forceBound) {
      if (lValue.indexOf("TBD") == 0 || lValue.compareTo("") == 0
          || lValue.compareTo("-2147483648") == 0) {
        return "Unbounded";
      }
    }
    if (lValue.compareTo("") == 0) {
      return "TBD_minimum_characters";
    }
    return lValue;
  }

  public void setMinimum_characters(String minimum_characters) {
    this.minimum_characters = minimum_characters;
  }

  public String getMinimumValue(boolean forceBound) {
    String lValue = this.minimum_value;
    if (forceBound) {
      if (lValue.indexOf("TBD") == 0 || lValue.compareTo("") == 0
          || lValue.compareTo("-2147483648") == 0 || lValue.compareTo("-INF") == 0) {
        return "Unbounded";
      }
    }
    if (lValue.compareTo("") == 0) {
      return "TBD_minimum_value";
    }
    return lValue;
  }

  public void setMinimum_value(String minimum_value) {
    this.minimum_value = minimum_value;
  }

  public String getXml_schema_base_type() {
    return xml_schema_base_type;
  }

  public void setXml_schema_base_type(String xml_schema_base_type) {
    this.xml_schema_base_type = xml_schema_base_type;
  }

  public String getCharacter_encoding() {
    return character_encoding;
  }

  public void setCharacter_encoding(String character_encoding) {
    this.character_encoding = character_encoding;
  }

  public ArrayList<String> getPattern() {
    return pattern;
  }

  public void setPattern(ArrayList<String> pattern) {
    this.pattern = pattern;
  }

  // new - not currently being used - needs testing
  public void setDataTypeAttrs(DOMClass lClass) {
    this.setIdentifier(DMDocument.masterNameSpaceIdNCLC, lClass.title);
    this.title = lClass.title;
    this.nameSpaceIdNC = lClass.nameSpaceIdNC;
    this.type = lClass.title;
    this.versionId = lClass.versionId;
    this.definition = lClass.definition;

    // for each attribute of the class
    for (Iterator<DOMProp> j = lClass.ownedAttrArr.iterator(); j.hasNext();) {
      DOMProp lDOMProp = j.next();
      if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
        DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;

        // set the character_constraint
        if (lDOMAttr.title.compareTo("character_constraint") == 0) {
          String lVal =
              DOMInfoModel.getSingletonValueUpdate(lDOMAttr.valArr, this.character_constraint);
          if (lVal != null) {
            this.character_constraint = lVal;
          }
        }
        // set the formation_rule
        if (lDOMAttr.title.compareTo("formation_rule") == 0) {
          String lVal = DOMInfoModel.getSingletonValueUpdate(lDOMAttr.valArr, this.formation_rule);
          if (lVal != null) {
            this.formation_rule = lVal;
          }
        }
        // set the maximum_characters
        if (lDOMAttr.title.compareTo("maximum_characters") == 0) {
          String lVal =
              DOMInfoModel.getSingletonValueUpdate(lDOMAttr.valArr, this.maximum_characters);
          if (lVal != null) {
            this.maximum_characters = lVal;
          }
        }
        // set the maximum_value
        if (lDOMAttr.title.compareTo("maximum_value") == 0) {
          String lVal = DOMInfoModel.getSingletonValueUpdate(lDOMAttr.valArr, this.maximum_value);
          if (lVal != null) {
            this.maximum_value = lVal;
          }
        }
        // set the minimum_characters
        if (lDOMAttr.title.compareTo("minimum_characters") == 0) {
          String lVal =
              DOMInfoModel.getSingletonValueUpdate(lDOMAttr.valArr, this.minimum_characters);
          if (lVal != null) {
            this.minimum_characters = lVal;
          }
        }
        // set the minimum_value
        if (lDOMAttr.title.compareTo("minimum_value") == 0) {
          String lVal = DOMInfoModel.getSingletonValueUpdate(lDOMAttr.valArr, this.minimum_value);
          if (lVal != null) {
            this.minimum_value = lVal;
          }
        }
        // set the xml_schema_base_type
        if (lDOMAttr.title.compareTo("xml_schema_base_type") == 0) {
          String lVal =
              DOMInfoModel.getSingletonValueUpdate(lDOMAttr.valArr, this.xml_schema_base_type);
          if (lVal != null) {
            this.xml_schema_base_type = lVal;
          }
        }
        // set the character_encoding
        if (lDOMAttr.title.compareTo("character_encoding") == 0) {
          String lVal =
              DOMInfoModel.getSingletonValueUpdate(lDOMAttr.valArr, this.character_encoding);
          if (lVal != null) {
            this.character_encoding = lVal;
          }
        }
        // set the pattern
        /*
         * if (lAttr.title.compareTo("pattern") == 0) { String lVal =
         * DOMInfoModel.getSingletonValueUpdate(lAttr.valArr, lDataType.pattern); if (lVal != null)
         * { lDataType.pattern = lVal; } }
         */
        if (lDOMAttr.title.compareTo("pattern") == 0) {
          for (Iterator<String> l = lDOMAttr.valArr.iterator(); l.hasNext();) {
            String lValue = l.next();
            this.pattern.add(lValue);
          }
        }
      }
    }
    return;
  }
}
