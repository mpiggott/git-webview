package ca.piggott.git.webview.shared;

import java.util.Date;

public class File {

	private String content;

	private String filename;

	private Date lastChange;
	
	private String refid;

	@SuppressWarnings("unused")
	private File() {
		
	}

	public File(String refid, String filename, String content, Date lastChange) {
		super();
		this.refid = refid;
		this.filename = filename;
		this.lastChange = lastChange;
		this.setContent(content);
	}

	public String getContent() {
		return content;
	}

	public String getFilename() {
		return filename;
	}

	public Date getLastChange() {
		return lastChange;
	}

	public String getRefid() {
		return refid;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setLastChange(Date lastChange) {
		this.lastChange = lastChange;
	}

	public void setRefid(String refid) {
		this.refid = refid;
	}
}
