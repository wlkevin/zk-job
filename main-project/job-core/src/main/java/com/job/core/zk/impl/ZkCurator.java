package com.job.core.zk.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

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
	public boolean createChildren( String pNode, String cNode, CreateMode mode, ArrayList<ACL> acls) {

		try {
			client.create().withMode(mode).withACL(acls).forPath(pNode, cNode.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除节点
	 */
	public boolean deleteChildren(String path) {

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
	public List<String> getChildren(String path, CuratorWatcher watcher) {

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

	public String getData(String path) {
		
		try {
			byte[] data=client.getData().forPath(path);
			return new String(data,"UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean setData(String path, String data) {
		
		try {
			Stat stat=client.setData().forPath(path, data.getBytes());
			if(stat.getMzxid()>0)
				return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 检查节点是否存在
	 */
	public boolean checkNodeExists(String path) {
		
		try {
			Stat stat=client.checkExists().forPath(path);
			if(stat!=null)
				return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
