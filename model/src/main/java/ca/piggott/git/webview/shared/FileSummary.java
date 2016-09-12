package ca.piggott.git.webview.shared;

import java.util.Date;

public class FileSummary implements Comparable<FileSummary> {

	private String filename;

	private boolean isDirectory;

	private Date lastChange;

	private String refid;

	@SuppressWarnings("unused")
	private FileSummary() {
		
	}

	public FileSummary(String refid, String filename, Date lastChange, boolean isDirectory) {
		super();
		this.refid = refid;
		this.filename = filename;
		this.isDirectory = isDirectory;
		this.lastChange = lastChange;
	}

	public int compareTo(FileSummary o2) {
		if (isDirectory()) {
			return o2.isDirectory() ? getFilename().compareToIgnoreCase(o2.getFilename()) : -1;
		}
		return o2.isDirectory() ? 1 : getFilename().compareToIgnoreCase(o2.getFilename());
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

	public boolean isDirectory() {
		return isDirectory;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
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
