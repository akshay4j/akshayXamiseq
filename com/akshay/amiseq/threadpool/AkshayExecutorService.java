package com.akshay.amiseq.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * My Executor Service for multi-threading activities.
 * 
 * @author Akshay
 *
 */
public abstract class AkshayExecutorService {

	private Queue<Thread> queue;
	private List<Thread> threads = new ArrayList<>();
	public static final int DEFAULT_POOL_COUNT = 3;
	private int poolCount = DEFAULT_POOL_COUNT;

	/**
	 * Takes a specific type of queue, has to be provide by implementor. Pool count
	 * from user input.
	 * 
	 * @param queue
	 * @param poolCount
	 */
	public AkshayExecutorService(Queue<Thread> queue, int poolCount) {
		this.queue = queue;
		this.poolCount = poolCount;
	}

	//TODO: take advise for sync
	public Thread poll() {
		Thread polled = getQueue().poll();
		if(polled != null)
			System.out.println("polled: "+polled.getName());
		return polled;
	}
	
	// methods to be implemented by child

	/**
	 * Implement and put logic to create pool threads.
	 */
	abstract void createThreads();

	// Getters

	/**
	 * Get queue.
	 * 
	 * @return
	 */
	public Queue<Thread> getQueue() {
		return queue;
	}

	/**
	 * Get pool count value;
	 * 
	 * @return
	 */
	public int getPoolCount() {
		return poolCount;
	}

	/**
	 * Get list of pool threads.
	 * 
	 * @return
	 */
	public List<Thread> getThreads() {
		return threads;
	}

	/**
	 * Add your task to queue.
	 * 
	 * @param runnable
	 * @return
	 */
	public boolean add(Thread runnable) {
		return queue.add(runnable);
	}

	// TODO: try to call 2 times, if already running

	/**
	 * Start execution, starts all pool threads.
	 */
	public synchronized void execute() {
		for (Thread job : threads) {
			if (job.isAlive())
				continue;
			job.start();
		}
	}

//	abstract public void terminate();
}
