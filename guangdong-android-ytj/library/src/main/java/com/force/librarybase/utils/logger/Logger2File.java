/*
 * Copyright (C) 2013  WhiteCat 白猫 (www.thinkandroid.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.force.librarybase.utils.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;

import com.force.librarybase.utils.AndroidVersionCheckUtils;
import com.force.librarybase.utils.SDCardUtils;

/**
 * @Title TAPrintToFileLogger
 * @Package com.ta.core.util.log
 * @Description TAPrintToFileLogger是TA框架中打印到sdcard上面的日志类
 * @author 白猫
 * @date 2013-1-16 14:25
 * @version V1.0
 */
public class Logger2File  {

	public static final int VERBOSE = 2;

	public static final int DEBUG = 3;

	public static final int INFO = 4;

	public static final int WARN = 5;

	public static final int ERROR = 6;

	public static final int ASSERT = 7;
	private String mPath;
	private Writer mWriter;

	private static final SimpleDateFormat TIMESTAMP_FMT = new SimpleDateFormat(
			"[yyyy-MM-dd HH:mm:ss] ");
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
	private String basePath = "";
	private static String LOG_DIR = "log";
	private static String BASE_FILENAME = "lifeix";
	private File logDir;

	private Context context;

	public Logger2File(Context context) {
		this.context = context;
	}

	public void open() {
		if (AndroidVersionCheckUtils.hasFroyo()) {
			logDir = SDCardUtils.getDiskCacheDir(context, LOG_DIR);
		} else {
			logDir = SDCardUtils.getDiskCacheDir(context, LOG_DIR);
		}
		if (!logDir.exists()) {
			logDir.mkdirs();
			// do not allow media scan
			try {
				new File(logDir, ".nomedia").createNewFile();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		basePath = logDir.getAbsolutePath() + "/" + BASE_FILENAME;
		try {
			File file = new File(basePath + "_" + getCurrentTimeString() + ".log");
			mPath = file.getAbsolutePath();
			mWriter = new BufferedWriter(new FileWriter(mPath), 2048);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private String getCurrentTimeString() {
		Date now = new Date();
		return simpleDateFormat.format(now);
	}

	public String getPath() {
		return mPath;
	}

	public void d(String tag, String message) {
		println(DEBUG, tag, message);
	}

	public void e(String tag, String message) {
		println(ERROR, tag, message);
	}

	public void i(String tag, String message) {
		println(INFO, tag, message);
	}

	public void v(String tag, String message) {
		println(VERBOSE, tag, message);
	}

	public void w(String tag, String message) {
		println(WARN, tag, message);
	}

	public void println(int priority, String tag, String message) {
		String printMessage = "";
		switch (priority) {
		case VERBOSE:
			printMessage = "[V]|" + tag + "|" + context.getPackageName() + "|"
					+ message;
			break;
		case DEBUG:
			printMessage = "[D]|" + tag + "|" + context.getPackageName() + "|"
					+ message;
			break;
		case INFO:
			printMessage = "[I]|" + tag + "|" + context.getPackageName() + "|"
					+ message;
			break;
		case WARN:
			printMessage = "[W]|" + tag + "|" + context.getPackageName() + "|"
					+ message;
			break;
		case ERROR:
			printMessage = "[E]|" + tag + "|" + context.getPackageName() + "|"
					+ message;
			break;
		default:

			break;
		}
		println(printMessage);

	}

	public void println(String message) {
		try {
			mWriter.write(TIMESTAMP_FMT.format(new Date()));
			mWriter.write(message);
			mWriter.write('\n');
			mWriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void close() {
		try {
			mWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
