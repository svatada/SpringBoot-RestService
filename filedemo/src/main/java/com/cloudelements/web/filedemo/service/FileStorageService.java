package com.cloudelements.web.filedemo.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cloudelements.web.filedemo.beans.BrowseDirResponse;
import static com.cloudelements.web.filedemo.constants.Constants.*;

@Service
public class FileStorageService {
	private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);
	private Path filePath;

	@Autowired
	public FileStorageService() throws Exception {
	}

	public String storeFile(MultipartFile file, String uploadDirPath) {
		Path uploadPath = Paths.get(uploadDirPath).toAbsolutePath().normalize();
		/*
		 * Check uploadDir exists or not. if not exists create new one.
		 **/
		try {
			Resource resource = new UrlResource(uploadPath.toUri());
			if (!resource.exists()) {
				Files.createDirectories(uploadPath);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			// Copy file to the target location (Replacing existing file with the same name)
			Path targetLocation = uploadPath.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			return fileName;
		} catch (IOException ex) {
			logger.error(ex.getMessage());
		}
		return fileName;
	}

	public Resource loadFileAsResource(String fileName, String downloadDirPath) {
		try {
			Path downloadFrom = Paths.get(downloadDirPath).toAbsolutePath().normalize();
			filePath = downloadFrom.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists()) {
				return resource;
			} else {
				logger.error(String.format(FILE_NOT_FOUND, fileName));
			}
		} catch (MalformedURLException ex) {
			logger.error(String.format(FILE_NOT_FOUND, fileName), ex);
		}
		return null;
	}

	public String getMimeType() {
		try {
			//fetch content type from path obj
			return Files.probeContentType(filePath);
		} catch (IOException e) {
			return DEFAULT_MIME_TYPE;
		}
	}

	public List<BrowseDirResponse> browseFiles(String dirName, String pattern) {
		Path filePath = Paths.get(dirName).toAbsolutePath().normalize();
		List<BrowseDirResponse> fileList = new ArrayList<BrowseDirResponse>();

		try {
			//walk dir tree to read directory content
			Files.walkFileTree(filePath, new HashSet<FileVisitOption>(), 3, new java.nio.file.SimpleFileVisitor() {
				@Override
				public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
					Path dirPath = (Path) dir;
					fileList.add(
							new BrowseDirResponse(dirPath.toFile().getName(), dirPath.getParent().toFile().getName(),
									attrs.isDirectory() ? DEFAULT_DIRTYPE : DEFAULT_FILETYPE, attrs.size(), dirPath.toString()));
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
					Path filePath = (Path) file;
					fileList.add(
							new BrowseDirResponse(filePath.toFile().getName(), filePath.getParent().toFile().getName(),
									attrs.isDirectory() ? DEFAULT_DIRTYPE : DEFAULT_FILETYPE, attrs.size(), filePath.toString()));
					return FileVisitResult.CONTINUE;

				}

				@Override
				public FileVisitResult visitFileFailed(Object file, IOException exc) throws IOException {
					logger.error(exc.getMessage());
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Object dir, IOException exc) throws IOException {
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return fileList;
	}
}