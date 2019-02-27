package com.force.librarybase.command;

import java.util.UUID;

public class TaskDispacther<T extends Response> extends Thread {

	private TaskManager taskManager;
	private TaskQueue<T> taskQueue;

	private String taskDispatcherID;

	public TaskDispacther(TaskManager taskManager, TaskQueue<T> taskQueue) {
		this.taskManager = taskManager;
		this.taskQueue = taskQueue;
		taskDispatcherID = UUID.randomUUID().toString();
	}

	@Override
	public void run() {
		Task<T> task = null;
		while (true) {
			try {
				task = taskQueue.take();
			} catch (InterruptedException e) {
				System.out.println(getTaskDispatcherID() + " interrupte");
				if (taskQueue.isStop()) {
					break;
				}
				continue;
			}
			if (task == null || task.isCanceled()) {
				continue;
			}
			task.setStart();
			task.execute();
			taskQueue.removeFromExecutting(task);
			if (taskQueue.isStop()) {
				break;
			}
		}
		System.out.println(getTaskDispatcherID()
				+ " complete or interrupted for some operation...");
		taskManager.clearTaskId(task.getId());
	}

	public String getTaskDispatcherID() {
		return taskDispatcherID;
	}

}
