package com.github.wmarkow.amp.arduino.platform;

public class System {
	private String host;
	private String archiveFileName;
	private String url;

	public String getHost() {
		return host;
	}

	public String getArchiveFileName() {
		return archiveFileName;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return "System [host=" + host + ", archiveFileName=" + archiveFileName + ", url=" + url + "]";
	}

}
