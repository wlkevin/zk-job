package com.job.core.zk;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;

public interface IZkCurator {

	public CuratorFramework initClient(String ip, int retryTimes, int times);

	public boolean createChildren(CuratorFramework client, String parentNode, String childrenNode, CreateMode mode,
			Ids ids);

	public boolean deleteChildren(CuratorFramework client, String path);

	public List<String> getChildren(CuratorFramework client, String path, CuratorWatcher watcher);

	public boolean start();

	public boolean stop();
}
