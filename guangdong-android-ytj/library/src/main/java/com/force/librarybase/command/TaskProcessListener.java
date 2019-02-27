package com.force.librarybase.command;

public interface TaskProcessListener<T extends Response> {

	void onPrepare();
	
	void onSuccess(T response);

	void onFail(TPError error);
}
