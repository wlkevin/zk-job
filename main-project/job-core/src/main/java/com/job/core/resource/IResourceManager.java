package com.job.core.resource;

import java.util.List;

public interface IResourceManager {

	public List<String> getJobs(String jobsPath);
	public boolean init(String rootPath);
	public void parseJobs(List<String> jobs);
}
