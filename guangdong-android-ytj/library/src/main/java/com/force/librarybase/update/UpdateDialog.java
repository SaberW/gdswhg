package com.force.librarybase.update;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.KeyEvent;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.force.librarybase.BaseApplication;
import com.force.librarybase.R;

class UpdateDialog {

    /**
     * 非强制更新
     *
     * @param context
     * @param content
     * @param downloadUrl
     * @return
     */
    public static MaterialDialog createUpdateDialogNotMust(final Context context, String content, final String downloadUrl) {
        return new MaterialDialog.Builder(context)
                .title("发现新版本")
                .content(Html.fromHtml(content))
//                .contentGravity(GravityEnum.CENTER)
//                .buttonsGravity(GravityEnum.CENTER)
                .positiveText(R.string.update_confirm)
                .negativeText(R.string.update_cancel)
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        goToDownload(context, downloadUrl);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        BaseApplication.getInstance().getEventBus().post(new DownloadApkEvent());
                        dialog.dismiss();
                    }
                })
                .build();
    }

    /**
     * 强制更新
     *
     * @param context
     * @param content
     * @param downloadUrl
     */
    public static void createUpdateDialogIsMust(final Context context, String content, final String downloadUrl) {
        new AlertDialogWrapper.Builder(context)
                .setTitle("发现新版本")
                .setMessage(Html.fromHtml(content))
                .setCancelable(false)
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .setNegativeButton(R.string.update_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToDownload(context, downloadUrl);
                    }
                }).show();
    }

    private static boolean isContextValid(Context context) {
        return context instanceof Activity && !((Activity) context).isFinishing();
    }


    private static void goToDownload(Context context, String downloadUrl) {

//        ToastUtils.show(context,"开始下载，请在通知栏中查看进度");

        Intent intent = new Intent(context.getApplicationContext(), DownloadService.class);
        intent.putExtra(Constants.APK_DOWNLOAD_URL, downloadUrl);
        context.startService(intent);
    }
}
