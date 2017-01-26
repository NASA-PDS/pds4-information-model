package gov.nasa.pds.model.plugin; 
import java.util.ArrayList;

public class DOMPropGroup {						// this class is used to group attributes and classes under a single property
	DOMProp domProp;								// this is the property
	ArrayList <ISOClassOAIS11179> domObjectArr;		// these are the attributes and classes

	public DOMPropGroup () {
		domProp = null; 		
		domObjectArr = new ArrayList <ISOClassOAIS11179>();
	}
}
