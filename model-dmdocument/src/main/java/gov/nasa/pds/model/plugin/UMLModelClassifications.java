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
import java.util.TreeMap;

class UMLModelClassifications extends Object {
  // classification map and array - categories {method, binding, ...} with an order, title,
  // description, and a set of IM classes.
  static TreeMap<String, ClassDOMCCSDSClassificationDefn> classClassificationMap = new TreeMap<>();
  static ArrayList<ClassDOMCCSDSClassificationDefn> classClassificationArr;

  static ArrayList<String> activeClasses;

  static TreeMap<String, String> definedClassToClassificationMap = new TreeMap<>();

  public UMLModelClassifications() {

    // Initialize the maps
    initMapDeclaration();
    initMapProcess();

  }

  // ==========================================================================================================

  public void initMapDeclaration() {
    // For CCSDS Book insert

    // intialize the classifications (applications, services, methods, etc)

    // System Architecture
    // uid uid order title indent description
    // classClassificationMap.put("1080", new ClassDOMCCSDSClassificationDefn ("1080", "2010",
    // "Archive", "i3", "An Archive is an organization that intends to preserve information for
    // access and use by a Designated Community."));
    // classClassificationMap.put("1086", new ClassDOMCCSDSClassificationDefn ("1086", "2015", "OAIS
    // Environmental Model", "i3", "The environment surrounding an OAIS."));
    // classClassificationMap.put("1082", new ClassDOMCCSDSClassificationDefn ("1082", "2020", "OAIS
    // Archive", "i3", "An OAIS Archive is an organization that intends to preserve information for
    // access and use by a Designated Community and is OAIS compliant."));
    // classClassificationMap.put("1084", new ClassDOMCCSDSClassificationDefn ("1084", "2030",
    // "Other Archive", "i3", "Other Archives are archives that are not OAIS compliant."));

    // classClassificationMap.put("1000", new ClassDOMCCSDSClassificationDefn ("1000", "2040",
    // "System Architecture", "i3", "The System_Architecture consists of the software, services, and
    // protocols."));
    classClassificationMap.put("1000",
        new ClassDOMCCSDSClassificationDefn("1000", "2040", "Active_Object", "i3",
            "An Active Object is an object that reside in its own thread of control."));
    classClassificationMap.put("1002",
        new ClassDOMCCSDSClassificationDefn("1002", "2042", "External_Object", "i3",
            "An External Object is not a part of the systems' infrastructure."));
    // classClassificationMap.put("1010", new ClassDOMCCSDSClassificationDefn ("1010", "3050",
    // "Registry", "i3", "A Registry maintains a record of objects."));
    // classClassificationMap.put("1020", new ClassDOMCCSDSClassificationDefn ("1020", "3060",
    // "Repository", "i3", "A Repository is a location in which objects are stored and managed."));
    // 1 classClassificationMap.put("1090", new ClassDOMCCSDSClassificationDefn ("1090", "2070",
    // "OAIS Functional Entity", "i3", "A Functional Entity is an entity responsible for a function
    // that is required to ensure the reliable operation of a specific part of an Open Archive
    // Information System."));
    // 1 classClassificationMap.put("1100", new ClassDOMCCSDSClassificationDefn ("1100", "2080",
    // "Other Functions", "i3", "Other Functions are functions that are not defined within OAIS"));
    // 1 classClassificationMap.put("1040", new ClassDOMCCSDSClassificationDefn ("1040", "2090",
    // "OAIS Application", "i3", "An Application is a computer program designed to perform a group
    // of coordinated functions, tasks, or activities for the benefit of the user."));
    // 1 classClassificationMap.put("1042", new ClassDOMCCSDSClassificationDefn ("1042", "2100",
    // "Other Application", "i3", "Other Applications are computer programs that are not defined
    // within OAIS."));
    classClassificationMap.put("1050",
        new ClassDOMCCSDSClassificationDefn("1050", "3110", "Service", "i3",
            "A service is a software component that performs work that benefits another."));
    // 1 classClassificationMap.put("1055", new ClassDOMCCSDSClassificationDefn ("1055", "2120",
    // "OAIS Interface", "i3", "An Interface is the abstraction of a service that only defines the
    // operations supported by that service, but not their implementations."));
    // 1 classClassificationMap.put("1057", new ClassDOMCCSDSClassificationDefn ("1057", "2130",
    // "Other Interface", "i3", "Other Interfaces are interfaces that are not defined within
    // OAIS"));
    classClassificationMap.put("1060", new ClassDOMCCSDSClassificationDefn("1060", "3140", "Method",
        "i3",
        "A Method in object-oriented programming (OOP) is a procedure associated with a message and an object."));
    // classClassificationMap.put("1065", new ClassDOMCCSDSClassificationDefn ("1065", "3145",
    // "Method Signature", "i3", "A Method signature is part of the method declaration and is the
    // combination of the method name and the parameter list."));
    classClassificationMap.put("1070", new ClassDOMCCSDSClassificationDefn("1070", "3150",
        "Binding", "i3",
        "A Binding is a wrapper library that bridges two programming languages, so that a library written for one language can be used in another language."));
    // 1 classClassificationMap.put("1110", new ClassDOMCCSDSClassificationDefn ("1110", "2160",
    // "OAIS Interface", "i3", "An Interface is an abstraction of a service that only defines the
    // operations supported by that service, but not their implementations."));
    // classClassificationMap.put("1115", new ClassDOMCCSDSClassificationDefn ("1115", "2170",
    // "Derived Interface", "i3", "Derived Interfaces are interfaces that are derived from
    // relationships between OAIS Producers, Consumers, and Functional Entities."));

    // Information Architecture
    classClassificationMap.put("1120", new ClassDOMCCSDSClassificationDefn("1120", "3180",
        "Information Architecture", "i3",
        "The Information Architecture consists of the information model and supporting artifacts."));
    classClassificationMap.put("1121", new ClassDOMCCSDSClassificationDefn("1121", "3190",
        "Information Model", "i3",
        "An information model is a representation of concepts and the relationships, constraints, rules, and operations to specify data semantics for a chosen domain of discourse."));
    // 1 classClassificationMap.put("1124", new ClassDOMCCSDSClassificationDefn ("1124", "2190",
    // "Information Model", "i3", "An information model is a representation of concepts and the
    // relationships, constraints, rules, and operations to specify data semantics for a chosen
    // domain of discourse."));
    classClassificationMap.put("1030", new ClassDOMCCSDSClassificationDefn("1030", "3200",
        "Data_Structure", "i3",
        "A Data Structure is a particular way of organizing data in a computer so that it can be used efficiently."));
    // 2 classClassificationMap.put("1600", new ClassDOMCCSDSClassificationDefn ("1600", "4600",
    // "Implementation", "i3", "Implementation is the realization of an application, model, design,
    // specification, standard, algorithm, or policy."));
    // 2 classClassificationMap.put("2100", new ClassDOMCCSDSClassificationDefn ("2100", "5100",
    // "UseCase", "i3", "A use case is a list of actions or event steps typically defining the
    // interactions between a role and a system to achieve a goal."));
    // classClassificationMap.put("1150", new ClassDOMCCSDSClassificationDefn ("1150", "1150",
    // "title", "i3", "A definition"));
    // classClassificationMap.put("1160", new ClassDOMCCSDSClassificationDefn ("1160", "1160",
    // "title", "i3", "A definition"));
    // classClassificationMap.put("1170", new ClassDOMCCSDSClassificationDefn ("1170", "1170",
    // "title", "i3", "A definition"));
    // classClassificationMap.put("1180", new ClassDOMCCSDSClassificationDefn ("1180", "1180",
    // "title", "i3", "A definition"));
    // classClassificationMap.put("1190", new ClassDOMCCSDSClassificationDefn ("1190", "1190",
    // "title", "i3", "A definition"));
    // classClassificationMap.put("1200", new ClassDOMCCSDSClassificationDefn ("1200", "1200",
    // "title", "i3", "A definition"));
    // classClassificationMap.put("1210", new ClassDOMCCSDSClassificationDefn ("1210", "1210",
    // "title", "i3", "A definition"));
    classClassificationMap.put("9999",
        new ClassDOMCCSDSClassificationDefn("9999", "9999", "TBD", "i3", "TBD"));

    definedClassToClassificationMap.put("Producer", "1002");
    definedClassToClassificationMap.put("Consumer", "1002");
    // definedClassToClassificationMap.put("Management", "1086");


    // definedClassToClassificationMap.put("System_Architecture", "1000");
    // 3 definedClassToClassificationMap.put("Active_Object", "1000");
    definedClassToClassificationMap.put("Registry", "1000");
    definedClassToClassificationMap.put("Repository", "1000");
    definedClassToClassificationMap.put("Archive_Service", "1000");
    definedClassToClassificationMap.put("Search_Platform", "1000");
    definedClassToClassificationMap.put("Harvester", "1000");
    definedClassToClassificationMap.put("Archive_Abstraction", "1000");
    definedClassToClassificationMap.put("Security_Service", "1000");

    // definedClassToClassificationMap.put("Archive_Abstraction_Layer", "9999");


    definedClassToClassificationMap.put("Abstraction_Mapping_Registry", "1010");
    definedClassToClassificationMap.put("Query_Model_Registry", "1010");
    // definedClassToClassificationMap.put("Representation_Information_Registry", "1010");
    definedClassToClassificationMap.put("Persistent_Identifier_Registry", "1010");
    definedClassToClassificationMap.put("Repository_Registry", "1010");
    definedClassToClassificationMap.put("Subscription_Registry", "1010");
    // definedClassToClassificationMap.put("Resource_Registry", "1010");

    // definedClassToClassificationMap.put("Annotation_Registry", "1010");
    // definedClassToClassificationMap.put("Authentication_Registry", "1010");
    // definedClassToClassificationMap.put("License_Registry", "1010");
    // definedClassToClassificationMap.put("Metadata_Registry", "1010");
    // definedClassToClassificationMap.put("Tools_Registry", "1010");
    // definedClassToClassificationMap.put("Data_Type_Registry", "1010");

    definedClassToClassificationMap.put("OAIS_Repository", "1020");
    definedClassToClassificationMap.put("non_OAIS_Repository", "1020");

    // definedClassToClassificationMap.put("Registry_Object", "1030");
    definedClassToClassificationMap.put("regObjFileURIArrList", "1030");
    definedClassToClassificationMap.put("regObjFileURIStatArrList", "1030");
    definedClassToClassificationMap.put("regObjPID", "1030");
    definedClassToClassificationMap.put("regObjPIDArrList", "1030");
    definedClassToClassificationMap.put("regObjPIDStatArrList", "1030");
    definedClassToClassificationMap.put("registryQueryString", "1030");
    definedClassToClassificationMap.put("regIOFileManifest", "1030");
    definedClassToClassificationMap.put("regIPObjStatArrList", "1030");
    definedClassToClassificationMap.put("regObjFileURIPIDArrList", "1030");
    definedClassToClassificationMap.put("regObjFileURIStatArrList", "1030");
    definedClassToClassificationMap.put("message", "1030");
    definedClassToClassificationMap.put("searchEngineQuery", "1030");
    definedClassToClassificationMap.put("searchEngineQueryResponse", "1030");
    definedClassToClassificationMap.put("serviceStatusQuery", "1030");
    definedClassToClassificationMap.put("serviceStatusResponse", "1030");

    definedClassToClassificationMap.put("send", "1030");
    definedClassToClassificationMap.put("submit", "1030");
    definedClassToClassificationMap.put("request", "1030");
    definedClassToClassificationMap.put("invoke", "1030");
    definedClassToClassificationMap.put("progress", "1030");
    definedClassToClassificationMap.put("publishSubscribe", "1030");

    definedClassToClassificationMap.put("Service", "1050");
    definedClassToClassificationMap.put("Consumer_Querys_OAIS", "1050");
    definedClassToClassificationMap.put("Consumer_Receives_DIP", "1050");
    definedClassToClassificationMap.put("Consumer_Requests_Report", "1050");
    definedClassToClassificationMap.put("Consumer_Requests_Information", "1050");
    definedClassToClassificationMap.put("Consumer_Pays_OAIS", "1050");
    definedClassToClassificationMap.put("Negotiate_Submission_Agreement", "1050");
    definedClassToClassificationMap.put("Producer_Submits_SIP", "1050");
    definedClassToClassificationMap.put("Consumer_Receives_DIP", "1050");
    // definedClassToClassificationMap.put("Security_Service", "1050");
    // definedClassToClassificationMap.put("Registry_Service", "1050");
    // definedClassToClassificationMap.put("Repository_Service", "1050");
    // definedClassToClassificationMap.put("Query_Service", "1050");
    // definedClassToClassificationMap.put("Subscription_Service", "1050");
    // definedClassToClassificationMap.put("Transfer_Service", "1050");
    // definedClassToClassificationMap.put("Configure_Search_Service", "1050");
    // definedClassToClassificationMap.put("Archive_Abstraction_Service", "1050");

    // definedClassToClassificationMap.put("Gap_Identification_Service", "1050");
    // definedClassToClassificationMap.put("Orchestration_Service", "1050");
    // definedClassToClassificationMap.put("Annotation Service", "1050");
    // definedClassToClassificationMap.put("Authentication_Service", "1050");
    // definedClassToClassificationMap.put("License_Service", "1050");
    // definedClassToClassificationMap.put("Metadata_Registry_Service", "1050");
    // definedClassToClassificationMap.put("Representation_Information_Registry_Service", "1050");
    // definedClassToClassificationMap.put("Persistent_Identifier_Service", "1050");
    // definedClassToClassificationMap.put("Tool_Service", "1050");
    // definedClassToClassificationMap.put("Data_Type_Service", "1050");


    // 3 definedClassToClassificationMap.put("Method", "1060");
    definedClassToClassificationMap.put("getMetrics", "1060");
    definedClassToClassificationMap.put("getServiceStatus", "1060");
    definedClassToClassificationMap.put("migratelnformationObject", "1060");
    definedClassToClassificationMap.put("packageInformationObject", "1060");
    definedClassToClassificationMap.put("processInformationObject", "1060");
    definedClassToClassificationMap.put("pushlInformationObject", "1060");
    definedClassToClassificationMap.put("pullInformationObject", "1060");
    definedClassToClassificationMap.put("configureArchive", "1060");
    definedClassToClassificationMap.put("putInformationObject", "1060");
    definedClassToClassificationMap.put("putQueryModel", "1060");
    definedClassToClassificationMap.put("queryRegistry", "1060");
    definedClassToClassificationMap.put("registerInformationObject", "1060");
    definedClassToClassificationMap.put("subscribeUser", "1060");
    definedClassToClassificationMap.put("transformInformationObject", "1060");
    definedClassToClassificationMap.put("updateQueryModel", "1060");
    definedClassToClassificationMap.put("validateInformationObjectData", "1060");
    definedClassToClassificationMap.put("validateInformationObjectMetadata", "1060");
    definedClassToClassificationMap.put("validateUser", "1060");
    definedClassToClassificationMap.put("harvestInformationObjectMetadata", "1060");
    definedClassToClassificationMap.put("configureSearchEngine", "1060");
    // definedClassToClassificationMap.put("mapNonOAISStructuresToAIP", "1060");
    definedClassToClassificationMap.put("bagInformationObjects", "1060");
    definedClassToClassificationMap.put("createQueryModel", "1060");
    definedClassToClassificationMap.put("getPersistentIdentifier", "1060");
    definedClassToClassificationMap.put("harvestMetadata", "1060");
    definedClassToClassificationMap.put("packageInformationObjects", "1060");
    definedClassToClassificationMap.put("pullBag", "1060");
    definedClassToClassificationMap.put("pushBag", "1060");
    definedClassToClassificationMap.put("putInformationObjectData", "1060");
    definedClassToClassificationMap.put("putInformationObjectMetadata", "1060");
    definedClassToClassificationMap.put("setDataObject", "1060");
    definedClassToClassificationMap.put("setPID", "1060");
    definedClassToClassificationMap.put("setRepresentationInformation", "1060");
    definedClassToClassificationMap.put("setIdentificationInformation", "1060");
    definedClassToClassificationMap.put("setReferenceInformation", "1060");
    definedClassToClassificationMap.put("setProvenanceInformation", "1060");
    definedClassToClassificationMap.put("setFixityInformation", "1060");
    definedClassToClassificationMap.put("setContextInformation", "1060");
    definedClassToClassificationMap.put("setAccessRightsInformation", "1060");
    definedClassToClassificationMap.put("getPID", "1060");
    definedClassToClassificationMap.put("getPersistentIdentifier", "1060");
    definedClassToClassificationMap.put("getDataObject", "1060");
    definedClassToClassificationMap.put("getDigitalObject", "1060");
    definedClassToClassificationMap.put("getInformationObjectMetadata", "1060");
    definedClassToClassificationMap.put("getInformationObject", "1060");
    definedClassToClassificationMap.put("getInformationObjectData", "1060");
    definedClassToClassificationMap.put("getInformationObjectMetadata", "1060");
    definedClassToClassificationMap.put("getRepresentationInformation", "1060");
    definedClassToClassificationMap.put("getIdentificationInformation", "1060");
    definedClassToClassificationMap.put("getReferenceInformation", "1060");
    definedClassToClassificationMap.put("getProvenanceInformation", "1060");
    definedClassToClassificationMap.put("getFixityInformation", "1060");
    definedClassToClassificationMap.put("getContextInformation", "1060");
    definedClassToClassificationMap.put("getAccessRightsInformation", "1060");
    definedClassToClassificationMap.put("getMapping", "1060");
    definedClassToClassificationMap.put("configureArchive_Abstraction", "1060");
    definedClassToClassificationMap.put("configureHarvester", "1060");
    definedClassToClassificationMap.put("sendMessage", "1060");
    definedClassToClassificationMap.put("receiveMessage", "1060");
    definedClassToClassificationMap.put("getPreservationDescriptionInformation", "1060");
    definedClassToClassificationMap.put("setPreservationDescriptionInformation", "1060");

    definedClassToClassificationMap.put("Protocol", "1070");
    // 3 definedClassToClassificationMap.put("Binding", "1070");
    definedClassToClassificationMap.put("REST_HTTP", "1600");
    definedClassToClassificationMap.put("JAVA_JSON_Data_Archive_Binding", "1600");
    definedClassToClassificationMap.put("JAVA_XML_Data_Archive_Binding", "1600");
    definedClassToClassificationMap.put("Python_XML_Data_Archive_Binding", "1600");
    definedClassToClassificationMap.put("Python_JSON_Data_Archive_Binding", "1600");
    definedClassToClassificationMap.put("Apache_Jena_Fuseki", "1600");
    definedClassToClassificationMap.put("Apache_Solr", "1600");
    definedClassToClassificationMap.put("Bagit", "1600");
    definedClassToClassificationMap.put("Java", "1600");

    definedClassToClassificationMap.put("SPARQL", "1600");
    definedClassToClassificationMap.put("ebXML_Registry", "1600");



    definedClassToClassificationMap.put("Information_Architecture", "1120");
    definedClassToClassificationMap.put("Information_Object", "1121");


    definedClassToClassificationMap.put("Data_Object", "1121");
    definedClassToClassificationMap.put("Physical_Object", "1121");
    definedClassToClassificationMap.put("Digital_Object", "1121");
    definedClassToClassificationMap.put("Conceptual_Object", "1121");
    definedClassToClassificationMap.put("Bit", "1121");

    definedClassToClassificationMap.put("OAIS_AIP", "1121");
    definedClassToClassificationMap.put("AIP", "1121");
    definedClassToClassificationMap.put("AIP_plus_Common", "1121");
    definedClassToClassificationMap.put("AIP_plus_Discipline", "1121");
    definedClassToClassificationMap.put("AIP_plus_Local", "1121");

    definedClassToClassificationMap.put("Dissemination_Information_Package", "1121");
    definedClassToClassificationMap.put("Package_Description", "1121");
    definedClassToClassificationMap.put("Other_Representation_Information", "1121");

    definedClassToClassificationMap.put("Associated_Description", "1121");
    definedClassToClassificationMap.put("Semantic_Information", "1124");
    definedClassToClassificationMap.put("Structure_Information", "1124");
    definedClassToClassificationMap.put("Content_Information", "1121");
    definedClassToClassificationMap.put("Content_Information_Group", "1121");
    definedClassToClassificationMap.put("Descriptive_Information", "1121");
    definedClassToClassificationMap.put("Packaging_Information", "1121");
    definedClassToClassificationMap.put("Preservation_Description_Information", "1121");
    definedClassToClassificationMap.put("Access_Rights_Information", "1121");
    definedClassToClassificationMap.put("Fixity_Information", "1121");
    definedClassToClassificationMap.put("Context_Information", "1121");
    definedClassToClassificationMap.put("Provenance_Information", "1121");
    definedClassToClassificationMap.put("Reference_Information", "1121");
    definedClassToClassificationMap.put("Representation_Information", "1121");
    definedClassToClassificationMap.put("Information_Package", "1121");
    definedClassToClassificationMap.put("Submission_Information_Package", "1121");
    definedClassToClassificationMap.put("Archival_Information_Package", "1121");
    definedClassToClassificationMap.put("Archival_Information_Collection", "1121");
    definedClassToClassificationMap.put("Archival_Information_Unit", "1121");
    definedClassToClassificationMap.put("Content_Data_Object", "1121");
    definedClassToClassificationMap.put("Unit_Description", "1121");
    definedClassToClassificationMap.put("Derived_AIP", "1124");
    definedClassToClassificationMap.put("Information_Property", "1121");
    definedClassToClassificationMap.put("Order_Agreement", "1124");
    // definedClassToClassificationMap.put("Other_Component", "1124");
    definedClassToClassificationMap.put("Overview_Description", "1121");
    definedClassToClassificationMap.put("Representation_Network", "1121");
    definedClassToClassificationMap.put("Submission_Agreement", "1124");
    definedClassToClassificationMap.put("Representation_Network", "1121");
    definedClassToClassificationMap.put("Transformational_Information_Property", "1121");
    definedClassToClassificationMap.put("Data_Structure", "1124");


    // definedClassToClassificationMap.put("Interface", "1110");
    // definedClassToClassificationMap.put("User_Interface", "1110");
    // definedClassToClassificationMap.put("Producer_Interface", "1110");
    // definedClassToClassificationMap.put("Consumer_Interface", "1110");
    // definedClassToClassificationMap.put("Archive_Abstraction_Interface", "1110");
    // definedClassToClassificationMap.put("OAIS_Interface", "1110");
    // definedClassToClassificationMap.put("Producer_Archive_Interface", "1110");
    // definedClassToClassificationMap.put("Administration_develops_Submission/Schedule_Agreement_with_Producer",
    // "1115");
    // definedClassToClassificationMap.put("Administration_sends_Final_Ingest_Report_to_Producer",
    // "1115");
    // definedClassToClassificationMap.put("Administration_sends_Lien_to_Producer", "1115");
    // definedClassToClassificationMap.put("Ingest_sends_Receipt_Confirmation_to_Producer", "1115");
    // definedClassToClassificationMap.put("Ingest_sends_Resubmit_Request_to_Producer", "1115");
    // definedClassToClassificationMap.put("Preservation_Planning_surveys_Producers", "1115");
    // definedClassToClassificationMap.put("Producer_develops_Submission/Schedule_Agreement_with_Administration",
    // "1115");
    // definedClassToClassificationMap.put("Producer_proposes_Product_Technologies_to_Preservation_Planning",
    // "1115");
    // definedClassToClassificationMap.put("Producer_provides_a_Submission_Interface_Package_to_the_Ingest_Functional_Entity",
    // "1115");

    // definedClassToClassificationMap.put("Consumer_Interface", "1115");
    // definedClassToClassificationMap.put("Access_generates_DIP_for_Consumer", "1115");
    // definedClassToClassificationMap.put("Access_produces_Query_Response_for_Consumer", "1115");
    // definedClassToClassificationMap.put("Administration_sends_Bill_to_Consumer", "1115");
    // definedClassToClassificationMap.put("Administration_sends_Information_Requests_to_Consumers",
    // "1115");
    // definedClassToClassificationMap.put("Administration_sends_Special_Request_Response_to_Consumers",
    // "1115");
    // definedClassToClassificationMap.put("Consumer_makes_Payment_to_Administration", "1115");
    // definedClassToClassificationMap.put("Consumer_makes_Special_Service_Request_to_Administration",
    // "1115");
    // definedClassToClassificationMap.put("Consumer_submits_a_Query_Request_to_Access", "1115");
    // definedClassToClassificationMap.put("Consumer_submits_a_Report_Request_to_Access", "1115");
    // definedClassToClassificationMap.put("Consumer_submits_an_Order_Assistance_Request_to_Access",
    // "1115");
    // definedClassToClassificationMap.put("Consumer_submits_Service_Requirements_to_Preservation_Planning",
    // "1115");
    // definedClassToClassificationMap.put("Preservation_Planning_surveys_Consumers", "1115");
    // definedClassToClassificationMap.put("Architecture_Interface", "1115");

    definedClassToClassificationMap.put("Archive_Abstraction_Layer", "9999");
    // definedClassToClassificationMap.put("Product", "9999");

    return;
  }

  // ==========================================================================================================

  public void initMapDeclaration_UML() {

    // intialize the classifications (applications, services, methods, etc)

    // System Architecture
    // uid uid order title indent description
    // classClassificationMap.put("1080", new ClassDOMCCSDSClassificationDefn ("1080", "2010",
    // "Archive", "i3", "An Archive is an organization that intends to preserve information for
    // access and use by a Designated Community."));
    // classClassificationMap.put("1086", new ClassDOMCCSDSClassificationDefn ("1086", "2015", "OAIS
    // Environmental Model", "i3", "The environment surrounding an OAIS."));
    // classClassificationMap.put("1082", new ClassDOMCCSDSClassificationDefn ("1082", "2020", "OAIS
    // Archive", "i3", "An OAIS Archive is an organization that intends to preserve information for
    // access and use by a Designated Community and is OAIS compliant."));
    // classClassificationMap.put("1084", new ClassDOMCCSDSClassificationDefn ("1084", "2030",
    // "Other Archive", "i3", "Other Archives are archives that are not OAIS compliant."));

    // classClassificationMap.put("1000", new ClassDOMCCSDSClassificationDefn ("1000", "2040",
    // "System Architecture", "i3", "The System_Architecture consists of the software, services, and
    // protocols."));
    classClassificationMap.put("1000",
        new ClassDOMCCSDSClassificationDefn("1000", "2040", "Active_Object", "i3",
            "An Active Object is an object that reside in its own thread of control."));
    // classClassificationMap.put("1010", new ClassDOMCCSDSClassificationDefn ("1010", "3050",
    // "Registry", "i3", "A Registry maintains a record of objects."));
    // classClassificationMap.put("1020", new ClassDOMCCSDSClassificationDefn ("1020", "3060",
    // "Repository", "i3", "A Repository is a location in which objects are stored and managed."));
    // 1 classClassificationMap.put("1090", new ClassDOMCCSDSClassificationDefn ("1090", "2070",
    // "OAIS Functional Entity", "i3", "A Functional Entity is an entity responsible for a function
    // that is required to ensure the reliable operation of a specific part of an Open Archive
    // Information System."));
    // 1 classClassificationMap.put("1100", new ClassDOMCCSDSClassificationDefn ("1100", "2080",
    // "Other Functions", "i3", "Other Functions are functions that are not defined within OAIS"));
    // 1 classClassificationMap.put("1040", new ClassDOMCCSDSClassificationDefn ("1040", "2090",
    // "OAIS Application", "i3", "An Application is a computer program designed to perform a group
    // of coordinated functions, tasks, or activities for the benefit of the user."));
    // 1 classClassificationMap.put("1042", new ClassDOMCCSDSClassificationDefn ("1042", "2100",
    // "Other Application", "i3", "Other Applications are computer programs that are not defined
    // within OAIS."));
    classClassificationMap.put("1050",
        new ClassDOMCCSDSClassificationDefn("1050", "3110", "Service", "i3",
            "A service is a software component that performs work that benefits another."));
    // 1 classClassificationMap.put("1055", new ClassDOMCCSDSClassificationDefn ("1055", "2120",
    // "OAIS Interface", "i3", "An Interface is the abstraction of a service that only defines the
    // operations supported by that service, but not their implementations."));
    // 1 classClassificationMap.put("1057", new ClassDOMCCSDSClassificationDefn ("1057", "2130",
    // "Other Interface", "i3", "Other Interfaces are interfaces that are not defined within
    // OAIS"));
    classClassificationMap.put("1060", new ClassDOMCCSDSClassificationDefn("1060", "3140", "Method",
        "i3",
        "A Method in object-oriented programming (OOP) is a procedure associated with a message and an object."));
    // classClassificationMap.put("1065", new ClassDOMCCSDSClassificationDefn ("1065", "3145",
    // "Method Signature", "i3", "A Method signature is part of the method declaration and is the
    // combination of the method name and the parameter list."));
    classClassificationMap.put("1070", new ClassDOMCCSDSClassificationDefn("1070", "3150",
        "Binding", "i3",
        "A Binding is a wrapper library that bridges two programming languages, so that a library written for one language can be used in another language."));
    // 1 classClassificationMap.put("1110", new ClassDOMCCSDSClassificationDefn ("1110", "2160",
    // "OAIS Interface", "i3", "An Interface is an abstraction of a service that only defines the
    // operations supported by that service, but not their implementations."));
    // classClassificationMap.put("1115", new ClassDOMCCSDSClassificationDefn ("1115", "2170",
    // "Derived Interface", "i3", "Derived Interfaces are interfaces that are derived from
    // relationships between OAIS Producers, Consumers, and Functional Entities."));

    // Information Architecture
    classClassificationMap.put("1120", new ClassDOMCCSDSClassificationDefn("1120", "3180",
        "Information Architecture", "i3",
        "The Information Architecture consists of the information model and supporting artifacts."));
    classClassificationMap.put("1121", new ClassDOMCCSDSClassificationDefn("1121", "3190",
        "Information Model", "i3",
        "An information model is a representation of concepts and the relationships, constraints, rules, and operations to specify data semantics for a chosen domain of discourse."));
    // 1 classClassificationMap.put("1124", new ClassDOMCCSDSClassificationDefn ("1124", "2190",
    // "Information Model", "i3", "An information model is a representation of concepts and the
    // relationships, constraints, rules, and operations to specify data semantics for a chosen
    // domain of discourse."));
    classClassificationMap.put("1030", new ClassDOMCCSDSClassificationDefn("1030", "3200",
        "Data_Structure", "i3",
        "A Data Structure is a particular way of organizing data in a computer so that it can be used efficiently."));
    // 2 classClassificationMap.put("1600", new ClassDOMCCSDSClassificationDefn ("1600", "4600",
    // "Implementation", "i3", "Implementation is the realization of an application, model, design,
    // specification, standard, algorithm, or policy."));
    // 2 classClassificationMap.put("2100", new ClassDOMCCSDSClassificationDefn ("2100", "5100",
    // "UseCase", "i3", "A use case is a list of actions or event steps typically defining the
    // interactions between a role and a system to achieve a goal."));
    // classClassificationMap.put("1150", new ClassDOMCCSDSClassificationDefn ("1150", "1150",
    // "title", "i3", "A definition"));
    // classClassificationMap.put("1160", new ClassDOMCCSDSClassificationDefn ("1160", "1160",
    // "title", "i3", "A definition"));
    // classClassificationMap.put("1170", new ClassDOMCCSDSClassificationDefn ("1170", "1170",
    // "title", "i3", "A definition"));
    // classClassificationMap.put("1180", new ClassDOMCCSDSClassificationDefn ("1180", "1180",
    // "title", "i3", "A definition"));
    // classClassificationMap.put("1190", new ClassDOMCCSDSClassificationDefn ("1190", "1190",
    // "title", "i3", "A definition"));
    // classClassificationMap.put("1200", new ClassDOMCCSDSClassificationDefn ("1200", "1200",
    // "title", "i3", "A definition"));
    // classClassificationMap.put("1210", new ClassDOMCCSDSClassificationDefn ("1210", "1210",
    // "title", "i3", "A definition"));
    classClassificationMap.put("9999",
        new ClassDOMCCSDSClassificationDefn("9999", "9999", "TBD", "i3", "TBD"));



    // definedClassToClassificationMap.put("System_Architecture", "1000");
    definedClassToClassificationMap.put("Active_Object", "1000");
    definedClassToClassificationMap.put("Registry", "1000");
    definedClassToClassificationMap.put("Repository", "1000");
    definedClassToClassificationMap.put("Archive_Service", "1000");
    definedClassToClassificationMap.put("Search_Platform", "1000");
    definedClassToClassificationMap.put("Harvester", "1000");
    definedClassToClassificationMap.put("Archive_Abstraction", "1000");
    definedClassToClassificationMap.put("External_Object", "1000");
    // definedClassToClassificationMap.put("Archive_Abstraction_Layer", "9999");

    definedClassToClassificationMap.put("Registry", "1010");
    definedClassToClassificationMap.put("Abstraction_Mapping_Registry", "1010");
    // definedClassToClassificationMap.put("Query_Model_Registry", "1010");
    // definedClassToClassificationMap.put("Representation_Information_Registry", "1010");
    // definedClassToClassificationMap.put("Persistent_Identifier_Registry", "1010");
    // definedClassToClassificationMap.put("Repository_Registry", "1010");
    // definedClassToClassificationMap.put("Subscription_Registry", "1010");
    // definedClassToClassificationMap.put("Resource_Registry", "1010");

    // definedClassToClassificationMap.put("Annotation_Registry", "1010");
    // definedClassToClassificationMap.put("Authentication_Registry", "1010");
    // definedClassToClassificationMap.put("License_Registry", "1010");
    // definedClassToClassificationMap.put("Metadata_Registry", "1010");
    // definedClassToClassificationMap.put("Tools_Registry", "1010");
    // definedClassToClassificationMap.put("Data_Type_Registry", "1010");

    definedClassToClassificationMap.put("Repository", "1020");
    definedClassToClassificationMap.put("OAIS_Repository", "1020");
    definedClassToClassificationMap.put("non_OAIS_Repository", "1020");

    // definedClassToClassificationMap.put("Registry_Object", "1030");
    definedClassToClassificationMap.put("regObjFileURIArrList", "1030");
    definedClassToClassificationMap.put("regObjFileURIStatArrList", "1030");
    definedClassToClassificationMap.put("regObjPID", "1030");
    definedClassToClassificationMap.put("regObjPIDArrList", "1030");
    definedClassToClassificationMap.put("regObjPIDStatArrList", "1030");
    definedClassToClassificationMap.put("registryQueryString", "1030");

    definedClassToClassificationMap.put("Service", "1050");
    definedClassToClassificationMap.put("Consumer_Querys_OAIS", "1050");
    definedClassToClassificationMap.put("Consumer_Receives_DIP", "1050");
    definedClassToClassificationMap.put("Consumer_Requests_Report", "1050");
    definedClassToClassificationMap.put("Consumer_Requests_Information", "1050");
    definedClassToClassificationMap.put("Consumer_Pays_OAIS", "1050");
    definedClassToClassificationMap.put("Negotiate_Submission_Agreement", "1050");
    definedClassToClassificationMap.put("Producer_Submits_SIP", "1050");
    definedClassToClassificationMap.put("Consumer_Receives_DIP", "1050");
    // definedClassToClassificationMap.put("Security_Service", "1050");
    // definedClassToClassificationMap.put("Registry_Service", "1050");
    // definedClassToClassificationMap.put("Repository_Service", "1050");
    // definedClassToClassificationMap.put("Query_Service", "1050");
    // definedClassToClassificationMap.put("Subscription_Service", "1050");
    // definedClassToClassificationMap.put("Transfer_Service", "1050");
    // definedClassToClassificationMap.put("Configure_Search_Service", "1050");
    // definedClassToClassificationMap.put("Archive_Abstraction_Service", "1050");

    // definedClassToClassificationMap.put("Gap_Identification_Service", "1050");
    // definedClassToClassificationMap.put("Orchestration_Service", "1050");
    // definedClassToClassificationMap.put("Annotation Service", "1050");
    // definedClassToClassificationMap.put("Authentication_Service", "1050");
    // definedClassToClassificationMap.put("License_Service", "1050");
    // definedClassToClassificationMap.put("Metadata_Registry_Service", "1050");
    // definedClassToClassificationMap.put("Representation_Information_Registry_Service", "1050");
    // definedClassToClassificationMap.put("Persistent_Identifier_Service", "1050");
    // definedClassToClassificationMap.put("Tool_Service", "1050");
    // definedClassToClassificationMap.put("Data_Type_Service", "1050");


    // 3 definedClassToClassificationMap.put("Method", "1060");
    definedClassToClassificationMap.put("getMetrics", "1060");
    definedClassToClassificationMap.put("getServiceStatus", "1060");
    definedClassToClassificationMap.put("migratelnformationObject", "1060");
    definedClassToClassificationMap.put("packageInformationObject", "1060");
    definedClassToClassificationMap.put("processInformationObject", "1060");
    definedClassToClassificationMap.put("pushlInformationObject", "1060");
    definedClassToClassificationMap.put("pullInformationObject", "1060");
    definedClassToClassificationMap.put("configureArchive", "1060");
    definedClassToClassificationMap.put("putInformationObject", "1060");
    definedClassToClassificationMap.put("putQueryModel", "1060");
    definedClassToClassificationMap.put("queryRegistry", "1060");
    definedClassToClassificationMap.put("registerInformationObject", "1060");
    definedClassToClassificationMap.put("subscribeUser", "1060");
    definedClassToClassificationMap.put("transformInformationObject", "1060");
    definedClassToClassificationMap.put("updateQueryModel", "1060");
    definedClassToClassificationMap.put("validateInformationObjectData", "1060");
    definedClassToClassificationMap.put("validateInformationObjectMetadata", "1060");
    definedClassToClassificationMap.put("validateUser", "1060");
    definedClassToClassificationMap.put("harvestInformationObjectMetadata", "1060");
    definedClassToClassificationMap.put("configureSearchEngine", "1060");
    // definedClassToClassificationMap.put("mapNonOAISStructuresToAIP", "1060");
    definedClassToClassificationMap.put("bagInformationObjects", "1060");
    definedClassToClassificationMap.put("createQueryModel", "1060");
    definedClassToClassificationMap.put("getPersistentIdentifier", "1060");
    definedClassToClassificationMap.put("harvestMetadata", "1060");
    definedClassToClassificationMap.put("packageInformationObjects", "1060");
    definedClassToClassificationMap.put("pullBag", "1060");
    definedClassToClassificationMap.put("pushBag", "1060");
    definedClassToClassificationMap.put("putInformationObjectData", "1060");
    definedClassToClassificationMap.put("putInformationObjectMetadata", "1060");
    definedClassToClassificationMap.put("setDataObject", "1060");
    definedClassToClassificationMap.put("setPID", "1060");
    definedClassToClassificationMap.put("setRepresentationInformation", "1060");
    definedClassToClassificationMap.put("setIdentificationInformation", "1060");
    definedClassToClassificationMap.put("setReferenceInformation", "1060");
    definedClassToClassificationMap.put("setProvenanceInformation", "1060");
    definedClassToClassificationMap.put("setFixityInformation", "1060");
    definedClassToClassificationMap.put("setContextInformation", "1060");
    definedClassToClassificationMap.put("setAccessRightsInformation", "1060");
    definedClassToClassificationMap.put("getPID", "1060");
    definedClassToClassificationMap.put("getPersistentIdentifier", "1060");
    definedClassToClassificationMap.put("getDataObject", "1060");
    definedClassToClassificationMap.put("getDigitalObject", "1060");
    definedClassToClassificationMap.put("getInformationObjectMetadata", "1060");
    definedClassToClassificationMap.put("getInformationObject", "1060");
    definedClassToClassificationMap.put("getInformationObjectData", "1060");
    definedClassToClassificationMap.put("getInformationObjectMetadata", "1060");
    definedClassToClassificationMap.put("getRepresentationInformation", "1060");
    definedClassToClassificationMap.put("getIdentificationInformation", "1060");
    definedClassToClassificationMap.put("getReferenceInformation", "1060");
    definedClassToClassificationMap.put("getProvenanceInformation", "1060");
    definedClassToClassificationMap.put("getFixityInformation", "1060");
    definedClassToClassificationMap.put("getContextInformation", "1060");
    definedClassToClassificationMap.put("getAccessRightsInformation", "1060");
    definedClassToClassificationMap.put("getMapping", "1060");
    definedClassToClassificationMap.put("configureArchive_Abstraction", "1060");
    definedClassToClassificationMap.put("configureHarvester", "1060");
    definedClassToClassificationMap.put("sendMessage", "1060");
    definedClassToClassificationMap.put("receiveMessage", "1060");



    definedClassToClassificationMap.put("Protocol", "1070");
    definedClassToClassificationMap.put("Binding", "1070");
    definedClassToClassificationMap.put("REST_HTTP", "1600");
    definedClassToClassificationMap.put("JAVA_JSON_Data_Archive_Binding", "1600");
    definedClassToClassificationMap.put("JAVA_XML_Data_Archive_Binding", "1600");
    definedClassToClassificationMap.put("Python_XML_Data_Archive_Binding", "1600");
    definedClassToClassificationMap.put("Python_JSON_Data_Archive_Binding", "1600");
    definedClassToClassificationMap.put("Apache_Jena_Fuseki", "1600");
    definedClassToClassificationMap.put("Apache_Solr", "1600");
    definedClassToClassificationMap.put("Bagit", "1600");
    definedClassToClassificationMap.put("Java", "1600");

    definedClassToClassificationMap.put("SPARQL", "1600");
    definedClassToClassificationMap.put("ebXML_Registry", "1600");



    definedClassToClassificationMap.put("Information_Architecture", "1120");
    definedClassToClassificationMap.put("Information_Object", "1121");

    /*
     * 2222 definedClassToClassificationMap.put("Data_Object", "1121");
     * definedClassToClassificationMap.put("Physical_Object", "1121");
     * definedClassToClassificationMap.put("Digital_Object", "1121");
     * definedClassToClassificationMap.put("Conceptual_Object", "1121");
     * definedClassToClassificationMap.put("Bit", "1121");
     * 
     * definedClassToClassificationMap.put("OAIS_AIP", "1121");
     * definedClassToClassificationMap.put("AIP", "1121");
     * definedClassToClassificationMap.put("AIP_plus_Common", "1121");
     * definedClassToClassificationMap.put("AIP_plus_Discipline", "1121");
     * definedClassToClassificationMap.put("AIP_plus_Local", "1121");
     * 
     * definedClassToClassificationMap.put("Dissemination_Information_Package", "1121");
     * definedClassToClassificationMap.put("Package_Description", "1121");
     * definedClassToClassificationMap.put("Other_Representation_Information", "1121");
     * 
     * definedClassToClassificationMap.put("Associated_Description", "1121");
     * definedClassToClassificationMap.put("Semantic_Information", "1124");
     * definedClassToClassificationMap.put("Structure_Information", "1124");
     * definedClassToClassificationMap.put("Content_Information", "1121");
     * definedClassToClassificationMap.put("Content_Information_Group", "1121");
     * definedClassToClassificationMap.put("Descriptive_Information", "1121");
     * definedClassToClassificationMap.put("Packaging_Information", "1121");
     * definedClassToClassificationMap.put("Preservation_Description_Information", "1121");
     * definedClassToClassificationMap.put("Access_Rights_Information", "1121");
     * definedClassToClassificationMap.put("Fixity_Information", "1121");
     * definedClassToClassificationMap.put("Context_Information", "1121");
     * definedClassToClassificationMap.put("Provenance_Information", "1121");
     * definedClassToClassificationMap.put("Reference_Information", "1121");
     * definedClassToClassificationMap.put("Representation_Information", "1121");
     * definedClassToClassificationMap.put("Information_Package", "1121");
     * definedClassToClassificationMap.put("Submission_Information_Package", "1121");
     * definedClassToClassificationMap.put("Archival_Information_Package", "1121");
     * definedClassToClassificationMap.put("Archival_Information_Collection", "1121");
     * definedClassToClassificationMap.put("Archival_Information_Unit", "1121");
     * definedClassToClassificationMap.put("Content_Data_Object", "1121");
     * definedClassToClassificationMap.put("Unit_Description", "1121");
     * definedClassToClassificationMap.put("Derived_AIP", "1124");
     * definedClassToClassificationMap.put("Information_Property", "1121");
     * definedClassToClassificationMap.put("Order_Agreement", "1124"); //
     * definedClassToClassificationMap.put("Other_Component", "1124");
     * definedClassToClassificationMap.put("Overview_Description", "1121");
     * definedClassToClassificationMap.put("Representation_Network", "1121");
     * definedClassToClassificationMap.put("Submission_Agreement", "1124");
     * definedClassToClassificationMap.put("Representation_Network", "1121"); ;
     * definedClassToClassificationMap.put("Transformational_Information_Property", "1121");
     * definedClassToClassificationMap.put("Data_Structure", "1124");
     */

    // definedClassToClassificationMap.put("Interface", "1110");
    // definedClassToClassificationMap.put("User_Interface", "1110");
    // definedClassToClassificationMap.put("Producer_Interface", "1110");
    // definedClassToClassificationMap.put("Consumer_Interface", "1110");
    // definedClassToClassificationMap.put("Archive_Abstraction_Interface", "1110");
    // definedClassToClassificationMap.put("OAIS_Interface", "1110");
    // definedClassToClassificationMap.put("Producer_Archive_Interface", "1110");
    // definedClassToClassificationMap.put("Administration_develops_Submission/Schedule_Agreement_with_Producer",
    // "1115");
    // definedClassToClassificationMap.put("Administration_sends_Final_Ingest_Report_to_Producer",
    // "1115");
    // definedClassToClassificationMap.put("Administration_sends_Lien_to_Producer", "1115");
    // definedClassToClassificationMap.put("Ingest_sends_Receipt_Confirmation_to_Producer", "1115");
    // definedClassToClassificationMap.put("Ingest_sends_Resubmit_Request_to_Producer", "1115");
    // definedClassToClassificationMap.put("Preservation_Planning_surveys_Producers", "1115");
    // definedClassToClassificationMap.put("Producer_develops_Submission/Schedule_Agreement_with_Administration",
    // "1115");
    // definedClassToClassificationMap.put("Producer_proposes_Product_Technologies_to_Preservation_Planning",
    // "1115");
    // definedClassToClassificationMap.put("Producer_provides_a_Submission_Interface_Package_to_the_Ingest_Functional_Entity",
    // "1115");

    // definedClassToClassificationMap.put("Consumer_Interface", "1115");
    // definedClassToClassificationMap.put("Access_generates_DIP_for_Consumer", "1115");
    // definedClassToClassificationMap.put("Access_produces_Query_Response_for_Consumer", "1115");
    // definedClassToClassificationMap.put("Administration_sends_Bill_to_Consumer", "1115");
    // definedClassToClassificationMap.put("Administration_sends_Information_Requests_to_Consumers",
    // "1115");
    // definedClassToClassificationMap.put("Administration_sends_Special_Request_Response_to_Consumers",
    // "1115");
    // definedClassToClassificationMap.put("Consumer_makes_Payment_to_Administration", "1115");
    // definedClassToClassificationMap.put("Consumer_makes_Special_Service_Request_to_Administration",
    // "1115");
    // definedClassToClassificationMap.put("Consumer_submits_a_Query_Request_to_Access", "1115");
    // definedClassToClassificationMap.put("Consumer_submits_a_Report_Request_to_Access", "1115");
    // definedClassToClassificationMap.put("Consumer_submits_an_Order_Assistance_Request_to_Access",
    // "1115");
    // definedClassToClassificationMap.put("Consumer_submits_Service_Requirements_to_Preservation_Planning",
    // "1115");
    // definedClassToClassificationMap.put("Preservation_Planning_surveys_Consumers", "1115");
    // definedClassToClassificationMap.put("Architecture_Interface", "1115");

    definedClassToClassificationMap.put("Archive_Abstraction_Layer", "9999");
    // definedClassToClassificationMap.put("Product", "9999");

    return;
  }


  // ==========================================================================================================

  public void initMapDeclarationAll() {

    // intialize the classifications (applications, services, methods, etc)

    // System Architecture
    // uid uid order title indent description
    // classClassificationMap.put("1080", new ClassDOMCCSDSClassificationDefn ("1080", "2010",
    // "Archive", "i3", "An Archive is an organization that intends to preserve information for
    // access and use by a Designated Community."));
    // classClassificationMap.put("1086", new ClassDOMCCSDSClassificationDefn ("1086", "2015", "OAIS
    // Environmental Model", "i3", "The environment surrounding an OAIS."));
    // classClassificationMap.put("1082", new ClassDOMCCSDSClassificationDefn ("1082", "2020", "OAIS
    // Archive", "i3", "An OAIS Archive is an organization that intends to preserve information for
    // access and use by a Designated Community and is OAIS compliant."));
    // classClassificationMap.put("1084", new ClassDOMCCSDSClassificationDefn ("1084", "2030",
    // "Other Archive", "i3", "Other Archives are archives that are not OAIS compliant."));

    // classClassificationMap.put("1000", new ClassDOMCCSDSClassificationDefn ("1000", "2040",
    // "System Architecture", "i3", "The System_Architecture consists of the software, services, and
    // protocols."));
    classClassificationMap.put("1000",
        new ClassDOMCCSDSClassificationDefn("1000", "2040", "Active_Object", "i3",
            "An Active Object is an object that reside in its own thread of control."));
    // classClassificationMap.put("1010", new ClassDOMCCSDSClassificationDefn ("1010", "3050",
    // "Registry", "i3", "A Registry maintains a record of objects."));
    // classClassificationMap.put("1020", new ClassDOMCCSDSClassificationDefn ("1020", "3060",
    // "Repository", "i3", "A Repository is a location in which objects are stored and managed."));
    // 1 classClassificationMap.put("1090", new ClassDOMCCSDSClassificationDefn ("1090", "2070",
    // "OAIS Functional Entity", "i3", "A Functional Entity is an entity responsible for a function
    // that is required to ensure the reliable operation of a specific part of an Open Archive
    // Information System."));
    // 1 classClassificationMap.put("1100", new ClassDOMCCSDSClassificationDefn ("1100", "2080",
    // "Other Functions", "i3", "Other Functions are functions that are not defined within OAIS"));
    // 1 classClassificationMap.put("1040", new ClassDOMCCSDSClassificationDefn ("1040", "2090",
    // "OAIS Application", "i3", "An Application is a computer program designed to perform a group
    // of coordinated functions, tasks, or activities for the benefit of the user."));
    // 1 classClassificationMap.put("1042", new ClassDOMCCSDSClassificationDefn ("1042", "2100",
    // "Other Application", "i3", "Other Applications are computer programs that are not defined
    // within OAIS."));
    classClassificationMap.put("1050",
        new ClassDOMCCSDSClassificationDefn("1050", "3110", "Service", "i3",
            "A service is a software component that performs work that benefits another."));
    // 1 classClassificationMap.put("1055", new ClassDOMCCSDSClassificationDefn ("1055", "2120",
    // "OAIS Interface", "i3", "An Interface is the abstraction of a service that only defines the
    // operations supported by that service, but not their implementations."));
    // 1 classClassificationMap.put("1057", new ClassDOMCCSDSClassificationDefn ("1057", "2130",
    // "Other Interface", "i3", "Other Interfaces are interfaces that are not defined within
    // OAIS"));
    classClassificationMap.put("1060", new ClassDOMCCSDSClassificationDefn("1060", "3140", "Method",
        "i3",
        "A Method in object-oriented programming (OOP) is a procedure associated with a message and an object."));
    // classClassificationMap.put("1065", new ClassDOMCCSDSClassificationDefn ("1065", "3145",
    // "Method Signature", "i3", "A Method signature is part of the method declaration and is the
    // combination of the method name and the parameter list."));
    classClassificationMap.put("1070", new ClassDOMCCSDSClassificationDefn("1070", "3150",
        "Binding", "i3",
        "A Binding is a wrapper library that bridges two programming languages, so that a library written for one language can be used in another language."));
    // 1 classClassificationMap.put("1110", new ClassDOMCCSDSClassificationDefn ("1110", "2160",
    // "OAIS Interface", "i3", "An Interface is an abstraction of a service that only defines the
    // operations supported by that service, but not their implementations."));
    // classClassificationMap.put("1115", new ClassDOMCCSDSClassificationDefn ("1115", "2170",
    // "Derived Interface", "i3", "Derived Interfaces are interfaces that are derived from
    // relationships between OAIS Producers, Consumers, and Functional Entities."));

    // Information Architecture
    classClassificationMap.put("1120", new ClassDOMCCSDSClassificationDefn("1120", "3180",
        "Information Architecture", "i3",
        "The Information Architecture consists of the information model and supporting artifacts."));
    classClassificationMap.put("1121", new ClassDOMCCSDSClassificationDefn("1121", "3190",
        "Information Model", "i3",
        "An information model is a representation of concepts and the relationships, constraints, rules, and operations to specify data semantics for a chosen domain of discourse."));
    // 1 classClassificationMap.put("1124", new ClassDOMCCSDSClassificationDefn ("1124", "2190",
    // "Information Model", "i3", "An information model is a representation of concepts and the
    // relationships, constraints, rules, and operations to specify data semantics for a chosen
    // domain of discourse."));
    classClassificationMap.put("1030", new ClassDOMCCSDSClassificationDefn("1030", "3200",
        "Data_Structure", "i3",
        "A Data Structure is a particular way of organizing data in a computer so that it can be used efficiently."));
    // 2 classClassificationMap.put("1600", new ClassDOMCCSDSClassificationDefn ("1600", "4600",
    // "Implementation", "i3", "Implementation is the realization of an application, model, design,
    // specification, standard, algorithm, or policy."));
    // 2 classClassificationMap.put("2100", new ClassDOMCCSDSClassificationDefn ("2100", "5100",
    // "UseCase", "i3", "A use case is a list of actions or event steps typically defining the
    // interactions between a role and a system to achieve a goal."));
    // classClassificationMap.put("1150", new ClassDOMCCSDSClassificationDefn ("1150", "1150",
    // "title", "i3", "A definition"));
    // classClassificationMap.put("1160", new ClassDOMCCSDSClassificationDefn ("1160", "1160",
    // "title", "i3", "A definition"));
    // classClassificationMap.put("1170", new ClassDOMCCSDSClassificationDefn ("1170", "1170",
    // "title", "i3", "A definition"));
    // classClassificationMap.put("1180", new ClassDOMCCSDSClassificationDefn ("1180", "1180",
    // "title", "i3", "A definition"));
    // classClassificationMap.put("1190", new ClassDOMCCSDSClassificationDefn ("1190", "1190",
    // "title", "i3", "A definition"));
    // classClassificationMap.put("1200", new ClassDOMCCSDSClassificationDefn ("1200", "1200",
    // "title", "i3", "A definition"));
    // classClassificationMap.put("1210", new ClassDOMCCSDSClassificationDefn ("1210", "1210",
    // "title", "i3", "A definition"));
    classClassificationMap.put("9999",
        new ClassDOMCCSDSClassificationDefn("9999", "9999", "TBD", "i3", "TBD"));



    // definedClassToClassificationMap.put("Archive", "1080");
    // definedClassToClassificationMap.put("OAIS", "1082");
    // definedClassToClassificationMap.put("Non_OAIS", "1084");
    // definedClassToClassificationMap.put("Planetary_Science_Data_Archive", "1084");
    // definedClassToClassificationMap.put("Earth_Science_Data_Archive", "1084");
    // definedClassToClassificationMap.put("Astronomy_Data_Archive", "1084");
    // definedClassToClassificationMap.put("Mission_Housekeeping_Archive", "1084");

    definedClassToClassificationMap.put("Producer", "1000");
    definedClassToClassificationMap.put("Consumer", "1000");
    // definedClassToClassificationMap.put("Management", "1086");

    // definedClassToClassificationMap.put("System_Architecture", "1000");
    definedClassToClassificationMap.put("Active_Object", "1000");
    definedClassToClassificationMap.put("Registry", "1000");
    definedClassToClassificationMap.put("Repository", "1000");
    definedClassToClassificationMap.put("Archive_Service", "1000");
    definedClassToClassificationMap.put("Search_Platform", "1000");
    definedClassToClassificationMap.put("Harvester", "1000");
    definedClassToClassificationMap.put("Archive_Abstraction", "1000");
    definedClassToClassificationMap.put("External_Object", "1000");
    // definedClassToClassificationMap.put("Archive_Abstraction_Layer", "9999");

    definedClassToClassificationMap.put("Registry", "1010");
    definedClassToClassificationMap.put("Abstraction_Mapping_Registry", "1010");
    // definedClassToClassificationMap.put("Query_Model_Registry", "1010");
    // definedClassToClassificationMap.put("Representation_Information_Registry", "1010");
    // definedClassToClassificationMap.put("Persistent_Identifier_Registry", "1010");
    // definedClassToClassificationMap.put("Repository_Registry", "1010");
    // definedClassToClassificationMap.put("Subscription_Registry", "1010");
    // definedClassToClassificationMap.put("Resource_Registry", "1010");

    // definedClassToClassificationMap.put("Annotation_Registry", "1010");
    // definedClassToClassificationMap.put("Authentication_Registry", "1010");
    // definedClassToClassificationMap.put("License_Registry", "1010");
    // definedClassToClassificationMap.put("Metadata_Registry", "1010");
    // definedClassToClassificationMap.put("Tools_Registry", "1010");
    // definedClassToClassificationMap.put("Data_Type_Registry", "1010");

    definedClassToClassificationMap.put("Repository", "1020");
    definedClassToClassificationMap.put("OAIS_Repository", "1020");
    definedClassToClassificationMap.put("non_OAIS_Repository", "1020");

    // definedClassToClassificationMap.put("Registry_Object", "1030");
    definedClassToClassificationMap.put("regObjFileURIArrList", "1030");
    definedClassToClassificationMap.put("regObjFileURIStatArrList", "1030");
    definedClassToClassificationMap.put("regObjPID", "1030");
    definedClassToClassificationMap.put("regObjPIDArrList", "1030");
    definedClassToClassificationMap.put("regObjPIDStatArrList", "1030");
    definedClassToClassificationMap.put("registryQueryString", "1030");

    // definedClassToClassificationMap.put("Application", "1040");
    // definedClassToClassificationMap.put("Producer_Application", "1040");
    // definedClassToClassificationMap.put("Consumer_Application", "1040");
    // definedClassToClassificationMap.put("Administration_Application", "1040");
    // definedClassToClassificationMap.put("Archival_Storage_Application", "1040");
    // definedClassToClassificationMap.put("Access_Application", "1040");
    // definedClassToClassificationMap.put("Data_Management_Application", "1040");
    // definedClassToClassificationMap.put("Ingest_Application", "1040");
    // definedClassToClassificationMap.put("Preservation_Planning_Application", "1040");
    // definedClassToClassificationMap.put("Archive_Abstraction_Application", "1040");

    // definedClassToClassificationMap.put("Access_Aid", "1040");
    // definedClassToClassificationMap.put("Finding_Aid", "1040");
    // definedClassToClassificationMap.put("Ordering_Aid", "1040");
    // definedClassToClassificationMap.put("Retrieving_Aid", "1040");

    definedClassToClassificationMap.put("Service", "1050");
    definedClassToClassificationMap.put("Consumer_Querys_OAIS", "1050");
    definedClassToClassificationMap.put("Consumer_Receives_DIP", "1050");
    definedClassToClassificationMap.put("Consumer_Requests_Report", "1050");
    definedClassToClassificationMap.put("Consumer_Requests_Information", "1050");
    definedClassToClassificationMap.put("Consumer_Pays_OAIS", "1050");
    definedClassToClassificationMap.put("Negotiate_Submission_Agreement", "1050");
    definedClassToClassificationMap.put("Producer_Submits_SIP", "1050");
    definedClassToClassificationMap.put("Consumer_Receives_DIP", "1050");
    // definedClassToClassificationMap.put("Security_Service", "1050");
    // definedClassToClassificationMap.put("Registry_Service", "1050");
    // definedClassToClassificationMap.put("Repository_Service", "1050");
    // definedClassToClassificationMap.put("Query_Service", "1050");
    // definedClassToClassificationMap.put("Subscription_Service", "1050");
    // definedClassToClassificationMap.put("Transfer_Service", "1050");
    // definedClassToClassificationMap.put("Configure_Search_Service", "1050");
    // definedClassToClassificationMap.put("Archive_Abstraction_Service", "1050");

    // definedClassToClassificationMap.put("Gap_Identification_Service", "1050");
    // definedClassToClassificationMap.put("Orchestration_Service", "1050");
    // definedClassToClassificationMap.put("Annotation Service", "1050");
    // definedClassToClassificationMap.put("Authentication_Service", "1050");
    // definedClassToClassificationMap.put("License_Service", "1050");
    // definedClassToClassificationMap.put("Metadata_Registry_Service", "1050");
    // definedClassToClassificationMap.put("Representation_Information_Registry_Service", "1050");
    // definedClassToClassificationMap.put("Persistent_Identifier_Service", "1050");
    // definedClassToClassificationMap.put("Tool_Service", "1050");
    // definedClassToClassificationMap.put("Data_Type_Service", "1050");


    // definedClassToClassificationMap.put("Registry_Interface", "1055");
    // definedClassToClassificationMap.put("Repository_Interface", "1055");
    // definedClassToClassificationMap.put("Archive_Interface", "1055");

    definedClassToClassificationMap.put("Method", "1060");
    definedClassToClassificationMap.put("getMetrics", "1060");
    definedClassToClassificationMap.put("getServiceStatus", "1060");
    definedClassToClassificationMap.put("migratelnformationObject", "1060");
    definedClassToClassificationMap.put("packageInformationObject", "1060");
    definedClassToClassificationMap.put("processInformationObject", "1060");
    definedClassToClassificationMap.put("pushlInformationObject", "1060");
    definedClassToClassificationMap.put("pullInformationObject", "1060");
    definedClassToClassificationMap.put("configureArchive", "1060");
    definedClassToClassificationMap.put("putInformationObject", "1060");
    definedClassToClassificationMap.put("putQueryModel", "1060");
    definedClassToClassificationMap.put("queryRegistry", "1060");
    definedClassToClassificationMap.put("registerInformationObject", "1060");
    definedClassToClassificationMap.put("subscribeUser", "1060");
    definedClassToClassificationMap.put("transformInformationObject", "1060");
    definedClassToClassificationMap.put("updateQueryModel", "1060");
    definedClassToClassificationMap.put("validateInformationObjectData", "1060");
    definedClassToClassificationMap.put("validateInformationObjectMetadata", "1060");
    definedClassToClassificationMap.put("validateUser", "1060");
    definedClassToClassificationMap.put("harvestInformationObjectMetadata", "1060");
    definedClassToClassificationMap.put("configureSearchEngine", "1060");
    definedClassToClassificationMap.put("mapNonOAISStructuresToAIP", "1060");
    definedClassToClassificationMap.put("bagInformationObjects", "1060");
    definedClassToClassificationMap.put("createQueryModel", "1060");
    definedClassToClassificationMap.put("getPersistentIdentifier", "1060");
    definedClassToClassificationMap.put("harvestMetadata", "1060");
    definedClassToClassificationMap.put("packageInformationObjects", "1060");
    definedClassToClassificationMap.put("pullBag", "1060");
    definedClassToClassificationMap.put("pushBag", "1060");
    definedClassToClassificationMap.put("putInformationObjectData", "1060");
    definedClassToClassificationMap.put("putInformationObjectMetadata", "1060");
    definedClassToClassificationMap.put("setDataObject", "1060");
    definedClassToClassificationMap.put("setPID", "1060");
    definedClassToClassificationMap.put("setRepresentationInformation", "1060");
    definedClassToClassificationMap.put("setIdentificationInformation", "1060");
    definedClassToClassificationMap.put("setReferenceInformation", "1060");
    definedClassToClassificationMap.put("setProvenanceInformation", "1060");
    definedClassToClassificationMap.put("setFixityInformation", "1060");
    definedClassToClassificationMap.put("setContextInformation", "1060");
    definedClassToClassificationMap.put("setAccessRightsInformation", "1060");
    definedClassToClassificationMap.put("getPID", "1060");
    definedClassToClassificationMap.put("getPersistentIdentifier", "1060");
    definedClassToClassificationMap.put("getDataObject", "1060");
    definedClassToClassificationMap.put("getDigitalObject", "1060");
    definedClassToClassificationMap.put("getInformationObjectMetadata", "1060");
    definedClassToClassificationMap.put("getInformationObject", "1060");
    definedClassToClassificationMap.put("getInformationObjectData", "1060");
    definedClassToClassificationMap.put("getInformationObjectMetadata", "1060");
    definedClassToClassificationMap.put("getRepresentationInformation", "1060");
    definedClassToClassificationMap.put("getIdentificationInformation", "1060");
    definedClassToClassificationMap.put("getReferenceInformation", "1060");
    definedClassToClassificationMap.put("getProvenanceInformation", "1060");
    definedClassToClassificationMap.put("getFixityInformation", "1060");
    definedClassToClassificationMap.put("getContextInformation", "1060");
    definedClassToClassificationMap.put("getAccessRightsInformation", "1060");
    definedClassToClassificationMap.put("getMapping", "1060");
    definedClassToClassificationMap.put("configureArchive_Abstraction", "1060");
    definedClassToClassificationMap.put("configureHarvester", "1060");
    definedClassToClassificationMap.put("sendMessage", "1060");
    definedClassToClassificationMap.put("receiveMessage", "1060");


    /*
     * definedClassToClassificationMap.put("bagInformationObjects__Signature", "1065");
     * definedClassToClassificationMap.put("configureSearchEngine__Signature", "1065");
     * definedClassToClassificationMap.put("createQueryModel__Signature", "1065");
     * definedClassToClassificationMap.put("createSIP__Signature", "1065");
     * definedClassToClassificationMap.put("getAccessRightsInformation__Signature", "1065");
     * definedClassToClassificationMap.put("getContextInformation__Signature", "1065");
     * definedClassToClassificationMap.put("getDataObject__Signature", "1065");
     * definedClassToClassificationMap.put("getDigitalObject__Signature", "1065");
     * definedClassToClassificationMap.put("getFixityInformation__Signature", "1065");
     * definedClassToClassificationMap.put("getIdentificationInformation__Signature", "1065");
     * definedClassToClassificationMap.put("getInformationObject__Signature", "1065");
     * definedClassToClassificationMap.put("getInformationObjectData__Signature", "1065");
     * definedClassToClassificationMap.put("getInformationObjectMetadata__Signature", "1065");
     * definedClassToClassificationMap.put("getMetrics__Signature", "1065");
     * definedClassToClassificationMap.put("getPersistentIdentifier__Signature", "1065");
     * definedClassToClassificationMap.put("getPID__Signature", "1065");
     * definedClassToClassificationMap.put("getProvenanceInformation__Signature", "1065");
     * definedClassToClassificationMap.put("getReferenceInformation__Signature", "1065");
     * definedClassToClassificationMap.put("getRepresentationInformation__Signature", "1065");
     * definedClassToClassificationMap.put("getServiceStatus__Signature", "1065");
     * definedClassToClassificationMap.put("harvestMetadata__Signature", "1065");
     * definedClassToClassificationMap.put("ingestSIP__Signature", "1065");
     * definedClassToClassificationMap.put("mapNonOAISStructuresToAIP__Signature", "1065");
     * definedClassToClassificationMap.put("migratelnformationObject__Signature", "1065");
     * definedClassToClassificationMap.put("packageInformationObjects__Signature", "1065");
     * definedClassToClassificationMap.put("processInformationObject__Signature", "1065");
     * definedClassToClassificationMap.put("pullBag__Signature", "1065");
     * definedClassToClassificationMap.put("pushBag__Signature", "1065");
     * definedClassToClassificationMap.put("configureArchive__Signature", "1065");
     * definedClassToClassificationMap.put("putInformationObject__Signature", "1065");
     * definedClassToClassificationMap.put("putInformationObjectData__Signature", "1065");
     * definedClassToClassificationMap.put("putInformationObjectMetadata__Signature", "1065");
     * definedClassToClassificationMap.put("putQueryModel__Signature", "1065");
     * definedClassToClassificationMap.put("queryForAIP__Signature", "1065");
     * definedClassToClassificationMap.put("queryForSIP__Signature", "1065");
     * definedClassToClassificationMap.put("queryRegistry__Signature", "1065");
     * definedClassToClassificationMap.put("registerInformationObject__Signature", "1065");
     * definedClassToClassificationMap.put("retrieveDIP__Signature", "1065");
     * definedClassToClassificationMap.put("setDataObject__Signature", "1065");
     * definedClassToClassificationMap.put("setPID__Signature", "1065");
     * definedClassToClassificationMap.put("setRepresentationInformation__Signature", "1065");
     * definedClassToClassificationMap.put("subscribeUser__Signature", "1065");
     * definedClassToClassificationMap.put("transformInformationObject__Signature", "1065");
     * definedClassToClassificationMap.put("updateQueryModel__Signature", "1065");
     * definedClassToClassificationMap.put("validateInformationObjectData__Signature", "1065");
     * definedClassToClassificationMap.put("validateInformationObjectMetadata__Signature", "1065");
     * definedClassToClassificationMap.put("validateUser__Signature", "1065");
     */

    definedClassToClassificationMap.put("Protocol", "1070");
    definedClassToClassificationMap.put("Binding", "1070");
    definedClassToClassificationMap.put("REST_HTTP", "1600");
    definedClassToClassificationMap.put("JAVA_JSON_Data_Archive_Binding", "1600");
    definedClassToClassificationMap.put("JAVA_XML_Data_Archive_Binding", "1600");
    definedClassToClassificationMap.put("Python_XML_Data_Archive_Binding", "1600");
    definedClassToClassificationMap.put("Python_JSON_Data_Archive_Binding", "1600");
    definedClassToClassificationMap.put("Apache_Jena_Fuseki", "1600");
    definedClassToClassificationMap.put("Apache_Solr", "1600");
    definedClassToClassificationMap.put("Bagit", "1600");
    definedClassToClassificationMap.put("Java", "1600");
    // definedClassToClassificationMap.put("OData", "1600");
    // definedClassToClassificationMap.put("OData_DELETE", "1600");
    // definedClassToClassificationMap.put("OData_GET", "1600");
    // definedClassToClassificationMap.put("OData_Metadata_Document", "1600");
    // definedClassToClassificationMap.put("OData_PATCH", "1600");
    // definedClassToClassificationMap.put("OData_POST", "1600");
    // definedClassToClassificationMap.put("OData_PUT", "1600");
    // definedClassToClassificationMap.put("OData_Service_Document", "1600");
    definedClassToClassificationMap.put("SPARQL", "1600");
    definedClassToClassificationMap.put("ebXML_Registry", "1600");

    // definedClassToClassificationMap.put("Preservation_Planning", "1090");
    // definedClassToClassificationMap.put("Data_Management", "1090");
    // definedClassToClassificationMap.put("Archival_Storage", "1090");
    // definedClassToClassificationMap.put("Administration", "1090");
    // definedClassToClassificationMap.put("Access", "1090");
    // definedClassToClassificationMap.put("Ingest", "1090");

    // definedClassToClassificationMap.put("Functional_Entity", "1090");
    // definedClassToClassificationMap.put("Access_Functional_Entity", "1090");
    // definedClassToClassificationMap.put("Administration_Functional_Entity", "1090");
    // definedClassToClassificationMap.put("Archival_Storage_Functional_Entity", "1090");
    // definedClassToClassificationMap.put("Data_Management_Functional_Entity", "1090");
    // definedClassToClassificationMap.put("Ingest_Functional_Entity", "1090");
    // definedClassToClassificationMap.put("Preservation_Planning_Functional_Entity", "1090");



    definedClassToClassificationMap.put("Information_Architecture", "1120");
    definedClassToClassificationMap.put("Information_Object", "1121");

    /*
     * 2222 definedClassToClassificationMap.put("Data_Object", "1121");
     * definedClassToClassificationMap.put("Physical_Object", "1121");
     * definedClassToClassificationMap.put("Digital_Object", "1121");
     * definedClassToClassificationMap.put("Conceptual_Object", "1121");
     * definedClassToClassificationMap.put("Bit", "1121");
     * 
     * definedClassToClassificationMap.put("OAIS_AIP", "1121");
     * definedClassToClassificationMap.put("AIP", "1121");
     * definedClassToClassificationMap.put("AIP_plus_Common", "1121");
     * definedClassToClassificationMap.put("AIP_plus_Discipline", "1121");
     * definedClassToClassificationMap.put("AIP_plus_Local", "1121");
     * 
     * definedClassToClassificationMap.put("Dissemination_Information_Package", "1121");
     * definedClassToClassificationMap.put("Package_Description", "1121");
     * definedClassToClassificationMap.put("Other_Representation_Information", "1121");
     * 
     * definedClassToClassificationMap.put("Associated_Description", "1121");
     * definedClassToClassificationMap.put("Semantic_Information", "1124");
     * definedClassToClassificationMap.put("Structure_Information", "1124");
     * definedClassToClassificationMap.put("Content_Information", "1121");
     * definedClassToClassificationMap.put("Content_Information_Group", "1121");
     * definedClassToClassificationMap.put("Descriptive_Information", "1121");
     * definedClassToClassificationMap.put("Packaging_Information", "1121");
     * definedClassToClassificationMap.put("Preservation_Description_Information", "1121");
     * definedClassToClassificationMap.put("Access_Rights_Information", "1121");
     * definedClassToClassificationMap.put("Fixity_Information", "1121");
     * definedClassToClassificationMap.put("Context_Information", "1121");
     * definedClassToClassificationMap.put("Provenance_Information", "1121");
     * definedClassToClassificationMap.put("Reference_Information", "1121");
     * definedClassToClassificationMap.put("Representation_Information", "1121");
     * definedClassToClassificationMap.put("Information_Package", "1121");
     * definedClassToClassificationMap.put("Submission_Information_Package", "1121");
     * definedClassToClassificationMap.put("Archival_Information_Package", "1121");
     * definedClassToClassificationMap.put("Archival_Information_Collection", "1121");
     * definedClassToClassificationMap.put("Archival_Information_Unit", "1121");
     * definedClassToClassificationMap.put("Content_Data_Object", "1121");
     * definedClassToClassificationMap.put("Unit_Description", "1121");
     * definedClassToClassificationMap.put("Derived_AIP", "1124");
     * definedClassToClassificationMap.put("Information_Property", "1121");
     * definedClassToClassificationMap.put("Order_Agreement", "1124"); //
     * definedClassToClassificationMap.put("Other_Component", "1124");
     * definedClassToClassificationMap.put("Overview_Description", "1121");
     * definedClassToClassificationMap.put("Representation_Network", "1121");
     * definedClassToClassificationMap.put("Submission_Agreement", "1124");
     * definedClassToClassificationMap.put("Representation_Network", "1121"); ;
     * definedClassToClassificationMap.put("Transformational_Information_Property", "1121");
     * definedClassToClassificationMap.put("Data_Structure", "1124");
     */

    // definedClassToClassificationMap.put("Interface", "1110");
    // definedClassToClassificationMap.put("User_Interface", "1110");
    // definedClassToClassificationMap.put("Producer_Interface", "1110");
    // definedClassToClassificationMap.put("Consumer_Interface", "1110");
    // definedClassToClassificationMap.put("Archive_Abstraction_Interface", "1110");
    // definedClassToClassificationMap.put("OAIS_Interface", "1110");
    // definedClassToClassificationMap.put("Producer_Archive_Interface", "1110");
    // definedClassToClassificationMap.put("Administration_develops_Submission/Schedule_Agreement_with_Producer",
    // "1115");
    // definedClassToClassificationMap.put("Administration_sends_Final_Ingest_Report_to_Producer",
    // "1115");
    // definedClassToClassificationMap.put("Administration_sends_Lien_to_Producer", "1115");
    // definedClassToClassificationMap.put("Ingest_sends_Receipt_Confirmation_to_Producer", "1115");
    // definedClassToClassificationMap.put("Ingest_sends_Resubmit_Request_to_Producer", "1115");
    // definedClassToClassificationMap.put("Preservation_Planning_surveys_Producers", "1115");
    // definedClassToClassificationMap.put("Producer_develops_Submission/Schedule_Agreement_with_Administration",
    // "1115");
    // definedClassToClassificationMap.put("Producer_proposes_Product_Technologies_to_Preservation_Planning",
    // "1115");
    // definedClassToClassificationMap.put("Producer_provides_a_Submission_Interface_Package_to_the_Ingest_Functional_Entity",
    // "1115");

    // definedClassToClassificationMap.put("Consumer_Interface", "1115");
    // definedClassToClassificationMap.put("Access_generates_DIP_for_Consumer", "1115");
    // definedClassToClassificationMap.put("Access_produces_Query_Response_for_Consumer", "1115");
    // definedClassToClassificationMap.put("Administration_sends_Bill_to_Consumer", "1115");
    // definedClassToClassificationMap.put("Administration_sends_Information_Requests_to_Consumers",
    // "1115");
    // definedClassToClassificationMap.put("Administration_sends_Special_Request_Response_to_Consumers",
    // "1115");
    // definedClassToClassificationMap.put("Consumer_makes_Payment_to_Administration", "1115");
    // definedClassToClassificationMap.put("Consumer_makes_Special_Service_Request_to_Administration",
    // "1115");
    // definedClassToClassificationMap.put("Consumer_submits_a_Query_Request_to_Access", "1115");
    // definedClassToClassificationMap.put("Consumer_submits_a_Report_Request_to_Access", "1115");
    // definedClassToClassificationMap.put("Consumer_submits_an_Order_Assistance_Request_to_Access",
    // "1115");
    // definedClassToClassificationMap.put("Consumer_submits_Service_Requirements_to_Preservation_Planning",
    // "1115");
    // definedClassToClassificationMap.put("Preservation_Planning_surveys_Consumers", "1115");
    // definedClassToClassificationMap.put("Architecture_Interface", "1115");

    definedClassToClassificationMap.put("Archive_Abstraction_Layer", "9999");
    // definedClassToClassificationMap.put("Product", "9999");

    // definedClassToClassificationMap.put("Method_Signature", "1060");

    /*
     * definedClassToClassificationMap.put("UC-1.1 - OAIS verifies  the Customer's credentials",
     * "2100");
     * definedClassToClassificationMap.put("UC-1.2 - OAIS verifies the Producer's credentials",
     * "2100"); definedClassToClassificationMap.
     * put("UC-2.1 - Producer and OAIS negotiate submission agreement", "2100");
     * definedClassToClassificationMap.put("UC-2.2 - Producer creates a SIP", "2100");
     * definedClassToClassificationMap.put("UC-2.3 - Producer submits SIP for Ingestion - Complete",
     * "2100");
     * definedClassToClassificationMap.put("UC-2.4 - Producer submits SIP for Ingestion - Partial",
     * "2100");
     * definedClassToClassificationMap.put("UC-2.5 - Producer receives submission status reports",
     * "2100"); definedClassToClassificationMap.put("UC-3.1 - Search for AIP", "2100");
     * definedClassToClassificationMap.put("UC-3.2 - Select AIP Identifiers from search results",
     * "2100"); definedClassToClassificationMap.put("UC-3.3 - Retrieve an AIP repackaged as a DIP",
     * "2100");
     * definedClassToClassificationMap.put("UC-3.4 - Retrieve some part of an AIP packaged as a DIP"
     * , "2100"); definedClassToClassificationMap.put("UC-3.5 - Package DIP for transfer", "2100");
     * definedClassToClassificationMap.put("UC-4.1 - Consumer extracts information from the DIP",
     * "2100"); definedClassToClassificationMap.
     * put("UC-5.1 - OAIS manages Information Objects that are extensions to the OAIS Information Model"
     * , "2100"); definedClassToClassificationMap.
     * put("UC-5.3 - A proxy OAIS manages non-OAIS Information Objects as if they were OAIS Information Objects"
     * , "2100");
     */
    return;
  }

  // ==========================================================================================================

  public void initMapProcess() {

    classClassificationArr = new ArrayList<>(classClassificationMap.values());

    activeClasses = new ArrayList<>(definedClassToClassificationMap.keySet());

    for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();

      // System.out.println("debug WriteDocCSV - init - lClass.identifier:" + lClass.identifier);

      ClassDOMCCSDSClassificationDefn lClassDOMCCSDSClassificationDefn = null;
      String lClassificationId = definedClassToClassificationMap.get(lClass.title);
      if (lClassificationId != null) {
        lClassDOMCCSDSClassificationDefn = classClassificationMap.get(lClassificationId);
        if (lClassDOMCCSDSClassificationDefn == null) {
          lClassDOMCCSDSClassificationDefn = classClassificationMap.get("9999");
        }
      } else {
        lClassDOMCCSDSClassificationDefn = classClassificationMap.get("9999");
      }
      lClassDOMCCSDSClassificationDefn.classMap.put(lClass.identifier, lClass);
    }

    return;
  }
}
