package gov.nasa.pds.model.plugin;
import java.util.ArrayList;
import java.io.File;

public class SchemaFileDefn {
	// identifier is the namespace id, without colon, and in caps; it must be unique within the PDS
	String identifier;

	// each namespace has a version identifier
	String versionId;

	// steward_id is the primary steward identifier; the steward assigns the namespaceid 
	String stewardId;

	// nameSpaceId is the name Space Id; it must be unique within the PDS; NC stands for No Colon
	String nameSpaceIdNC;
	String nameSpaceId;

	// isMaster indicates the master schema
	boolean isMaster;
	
	// isLDD indicates an LDD schema
	boolean isLDD;
	
	// file name, extension, and directory path for the schema file
	String fileNameUC;
	String fileNameNC;
//	String fileExtension;
	String directoryPath;
	
	// stewarArr is a list of all stewards that share authority over this namespaceid
	ArrayList <String> stewardArr; 
	
	// various version identifiers
	String ont_version_id;						// 0.1.0.0.a
	String lab_version_id;						// 0100a
	String sch_version_id;						// 1.0.0
	String ns_version_id;						// 01
	String identifier_version_id;				// 0.1
	
	public SchemaFileDefn (String id) {
		identifier = id;
		versionId = "0.0.0.0.n";
		stewardId = "TBD_stewardId";
//		nameSpaceIdNC = "TBD_nameSpaceIdNC";
//		nameSpaceId = "TBD_nameSpaceId";
		nameSpaceIdNC = id;
		nameSpaceId = nameSpaceIdNC + ":";
		isMaster = false;
		isLDD = false;
		fileNameUC = id.toUpperCase();					// forced upper case
		fileNameNC = id;								// no change
//		fileExtension = "xsd";
		File targetDir = new File(DMDocument.outputDirPath + "SchemaXML4");
		targetDir.mkdirs();
		directoryPath = DMDocument.outputDirPath + "SchemaXML4/" + "PDS4_";		
		stewardArr = new ArrayList <String>();
	} 
	
	public void setStewardIds (String lSteward) {
		stewardId = lSteward;
		stewardArr.add(lSteward);
		return;
	}	
	
	public void setNameSpaceIds (String lNameSpaceIdNC) {
		nameSpaceIdNC = lNameSpaceIdNC;
		nameSpaceId = lNameSpaceIdNC + ":";
		return;
	}		
	
	//	set the various version identifiers
	public void setVersionIds () {		
		if (versionId.length() <= 3) versionId += ".0.0";
		String lVersionId = versionId; 						// 1.0.0.0[b]
		ont_version_id = lVersionId; 						// 1.0.0.0[b]
		sch_version_id = lVersionId.substring(0,7);			// 1.0.0.0
		identifier_version_id = lVersionId.substring(0,3);	// 1.0
		lVersionId = DMDocument.replaceString(lVersionId, ".", "");		// 1000[b]
		if (lVersionId.length() > 4) {
			lab_version_id = lVersionId.substring(0,5);		// 1000B from Beta
		} else {
			lab_version_id = lVersionId;					// 1000[b]	
		}
		ns_version_id = lVersionId.substring(0,1);	// 1
		return;
	}
}
