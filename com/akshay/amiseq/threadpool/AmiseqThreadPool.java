package com.akshay.amiseq.threadpool;

import java.util.concurrent.ConcurrentLinkedQueue;

/** A very simple thread pool implementation.
 * @author Akshay
 *
 */
public class AmiseqThreadPool extends AkshayExecutorService {

	//default alues
	static final int DEFAULT_RETRY_DELAY = 1000;
	static final int DEFAULT_RETRY_TIMES = 2;

	//input values
	private int retryDelay = DEFAULT_RETRY_DELAY;
	private int retryTimes = DEFAULT_RETRY_TIMES;

	/**
	 * Default constructor
	 */
	public AmiseqThreadPool() {
		this(DEFAULT_POOL_COUNT);
	}

	/**
	 * Input no. of pools, retry params will be used default.
	 * @param poolCount
	 */
	public AmiseqThreadPool(int poolCount) {
		this(poolCount, DEFAULT_RETRY_DELAY, DEFAULT_RETRY_TIMES);
	}

	/**Custom arg constructor
	 * @param poolCount
	 * @param retryDelay
	 * @param retryTimes
	 */
	public AmiseqThreadPool(int poolCount, int retryDelay, int retryTimes) {
		super(new ConcurrentLinkedQueue<>(), poolCount);
		this.retryDelay = retryDelay;
		this.retryTimes = retryTimes;
	
		//post constructor work
		createThreads();
	}

	/**
	 *Create pool thread and provide how pool thread will process all inputs.
	 */
	@Override
	protected void createThreads() {
		for (int i = getPoolCount(); i > 0; i--) {
			getThreads().add(new Job());
		}
	}

	/**
	 * job thread class
	 * @author Akshay
	 *
	 */
	class Job extends Thread {

		@Override
		public void run() {
			while (hasData()) {
				Runnable r = poll();
				if (r != null)
					r.run();
			}
		}

		/**Utility method with retry logic checked with delays
		 * @return boolean
		 */
		boolean hasData() {
			return hasData(retryTimes);
		}

		/**Recursive queue size check method.
		 * @param retry
		 * @return
		 */
		private boolean hasData(int retry) {
			if (!getQueue().isEmpty()) {
				return true;
			} else if (retry >= 0) {
				// sleep for delay sec then retry
				try {
					Thread.sleep(retryDelay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return hasData(--retry);
			} else
				return false;
		}
	}
}
