package com.akshay.amiseq.threadpool;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * This thread pool implementation works like product-consumer.
 * 
 * @author Akshay
 *
 */
public class StreammingThreadPool extends AkshayExecutorService {

	/**
	 * Default queue size is maximum.
	 */
	final static int DEFAULT_QUEUE_SIZE = Integer.MAX_VALUE;// default

	/**
	 * Default Constructor
	 */
	public StreammingThreadPool() {
		this(DEFAULT_POOL_COUNT);
	}

	/**
	 * Input pool count
	 * 
	 * @param poolCount
	 */
	public StreammingThreadPool(int poolCount) {
		this(poolCount, DEFAULT_QUEUE_SIZE);
	}

	/**
	 * Custom constructor
	 * 
	 * @param poolCount
	 * @param queueSize
	 */
	public StreammingThreadPool(int poolCount, int queueSize) {
		// here the appropriate queue will be set, which will help to archive streamming
		// nature
		super(new LinkedBlockingQueue<>(queueSize), poolCount);
		createThreads();
	}

	/**
	 * Create pool thread and provide how pool thread will process all inputs.
	 */
	@Override
	protected void createThreads() {
		for (int i = getPoolCount(); i > 0; i--) {
			getThreads().add(new Job());
		}
	}

	/**
	 * job thread class
	 * 
	 * @author Akshay
	 *
	 */
	class Job extends Thread {

		@Override
		public void run() {
			while (true) {
				Runnable r = poll();
//				Runnable r = null;

				r.run();

			}
		}
	}

	@Override
	public Thread poll() {
		try {
			Thread polled = ((LinkedBlockingQueue<Thread>) getQueue()).take();
			if (polled != null)
				System.out.println("polled: " + polled.getName());
			return polled;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
