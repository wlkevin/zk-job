package com.job.core.zk.impl;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs.Ids;

import com.job.core.zk.IZkCurator;

public class ZkCurator implements IZkCurator {

	private CuratorFramework client = null;
	private CuratorWatcher defaultWatcher = new CuratorWatcher() {

		public void process(WatchedEvent event) throws Exception {
			System.out.println("监控： " + event);
		}
	};

	public CuratorFramework initClient(String ip, int retryTimes, int times) {

		client = CuratorFrameworkFactory.newClient(ip, new RetryNTimes(retryTimes, times));
		return client;
	}

	/**
	 * 创建子节点
	 */
	public boolean createChildren(CuratorFramework client, String pNode, String cNode, CreateMode mode, Ids ids) {

		try {
			client.create().withMode(mode).withACL(Ids.OPEN_ACL_UNSAFE).forPath(pNode, cNode.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除节点
	 */
	public boolean deleteChildren(CuratorFramework client, String path) {

		try {
			client.delete().withVersion(-1).forPath(path);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 获取子节点
	 */
	public List<String> getChildren(CuratorFramework client, String path, CuratorWatcher watcher) {

		if (watcher != null) {
			defaultWatcher = watcher;
		}
		List<String> childrenNode = null;
		try {
			childrenNode = client.getChildren().usingWatcher(defaultWatcher).forPath(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return childrenNode;
	}

	/**
	 * 启动连接
	 */
	public boolean start() {

		client.start();
		return false;
	}

	/**
	 * 关闭连接
	 */
	public boolean stop() {

		client.close();
		return false;
	}

}
