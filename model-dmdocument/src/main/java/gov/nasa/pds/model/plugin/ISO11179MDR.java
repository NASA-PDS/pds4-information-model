package gov.nasa.pds.model.plugin;
import java.io.*;
import java.util.*;

class ISO11179MDR extends Object {
	TreeMap <String, String> hashCodedPersmissibleValueMap;
	TreeMap <String, String> hashCodedValueMeaningMap;
	TreeMap <String, String> valueChangeMap;

	public ISO11179MDR () {
		hashCodedPersmissibleValueMap = new TreeMap <String, String> ();
		hashCodedValueMeaningMap = new TreeMap <String, String> ();
		valueChangeMap = new TreeMap <String, String> ();
		
		// *** Values to be updated go here ***
		// - changes need to be made to UpperModel.pont
		// - resulting dd11179_Gen.pins needs to be made active
		// - the value meaning text needs to be updated in dd11179.pins (using Protege) if the value is in the text.
		// - comment out the following after testing
		// - this approach is needed since a hash of the value is used as part of the value meaning primary key.
		
//		valueChangeMap.put("W*m*\\*-3*sr*\\*-1", "W*m**-3*sr**-1");
//		valueChangeMap.put("W*m*\\*-2*sr*\\*-1*Hz*\\*-1", "W*m**-2*sr**-1*Hz**-1");
//		valueChangeMap.put("W*m*\\*-2*sr*\\*-1*nm*\\*-1", "W*m**-2*sr**-1*nm**-1");
//		valueChangeMap.put("W*m*\\*-2*sr*\\*-1*um*\\*-1", "W*m**-2*sr**-1*um**-1");
//		valueChangeMap.put("W*m*\\*-3*sr*\\*-1", "W*m**-3*sr**-1");
//		valueChangeMap.put("uW*cm*\\*-2*sr*\\*-1*um*\\*-1", "uW*cm**-2*sr**-1*um**-1");
		
//		valueChangeMap.put("W*m*\\*-3", "W*m**-3");
//		valueChangeMap.put("W*m*\\*-2*Hz*\\*-1", "W*m**-2*Hz**-1");
//		valueChangeMap.put("W*m*\\*-2*nm*\\*-1", "W*m**-2*nm**-1");
//		valueChangeMap.put("W*m*\\*-3", "W*m**-3");
//		valueChangeMap.put("uW*cm*\\*-2*um*\\*-1", "uW*cm**-2*um**-1");
		
		
		Set <String> set9 = InfoModel.master11179DataDict.keySet();
		Iterator <String> iter9 = set9.iterator();
		while(iter9.hasNext()) {
			String lId = (String) iter9.next();
			InstDefn lInst = (InstDefn) InfoModel.master11179DataDict.get(lId);
			
//			System.out.println("\ndebug ISO11179MDR - lInst.rdfIdentifier:" + lInst.rdfIdentifier);
//			System.out.println("debug ISO11179MDR - lInst.identifier:" + lInst.identifier);
//			System.out.println("debug ISO11179MDR - lId:" + lId);
//			System.out.println("debug ISO11179MDR - lInst.title:" + lInst.title);

			if (lInst.className.compareTo ("PermissibleValue") == 0) {
				ArrayList <String> lValArr = lInst.genSlotMap.get("value");
				if (lValArr != null) {
					String lValue = lValArr.get(0);
//					System.out.println("debug ISO11179MDR - GOT - lValue:" + lValue);			
					// title:vm.0001_NASA_PDS_1.pds.DD_Class_Full.steward_id.111209
					String lKey = lInst.title;
					int lOffset = lInst.title.lastIndexOf(".");
					if (lOffset > -1) lKey = lInst.title.substring(0, lOffset);
//					System.out.println("debug ISO11179MDR - GOT - lKey:" + lKey);							
					lValArr = lInst.genSlotMap.get("usedIn");
					if (lValArr != null) {
						String lVMId = lValArr.get(0);
//						System.out.println("debug ISO11179MDR - GOT - lVMId:" + lVMId);	
						String lVMIdExt = "ValueMeaning" + "." + lVMId; 
//						System.out.println("debug ISO11179MDR - GOT - lVMIdExt:" + lVMIdExt);	
						InstDefn lVMInst = (InstDefn) InfoModel.master11179DataDict.get(lVMIdExt);
						if (lVMInst != null) {
//							System.out.println("debug ISO11179MDR - GOT - lVMInst.title:" + lVMInst.title);							
							lValArr = lVMInst.genSlotMap.get("description");
							if (lValArr != null) {
								String lDescription = lValArr.get(0);

								// *** Value Updates Go Here ***							
//								String lValueNew = valueChangeMap.get(lValue);
//								if (lValueNew != null) {
//									System.out.println("debug ISO11179MDR - Updating Value - lValue:" + lValue + " - lValueNew:" + lValueNew);
//									lValue = lValueNew;
//								}		
								lKey += "." + lValue;
//								System.out.println("debug ISO11179MDR - ADDING - lKey:" + lKey);
								hashCodedValueMeaningMap.put(lKey, lDescription);
							}
						}
					}
				}
			}
		}
		
		// *** New Value Meanings Here ***
/*		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Discipline_Facets.discipline_name.Spectroscopy", "light wavelength/wave number spectra of any and all dimensionalities");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Discipline_Facets.discipline_name.Imaging", "any non-spectroscopic image, of any dimensionality (color, movies, etc.)");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Discipline_Facets.discipline_name.Photopolarimetry", "photometry/polarimetry not resulting in images or spectra");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Discipline_Facets.discipline_name.Fields", "electric and magnetic field data");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Discipline_Facets.discipline_name.Particles", "ions, electrons, and anything not classified as 'dust'");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Discipline_Facets.discipline_name.Atmospheres", "atmospheric observations");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Discipline_Facets.discipline_name.Geosciences", "other geoscience data, like surface chemistry");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Discipline_Facets.discipline_name.Radio science", "other radio science data, like gravity observations");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Discipline_Facets.discipline_name.Ring-Moon Systems", "other ring or ring-moon system data");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Discipline_Facets.discipline_name.Small Bodies", "other small body observations, including dust, shape models, etc.");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Discipline_Facets.discipline_name.Flux Measurements", "photometry/polarimetry not resulting in images or spectra");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Tabulated", "A table with one spectrum per record, possibly for a different target in each record");		
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Linear", "A table representing a single spectrum");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.2D", "A 2D array in which each pixel value directly represents the spectral measurement at that point.  The physical axes of the array align with the axes of the spectral data.");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Spectral Image", "A 2D image of a spectrum, as projected on a focal plane. There may be multiple orders present, and the axes of the spectrum/spectra typically do not align with the edges of the image.");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Spectral Cube", "Any 3D structure containing spectral data");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Grayscale", "2D data, typically with two spatial axes");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Color", "3D data, typically with two spatial axes, where the third axis contains display color levels (RGB, CMYK, false color, etc.)");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Movie", "3D data, typically with two spatial and one temporal axis");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Color Movie", "4D data, typically with two spatial, one color, and one temporal axis");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Photometry", "Photon measurements resulting in magnitudes, colors, etc.");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Polarimetry", "Linear and circular polarization studies");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Structure", "Atmospheric structure observations");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Meteorology", "Meteorological observations");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Satellite Astrometry", "Astrometry of natural satellite in ring systems");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Ring Occultation Profile", "");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Ring Thermal Map", "");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Ring Compositional Map", "");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Gas Study", "Gas measurements of all kinds and targets");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Dust Study", "Dust measurements of all kinds and all targets");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Dynamical Properties", "Orbital parameters, proper elements, etc.");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Historical Reference", "Discovery circumstances, reference collections");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Lightcurve", "Light intensity variation with time, including rotational, secular, and occultation light curves");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Meteoritics", "Meteoroid streams, meteorite studies");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Physical Properties", "Mass, density, albedo, etc.");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Production Rates", "Quantification of mass loss from, e.g., the nucleus of a comet: molecular production rates, Af?, etc.");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Shape Model", "Shape models, slope models, terrain models, elevation models, etc.");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Taxonomy", "Physical and dynamical taxonomies of small bodies");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Electric", "Electrical field measurements");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Magnetic", "Magnetic field measurements");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Ions", "Ion measurements");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Electrons", "Electron measurements");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Neutrals", "Neutral particle measurements");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet2.facet2.Background", "slowly varying background field (typically at less than 100 Hz)");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet2.facet2.Waves", "higher frequency field variations and/or oscillations (typically at greater than 100 Hz).");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet2.facet2.Plasma", "< 30keV");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet2.facet2.Cosmic Ray", "> 10 MeV");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet2.facet2.Solar Energetic", "0.1-10MeV");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet2.facet2.Energetic", "> 30keV"); */

		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Spectral Image", "A 2D image of a spectrum, as projected on a focal plane. There may be multiple orders present, and the axes of the spectrum/spectra typically do not align with the edges of the image.");

		return;
	}
		
//	set up the 11179 elements
	public void ISO11179MDRSetup (InfoModel masterInfoModel) {
		for (Iterator <AttrDefn> i = InfoModel.getAttArrByTitleStewardClassSteward().iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
//			System.out.println("\ndebug getAttrISOAttr1 lattr.rdfIdentifier:" + lAttr.rdfIdentifier);
			if (lAttr.isUsedInModel && lAttr.propType.compareTo("ATTRIBUTE") == 0) {
				getAttrISOAttr (lAttr);
			}	
		}
	}	

	// Get the ISO components of a data element
	private void getAttrISOAttr (AttrDefn lattr) {
	//  add the namespace to the above	
		lattr.administrationRecordValue = DMDocument.administrationRecordValue;
		lattr.versionIdentifierValue = DMDocument.versionIdentifierValue;
		lattr.submitter = DMDocument.submitterValue;
		lattr.registeredByValue = DMDocument.registeredByValue;
		lattr.registrationAuthorityIdentifierValue = DMDocument.registrationAuthorityIdentifierValue;
	}
	
//	scan through the master attribute list and overwrite with the latest from the 11179 DD database.
	public void OverwriteFrom11179DataDict () {
		HashMap <String, ArrayList<String>> lInstMap = new HashMap <String, ArrayList<String>> ();
//		InstDefn lInst;
		String lSuffix;
		String lInstId;
		String lVal;
		boolean isEnumerated = false;
		boolean lDebugFlag = false;
		
		// iterate through the master attribute array
		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (lAttr.propType.compareTo("INSTANCE") == 0) continue;
			if (lAttr.isFromLDD) continue;
			PDSObjDefn lParentClass = InfoModel.masterMOFClassTitleMap.get(lAttr.className);
			if (lParentClass.isFromLDD) continue;
			
//			System.out.println("\ndebug OverwriteFrom11179DataDict - lAttr.identifier:" + lAttr.identifier);
			
			lSuffix = DMDocument.registrationAuthorityIdentifierValue + "." + lAttr.attrNameSpaceIdNC + "." + lAttr.className + "." + lAttr.title;
			// e.g. 0001_NASA_PDS_1.AAREADME.encoding_type 			
			
			lInstId = "DataElement" + "." + "DE" + "." + lSuffix;
			// e.g. DataElement.DE.0001_NASA_PDS_1.AAREADME.encoding_type 			
 
//			System.out.println("\ndebug Overwrite with 11179 - attempting to update lInstId:" + lInstId);			
			InstDefn lDEInst = InfoModel.master11179DataDict.get(lInstId);
			if (lDEInst != null) {
				lInstMap = lDEInst.genSlotMap;

				// update steward
				lVal = ProtPins11179DD.getStringValueUpdate (false, lInstMap, "steward", lAttr.steward);                        
				if (lVal != null) {
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.steward:" + lAttr.steward + "  - with lVal:" + lVal); }
					lAttr.steward = lVal;
				}
				
				// update classConcept
				lVal = ProtPins11179DD.getStringValueUpdate (false, lInstMap, "expressedBy", lAttr.classConcept);                        
				if (lVal != null) {
					lVal = lVal.substring(4, lVal.length());
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.classConcept:" + lAttr.classConcept + "  - with lVal:" + lVal); }					
					lAttr.classConcept = lVal;
			}
			
				lVal = ProtPins11179DD.getStringValueUpdate (false, lInstMap, "isNillable", "false");                        
				if (lVal != null && lVal.compareTo("true") == 0) {
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.isNilable:" + lAttr.isNilable + "  - with lVal:" + lVal); }
//					System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.isNilable:" + lAttr.isNilable + "  - with lVal:" + lVal);
					lAttr.isNilable = true;
				}							

			} else {
//				if (! (lAttr.isFromLDD || lAttr.title.compareTo("%3ANAME") == 0)) {
				if (! (lAttr.title.compareTo("%3ANAME") == 0)) {
					System.out.println(">>error   - 11179 data dictionary attribute is missing for overwrite - Identifier:" + lInstId);
				}
			}
			boolean hasVD = false;
			isEnumerated = false;
			lInstId = "EnumeratedValueDomain" + "." + "EVD" + "." + lSuffix;
			InstDefn lVDInst = InfoModel.master11179DataDict.get(lInstId);
			if (lAttr.isEnumerated && lVDInst != null) {
				hasVD = true;
				isEnumerated = true;
			} else {
				lInstId = "NonEnumeratedValueDomain" + "." + "NEVD" + "." + lSuffix;
				lVDInst = InfoModel.master11179DataDict.get(lInstId);
				if (lVDInst != null) {
					hasVD = true;
				}
			}
			if (hasVD) {
				lInstMap = lVDInst.genSlotMap;					
				// dataType
				lVal = ProtPins11179DD.getStringValueUpdate (false, lInstMap, "datatype", lAttr.valueType);                        
				if (lVal != null) {
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.valueType:" + lAttr.valueType + "  - with lVal:" + lVal); }
					lAttr.valueType = lVal;
				}
				
				// dataConcept
				lVal = ProtPins11179DD.getStringValueUpdate (false, lInstMap, "representedBy2", lAttr.dataConcept);                        
				if (lVal != null) {
					lVal = lVal.substring(3, lVal.length());
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.dataConcept:" + lAttr.dataConcept + "  - with lVal:" + lVal); }
					lAttr.dataConcept = lVal;	
				}
				
				// Minimum Value
				lVal = ProtPins11179DD.getStringValueUpdate (false, lInstMap, "minimumValue", lAttr.minimum_value);                        
				if (lVal != null) {
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.minimum_value:" + lAttr.minimum_value + "  - with lVal:" + lVal); }
					lAttr.minimum_value = lVal;
				}				
				
				// Maximum Value
				lVal = ProtPins11179DD.getStringValueUpdate (false, lInstMap, "maximumValue", lAttr.maximum_value);                        
				if (lVal != null) {
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.maximum_value:" + lAttr.maximum_value + "  - with lVal:" + lVal); }
					lAttr.maximum_value = lVal;
				}	

				// Minimum Characters
				lVal = ProtPins11179DD.getStringValueUpdate (false, lInstMap, "minimumCharacterQuantity", lAttr.minimum_characters);                        
				if (lVal != null) {
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.minimum_characters:" + lAttr.minimum_characters + "  - with lVal:" + lVal); }
//					System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.minimum_characters:" + lAttr.minimum_characters + "  - with lVal:" + lVal);
					lAttr.minimum_characters = lVal;
				}	
				
				// Maximum Characters
				lVal = ProtPins11179DD.getStringValueUpdate (false, lInstMap, "maximumCharacterQuantity", lAttr.maximum_characters);                        
				if (lVal != null) {
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.maximum_characters:" + lAttr.maximum_characters + "  - with lVal:" + lVal); }
					lAttr.maximum_characters = lVal;
				}	
				
				// Pattern
				lVal = ProtPins11179DD.getStringValueUpdate (true, lInstMap, "pattern", lAttr.pattern);                        
				if (lVal != null) {
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.pattern:" + lAttr.pattern + "  - with lVal:" + lVal); }
					lAttr.pattern = lVal;
				}				
				
				// Unit_of_measure_type
				lVal = ProtPins11179DD.getStringValueUpdate (false, lInstMap, "unitOfMeasure", lAttr.unit_of_measure_type);               
				if (lVal != null) {
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.unit_of_measure_type:" + lAttr.unit_of_measure_type + "  - with lVal:" + lVal); }
					lAttr.unit_of_measure_type = lVal;
				}				
				
				// default_unit_id
				lVal = ProtPins11179DD.getStringValueUpdate (false, lInstMap, "defaultUnitId", lAttr.default_unit_id);               
				if (lVal != null) {
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.default_unit_id:" + lAttr.default_unit_id + "  - with lVal:" + lVal); }
					lAttr.default_unit_id = lVal;
				}
				
//				System.out.println("debug OverwriteFrom11179DataDict -2- lAttr.identifier:" + lAttr.identifier);

				// get the permissible values and value meanings
				if  (lAttr.valArr != null && lAttr.valArr.size() > 0) {
//					System.out.println("\ndebug OverwriteFrom11179DataDict - Value Meanings - lAttr.identifier:" + lAttr.identifier);
					
					ArrayList <String> lValArrSorted = new ArrayList <String> ();
					for (Iterator<String> j = lAttr.valArr.iterator(); j.hasNext();) {
						String lValue = (String) j.next();
						lValArrSorted.add(lValue);
					}
					Collections.sort(lValArrSorted);
					for (Iterator<String> j = lValArrSorted.iterator(); j.hasNext();) {
						String lValue = (String) j.next();
//						System.out.println("debug OverwriteFrom11179DataDict - Value Meanings - lValue:" + lValue);
						
						String lId = "0001_NASA_PDS_1" + "." + lAttr.className + "." + lAttr.title + "_Value." + lValue;
						PermValueDefn lPermValueDefn = new PermValueDefn (lId, lValue);
						lAttr.permValueArr.add(lPermValueDefn);

						// title:vm.0001_NASA_PDS_1.pds.DD_Class_Full.steward_id.111209
						String lKey = "pv.0001_NASA_PDS_1." + lAttr.classNameSpaceIdNC + "." + lAttr.className + "." + lAttr.title + "." + lValue;
//						System.out.println("debug Overwrite with 11179 - GOT - lKey:" + lKey);
						String lValueMeaning = hashCodedValueMeaningMap.get(lKey);
						if (lValueMeaning != null) {
//							System.out.println("debug Overwrite with 11179 - FOUND - lValueMeaning:" + lValueMeaning);
							lPermValueDefn.value_meaning = lValueMeaning;
						}
					}
				}

//	***Need to check this out ***
//  						if (lAttr.title.compareTo ("encoding_type") == 0) {
//							if (lVal.compareTo("Binary") == 0) {
//								lPermValueDefn = new PermValueDefn (lVal, lVal, "The data object contains binary and possibly some character encoded data.");
//							} else {
//								lPermValueDefn = new PermValueDefn (lVal, lVal, "The data object contains only character encoded data, for example ASCII or UTF-8 encoded characters.");
//							}
//							lAttr.permValueArr.add(lPermValueDefn);
//						} else {
//							lPermValueDefn = new PermValueDefn (lVal, lVal, "TBD_value_meaning");
//							lAttr.permValueArr.add(lPermValueDefn);
//						}
			}
		}
	}
	
//	Get class order
	public void getClassOrder () {
//		System.out.println("\ndebug getClassOrder"); 
		// iterate through ISO 11179 dictionary selecting the properties
		ArrayList <InstDefn> lDataDictArr = new ArrayList <InstDefn> (InfoModel.master11179DataDict.values());		
		for (Iterator<InstDefn> i = lDataDictArr.iterator(); i.hasNext();) {
			InstDefn lInst = (InstDefn) i.next();
//			System.out.println("debug getClassOrder lInst.identifier:" + lInst.identifier);
			if (lInst.title.indexOf("PR.") != 0) continue;
			String lId = lInst.title.substring(3);
//			System.out.println("debug getClassOrder lTitle:" + lTitle);
//			if ((lInst.identifier.compareTo("") == 0) || (lInst.identifier.indexOf("TBD") == 0)) continue;
//			String lId = lInst.identifier;
			HashMap <String, ArrayList<String>> lInstMap = lInst.genSlotMap;
			if (lInstMap == null) continue;
			ArrayList <String> lValArr = lInstMap.get("classOrder");
			if (lValArr == null) continue;
			String lClassOrder = lValArr.get(0);
			if (lClassOrder.compareTo("") == 0) continue;
//			System.out.println("\ndebug getClassOrder lId:" + lId + " - lClassOrder:" + lClassOrder + " - title:" + lInst.title);
			AssocDefn lAssoc = InfoModel.masterMOFAssocIdMap.get(lId);
			if (lAssoc != null) {
//				System.out.println("debug getClassOrder FOUND Association lId:" + lId);
				lAssoc.classOrder = lClassOrder;
			} else {
				System.out.println("debug getClassOrder NOT FOUND Association lId:" + lId);
			}
		}
		return;
	}	

//	=======================  Utilities ================================================================
		
	/**
	*  sort DEs by class and namespaces
	*/
/*	public ArrayList <String> getSortedDEIds () {
		TreeMap  <String, String> lTreeMap = new TreeMap <String, String>();
		Set <String> set1 = DEMap.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String lId = (String) iter1.next();
			AttrDefn lDE =  (AttrDefn) DEMap.get(lId);
			String rId = lDE.title + ":" + lDE.steward + "." + lDE.className + ":" + lDE.classSteward;
			rId = rId.toUpperCase();
			lTreeMap.put(rId, lId);
		}
		Collection <String> values = lTreeMap.values();		
		return (new ArrayList <String> ( values ));
	}	*/
}
