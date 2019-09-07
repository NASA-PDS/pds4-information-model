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
import java.io.IOException;
import java.util.Date;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * Parses an XML file for the MDPTN (Model Driven Parser, Translator, and Notation generator).
 */
public class XMLDocParserDomMDPTNConfig extends Object
{
	// initialize the attribute structures
//	TreeMap <String, String> Table2Map; 	
	TreeMap <String, DispDefn> classDispositionMap; 	
	
	//No generics
	Document dom;
	String lFullName;				
	String lLastModificationDateTime;				
//	String lPds4MergeFlag;				
	Date rTodaysDate = new Date();
	String sTodaysDate  = rTodaysDate.toString();

	
	public XMLDocParserDomMDPTNConfig () {
//		Table2Map = new TreeMap <String, String> (); 	
		classDispositionMap = new TreeMap <String, DispDefn> (); 	
	}

	public TreeMap <String, DispDefn> getXMLTable2(String fileSpecName) throws java.io.IOException {
		//parse the xml file and get the dom object
//		System.out.println("debug starting getLocalDD");
		parseXmlFile(fileSpecName);
		
		//get the table
		getTable();
		return classDispositionMap;
	}	

	private void parseXmlFile(String fileSpecName){
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			//parse using builder to get DOM representation of the XML file
			dom = db.parse(fileSpecName);
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private void getTable() {		
		//get the root element
		Element lRootElem = dom.getDocumentElement();
		if(lRootElem != null) {
//			System.out.println(">>info    - " + "Found required file: " + DMDocument.dataDirPath +  "MDPTNConfigClassDisp.xml");
			getRecord (lRootElem);
		} else {
			System.out.println(">>error   - " + "Did not find required file: " + DMDocument.dataDirPath +  "MDPTNConfigClassDisp.xml");
		}
	}
	
	private void getRecord(Element lElem){
		//get a nodelist of Record> elements
		NodeList nl = lElem.getElementsByTagName("Record");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				//get the elements
				Element el = (Element)nl.item(i);
				//get the field
				getField(el);
			}
		}
	}
	
	private void getField(Element lElem){
		//get a nodelist of Record elements
		NodeList nl = lElem.getElementsByTagName("Field");
		if(nl != null && nl.getLength() > 0) {
			
			// get the config file record type
			Element el = (Element)nl.item(0);
			if(el == null) {
				System.out.println("error - null item(0)");
				return;
			}
			String lConfigFileRecType = el.getFirstChild().getNodeValue();
			if (lConfigFileRecType  == null) {
				System.out.println("error - null lConfigFileRecType");
				return;
			}			
			
			// get the use flag
			el = (Element)nl.item(1);
			if(el == null) {
				System.out.println("error - null item(1)");
				return;
			}
			String lFlagValue = el.getFirstChild().getNodeValue();
			if (lFlagValue == null) {
				System.out.println("error - null lFlagValue");
				return;
			}

			// get the name
			el = (Element)nl.item(2);
			if(el == null) {
				System.out.println("error - null item(2)");
				return;
			}
			String lNameValue = el.getFirstChild().getNodeValue();
			if (lNameValue == null) {
				System.out.println("error - null lNameValue");
				return;
			}
//			System.out.println("debug lNameValue:" + lNameValue);
			
			// get the disposition
			el = (Element)nl.item(3);
			if(el == null) {
				System.out.println("error - null item(3)");
				return;
			}
			String lDispValue = el.getFirstChild().getNodeValue();
			if (lDispValue == null) {
				System.out.println("error - null lDispValue");
				return;
			}

			// get the section
			el = (Element)nl.item(4);
			if(el == null) {
				System.out.println("error - null item(4)");
				return;
			}
			String lSection = el.getFirstChild().getNodeValue();
			if (lSection == null) {
				System.out.println("error - null lSection");
				return;
			}
			
			// get the internal namespace
			el = (Element)nl.item(5);
			if(el == null) {
				System.out.println("error - null item(5)");
				return;
			}
			String lIntNSId = el.getFirstChild().getNodeValue();
			if (lIntNSId == null) {
				System.out.println("error - null lIntNSId");
				return;
			}
			
			// get the internal steward
			el = (Element)nl.item(6);
			if(el == null) {
				System.out.println("error - null item(6)");
				return;
			}
			String lSteward = el.getFirstChild().getNodeValue();
			if (lSteward == null) {
				System.out.println("error - null lSteward");
				return;
			}
			
			//			System.out.println("debug lDispValue:" + lDispValue);
			if (classDispositionMap.get(lNameValue) == null) {
				DispDefn classDisposition = new DispDefn (lNameValue);
				classDisposition.used = lFlagValue;
				classDisposition.disposition = lDispValue ;
				classDisposition.section = lSection;
				classDisposition.intNSId = lIntNSId;
				classDisposition.intSteward = lSteward;
				classDispositionMap.put(lNameValue, classDisposition);
			} else {
				if (! DMDocument.LDDToolFlag) {
					System.out.println(">>error   - Duplicate class exists - Class Identifier:" + lNameValue);
				}
			}
		}
	}
	
	private String getTextValue(Element ele, String tagName) {
		String textVal = "TBD_Ingest_LDD";
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}
		return textVal;
	}
	
	private int getIntValue(Element ele, String tagName) {
		//in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele,tagName));
	}
}
					