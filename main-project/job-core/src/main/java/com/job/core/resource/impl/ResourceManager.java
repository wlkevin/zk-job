package com.job.core.resource.impl;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;

import com.job.core.resource.IResourceManager;
import com.job.core.zk.IZkCurator;
import com.job.core.zk.impl.ZkCurator;

public class ResourceManager implements IResourceManager {

	private IZkCurator curator;
	
	/**
	 * 获取调度任务
	 */
	public List<String> getJobs() {
		
		curator=new ZkCurator();
		CuratorFramework client=curator.initClient("", 10, 5000);
		List<String> children=curator.getChildren(client, "", null);
		return children;
	}

	

}
