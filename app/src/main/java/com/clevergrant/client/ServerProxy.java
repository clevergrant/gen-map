package com.clevergrant.client;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.clevergrant.result.LoginResult;
import com.clevergrant.result.RegisterResult;
import com.clevergrant.tasks.LoginTask;
import com.clevergrant.tasks.RegisterTask;
import com.clevergrant.tasks.SyncDataTask;

public class ServerProxy implements RegisterTask.Context, LoginTask.Context, SyncDataTask.Context {

	private CallbackListener callbackListener;

	private void setCallbackListener(AppCompatActivity activity) {
		callbackListener = (CallbackListener) activity;
	}

	public ServerProxy(Context c) {
		setCallbackListener((AppCompatActivity) c);
	}

	public void register() {
		RegisterTask task = new RegisterTask(this);
		task.execute();
	}

	public void login() {
		LoginTask task = new LoginTask(this);
		task.execute();
	}

	public void syncData() {
		SyncDataTask task = new SyncDataTask(this);
		task.execute();
	}

	// Callbacks

	@Override
	public void registerComplete(RegisterResult result) {
		callbackListener.registerComplete(result);
	}

	@Override
	public void loginComplete(LoginResult result) {
		callbackListener.loginComplete(result);
	}

	@Override
	public void dataFetched(SyncDataTask.SyncResult result) {
		callbackListener.dataFetched(result);
	}

	// Callback Interface

	public interface CallbackListener {
		void registerComplete(RegisterResult result);

		void loginComplete(LoginResult result);

		void dataFetched(SyncDataTask.SyncResult result);
	}
}
