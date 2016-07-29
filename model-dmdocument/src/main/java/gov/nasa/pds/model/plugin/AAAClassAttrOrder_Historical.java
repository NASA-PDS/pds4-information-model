
public class AAAClassAttrOrder_Historical {
	
/*	
	// =================== Class Sorting Maps ==========================================
	iLabelGroup = new TreeMap <String, Integer> ();
	
	// iLabelGroup is the master class sort order map by default
	masterClassSortOrderMap = iLabelGroup;
	
	iLabelGroup.put("Identification_Area", 1);
	iLabelGroup.put("Alias_List", 3);
	iLabelGroup.put("Citation_Information", 5);
	iLabelGroup.put("Modification_History", 7);
	iLabelGroup.put("Modification_Detail", 9);

	iLabelGroup.put("Observation_Area", 11);
	iLabelGroup.put("Subject_Area", 11);
	iLabelGroup.put("Context_Area", 11);
	iLabelGroup.put("Time_Coordinates", 13);
//	iLabelGroup.put("Primary_Result_Description", 15);
	iLabelGroup.put("Primary_Result_Summary", 15);
	iLabelGroup.put("Investigation_Area", 17);
	iLabelGroup.put("Observing_System", 19);
	iLabelGroup.put("Observing_System_Component", 21);
	iLabelGroup.put("Target_Identification", 23);
	iLabelGroup.put("Geometry", 25);
	iLabelGroup.put("Cartography", 27);
	iLabelGroup.put("Mission_Area", 29);
//	iLabelGroup.put("Node_Area", 31);
	iLabelGroup.put("Discipline_Area", 31);
//	iLabelGroup.put("Document_Format", 34);
//	iLabelGroup.put("Document_Description", 35);
//	iLabelGroup.put("Document_Format_Set", 36);
	
	iLabelGroup.put("Reference_List", 41);
	iLabelGroup.put("Internal_Reference", 42);
	iLabelGroup.put("External_Reference", 43);

	iLabelGroup.put("Collection", 50);
	iLabelGroup.put("Bundle", 50);
	iLabelGroup.put("Archive_Bundle", 50);
	iLabelGroup.put("Instrument", 51);
	iLabelGroup.put("Instrument_Host", 52);
	iLabelGroup.put("Investigation", 53);
	iLabelGroup.put("Target", 59);
	iLabelGroup.put("Document_Format", 51);
//	iLabelGroup.put("Document_Description", 52);
	iLabelGroup.put("Document", 52);
	iLabelGroup.put("Document_Format_Set", 53);
	iLabelGroup.put("Zip", 50);

	
	iLabelGroup.put("File_Area", 51);
	iLabelGroup.put("File_Area_Observational", 51);
	iLabelGroup.put("File_Area_Observational_Supplemental", 52);
	iLabelGroup.put("File_Area_Browse", 51);
	iLabelGroup.put("File_Area_Text", 51);
	iLabelGroup.put("File_Area_Encoded_Image", 51);
	iLabelGroup.put("File_Area_SPICE_Kernel", 51);
	iLabelGroup.put("File_Area_Manifest", 51);
	iLabelGroup.put("File_Area_Inventory", 51);
//	iLabelGroup.put("File_Area_Inventory_LIDVID", 51);
//	iLabelGroup.put("File_Area_Inventory_LID", 51);
	iLabelGroup.put("File_Area_XML_Schema", 51);
	iLabelGroup.put("File", 53);
	iLabelGroup.put("Document_File", 54);

//	iLabelGroup.put("Array_Element", 33);
//	iLabelGroup.put("Array_Axis", 34);
	iLabelGroup.put("Element_Array", 33);
	iLabelGroup.put("Axis_Array", 34);
	iLabelGroup.put("Special_Constants",41);
	iLabelGroup.put("Object_Statistics",42);
	
	iLabelGroup.put("Uniformly_Sampled",31);	
	iLabelGroup.put("Record_Binary",33);	
	iLabelGroup.put("Record_Character",33);	
	
	iLabelGroup.put("Field_Binary",34);	
	iLabelGroup.put("Field_Character",34);	
	iLabelGroup.put("Group_Field_Binary",35);	
	iLabelGroup.put("Group_Field_Character",35);	

	iLabelGroup.put("Software_Format_Set", 17);
	iLabelGroup.put("Software_Desc", 17);	
	iLabelGroup.put("Data_Set", 17);		
	
//=================== Attribute Sorting Maps ==========================================
	
	classGroup = new TreeMap <String, Integer> ();
	
	// classGroup is the master attribute sort order map by default
	masterAttrSortOrderMap = classGroup;

	classGroup.put("logical_identifier", 1);
	classGroup.put("name", 1);
	classGroup.put("version_id", 3);
	classGroup.put("ldd_version_id", 3);
	classGroup.put("revision_id", 3);
	classGroup.put("title", 5);
//	classGroup.put("name", 7);
	classGroup.put("doi", 7);
	classGroup.put("file_name", 7);
	classGroup.put("date_time", 7);				// needed for Update
	classGroup.put("directory_path_name", 8);
	classGroup.put("full_name", 8);
	classGroup.put("document_name", 8);
	classGroup.put("axis_name", 8);
	classGroup.put("lid_reference", 8);
	classGroup.put("lidvid_reference", 8);
	classGroup.put("file_specification_name", 9);
	classGroup.put("alternate_id", 9);
	classGroup.put("alternate_title", 11);
	classGroup.put("alternate_designation", 13);
	classGroup.put("local_identifier", 15);
	classGroup.put("starting_point_identifier", 15);
	
	classGroup.put("type", 17);
	classGroup.put("product_class", 19);
	classGroup.put("reference_type", 21);
	classGroup.put("format_type", 21);
	classGroup.put("bundle_type", 21);
	classGroup.put("collection_type", 21);
	classGroup.put("container_type", 21);

//	classGroup.put("observing_system_component_type", 23);
	classGroup.put("member_status", 21);
	classGroup.put("information_model_version", 13);

	classGroup.put("author_list", 21);
	classGroup.put("editor_list", 23);
	
	classGroup.put("purpose", 21);
	classGroup.put("data_regime", 23);
//	classGroup.put("reduction_level", 25);
	classGroup.put("processing_level_id", 25);
	
	classGroup.put("local_mean_solar_time", 9);
	classGroup.put("local_true_solar_time", 11);
	classGroup.put("solar_longitude", 13);
			
	classGroup.put("creation_date_time", 31);
	classGroup.put("modification_date",2);
	classGroup.put("publication_year", 33);
	
	classGroup.put("file_size", 33);
	classGroup.put("offset", 33);
	classGroup.put("records", 35);
	classGroup.put("object_length", 36);
	classGroup.put("record_length", 36);
	classGroup.put("record_delimiter", 37);
//	classGroup.put("maximum_record_bytes", 37);
	classGroup.put("maximum_record_length", 37);
	classGroup.put("fields", 38);
	classGroup.put("groups", 39);
	classGroup.put("bit_fields", 38);
	classGroup.put("md5_checksum", 39);
//	classGroup.put("external_standard_id", 41);
	classGroup.put("parsing_standard_id", 41);
	classGroup.put("encoding_standard_id", 41);
	classGroup.put("document_standard_id", 41);
//	classGroup.put("external_standard_version_id", 42);
	classGroup.put("kernel_type", 43);
	classGroup.put("spacecraft_clock_start_count", 44);
	classGroup.put("spacecraft_clock_stop_count", 45);
	classGroup.put("spacecraft_clock_count_partition", 46);
	classGroup.put("encoding_type", 45);	
			
	classGroup.put("repetitions", 31);	
	classGroup.put("group_location", 33);	
	classGroup.put("group_length", 39);	

	classGroup.put("axes", 35);	// must be after offset
	classGroup.put("axis_index_order", 37);
	classGroup.put("axis_storage_order", 39);
	
	classGroup.put("elements", 35);
	classGroup.put("unit", 39);
	
	classGroup.put("field_number", 21);
	classGroup.put("group_number", 21);
	classGroup.put("field_location", 22);
	classGroup.put("start_bit", 21);
	classGroup.put("stop_bit", 22);
	classGroup.put("data_type", 23);
	classGroup.put("field_length", 24);
	classGroup.put("maximum_field_length", 24);
	classGroup.put("bit_mask", 25);
	classGroup.put("field_format", 26);
//	classGroup.put("scaling_factor", 31);
//	classGroup.put("value_offset", 32);

//	classGroup.put("sample_bit_mask", 41);
	classGroup.put("scaling_factor", 43);
	classGroup.put("value_offset", 44);
	
	classGroup.put("display_direction", 31);
	classGroup.put("default_red", 32);
	classGroup.put("default_green", 33);
	classGroup.put("default_blue", 34);
	classGroup.put("frame_rate", 35);
	classGroup.put("continuous_loop_flag", 36);

	classGroup.put("sampling_parameter_name", 31);
	classGroup.put("sampling_parameter_interval", 32);
	classGroup.put("sampling_parameter_unit", 33);
	classGroup.put("first_sampling_parameter_value", 34);
	classGroup.put("last_sampling_parameter_value", 35);
	classGroup.put("sampling_parameter_scale", 36);	
	
	classGroup.put("saturated_constant", 31);
	classGroup.put("missing_constant", 32);
	classGroup.put("error_constant", 33);
	classGroup.put("invalid_constant", 34);
	classGroup.put("unknown_constant", 35);
	classGroup.put("not_applicable_constant", 36);
	
	classGroup.put("valid_maximum", 37);
	classGroup.put("high_instrument_saturation", 38);
	classGroup.put("high_representation_saturation", 39);
	classGroup.put("valid_minimum", 40);
	classGroup.put("low_instrument_saturation", 41);
	classGroup.put("low_representation_saturation", 42);
	
	classGroup.put("maximum", 21);
	classGroup.put("minimum", 22);
	classGroup.put("mean", 23);
	classGroup.put("standard_deviation", 24);
	classGroup.put("median", 25);
	classGroup.put("maximum_scaled_value", 40);
	classGroup.put("minimum_scaled_value", 41);

	classGroup.put("band_number", 11);
	classGroup.put("band_width", 12);
	classGroup.put("center_wavelength", 13);
	classGroup.put("detector_number", 14);
	classGroup.put("filter_number", 15);
	classGroup.put("grating_position", 16);
	classGroup.put("original_band", 17);
	// scaling - 31
	// std dev - 24
	// Value offset - 32
	
	classGroup.put("reference_text", 51);
	classGroup.put("acknowledgement_text", 51);
	classGroup.put("keywords", 53);
	classGroup.put("copyright", 53);
	classGroup.put("comment", 55);
//	classGroup.put("description", 57);
	classGroup.put("description", 97);

//	classGroup.put("reference_text", 91);
//	classGroup.put("acknowledgement_text", 91);
//	classGroup.put("keywords", 93);
//	classGroup.put("copyright", 93);
//	classGroup.put("comment", 95);
//	classGroup.put("description", 97);

	classGroup.put("address", 51);
	classGroup.put("country", 52);

	classGroup.put("aperture", 51);
	classGroup.put("longitude", 52);
	classGroup.put("latitude", 53);
	classGroup.put("telescope_longitude", 52);
	classGroup.put("telescope_latitude", 53);
	classGroup.put("altitude", 54);
	classGroup.put("coordinate_source", 55);

	classGroup.put("reference_frame_id", 51);
	classGroup.put("x", 52);
	classGroup.put("y", 53);
	classGroup.put("z", 54);		
	classGroup.put("vector_components", 52);
	
	classGroup.put("data_set_id",3);
	classGroup.put("data_set_name", 4);
	classGroup.put("data_set_release_date", 5);
	classGroup.put("start_date_time", 6);
	classGroup.put("stop_date_time", 7);
	classGroup.put("start_date", 18);
	classGroup.put("stop_date", 19);
	classGroup.put("producer_full_name", 8);
	classGroup.put("citation_text", 9);
	classGroup.put("data_set_terse_desc", 10);
	classGroup.put("abstract_desc", 11);
	classGroup.put("data_set_desc", 12);
	classGroup.put("confidence_level_note", 13);
	classGroup.put("archive_status", 14);
	
	classGroup.put("dataset_name", 1);
	classGroup.put("mission_name", 2);
	classGroup.put("target_name", 3);
	classGroup.put("instrument_name", 4);
	classGroup.put("instrument_host_name", 5);
	classGroup.put("node_name", 6);
	classGroup.put("document_name", 7);
	
	classGroup.put("registration_date", 11);
	classGroup.put("electronic_mail_address", 12);
	classGroup.put("sort_name", 13);
	
	classGroup.put("enumeration_flag", 10);
	classGroup.put("class_name", 12);
	classGroup.put("steward_id", 16);
//	classGroup.put("name_space_id", 18);
	classGroup.put("namespace_id", 18);
	classGroup.put("nillable_flag", 20);
//	classGroup.put("submitter_id", 22);
	classGroup.put("submitter_name", 22);
	classGroup.put("data_element_concept", 24);
	classGroup.put("designation", 26);
	classGroup.put("definition", 28);
	classGroup.put("language", 30);
	classGroup.put("registered_by", 56);
	classGroup.put("registration_authority_id", 57);
	classGroup.put("abstract_flag", 58);
//	classGroup.put("choice_flag", 59);
	
	classGroup.put("local_attribute_id", 1);
//	classGroup.put("name", 2);
	classGroup.put("value_data_type", 32);
	classGroup.put("formation_rule", 34);
	classGroup.put("minimum_characters", 36);
	classGroup.put("maximum_characters", 38);
	classGroup.put("minimum_value", 40);
	classGroup.put("minimum_value_exclusive_flag", 41);
	classGroup.put("maximum_value", 42);
	classGroup.put("maximum_value_exclusive_flag", 43);
	classGroup.put("minimum_occurrences", 40);
	classGroup.put("maximum_occurrences", 42);
	classGroup.put("pattern", 43);
	classGroup.put("unit_of_measure_type", 44);
	classGroup.put("conceptual_domain", 46);
	
	classGroup.put("value", 48);
	classGroup.put("value_meaning", 50);
	classGroup.put("value_begin_date", 52);
	classGroup.put("value_end_date", 54);
	*/
}
