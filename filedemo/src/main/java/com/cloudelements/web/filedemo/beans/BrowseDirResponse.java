package com.cloudelements.web.filedemo.beans;

public class BrowseDirResponse {

	    private String fileName;
	    private String parent;
	    private String fileType;
	    private long size;
	    private String dirPath;
	    
	    public BrowseDirResponse(String fileName, String parent, String fileType, long size, String dirPath ) {
	        this.fileName = fileName;
	        this.parent = parent;
	        this.fileType = fileType;
	        this.size = size;
	        this.dirPath = dirPath;
	    }

		public String getDirPath() {
			return dirPath;
		}

		public void setDirPath(String dirPath) {
			this.dirPath = dirPath;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getParent() {
			return parent;
		}

		public void setParent(String parent) {
			this.parent = parent;
		}

		public String getFileType() {
			return fileType;
		}

		public void setFileType(String fileType) {
			this.fileType = fileType;
		}

		public long getSize() {
			return size;
		}

		public void setSize(long size) {
			this.size = size;
		}
	    
	}
