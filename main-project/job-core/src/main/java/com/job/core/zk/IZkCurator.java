package com.job.core.zk;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.ACL;

public interface IZkCurator {

	public CuratorFramework initClient(String ip, int retryTimes, int times);

	public boolean createChildren( String parentNode, String childrenNode, CreateMode mode,
			ArrayList<ACL> acls);

	public boolean deleteChildren( String path);

	public List<String> getChildren(String path, CuratorWatcher watcher);

	public boolean start();

	public boolean stop();
	
	public String getData(String path);
	
	public boolean setData(String path,String data);
	
	public boolean checkNodeExists(String path);
}
