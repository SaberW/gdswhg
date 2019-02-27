package com.creatoo.guangdong_pos.mvp.presenter;

import android.Manifest;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.creatoo.guangdong_pos.R;
import com.creatoo.guangdong_pos.api.RetrofitApi;
import com.creatoo.guangdong_pos.api.domain.IDcardInfo;
import com.creatoo.guangdong_pos.mvp.contract.SignContract;
import com.creatoo.guangdong_pos.mvp.ui.activity.SignActivity;
import com.creatoo.guangdong_pos.mvp.ui.dialog.NoticeDialog;
import com.creatoo.guangdong_pos.utils.location.LocationHelper;
import com.force.librarybase.mvp.presenter.BasePresenter;
import com.force.librarybase.network.exception.ApiException;
import com.force.librarybase.network.observable.HttpRxObserver;
import com.force.librarybase.network.retrofit.HttpResponse;
import com.force.librarybase.utils.NetWorkUtil;
import com.force.librarybase.utils.Null;
import com.force.librarybase.utils.logger.Logger;
import com.hdos.usbdevice.publicSecurityIDCardLib;

import java.io.UnsupportedEncodingException;

import io.reactivex.disposables.Disposable;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.force.librarybase.network.observable.HttpRxObservable.getObservable;

/**
 * @author Administrator
 * @version v1.0
 * @Package com.creatoo.guangdong_pos.mvp.presenter
 * @Description:
 * @date 2018/4/18
 */
public class SignPresenter extends BasePresenter<SignContract.View,SignActivity> implements SignContract.Presenter,AMapLocationListener {
    private final String TAG = SignPresenter.class.getSimpleName();
    private publicSecurityIDCardLib mIdCardLib;
    private String mPageNameStr;

    public static final int RC_LOCATION_CONTACTS_PERM = 124;
    public static final String[] LOCATION_AND_CONTACTS =
            {Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE};

    private double mLongitude = 112.95168;
    private double mLatitude = 28.226608;

    private IDcardInfo mIdCardInfo;

    public SignPresenter(SignContract.View view, SignActivity activity) {
        super(view, activity);
        mView.setPresenter(SignPresenter.this);
        mPageNameStr = activity.getPackageName();
    }

    @Override
    public boolean readIDcard() {
        boolean result = false;
        mIdCardInfo = new IDcardInfo();
        int ret;
        String show = "";
        checkReadState();
        try {
            ret =mIdCardLib.readBaseMsgToStr(mPageNameStr,mIdCardInfo.getBmpFile(),mIdCardInfo.getName(),
                    mIdCardInfo.getSex(),mIdCardInfo.getNation(), mIdCardInfo.getBirth(), mIdCardInfo.getAddress(), mIdCardInfo.getIDNo(), mIdCardInfo.getDepartment(),
                    mIdCardInfo.getEffectDate(), mIdCardInfo.getExpireDate());
            if (ret==0x90){
                result = true;
            }
        }catch (Exception e){

        }
        // if(ret==0x90)
        //        {
        //            int[] colors =mIdCardLib.convertByteToColor(iDcardInfo.getBmpFile());
        //            b = Bitmap.createBitmap(colors, 102, 126, Bitmap.Config.ARGB_8888);
        //            ImageSpan imgSpan = new ImageSpan(b);
        //            spanString = new SpannableString("icon");
        //            spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //            try {
        //                show = "姓名:"+new String(iDcardInfo.getName(), "Unicode")+'\n'
        //                        +"性别:"+new String(iDcardInfo.getSex(), "Unicode")+'\n'
        //                        +"民族:"+ new String(iDcardInfo.getNation(), "Unicode")+"族"+'\n'
        //                        +"出生日期:"+new String(iDcardInfo.getBirth(), "Unicode")+'\n'
        //                        +"住址:"+new String(iDcardInfo.getAddress(), "Unicode")+'\n'
        //                        +"身份证号码:"++'\n'
        //                        +"签发机关:"+new String(iDcardInfo.getDepartment(), "Unicode")+'\n'
        //                        +"有效日期:"+ new String(iDcardInfo.getEffectDate(), "Unicode") + "至" + new String(iDcardInfo.getExpireDate(), "Unicode")+'\n';
        //            } catch (UnsupportedEncodingException e) {
        //                // TODO Auto-generated catch block
        //                e.printStackTrace();
        //            }
        return result;
    }

    @Override
    public void readState() {
        int ret;
        ret =mIdCardLib.getSAMStatus();
        String show;
        if(ret==0x90)
            show = "模块状态良好";
        else
            show ="模块状态错误:"+ String.format("0x%02x", ret);
    }

    @Override
    public void userSign() {
        HttpRxObserver<HttpResponse> httpRxObserver = new HttpRxObserver<HttpResponse>(TAG + "getInfo") {


            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                mView.showResultDialog(NoticeDialog.MODE_SIGN_FAIL,e.getMsg());
            }

            @Override
            protected void onSuccess(HttpResponse response) {
                if (!Null.isNull(response) && response.isSuccess()){
                    mView.showResultDialog(NoticeDialog.MODE_SIGN_SUCCESS,response.getErrormsg());
                }else{
                    mView.showResultDialog(NoticeDialog.MODE_SIGN_FAIL,response.getErrormsg());
                }
            }
        };

        /**
         * 切入后台移除RxJava监听
         * ActivityEvent.PAUSE(FragmentEvent.PAUSE)
         * 手动管理移除RxJava监听,如果不设置此参数默认自动管理移除RxJava监听（onCrete创建,onDestroy移除）
         */

        if (NetWorkUtil.isConn(mActivity)){
            try {
                getObservable(RetrofitApi.getInstance().mUserSign(new String(mIdCardInfo.getIDNo(), "Unicode"),mLatitude,mLongitude),mActivity).subscribe(httpRxObserver);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            mView.showToast(mActivity.getResources().getString(R.string.network_unavailable));
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        super.onDestroy(owner);
        LocationHelper.getInstance().destroyLocation();
    }


    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        super.onCreate(owner);
        LocationHelper.getInstance().initLoction();
        try {
            mIdCardLib = new publicSecurityIDCardLib(mActivity);
            mIdCardLib = null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        super.onStart(owner);
        LocationHelper.getInstance().setLocationListener(this);
        LocationHelper.getInstance().startLocation();
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        super.onStop(owner);
        LocationHelper.getInstance().stopLocation();
    }





    @Override
    public void onLocationChanged(AMapLocation location) {
        if (null != location) {

            StringBuffer sb = new StringBuffer();
            //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
            if(location.getErrorCode() == 0){

                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();


                sb.append("定位成功" + "\n");
                sb.append("定位类型: " + location.getLocationType() + "\n");
                sb.append("经    度    : " + location.getLongitude() + "\n");
                sb.append("纬    度    : " + location.getLatitude() + "\n");
                sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                sb.append("提供者    : " + location.getProvider() + "\n");

                sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                sb.append("角    度    : " + location.getBearing() + "\n");
                // 获取当前提供定位服务的卫星个数
                sb.append("星    数    : " + location.getSatellites() + "\n");
                sb.append("国    家    : " + location.getCountry() + "\n");
                sb.append("省            : " + location.getProvince() + "\n");
                sb.append("市            : " + location.getCity() + "\n");
                sb.append("城市编码 : " + location.getCityCode() + "\n");
                sb.append("区            : " + location.getDistrict() + "\n");
                sb.append("区域 码   : " + location.getAdCode() + "\n");
                sb.append("地    址    : " + location.getAddress() + "\n");
                sb.append("兴趣点    : " + location.getPoiName() + "\n");
                //定位完成的时间
            } else {
                //定位失败
                sb.append("定位失败" + "\n");
                sb.append("错误码:" + location.getErrorCode() + "\n");
                sb.append("错误信息:" + location.getErrorInfo() + "\n");
                sb.append("错误描述:" + location.getLocationDetail() + "\n");
            }
            sb.append("***定位质量报告***").append("\n");
            sb.append("* WIFI开关：").append(location.getLocationQualityReport().isWifiAble() ? "开启":"关闭").append("\n");
            sb.append("* GPS状态：").append(LocationHelper.getInstance().getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
            sb.append("* GPS星数：").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
            sb.append("* 网络类型：" + location.getLocationQualityReport().getNetworkType()).append("\n");
            sb.append("* 网络耗时：" + location.getLocationQualityReport().getNetUseTime()).append("\n");
            sb.append("****************").append("\n");
            //定位之后的回调时间

            //解析定位结果，
            String result = sb.toString();
//            Logger.d(result);
        } else {
            Logger.d("定位失败");
        }
    }


    private void checkReadState(){
        if (Null.isNull(mIdCardLib)){
            try {
                mIdCardLib = new publicSecurityIDCardLib(mActivity);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
