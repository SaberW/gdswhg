package com.force.librarybase.command;

import com.force.librarybase.utils.StringUtils;
import com.force.librarybase.utils.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {

	public static final int DEFAULT_DISPATCHER_SIZE = 4;
	private int dispatcherSize;
	private HashMap<String, String> taskIds = new HashMap<String, String>();
	TaskQueue<Response> taskQueue;
	private List<TaskDispacther<Response>> taskDispacthers = new ArrayList<TaskDispacther<Response>>();

	public static TaskManager INSTANCE = null;

	public static synchronized TaskManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TaskManager();
		}
		return INSTANCE;
	}

	private TaskManager() {
		taskQueue = new TaskQueue<Response>();
		TaskDispacther<Response> taskDispacther;
		dispatcherSize = DEFAULT_DISPATCHER_SIZE;
		for (int i = 0; i < dispatcherSize; i++) {
			taskDispacther = new TaskDispacther<Response>(this, taskQueue);
			taskDispacthers.add(taskDispacther);
		}
	}

	/**
	 * 添加一个task到执行队列
	 * 
	 * @param task
	 */
	public void add(Task<Response> task) {
		if (taskIds.containsKey(task.getId())) {
			Logger.w("task [%s] already in task queue.");
			return;
		}
		taskQueue.add(task);
		taskIds.put(task.getId(), null);
	}

	/**
	 * 根据任务id从任务队列移除任务，默认不移除正在执行的任务
	 * 
	 * @param taskId
	 */
	void clearTaskId(String taskId) {
		if (taskIds.containsKey(taskId)) {
			taskIds.remove(taskId);
		}
	}

	/**
	 * 将任务从任务队列移除，默认不移除正在执行的任务
	 * 
	 * @param task
	 */
	public void cancelTask(Task<?> task) {
		if (task == null || task.isCanceled()) {
			Logger.w("task is null or already canceled");
			return;
		}
		if (task.isExecutting()) {
			return;
		}
		cancelTask(task.getId());
	}

	public void cancelTask(String taskId) {
		if (StringUtils.isBlank(taskId)) {
			Logger.w("taskId is empty");
			return;
		}
		taskQueue.removeTask(taskId, false);
		clearTaskId(taskId);
	}

	/**
	 * 将任务从任务队列移除，即使正在执行
	 * 
	 * @param task
	 */
	public void cancelTaskEvenInExecution(Task<?> task) {
		if (task == null || task.isCanceled()) {
			Logger.w("task is null or already canceled");
			return;
		}
		cancelTaskEvenInExecution(task.getId());
	}

	/**
	 * 将任务从任务队列移除，即使正在执行
	 * 
	 * @param taskId
	 */
	public void cancelTaskEvenInExecution(String taskId) {
		if (StringUtils.isBlank(taskId)) {
			Logger.w("taskId is empty");
			return;
		}
		taskQueue.removeTask(taskId, true);
		clearTaskId(taskId);
	}

	public void start() {
		for (TaskDispacther<Response> dispacther : taskDispacthers) {
			dispacther.start();
		}
	}

	public void stop() {
		for (TaskDispacther<Response> dispacther : taskDispacthers) {
			dispacther.interrupt();
		}
		taskQueue.stop();
	}

	public boolean isStop() {
		return taskQueue.isStop();
	}

}
