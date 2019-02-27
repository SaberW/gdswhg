package com.force.librarybase.command;

public interface Task<T extends Response> {

    public enum Priority {
        LOW, NORMAL, HIGH, IMMEDIATE
    }

    int getPriority();

    void setPriority(Priority priority);

    int getIndex();

    void setIndex(int index);

    void deliverResponse(T response);

    void deliverError(TPError error);

    void cancel();

    boolean isCanceled();

    void execute();

    String getId();

    boolean isExecutting();

    void removeCallback();

    /**
     * 任务处理器自动调用，开发者请勿调用该方法
     */
    void setStart();
}
