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

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;


class GetValueMeanings extends Object {
  // values and value meaning
  static TreeMap<String, PermValueDefn> masterValueMeaningMap;
  static TreeMap<String, PermValueDefn> masterValueMeaningKeyMap;

  public GetValueMeanings() {
    masterValueMeaningMap = new TreeMap<>();
    masterValueMeaningKeyMap = new TreeMap<>();
  }

  public void getSearchKey() {
    Set<String> set1 = masterValueMeaningMap.keySet();
    Iterator<String> iter1 = set1.iterator();
    while (iter1.hasNext()) {
      String lId = iter1.next();
      PermValueDefn lPVD = masterValueMeaningMap.get(lId);
      String lSearchKey = lPVD.identifier;
      String lSK1 = lPVD.identifier;
      int lValueLoc = lSK1.indexOf("<Value>");
      if (lValueLoc > -1) {
        lSearchKey = lSK1.substring(0, lValueLoc);
        lSearchKey += "Value." + lPVD.value;
      }
      lPVD.searchKey = lSearchKey;
      masterValueMeaningKeyMap.put(lPVD.searchKey, lPVD);
    }
  }

  public void insertValueMeaning() {
    for (String[] lValueMeaning : VALUE_MEANINGS) {
      if (lValueMeaning.length != 3) {
        throw new IllegalStateException(
            "GetValueMeanings.VALUE_MEANINGS row must have 3 entries: "
                + Arrays.toString(lValueMeaning));
      }
      PermValueDefn lPVD = new PermValueDefn(lValueMeaning[0], lValueMeaning[1], lValueMeaning[2]);
      masterValueMeaningMap.put(lPVD.identifier, lPVD);
    }
  }

  // Permissible value meanings: {identifier, value, value_meaning}
  // To add a value meaning, add a row to this table.
  // Order matters - when an identifier is repeated, the last row wins.
  private static final String[][] VALUE_MEANINGS = {
      // Bundle_Member_Entry
      {"0001_NASA_PDS_1.Bundle_Member_Entry.reference_type_<Value>_1",
          "bundle_has_browse_collection", "The bundle has a browse collection member"},
      {"0001_NASA_PDS_1.Bundle_Member_Entry.reference_type_<Value>_2",
          "bundle_has_calibration_collection", "The bundle has a calibration collection member"},
      {"0001_NASA_PDS_1.Bundle_Member_Entry.reference_type_<Value>_3",
          "bundle_has_context_collection", "The bundle has a context collection member"},
      {"0001_NASA_PDS_1.Bundle_Member_Entry.reference_type_<Value>_4",
          "bundle_has_data_collection", "The bundle has a data collection member"},
      {"0001_NASA_PDS_1.Bundle_Member_Entry.reference_type_<Value>_5",
          "bundle_has_document_collection", "The bundle has a document collection member"},
      {"0001_NASA_PDS_1.Bundle_Member_Entry.reference_type_<Value>_6",
          "bundle_has_geometry_collection", "The bundle has a geometry collection member"},
      {"0001_NASA_PDS_1.Bundle_Member_Entry.reference_type_<Value>_7",
          "bundle_has_member_collection", "The bundle has a member collection member"},
      {"0001_NASA_PDS_1.Bundle_Member_Entry.reference_type_<Value>_8",
          "bundle_has_schema_collection", "The bundle has a schema collection member"},
      {"0001_NASA_PDS_1.Bundle_Member_Entry.reference_type_<Value>_9",
          "bundle_has_spice_kernel_collection", "The bundle has a spice kernel collection member"},
      {"0001_NASA_PDS_1.Bundle_Member_Entry.reference_type_<Value>_10",
          "bundle_has_miscellaneous_collection", "The bundle has a miscellaneous collection member"},
      {"0001_NASA_PDS_1.Bundle_Member_Entry.reference_type_<Value>_11",
          "bundle_has_external_collection", "The bundle has an external collection member"},

      // DD_Association
      {"0001_NASA_PDS_1.DD_Association.reference_type_<Value>_1",
          "attribute_of", "The referenced attribute is a member of this class"},
      {"0001_NASA_PDS_1.DD_Association.reference_type_<Value>_2",
          "subclass_of", "The referenced class is a subclass of this class"},
      {"0001_NASA_PDS_1.DD_Association.reference_type_<Value>_3",
          "extension_of", "The referenced class is an extension of this class"},
      {"0001_NASA_PDS_1.DD_Association.reference_type_<Value>_4",
          "restriction_of", "The referenced class is a restriction of this class"},
      {"0001_NASA_PDS_1.DD_Association.reference_type_<Value>_5",
          "component_of", "The referenced class is a component of this class"},

      // DD_Association_External
      {"0001_NASA_PDS_1.DD_Association_External.reference_type_<Value>_1",
          "attribute_of", "The referenced attribute is a member of this class"},
      {"0001_NASA_PDS_1.DD_Association_External.reference_type_<Value>_2",
          "subclass_of", "The referenced class is a subclass of this class"},
      {"0001_NASA_PDS_1.DD_Association_External.reference_type_<Value>_3",
          "extension_of", "The referenced class is an extension of this class"},
      {"0001_NASA_PDS_1.DD_Association_External.reference_type_<Value>_4",
          "restriction_of", "The referenced class is a restriction of this class"},
      {"0001_NASA_PDS_1.DD_Association_External.reference_type_<Value>_5",
          "component_of", "The referenced class is a component of this class"},

      // Product_Zipped
      {"pds:Product_Zipped/pds:Internal_Reference.reference_type.zip_to_package",
          "zip_to_package", "The zip file contains a copy of the package"},

      // Inventory
      {"0001_NASA_PDS_1.Inventory.reference_type_<Value>_1",
          "inventory_has_member_product", "The collection inventory has member products identified by either LIDVID or LID references"},

      // Product_Observational
      {"pds:Product_Observational/pds:Identification_Area/pds:License_Information/pds:Internal_Reference.reference_type.product_to_license",
          "product_to_license", "A reference to a document or context product containing the full text of the license, terms, or public domain classification"},

      // Product_Ancillary
      {"pds:Product_Ancillary/pds:Identification_Area/pds:License_Information/pds:Internal_Reference.reference_type.product_to_license",
          "product_to_license", "A reference to a document or context product containing the full text of the license, terms, or public domain classification"},

      // Product_Browse
      {"pds:Product_Browse/pds:Identification_Area/pds:License_Information/pds:Internal_Reference.reference_type.product_to_license",
          "product_to_license", "A reference to a document or context product containing the full text of the license, terms, or public domain classification"},

      // Product_Bundle
      {"pds:Product_Bundle/pds:Identification_Area/pds:License_Information/pds:Internal_Reference.reference_type.product_to_license",
          "product_to_license", "A reference to a document or context product containing the full text of the license, terms, or public domain classification"},

      // Product_Collection
      {"pds:Product_Collection/pds:Identification_Area/pds:License_Information/pds:Internal_Reference.reference_type.product_to_license",
          "product_to_license", "A reference to a document or context product containing the full text of the license, terms, or public domain classification"},

      // Product_Context
      {"pds:Product_Context/pds:Identification_Area/pds:License_Information/pds:Internal_Reference.reference_type.product_to_license",
          "product_to_license", "A reference to a document or context product containing the full text of the license, terms, or public domain classification"},

      // Product_Document
      {"pds:Product_Document/pds:Identification_Area/pds:License_Information/pds:Internal_Reference.reference_type.product_to_license",
          "product_to_license", "A reference to a document or context product containing the full text of the license, terms, or public domain classification"},

      // Product_External
      {"pds:Product_External/pds:Identification_Area/pds:License_Information/pds:Internal_Reference.reference_type.product_to_license",
          "product_to_license", "A reference to a document or context product containing the full text of the license, terms, or public domain classification"},

      // Product_File_Text
      {"pds:Product_File_Text/pds:Identification_Area/pds:License_Information/pds:Internal_Reference.reference_type.product_to_license",
          "product_to_license", "A reference to a document or context product containing the full text of the license, terms, or public domain classification"},

      // Product_Metadata_Supplemental
      {"pds:Product_Metadata_Supplemental/pds:Identification_Area/pds:License_Information/pds:Internal_Reference.reference_type.product_to_license",
          "product_to_license", "A reference to a document or context product containing the full text of the license, terms, or public domain classification"},

      // Product_Native
      {"pds:Product_Native/pds:Identification_Area/pds:License_Information/pds:Internal_Reference.reference_type.product_to_license",
          "product_to_license", "A reference to a document or context product containing the full text of the license, terms, or public domain classification"},

      // Product_Observational
      {"pds:Product_Observational/pds:Observation_Area/pds:Investigation_Area/pds:Internal_Reference.reference_type.data_to_investigation",
          "data_to_investigation", "The data product is associated to an investigation"},
      {"pds:Product_Collection/pds:Context_Area/pds:Investigation_Area/pds:Internal_Reference.reference_type.collection_to_investigation",
          "collection_to_investigation", "The collection is associated to an investigation"},
      {"pds:Product_Bundle/pds:Context_Area/pds:Investigation_Area/pds:Internal_Reference.reference_type.bundle_to_investigation",
          "bundle_to_investigation", "The bundle is associated to an investigation"},

      {"pds:Product_Observational/pds:Observation_Area/pds:Investigation_Area.name.$name",
          "$name", "The value for name is to be provided by software during template processing."},
      {"pds:Product_Observational/pds:Observation_Area/pds:Observing_System.name.$name",
          "$name", "The value for name is to be provided by software during template processing."},
      {"pds:Product_Observational/pds:Observation_Area/pds:Observing_System/pds:Observing_System_Component.name.$name",
          "$name", "The value for name is to be provided by software during template processing."},
      {"pds:Product_Observational/pds:Observation_Area/pds:Target_Identification.name.$name",
          "$name", "The value for name is to be provided by software during template processing."},

      // Observing_System_Component
      {"pds:Observing_System_Component/pds:Internal_Reference.reference_type.is_instrument",
          "is_instrument", "The referenced class is a context product describing the instrument."},
      {"pds:Observing_System_Component/pds:Internal_Reference.reference_type.is_instrument_host",
          "is_instrument_host", "The referenced class is a context product describing the instrument_host."},
      {"pds:Observing_System_Component/pds:Internal_Reference.reference_type.is_other",
          "is_other", "The referenced class is a context product describing something not classified."},
      {"pds:Observing_System_Component/pds:Internal_Reference.reference_type.is_facility",
          "is_facility", "The referenced class is a context product describing the facility."},
      {"pds:Observing_System_Component/pds:Internal_Reference.reference_type.is_telescope",
          "is_telescope", "The referenced class is a context product describing the telescope."},
      {"pds:Observing_System_Component/pds:Internal_Reference.reference_type.is_airborne",
          "is_airborne", "*Deprecated* The referenced class is a context product describing an airborne platform."},

      // Product_Observational
      {"pds:Product_Observational/pds:Reference_List/pds:Internal_Reference.reference_type.data_to_associate",
          "data_to_associate", "The data product is associated to a product"},
      {"pds:Product_Observational/pds:Reference_List/pds:Internal_Reference.reference_type.data_to_resource",
          "data_to_resource", "The data product is associated to a resource"},

      {"pds:Product_Observational/pds:Reference_List/pds:Internal_Reference.reference_type.data_to_calibration_document",
          "data_to_calibration_document", "The data product is associated to a calibration document"},
      {"pds:Product_Observational/pds:Reference_List/pds:Internal_Reference.reference_type.data_to_calibration_product",
          "data_to_calibration_product", "The data product is associated to a calibration product"},
      {"pds:Product_Observational/pds:Reference_List/pds:Internal_Reference.reference_type.data_to_raw_product",
          "data_to_raw_product", "The data product is associated to a raw product"},
      {"pds:Product_Observational/pds:Reference_List/pds:Internal_Reference.reference_type.data_to_calibrated_product",
          "data_to_calibrated_product", "The data product is associated to a calibrated product"},
      {"pds:Product_Observational/pds:Reference_List/pds:Internal_Reference.reference_type.data_to_derived_product",
          "data_to_derived_product", "The data product is associated to a derived product"},
      {"pds:Product_Observational/pds:Reference_List/pds:Internal_Reference.reference_type.data_to_partially_processed_product",
          "data_to_partially_processed_product", "The data product is associated to a partially processed product"},

      // Product_Ancillary
      {"pds:Product_Ancillary/pds:Reference_List/pds:Internal_Reference.reference_type.ancillary_to_data",
          "ancillary_to_data", "The ancillary product is associated to a data product"},
      {"pds:Product_Ancillary/pds:Reference_List/pds:Internal_Reference.reference_type.ancillary_to_browse",
          "ancillary_to_browse", "The ancillary product is associated to a browse product"},

      {"pds:Product_Ancillary/pds:Context_Area/pds:Investigation_Area/pds:Internal_Reference.reference_type.ancillary_to_investigation",
          "ancillary_to_investigation", "The ancillary data product is associated to an investigation"},
      {"pds:Product_Ancillary/pds:Context_Area/pds:Target_Identification/pds:Internal_Reference.reference_type.ancillary_to_target",
          "ancillary_to_target", "The ancillary data product is associated to a target"},
      {"pds:Product_Ancillary/pds:Reference_List/pds:Internal_Reference.reference_type.ancillary_to_document",
          "ancillary_to_document", "The ancillary data product is associated to a document"},

      // Product_Observational
      {"pds:Product_Observational/pds:Reference_List/pds:Internal_Reference.reference_type.data_to_geometry",
          "data_to_geometry", "The data product is associated to geometry"},
      {"pds:Product_Observational/pds:Reference_List/pds:Internal_Reference.reference_type.data_to_spice_kernel",
          "data_to_spice_kernel", "The data product is associated to spice kernel(s)"},
      {"pds:Product_Observational/pds:Reference_List/pds:Internal_Reference.reference_type.data_to_thumbnail",
          "data_to_thumbnail", "The data product is associated to a thumbnail"},
      {"pds:Product_Observational/pds:Reference_List/pds:Internal_Reference.reference_type.data_to_document",
          "data_to_document", "The data product is associated to a document"},
      {"pds:Product_Observational/pds:Reference_List/pds:Internal_Reference.reference_type.data_curated_by_node",
          "data_curated_by_node", "The data product is curated by the referenced node"},
      {"pds:Product_Observational/pds:Reference_List/pds:Internal_Reference.reference_type.data_to_browse",
          "data_to_browse", "The data product is associated to a browse product"},
      {"pds:Product_Observational/pds:Reference_List/pds:Internal_Reference.reference_type.data_to_ancillary_data",
          "data_to_ancillary_data", "The referencing data product requires the referenced data product to provide specific support for its own use. For example, a table with footnotes can be archived as two products: a data table file with a field giving a footnote code number; and a footnotes file sorted by those code numbers. The label of the data table would then reference the footnotes file with an association type of \"data_to_ancillary_data\"."},
      {"pds:Product_Observational/pds:Reference_List/pds:Internal_Reference.reference_type.archival_to_native",
          "archival_to_native", "The native product is associated to an archival product"},
      {"pds:Product_Collection/pds:Reference_List/pds:Internal_Reference.reference_type.collection_to_resource",
          "collection_to_resource", "The collection is associated to a resource"},
      {"pds:Product_Collection/pds:Reference_List/pds:Internal_Reference.reference_type.collection_to_associate",
          "collection_to_associate", "The collection is associated to a product"},
      {"pds:Product_Collection/pds:Reference_List/pds:Internal_Reference.reference_type.collection_to_calibration",
          "collection_to_calibration", "The collection is associated to calibration data"},
      {"pds:Product_Collection/pds:Reference_List/pds:Internal_Reference.reference_type.collection_to_geometry",
          "collection_to_geometry", "The collection is associated to geometry"},
      {"pds:Product_Collection/pds:Reference_List/pds:Internal_Reference.reference_type.collection_to_spice_kernel",
          "collection_to_spice_kernel", "The collection is associated to spice kernels"},
      {"pds:Product_Collection/pds:Reference_List/pds:Internal_Reference.reference_type.collection_curated_by_node",
          "collection_curated_by_node", "The collection is curated by the referenced node"},
      {"pds:Product_Collection/pds:Reference_List/pds:Internal_Reference.reference_type.collection_to_document",
          "collection_to_document", "The collection is associated to a document"},
      {"pds:Product_Collection/pds:Reference_List/pds:Internal_Reference.reference_type.collection_to_browse",
          "collection_to_browse", "The collection is associated to a browse product"},
      {"pds:Product_Collection/pds:Reference_List/pds:Internal_Reference.reference_type.collection_to_context",
          "collection_to_context", "The collection is associated to a context product"},
      {"pds:Product_Collection/pds:Reference_List/pds:Internal_Reference.reference_type.collection_to_data",
          "collection_to_data", "The collection is associated to a data product"},
      {"pds:Product_Collection/pds:Reference_List/pds:Internal_Reference.reference_type.collection_to_schema",
          "collection_to_schema", "The collection is associated to a schema document"},
      {"pds:Product_Collection/pds:Reference_List/pds:Internal_Reference.reference_type.collection_to_errata",
          "collection_to_errata", "The collection is associated to an errata document"},
      {"pds:Product_Collection/pds:Reference_List/pds:Internal_Reference.reference_type.collection_to_bundle",
          "collection_to_bundle", "The collection is associated to a bundle"},
      {"pds:Product_Collection/pds:Reference_List/pds:Internal_Reference.reference_type.collection_to_personnel",
          "collection_to_personnel", "The collection is associated to personnel"},
      {"pds:Product_Collection/pds:Reference_List/pds:Internal_Reference.reference_type.collection_to_ancillary",
          "collection_to_ancillary", "The collection is associated to an ancillary product"},

      // Product_Collection
      {"pds:Product_Collection/pds:Reference_List/pds:Internal_Reference.reference_type.collection_to_investigation",
          "collection_to_investigation", "The collection is associated to an investigation"},
      {"pds:Product_Collection/pds:Reference_List/pds:Internal_Reference.reference_type.collection_to_instrument",
          "collection_to_instrument", "The collection is associated to an instrument"},
      {"pds:Product_Collection/pds:Reference_List/pds:Internal_Reference.reference_type.collection_to_instrument_host",
          "collection_to_instrument_host", "The collection is associated to an instrument host"},
      {"pds:Product_Collection/pds:Reference_List/pds:Internal_Reference.reference_type.collection_to_target",
          "collection_to_target", "The collection is associated to a target"},
      {"pds:Product_Collection/pds:Reference_List/pds:Internal_Reference.reference_type.collection_to_associate",
          "collection_to_associate", "The collection is associated to a product"},

      // Product_Bundle
      {"pds:Product_Bundle/pds:Reference_List/pds:Internal_Reference.reference_type.bundle_to_errata",
          "bundle_to_errata", "The bundle is associated to an errata document"},
      {"pds:Product_Bundle/pds:Reference_List/pds:Internal_Reference.reference_type.bundle_to_document",
          "bundle_to_document", "The bundle is associated to a document"},

      {"pds:Product_Bundle/pds:Reference_List/pds:Internal_Reference.reference_type.bundle_to_investigation",
          "bundle_to_investigation", "The bundle is associated to an investigation"},
      {"pds:Product_Bundle/pds:Reference_List/pds:Internal_Reference.reference_type.bundle_to_instrument",
          "bundle_to_instrument", "The bundle is associated to an instrument"},
      {"pds:Product_Bundle/pds:Reference_List/pds:Internal_Reference.reference_type.bundle_to_instrument_host",
          "bundle_to_instrument_host", "The bundle is associated to an instrument host"},
      {"pds:Product_Bundle/pds:Reference_List/pds:Internal_Reference.reference_type.bundle_to_target",
          "bundle_to_target", "The bundle is associated to a target"},
      {"pds:Product_Bundle/pds:Reference_List/pds:Internal_Reference.reference_type.bundle_to_resource",
          "bundle_to_resource", "The bundle is associated to a resource"},
      {"pds:Product_Bundle/pds:Reference_List/pds:Internal_Reference.reference_type.bundle_to_associate",
          "bundle_to_associate", "The bundle is associated to a product"},

      // Product_Context
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.context_to_resource",
          "context_to_resource", "The context product is associated to a resource"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.context_to_associate",
          "context_to_associate", "The context product is associated to a product "},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.context_to_document",
          "context_to_document", "The context product is associated to a document"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.context_to_collection",
          "context_to_collection", "The context product is associated to a collection"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.context_to_bundle",
          "context_to_bundle", "The context product is associated to a bundle"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.instrument_host_to_investigation",
          "instrument_host_to_investigation", "The instrument host is associated to an investigation"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.instrument_host_to_document",
          "instrument_host_to_document", "The instrument host is associated to a document"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.instrument_host_to_target",
          "instrument_host_to_target", "The instrument host is associated to a target"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.instrument_to_instrument_host",
          "instrument_to_instrument_host", "The instrument is associated to an instrument host"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.instrument_to_document",
          "instrument_to_document", "The instrument is associated to a document"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.instrument_to_telescope",
          "instrument_to_telescope", "The instrument is associated to a telescope"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.instrument_to_observatory",
          "instrument_to_observatory", "The instrument is associated to an observatory"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.instrument_to_facility",
          "instrument_to_facility", "The instrument is associated to a facility"},

      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.investigation_to_instrument_host",
          "investigation_to_instrument_host", "The investigation is associated to an instrument_host"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.investigation_to_instrument",
          "investigation_to_instrument", "The investigation is associated to an instrument"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.investigation_to_target",
          "investigation_to_target", "The investigation is associated to a target"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.investigation_to_document",
          "investigation_to_document", "The investigation is associated to a document"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.investigation_to_facility",
          "investigation_to_facility", "The investigation is associated to a facility"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.investigation_to_telescope",
          "investigation_to_telescope", "The investigation is associated to a telescope"},

      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.node_to_personnel",
          "node_to_personnel", "The node is associated to a person"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.node_to_agency",
          "node_to_agency", "The node is associated to an agency"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.node_to_manager",
          "node_to_manager", "The node is associated to a manager"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.node_to_operator",
          "node_to_operator", "The node is associated to an operator"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.node_to_data_archivist",
          "node_to_data_archivist", "The node is associated to a data archivist"},

      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.resource_to_instrument",
          "resource_to_instrument", "The resource is associated to an instrument"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.resource_to_instrument_host",
          "resource_to_instrument_host", "The resource is associated to an instrument host"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.resource_to_investigation",
          "resource_to_investigation", "The resource is associated to an investigation"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.resource_to_target",
          "resource_to_target", "The resource is associated to a target"},

      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.target_to_document",
          "target_to_document", "The target is associated to a document"},

      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.telescope_to_observatory",
          "telescope_to_observatory", "The telescope is associated to an observatory"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.telescope_to_facility",
          "telescope_to_facility", "The telescope is associated to a facility"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.telescope_to_airborne",
          "telescope_to_airborne", "The telescope is associated to an airborne platform"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.telescope_to_instrument",
          "telescope_to_instrument", "A telescope is associated to an instrument"},

      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.instrument_host_to_instrument",
          "instrument_host_to_instrument", "The instrument_host is associated to an instrument"},

      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.target_to_instrument_host",
          "target_to_instrument_host", "The target is associated to an instrument_host"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.target_to_instrument",
          "target_to_instrument", "The target is associated to an instrument"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.target_to_investigation",
          "target_to_investigation", "The target is associated to an investigation"},

      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.facility_to_investigation",
          "facility_to_investigation", "The facility is associated to an investigation"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.facility_to_instrument",
          "facility_to_instrument", "The facility is associated to an instrument"},
      {"pds:Product_Context/pds:Reference_List/pds:Internal_Reference.reference_type.facility_to_telescope",
          "facility_to_telescope", "A facility is associated to a telescope"},

      // Product_Delivery_Manifest
      {"pds:Product_Delivery_Manifest/pds:Reference_List/pds:Internal_Reference.reference_type.delivery_manifest_to_collection",
          "delivery_manifest_to_collection", "The delivery manifest is for the referenced collection"},
      {"pds:Product_Delivery_Manifest/pds:Reference_List/pds:Internal_Reference.reference_type.delivery_manifest_to_bundle",
          "delivery_manifest_to_bundle", "The delivery manifest is for the referenced bundle"},

      // Product_Document
      {"pds:Product_Document/pds:Reference_List/pds:Internal_Reference.reference_type.document_to_associate",
          "document_to_associate", "The document is associated to a product"},
      {"pds:Product_Document/pds:Reference_List/pds:Internal_Reference.reference_type.document_to_investigation",
          "document_to_investigation", "The document is associated to an investigation"},
      {"pds:Product_Document/pds:Reference_List/pds:Internal_Reference.reference_type.document_to_instrument_host",
          "document_to_instrument_host", "The document is associated to an instrument_host"},
      {"pds:Product_Document/pds:Reference_List/pds:Internal_Reference.reference_type.document_to_instrument",
          "document_to_instrument", "The document is associated to an instrument"},
      {"pds:Product_Document/pds:Reference_List/pds:Internal_Reference.reference_type.document_to_target",
          "document_to_target", "The document is associated to a target"},
      {"pds:Product_Document/pds:Reference_List/pds:Internal_Reference.reference_type.document_to_data",
          "document_to_data", "The document is associated to a data product"},
      {"pds:Product_Document/pds:Reference_List/pds:Internal_Reference.reference_type.document_to_browse",
          "document_to_browse", "The document is associated to a browse product"},
      {"pds:Product_Document/pds:Reference_List/pds:Internal_Reference.reference_type.document_to_document",
          "document_to_document", "The document is associated to another document product"},
      {"pds:Product_Document/pds:Reference_List/pds:Internal_Reference.reference_type.document_to_ancillary",
          "document_to_ancillary", "The document is associated to an ancillary product"},

      {"pds:Product_Document/pds:Context_Area/pds:Investigation_Area/pds:Internal_Reference.reference_type.document_to_investigation",
          "document_to_investigation", "The document is associated to an investigation"},
      {"pds:Product_Document/pds:Context_Area/pds:Observing_System/pds:Observing_System_Component/pds:Internal_Reference.reference_type.document_to_instrument",
          "document_to_instrument", "The document is associated to an instrument"},
      {"pds:Product_Document/pds:Context_Area/pds:Observing_System/pds:Observing_System_Component/pds:Internal_Reference.reference_type.document_to_instrument_host",
          "document_to_instrument_host", "The document is associated to an instrument host"},
      {"pds:Product_Document/pds:Context_Area/pds:Target_Identification/pds:Internal_Reference.reference_type.document_to_target",
          "document_to_target", "The document is associated to a target"},

      // Product_Browse
      {"pds:Product_Browse/pds:Reference_List/pds:Internal_Reference.reference_type.browse_to_data",
          "browse_to_data", "The browse product is associated to a data product"},
      {"pds:Product_Browse/pds:Reference_List/pds:Internal_Reference.reference_type.browse_to_thumbnail",
          "browse_to_thumbnail", "The browse product is associated to a thumbnail"},
      {"pds:Product_Browse/pds:Reference_List/pds:Internal_Reference.reference_type.browse_to_browse",
          "browse_to_browse", "The browse product is associated to a different browse product."},
      {"pds:Product_Browse/pds:Reference_List/pds:Internal_Reference.reference_type.browse_to_document",
          "browse_to_document", "The browse product is associated to a document."},
      {"pds:Product_Browse/pds:Reference_List/pds:Internal_Reference.reference_type.browse_to_ancillary",
          "browse_to_ancillary", "The browse product is associated to an ancillary product."},

      // Product_Thumbnail
      {"pds:Product_Thumbnail/pds:Reference_List/pds:Internal_Reference.reference_type.thumbnail_to_data",
          "thumbnail_to_data", "The thumbnail product is associated to a data product."},
      {"pds:Product_Thumbnail/pds:Reference_List/pds:Internal_Reference.reference_type.thumbnail_to_browse",
          "thumbnail_to_browse", "The thumbnail product is associated to a browse product."},
      {"pds:Product_Thumbnail/pds:Reference_List/pds:Internal_Reference.reference_type.thumbnail_to_document",
          "thumbnail_to_document", "The thumbnail product is associated to a document product."},

      // Product_Native
      {"pds:Product_Native/pds:Reference_List/pds:Internal_Reference.reference_type.described_by_document",
          "described_by_document", "The format of the digital object must be described one or more referenced documents"},
      {"pds:Product_Native/pds:Reference_List/pds:Internal_Reference.reference_type.native_to_archival",
          "native_to_archival", "The product native is associated to a product observational"},
      {"pds:Product_Telemetry/pds:Reference_List/pds:Internal_Reference.reference_type.described_by_document",
          "described_by_document", "The format of the digital object must be described one or more referenced documents"},

      // Product_Observational
      {"pds:Product_Observational/pds:Observation_Area/pds:Target_Identification/pds:Internal_Reference.reference_type.data_to_target",
          "data_to_target", "The data product is associated to a target"},

      // Product_Update
      {"pds:Product_Update/pds:Reference_List/pds:Internal_Reference.reference_type.update_to_collection",
          "update_to_collection", "The update is associated to a collection product"},

      // Product_AIP
      {"pds:Product_AIP/pds:Information_Package_Component/pds:Internal_Reference.reference_type.package_has_bundle",
          "package_has_bundle", "The Archival Information Package contains a Bundle."},
      {"pds:Product_AIP/pds:Information_Package_Component/pds:Internal_Reference.reference_type.package_has_collection",
          "package_has_collection", "The Archival Information Package contains a Collection."},
      {"pds:Product_AIP/pds:Information_Package_Component/pds:Internal_Reference.reference_type.package_has_product",
          "package_has_product", "The Archival Information Package contains a basic Product."},
      {"pds:Product_AIP/pds:Information_Package_Component/pds:Internal_Reference.reference_type.package_compiled_from_package",
          "package_compiled_from_package", "The Archival Information Package is compiled from a Submission Information Package."},

      // Product_SIP
      {"pds:Product_SIP/pds:Information_Package_Component/pds:Internal_Reference.reference_type.package_has_bundle",
          "package_has_bundle", "The Submission Information Package contains a Bundle."},
      {"pds:Product_SIP/pds:Information_Package_Component/pds:Internal_Reference.reference_type.package_has_collection",
          "package_has_collection", "The Submission Information Package contains a Collection."},
      {"pds:Product_SIP/pds:Information_Package_Component/pds:Internal_Reference.reference_type.package_has_product",
          "package_has_product", "The Submission Information Package is contains a basic Product."},

      // Product_SIP_Deep_Archive
      {"pds:Product_SIP_Deep_Archive/pds:Information_Package_Component_Deep_Archive/pds:Internal_Reference.reference_type.package_has_bundle",
          "package_has_bundle", "The Submission Information Package Deep Archive contains a Bundle."},

      // Product_DIP
      {"pds:Product_DIP/pds:Information_Package_Component/pds:Internal_Reference.reference_type.package_has_bundle",
          "package_has_bundle", "The Dissemination Information Package contains a Bundle."},
      {"pds:Product_DIP/pds:Information_Package_Component/pds:Internal_Reference.reference_type.package_has_collection",
          "package_has_collection", "The Dissemination Information Package contains a Collection."},
      {"pds:Product_DIP/pds:Information_Package_Component/pds:Internal_Reference.reference_type.package_has_product",
          "package_has_product", "The Dissemination Information Package contains a basic Product."},
      {"pds:Product_DIP/pds:Information_Package_Component/pds:Internal_Reference.reference_type.package_compiled_from_package",
          "package_compiled_from_package", "The Dissemination Information Package is compiled from an Archival Information Package."},

      // Product_DIP_Deep_Archive
      {"pds:Product_DIP_Deep_Archive/pds:Information_Package_Component/pds:Internal_Reference.reference_type.package_has_bundle",
          "package_has_bundle", "The Dissemination Information Package contains a Bundle."},
      {"pds:Product_DIP_Deep_Archive/pds:Information_Package_Component/pds:Internal_Reference.reference_type.package_has_collection",
          "package_has_collection", "The Dissemination Information Package contains a Collection."},
      {"pds:Product_DIP_Deep_Archive/pds:Information_Package_Component/pds:Internal_Reference.reference_type.package_has_product",
          "package_has_product", "The Dissemination Information Package contains a basic Product."},
      {"pds:Product_DIP_Deep_Archive/pds:Information_Package_Component/pds:Internal_Reference.reference_type.package_compiled_from_package",
          "package_compiled_from_package", "The Dissemination Information Package is compiled from an Archival Information Package."},

      // Reference_Entry_Observing_System_Component
      {"0001_NASA_PDS_1.Reference_Entry_Observing_System_Component.reference_type_<Value>_1",
          "Naked Eye", "Reference_Entry_Observing_System_Component has an association of type Naked Eye (a person)"},
      {"0001_NASA_PDS_1.Reference_Entry_Observing_System_Component.reference_type_<Value>_2",
          "Artificial Illumination", "Reference_Entry_Observing_System_Component has an association of type Artificial Illumination"},
      {"0001_NASA_PDS_1.Reference_Entry_Observing_System_Component.reference_type_<Value>_3",
          "Laboratory", "Reference_Entry_Observing_System_Component has an association of type Laboratory"},
      {"0001_NASA_PDS_1.Reference_Entry_Observing_System_Component.reference_type_<Value>_4",
          "Observatory", "Reference_Entry_Observing_System_Component has an association of type Observatory"},
      {"0001_NASA_PDS_1.Reference_Entry_Observing_System_Component.reference_type_<Value>_5",
          "Telescope", "Reference_Entry_Observing_System_Component has an association of type Telescope"},
      {"0001_NASA_PDS_1.Reference_Entry_Observing_System_Component.reference_type_<Value>_6",
          "Instrument", "Reference_Entry_Observing_System_Component has an association of type Instrument"},
      {"0001_NASA_PDS_1.Reference_Entry_Observing_System_Component.reference_type_<Value>_7",
          "Literature Search", "Reference_Entry_Observing_System_Component has an association of type Literature Search"},
      {"0001_NASA_PDS_1.Reference_Entry_Observing_System_Component.reference_type_<Value>_9",
          "Spacecraft", "Reference_Entry_Observing_System_Component has an association of type Spacecraft"},

      // DD_Class
      {"pds:DD_Class/pds:Internal_Reference.reference_type.class_to_image",
          "class_to_image", "The class is associated to an image"},
      {"pds:DD_Class/pds:Internal_Reference.reference_type.class_to_document",
          "class_to_document", "The class is associated to a document"},
      {"pds:DD_Attribute/pds:Internal_Reference.reference_type.attribute_to_image",
          "attribute_to_image", "The attribute is associated to an image"},
      {"pds:DD_Attribute/pds:Internal_Reference.reference_type.attribute_to_document",
          "attribute_to_document", "The attribute is associated to a document"},

      // Product_Observational
      {"pds:Product_Observational/pds:Observation_Area/pds:Target_Identification/pds:Internal_Reference.reference_type.data_to_target",
          "data_to_target", "The observational product is associated to a target product"},

      // Product_Bundle
      {"pds:Product_Bundle/pds:Context_Area/pds:Target_Identification/pds:Internal_Reference.reference_type.bundle_to_target",
          "bundle_to_target", "The bundle product is associated to a target product"},

      // Product_Collection
      {"pds:Product_Collection/pds:Context_Area/pds:Target_Identification/pds:Internal_Reference.reference_type.collection_to_target",
          "collection_to_target", "The collection product is associated to a target product"},

      // Product_Browse
      {"pds:Product_Browse/pds:Context_Area/pds:Target_Identification/pds:Internal_Reference.reference_type.browse_to_target",
          "browse_to_target", "The browse product is associated to a target product"},

      // Product_External
      {"pds:Product_External/pds:Context_Area/pds:Target_Identification/pds:Internal_Reference.reference_type.external_to_target",
          "external_to_target", "The external product is associated to a target product"},

      // Product_Native
      {"pds:Product_Native/pds:Context_Area/pds:Target_Identification/pds:Internal_Reference.reference_type.native_to_target",
          "native_to_target", "The native product is associated to a target product"},

      // Product_SPICE_Kernel
      {"pds:Product_SPICE_Kernel/pds:Context_Area/pds:Target_Identification/pds:Internal_Reference.reference_type.data_to_target",
          "data_to_target", "The SPICE kernel product is associated to a target product"},

      // Product_XML_Schema
      {"pds:Product_XML_Schema/pds:Context_Area/pds:Target_Identification/pds:Internal_Reference.reference_type.schema_to_target",
          "schema_to_target", "The XML schema product is associated to a target product"}
  };
}
