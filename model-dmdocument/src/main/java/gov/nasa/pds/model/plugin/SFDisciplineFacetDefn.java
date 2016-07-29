package gov.nasa.pds.model.plugin;

import java.util.ArrayList;

public class SFDisciplineFacetDefn {
	String identifier;
	String disciplineName;
	
	ArrayList <String> groupFacet1IdArr;
	ArrayList <String> groupFacet2IdArr;
	ArrayList <SFGroupFacetDefn> groupFacet1Arr;
	ArrayList <SFGroupFacetDefn> groupFacet2Arr;

	public SFDisciplineFacetDefn (String lClassName, String lDisciplineName) {
		identifier = lDisciplineName + "." + lClassName;
		disciplineName = DMDocument.replaceString (lDisciplineName, "+", " "); 		
		groupFacet1IdArr = new ArrayList <String>();
		groupFacet2IdArr = new ArrayList <String>();
		groupFacet1Arr = new ArrayList <SFGroupFacetDefn>();
		groupFacet2Arr = new ArrayList <SFGroupFacetDefn>();
	} 
}
