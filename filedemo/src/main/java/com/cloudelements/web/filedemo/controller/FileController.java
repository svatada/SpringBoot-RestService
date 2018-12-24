package com.cloudelements.web.filedemo.controller;

import static com.cloudelements.web.filedemo.constants.Constants.*;
import static com.cloudelements.web.filedemo.constants.Constants.DEFAULT_MIME_TYPE;
import static com.cloudelements.web.filedemo.constants.Constants.MAPPING_BROWSEFILE_URI;
import static com.cloudelements.web.filedemo.constants.Constants.MAPPING_DOWNLOADFILE_URI;
import static com.cloudelements.web.filedemo.constants.Constants.MAPPING_UPLOADFILE_URI;
import static com.cloudelements.web.filedemo.constants.Constants.REQUEST_PARAM_DOWNLOADFROM;
import static com.cloudelements.web.filedemo.constants.Constants.REQUEST_PARAM_FILE;
import static com.cloudelements.web.filedemo.constants.Constants.REQUEST_PARAM_FILENAME;
import static com.cloudelements.web.filedemo.constants.Constants.REQUEST_PARAM_PATTERN;
import static com.cloudelements.web.filedemo.constants.Constants.REQUEST_PARAM_SEARCHDIR;
import static com.cloudelements.web.filedemo.constants.Constants.REQUEST_PARAM_UPLOADTO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudelements.web.filedemo.beans.BrowseDirResponse;
import com.cloudelements.web.filedemo.beans.UploadFileResponse;
import com.cloudelements.web.filedemo.service.FileStorageService;

@RestController
public class FileController implements ErrorController {

	@Autowired
	private FileStorageService fileStorageService;

	@PostMapping(MAPPING_UPLOADFILE_URI)
	public UploadFileResponse uploadFile(@RequestPart(REQUEST_PARAM_FILE) MultipartFile file,
			@RequestParam(REQUEST_PARAM_UPLOADTO) String dirPath) {
		String fileName = fileStorageService.storeFile(file, dirPath);
		return new UploadFileResponse(fileName, file.getContentType(), file.getSize());
	}

	@GetMapping(MAPPING_DOWNLOADFILE_URI)
	public ResponseEntity<Resource> downloadFile(@RequestParam(REQUEST_PARAM_FILENAME) String fileName,
			@RequestParam(REQUEST_PARAM_DOWNLOADFROM) String dirPath) {
		// Load file as Resource
		Resource resource = fileStorageService.loadFileAsResource(fileName, dirPath);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(fileStorageService.getMimeType()))
				.header(HttpHeaders.CONTENT_DISPOSITION,
						String.format(DEFAULT_DISPOSITION_HEADER, resource.getFilename()))
				.body(resource);
	}

	@GetMapping(MAPPING_BROWSEFILE_URI)
	public ResponseEntity<List<BrowseDirResponse>> browseFile(
			@RequestParam(REQUEST_PARAM_SEARCHDIR) String searchFolder,
			@RequestParam(value = REQUEST_PARAM_PATTERN, required = false, defaultValue = "*") String pattern) {
		// Load file as Resource
		List<BrowseDirResponse> filesList = fileStorageService.browseFiles(searchFolder, pattern);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(DEFAULT_MIME_TYPE)).body(filesList);
	}

	@RequestMapping(MAPPING_ERRORS_URI)
	public String handleError(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
		return String.format(DEFAULT_ERROR_STRING, statusCode, exception);
	}

	@Override
	public String getErrorPath() {
		return MAPPING_ERRORS_URI;
	}
}