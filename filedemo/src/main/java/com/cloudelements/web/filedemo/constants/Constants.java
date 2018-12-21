package com.cloudelements.web.filedemo.constants;

public class Constants {

	// Error messages
	public static final String FILE_NOT_FOUND = "File not found %s";

	//constants
	public static final String MAPPING_UPLOADFILE_URI = "/uploadFile";
	public static final String MAPPING_DOWNLOADFILE_URI = "/downloadFile";
	public static final String MAPPING_BROWSEFILE_URI = "/browseFiles";
	public static final String MAPPING_ERRORS_URI = "/errors";

	public static final String REQUEST_PARAM_FILE = "file";
	public static final String REQUEST_PARAM_UPLOADTO = "uploadTo";
	public static final String REQUEST_PARAM_FILENAME = "filename";
	public static final String REQUEST_PARAM_DOWNLOADFROM = "downloadFrom";
	public static final String REQUEST_PARAM_SEARCHDIR = "searchDir";
	public static final String REQUEST_PARAM_PATTERN = "pattern";

	public static final String DEFAULT_MIME_TYPE = "application/json";
	public static final String DEFAULT_DISPOSITION_HEADER = "attachment; filename=\"%s\"";
	public static final String DEFAULT_FILE_SEARCH_PATTERN = "*";
	
	public static final String DEFAULT_DIRTYPE="dir";
	public static final String DEFAULT_FILETYPE="file";
	public static final String DEFAULT_ERROR_STRING= "{\"code\": %s, \"Exception Message\": \"%s\"}"; 
	
	
}
