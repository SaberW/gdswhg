package com.force.librarybase.command;

import java.util.UUID;

public abstract class TaskBase<T extends Response> implements Task<T>,
		Comparable<Task<T>> {

	protected Priority priority = Priority.NORMAL; // 优先级别
	protected String taskId;
	protected int index;
	protected boolean isExecutting; // 是否正在执行
	protected boolean isCanceled; // 是否已取消执行

	protected TaskProcessListener<T> taskProcessListener;

	public TaskBase() {
		taskId = UUID.randomUUID().toString();
	}

	public TaskBase(TaskProcessListener<T> taskProcessListener) {
		taskId = UUID.randomUUID().toString();
		this.taskProcessListener = taskProcessListener;
		if (taskProcessListener != null) {
			taskProcessListener.onPrepare();
		}
	}

	public int getPriority() {
		return priority.ordinal();
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String getId() {
		return taskId;
	}

	@Override
	public void setStart() {
		this.isExecutting = true;
	}

	@Override
	public boolean isExecutting() {
		return isExecutting;
	}
	
	@Override
	public boolean isCanceled() {
		return isCanceled;
	}
	
	@Override
	public void cancel() {
		isCanceled = true;
	}

	@Override
	public void deliverResponse(T response) {
		if (taskProcessListener != null) {
			taskProcessListener.onSuccess(response);
		}
	}

	@Override
	public void deliverError(TPError error) {
		if (taskProcessListener != null) {
			taskProcessListener.onFail(error);
		}
	}

	@Override
	public void removeCallback() {
		this.taskProcessListener = null;
	}

	@Override
	public int compareTo(Task<T> o) {
		int left = getPriority();
		int right = o.getPriority();

		// High-priority requests are "lesser" so they are sorted to the front.
		// Equal priorities are sorted by sequence number to provide FIFO
		// ordering.
		return left == right ? index - o.getIndex() : right - left;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}

		if (!(o instanceof Task<?>)) {
			return false;
		}
		Task<?> other = (Task<?>) o;
		return this.taskId.equals(other.getId());
	}
}
