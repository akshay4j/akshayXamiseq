package com.akshay.amiseq;

import com.akshay.amiseq.threadpool.AkshayExecutorService;
import com.akshay.amiseq.threadpool.AmiseqThreadPool;
import com.akshay.amiseq.threadpool.StreammingThreadPool;

public class EntryPoint {

	public static void main(String[] args) throws Exception {

		System.out.println("starting service 1...");

		EntryPoint e = new EntryPoint();

		// construct
		AkshayExecutorService service = new AmiseqThreadPool(2, 1000, 0);

		// add inputs
		e.producerFunction(service, 10, 0, 0, "service1");

		// start execution
//		service.execute();

		System.out.println("-----completed: pool 1 -----");

		System.out.println("starting pool 2 ");
		// construct
		AkshayExecutorService service2 = new StreammingThreadPool(1);

//		 thread for producer work 
//		it will keep adding values in background thread
		e.producerFunction(service2, 30, 1000, 10, "----");

		// thread for consumer work
		// this will hit consumer work in background thread
//		Thread cons = new Thread() {
//			@Override
//			public void run() {
//				// start exec in background
//				service2.execute();
//			}
//		};

		// start consuming
//		cons.start();

		service2.execute();

		e.producerFunction(service2, 30, 100, 10, "****");
		Thread.sleep(100);
		e.producerFunction(service2, 30, 0, 10, "****");
		Thread.sleep(100);
		e.producerFunction(service2, 30, 0, 10, "****");
		Thread.sleep(100);
		e.producerFunction(service2, 30, 0, 10, "****");
		Thread.sleep(100);
		e.producerFunction(service2, 30, 0, 10, "****");
		Thread.sleep(100);
		e.producerFunction(service2, 30, 0, 10, "****");
		Thread.sleep(100);
		e.producerFunction(service2, 30, 0, 10, "****");
		Thread.sleep(100);
		e.producerFunction(service2, 30, 0, 10, "****");

		System.out.println();
	}

	public void producerFunction(AkshayExecutorService service, int itr, int itrDelay, int taskDelay, String prefix) {
		Thread t = new Thread() {
			@Override
			public void run() {

				for (int i = 1; i <= itr; i++) {
					Thread r = new Thread() {

						@Override
						public void run() {
							System.out.println("executing: " + this.getName());
							try {
								Thread.sleep(taskDelay);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							System.out.println("done.....: " + this.getName());
						}
					};
					r.setName(prefix + "#" + i);
					service.add(r);

					try {
						Thread.sleep(taskDelay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();

	}

}
