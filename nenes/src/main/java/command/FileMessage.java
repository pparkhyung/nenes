package command;

import java.util.List;

public class FileMessage {

	private String fileName;
	private List<String> agent, fileList;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<String> getAgent() {
		return agent;
	}

	public void setAgent(List<String> agent) {
		this.agent = agent;
	}

	public List<String> getFileList() {
		return fileList;
	}

	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}

	
	
}
