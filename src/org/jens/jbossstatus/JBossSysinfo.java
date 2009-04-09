package org.jens.jbossstatus;

public class JBossSysinfo {

	private Integer activeThreadCount;
	private Integer availableProcessors;
	private String oSArch;
	private Long maxMemory;
	private String hostAddress;
	private String javaVersion;
	private String oSVersion;
	private String javaVendor;
	private Long totalMemory;
	private Integer activeThreadGroupCount;
	private String oSName;
	private Long freeMemory;
	private String hostName;
	private String javaVMVersion;
	private String javaVMVendor;
	private String javaVMName;


	public Integer getActiveThreadCount() {
		return activeThreadCount;
	}
	public void setActiveThreadCount(Integer activeThreadCount) {
		this.activeThreadCount = activeThreadCount;
	}
	public Integer getAvailableProcessors() {
		return availableProcessors;
	}
	public void setAvailableProcessors(Integer availableProcessors) {
		this.availableProcessors = availableProcessors;
	}
	public String getOSArch() {
		return oSArch;
	}
	public void setOSArch(String arch) {
		oSArch = arch;
	}
	public Long getMaxMemory() {
		return maxMemory;
	}
	public void setMaxMemory(Long maxMemory) {
		this.maxMemory = maxMemory;
	}
	public String getHostAddress() {
		return hostAddress;
	}
	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}
	public String getJavaVersion() {
		return javaVersion;
	}
	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}
	public String getOSVersion() {
		return oSVersion;
	}
	public void setOSVersion(String version) {
		oSVersion = version;
	}
	public String getJavaVendor() {
		return javaVendor;
	}
	public void setJavaVendor(String javaVendor) {
		this.javaVendor = javaVendor;
	}
	public Long getTotalMemory() {
		return totalMemory;
	}
	public void setTotalMemory(Long totalMemory) {
		this.totalMemory = totalMemory;
	}
	public Integer getActiveThreadGroupCount() {
		return activeThreadGroupCount;
	}
	public void setActiveThreadGroupCount(Integer activeThreadGroupCount) {
		this.activeThreadGroupCount = activeThreadGroupCount;
	}
	public String getOSName() {
		return oSName;
	}
	public void setOSName(String name) {
		oSName = name;
	}
	public Long getFreeMemory() {
		return freeMemory;
	}
	public void setFreeMemory(Long freeMemory) {
		this.freeMemory = freeMemory;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getJavaVMVersion() {
		return javaVMVersion;
	}
	public void setJavaVMVersion(String javaVMVersion) {
		this.javaVMVersion = javaVMVersion;
	}
	public String getJavaVMVendor() {
		return javaVMVendor;
	}
	public void setJavaVMVendor(String javaVMVendor) {
		this.javaVMVendor = javaVMVendor;
	}
	public String getJavaVMName() {
		return javaVMName;
	}
	public void setJavaVMName(String javaVMName) {
		this.javaVMName = javaVMName;
	}


}
