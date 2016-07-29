package gov.nasa.pds.model.plugin;
import java.io.PrintWriter;
import java.util.*;

class WritePropMapsSchema extends Object {
	
	PrintWriter prDDPins;
	
	public WritePropMapsSchema () {
		return;
	}
	
	// write the property maps
	public void writePropertyMaps (PrintWriter lPrinter) throws java.io.IOException {
	    prDDPins = lPrinter;
	    
	    // write the open to the XML block to contain the JSON code
	    prDDPins.println(" ");		
	    prDDPins.println("  <" + "xs:" + "annotation>");		
	    prDDPins.println("    <" + "xs:" + "documentation>");
	    
		printPDDPHdr();
		printPDDPBody ();
		printPDDPFtr();
		
	    // write the close to the XML block to contain the JSON code		
		prDDPins.println("    </" + "xs:" + "documentation>");		
		prDDPins.println("  </" + "xs:" + "annotation>");
	}	
	
	// Print the JSON Header
	public void printPDDPHdr () {
		prDDPins.println("[");
		prDDPins.println("  {");
		prDDPins.println("    " + formValue("dataDictionary") + ": {");
		prDDPins.println("      " + formValue("Title") + ": " + formValue("PDS4 Data Dictionary") + " ,");
		prDDPins.println("      " + formValue("Version") + ": " +  formValue(InfoModel.ont_version_id) + " ,");
		prDDPins.println("      " + formValue("Date") + ": " +  formValue(DMDocument.sTodaysDate) + " ,");
		prDDPins.println("      " + formValue("Description") + ": " + formValue("This document is a dump of the contents of the PDS4 Data Dictionary") + " ,");
	}
	
	// Format the Boolean String for JSON
	public String formBooleanValue(boolean lBoolean) {
		String rString = "" + lBoolean;
		return formValue(rString);
	}

	// Format the String for JSON
	public String formValue(String lString) {
		String rString = lString;
		if (rString == null) rString = "null";
		if (rString.indexOf("TBD") == 0) rString = "null";
		rString = InfoModel.escapeJSONChar(rString);
		rString = "\"" + rString + "\"";
		return rString;
	}

	// Print the Footer
	public  void printPDDPFtr () {
		prDDPins.println("    }");
		prDDPins.println("  }");
		prDDPins.println("]");
	}
	
//	print the body
	public  void printPDDPBody () {
		prDDPins.println("      " + formValue("PropertyMapDictionary") + ": [");
		printPropertyMaps (prDDPins);
		prDDPins.println("      ]");		
	}
	
	// Print the Property Maps
	public  void printPropertyMaps (PrintWriter prDDPins) {
		boolean isFirst = true;
		for (Iterator<PropertyMapsDefn> i = InfoModel.masterPropertyMapsArr.iterator(); i.hasNext();) {
			PropertyMapsDefn lPropertyMaps = (PropertyMapsDefn) i.next();
			if (isFirst) {
				prDDPins.println("        {" + formValue("PropertyMaps") + ": {");
				isFirst = false;
			} else {
				prDDPins.println("      , {" + formValue("PropertyMaps") + ": {");			
			}	
			prDDPins.println("            " + formValue("identifier") + ": " + formValue(lPropertyMaps.rdfIdentifier) + " ,");		
			prDDPins.println("            " + formValue("title") + ": " + formValue(lPropertyMaps.title) + " ,");		
			prDDPins.println("            " + formValue("namespace_id") + ": " + formValue(lPropertyMaps.namespace_id) + " ,");			
			prDDPins.println("            " + formValue("description") + ": " + formValue(lPropertyMaps.description) + " ,");	
			prDDPins.println("            " + formValue("external_property_map_id") + ": " + formValue(lPropertyMaps.external_property_map_id));	
			printPropertyMap (lPropertyMaps, prDDPins); 
			prDDPins.println("          }");
			prDDPins.println("        }");
		}
	}	
	
	// Print the Property Map
	public  void printPropertyMap (PropertyMapsDefn lPropertyMaps, PrintWriter prDDPins) {
		boolean isFirst = true;
		if (lPropertyMaps.propertyMapArr.isEmpty()) return;
		prDDPins.println("          , " + formValue("propertyMapList") + ": [");	
		for (Iterator<PropertyMapDefn> i = lPropertyMaps.propertyMapArr.iterator(); i.hasNext();) {
			PropertyMapDefn lPropertyMap = (PropertyMapDefn) i.next();
			if (isFirst) {
				prDDPins.println("              {" + formValue("PropertyMap") + ": {");
				isFirst = false;
			} else {
				prDDPins.println("            , {" + formValue("PropertyMap") + ": {");			
			}	
			prDDPins.println("                  " + formValue("identifier") + ": " + formValue(lPropertyMap.identifier) + " ,");		
			prDDPins.println("                  " + formValue("title") + ": " + formValue(lPropertyMap.title) + " ,");			
			prDDPins.println("                  " + formValue("model_object_id") + ": " + formValue(lPropertyMap.model_object_id) + " ,");	
			prDDPins.println("                  " + formValue("model_object_type") + ": " + formValue(lPropertyMap.model_object_type) + " ,");	
			prDDPins.println("                  " + formValue("instance_id") + ": " + formValue(lPropertyMap.instance_id) + " ,");		
			prDDPins.println("                  " + formValue("external_namespace_id") + ": " + formValue(lPropertyMap.external_namespace_id) + " ,");	
			prDDPins.println("                  " + formValue("description") + ": " + formValue(lPropertyMap.description));	
			printPropertyMapEntrys (lPropertyMap, prDDPins); 
			prDDPins.println("                 }");
			prDDPins.println("              }");
		}
		prDDPins.println("            ]");
	}
	
	// Print the Property Map Entrys
	public  void printPropertyMapEntrys (PropertyMapDefn lPropertyMap, PrintWriter prDDPins) {
		boolean isFirst = true;
		if (lPropertyMap.propertyMapEntryArr.isEmpty()) return;
		prDDPins.println("                , " + formValue("propertyMapEntryList") + ": [");	
		for (Iterator<PropertyMapEntryDefn> i = lPropertyMap.propertyMapEntryArr.iterator(); i.hasNext();) {
			PropertyMapEntryDefn lPropertyMapEntry = (PropertyMapEntryDefn) i.next();
			if (isFirst) {
				prDDPins.println("                    {" + formValue("PropertyMapEntry") + ": {");	
				isFirst = false;
			} else {
				prDDPins.println("                  , {" + formValue("PropertyMapEntry") + ": {");					
			}				
			prDDPins.println("                        " + formValue("property_name") + ": " + formValue(lPropertyMapEntry.property_name) + " ,");		
			prDDPins.println("                        " + formValue("property_value") + ": " + formValue(lPropertyMapEntry.property_value) + " }");		
			prDDPins.println("                    }");
		}
		prDDPins.println("                    ]");	
	}
	
}
