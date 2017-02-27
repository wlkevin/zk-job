package com.job.core.resource.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;

import com.job.core.model.Task;
import com.job.core.resource.IResourceManager;
import com.job.core.zk.IZkCurator;
import com.job.core.zk.impl.ZkCurator;

public class ResourceManager implements IResourceManager {

	private IZkCurator curator;
	private AtomicBoolean isInit=new AtomicBoolean(false);
	private LinkedBlockingQueue<Task> queue=new LinkedBlockingQueue<Task>();
	private ExecutorService executor=Executors.newSingleThreadExecutor(new ResourceThreadFactory());
	
	/**
	 * 获取调度任务
	 */
	public List<String> getJobs(String jobsPath) {
		
		List<String> children=curator.getChildren(jobsPath, new CuratorWatcher(){

			public void process(WatchedEvent arg0) throws Exception {
				if(arg0.getType().getIntValue()==EventType.NodeCreated.getIntValue()){
					String path=arg0.getPath();
					Task task=new Task();
					task.setState(1);
					task.setTaskName(path);
					String cronExpression=curator.getData(path);
					task.setCronExpression(cronExpression);
					queue.put(task);
				}else if(arg0.getType().getIntValue()==EventType.NodeDeleted.getIntValue()){
					String path=arg0.getPath();
					Task task=new Task();
					task.setState(3);
					task.setTaskName(path);
					queue.put(task);
				}
				
			}
		});
		return children;
	}
	
	public boolean init(String rootPath) {
		
		if(!isInit.compareAndSet(false, true)){
			return false;
		}
		curator=new ZkCurator();
		curator.initClient("", 10, 5000);
		curator.start();
		//检查job父节点是否存在
		boolean exists=curator.checkNodeExists(rootPath);
		if(!exists){
			curator.createChildren("/", "job",CreateMode.EPHEMERAL_SEQUENTIAL, Ids.READ_ACL_UNSAFE);
		}
		//获取任务
		List<String> jobs=getJobs("job");
		
		TaskRunner runner=new TaskRunner();
		executor.execute(runner);
		return true;
	}
	
	private class TaskRunner implements Runnable{

		public void run() {
			while(true){
				try {
					Task task=queue.take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	/**
	 * 线程工厂
	 * @author wl
	 *
	 */
	static class ResourceThreadFactory implements ThreadFactory{
		
		private final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;
		
        public ResourceThreadFactory(){
        	SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                                  Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                          poolNumber.getAndIncrement() +
                         "-thread-";
        }
		public Thread newThread(Runnable r) {
			
			Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
			if (!t.isDaemon())
			  t.setDaemon(true);
			if (t.getPriority() != Thread.NORM_PRIORITY)
			  t.setPriority(Thread.NORM_PRIORITY);
			return t;
		}
		
	}

	public void parseJobs(List<String> jobs) {
		
		if(jobs==null || jobs.isEmpty())
			return;
		int leng=jobs.size();
	}

}
