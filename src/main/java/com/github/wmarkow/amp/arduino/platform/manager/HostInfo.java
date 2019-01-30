package com.github.wmarkow.amp.arduino.platform.manager;

public class HostInfo {

	private String osArch;
	private String osName;

	public HostInfo(String osArch, String osName) {
		this.osArch = osArch;
		this.osName = osName;
	}

	public String getOsArch() {
		return osArch;
	}

	public String getOsName() {
		return osName;
	}

	public final static HostInfo populateLocalhostInfo() {
		String arch = java.lang.System.getProperty("os.arch");
		String name = java.lang.System.getProperty("os.name");

		java.lang.System.getProperty("sun.desktop");
		java.lang.System.getProperties();

		return new HostInfo(arch, name);
	}

	@Override
	public String toString() {
		return "HostInfo [osArch=" + osArch + ", osName=" + osName + "]";
	}
}
