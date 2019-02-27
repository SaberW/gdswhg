package com.force.librarybase.command;

import com.force.librarybase.utils.logger.Logger;

import java.util.Iterator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskQueue<T extends Response> {

	private final static int DEFAULT_INITIAL_CAPACITY = 10;

	public final static int INVALID_INDEX = Integer.MIN_VALUE;

	/**
	 * Used for generating monotonically-increasing sequence numbers for
	 * requests.
	 */
	private AtomicInteger mIndexGenerator = new AtomicInteger();

	private PriorityBlockingQueue<Task<T>> taskQueue; // 等待执行任务队列
	private PriorityBlockingQueue<Task<T>> taskQueueExecutting; // 正在执行的任务队列

	private int initialCapacity = DEFAULT_INITIAL_CAPACITY;

	private boolean isStop;

	public TaskQueue() {
		taskQueue = new PriorityBlockingQueue<Task<T>>(initialCapacity);
		taskQueueExecutting = new PriorityBlockingQueue<Task<T>>(
				initialCapacity);
	}

	private int getTaskIndex() {
		return mIndexGenerator.incrementAndGet();
	}

	public int add(Task<T> task) {
		if (task == null || task.isCanceled()) {
			return INVALID_INDEX;
		}
		int index = getTaskIndex();
		task.setIndex(index);
		taskQueue.add(task);
		return index;
	}

	public Task<T> take() throws InterruptedException {
		Task<T> task = taskQueue.take();
		addToExecutting(task);
		return task;
	}

	/**
	 * 将任务添加到正在执行的任务队列
	 * 
	 * @param task
	 */
	void addToExecutting(Task<T> task) {
		if (task == null || task.isCanceled()) {
			return;
		}
		taskQueueExecutting.add(task);
	}

	/**
	 * 将任务从正在执行队列移除
	 * 
	 * @param task
	 */
	void removeFromExecutting(Task<?> task) {
		if (task == null || task.isCanceled()) {
			return;
		}
		taskQueueExecutting.remove(task);
	}

	public void stop() {
		taskQueue.clear();
		isStop = true;
	}

	public boolean isStop() {
		return isStop;
	}

	void removeTask(String taskId, boolean evenInExecutting) {
		Iterator<Task<T>> iterator = null;
		if (evenInExecutting) {
			iterator = taskQueueExecutting.iterator();
			while (iterator.hasNext()) {
				Task<T> task = iterator.next();
				if (task.getId().equals(taskId)) {
					task.cancel();
					task.removeCallback();
					taskQueueExecutting.remove(task);
					Logger.w("task:%s has been canceled successfully.", taskId);
					return;
				}
			}
		}

		iterator = taskQueue.iterator();
		while (iterator.hasNext()) {
			Task<T> task = iterator.next();
			if (task.getId().equals(taskId)) {
				task.cancel();
				task.removeCallback();
				taskQueue.remove(task);
				Logger.w("task:%s has been canceled successfully.", taskId);
				return;
			}
		}
		Logger.w("task queue is empty or task:%s has been already executed.",
				taskId);
	}

}
