package com.force.librarybase.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.force.librarybase.BaseApplication;
import com.force.librarybase.R;
import com.force.librarybase.utils.Null;
import com.force.librarybase.utils.logger.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.File;


/**
 * @author feicien (ithcheng@gmail.com)
 * @since 2016-07-05 19:21
 */
class CheckUpdateTask extends AsyncTask<Void, Void, String> {

    private ProgressDialog dialog;
    private Context mContext;
    private int mType;
    private boolean mShowProgressDialog;
    private static final String url = "http://192.168.1.162:8080/edition/queryByType.do";

    CheckUpdateTask(Context context, int type, boolean showProgressDialog) {

        this.mContext = context;
        this.mType = type;
        this.mShowProgressDialog = showProgressDialog;

    }

    @Override
    protected void onPostExecute(String result) {
        Logger.d(result);
        if (!TextUtils.isEmpty(result)) {
            parseJson(result);
        } else {
            BaseApplication.getInstance().getEventBus().post(new DownloadApkEvent());
        }
    }

    private void parseJson(String result) {
        try {
            AppUpdateResponse appUpdateResponse = new Gson().fromJson(result, AppUpdateResponse.class);
            int versionCode = AppUtils.getVersionCode(mContext);
            if (!Null.isNull(appUpdateResponse) && !Null.isNull(appUpdateResponse.returnData)) {
                AppUpdateResponse.ReturnDataEntity returnDataEntity = appUpdateResponse.returnData;
                String apkUrl = appUpdateResponse.basePath + File.separator + returnDataEntity.editionUrl;//下载地址
                int apkCode = Integer.parseInt(returnDataEntity.editionId);
                if (apkCode > versionCode && !TextUtils.isEmpty(returnDataEntity.editionUrl)) {
//                    if (mType == Constants.TYPE_NOTIFICATION) {
//                        showNotification(mContext, returnDataEntity.editionContent, apkUrl);
//                    } else if (mType == Constants.TYPE_DIALOG) {

                    int isMust = returnDataEntity.isMust;
                    if (isMust == 1) {//强制更新
                        UpdateDialog.createUpdateDialogIsMust(mContext, returnDataEntity.editionContent, apkUrl);
                    } else {//非强制更新
                        UpdateDialog.createUpdateDialogNotMust(mContext, returnDataEntity.editionContent, apkUrl).show();
                    }
//                    BaseApplication.getInstance().getEventBus().post(new CleanDataEvent());
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.android_auto_update_toast_no_new_update), Toast.LENGTH_SHORT).show();
                    BaseApplication.getInstance().getEventBus().post(new DownloadApkEvent());
                }
            } else {
                BaseApplication.getInstance().getEventBus().post(new DownloadApkEvent());
            }
        } catch (JsonParseException e) {
            Log.e(Constants.TAG, "parse json error");
            BaseApplication.getInstance().getEventBus().post(new DownloadApkEvent());
        } catch (NumberFormatException e) {
            BaseApplication.getInstance().getEventBus().post(new DownloadApkEvent());
        }
    }

    /**
     * Show Notification
     */
    private void showNotification(Context context, String content, String apkUrl) {
        Intent myIntent = new Intent(context, DownloadService.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myIntent.putExtra(Constants.APK_DOWNLOAD_URL, apkUrl);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        int smallIcon = context.getApplicationInfo().icon;
        Notification notify = new NotificationCompat.Builder(context)
                .setTicker(context.getString(R.string.android_auto_update_notify_ticker))
                .setContentTitle(context.getString(R.string.android_auto_update_notify_content))
                .setContentText(content)
                .setSmallIcon(smallIcon)
                .setContentIntent(pendingIntent).build();

        notify.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notify);
    }

    @Override
    protected String doInBackground(Void... args) {
        return HttpUtils.get(url);
    }
}
