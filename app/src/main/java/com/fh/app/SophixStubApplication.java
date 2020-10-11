package com.fh.app;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Keep;
import androidx.multidex.MultiDex;

import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Author  QinTian
 * @ Date  2020/9/6
 * @ Describe
 */
public class SophixStubApplication extends SophixApplication {

    private final String TAG = "SophixStubApplication";
    // 此处SophixEntry应指定真正的Application，并且保证RealApplicationStub类名不被混淆。


    private static final String ALI_SOPHIX_APP_KEY = "31459929";
    private static final String ALI_SOPHIX_APP_SECRET = "0e4e32349500d9af531ae684f3dba044";
    private static final String ALI_SOPHIX_APP_RSA = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCQap+2BK/EiEnguJgLY1HZ3ssZsDgXm7MMDYiGFXWB3DShtLJOsh0qIzofG3uQHppYMII4mFXk5w9E7WvErH7825jpIAaA7W+te6w1NO/mhSB2Pdr5BUS+fqFtN3vcDKlXU7Roy2q3ALNzw5qfyg2zlMvQ4KjPklgAJDIf7b7lrf3BB5f7fWweao8XcMZLn1KOpvXLa7O0AUJcm3m/YisBZnoYBZ59KTbcGdtEiI5R/aZ0BZsNJmhiXysQwb5SHt4N9wEYtsfBKBWlmbvOVrtkYJYhMDAZ7lwbGl2HAezFWHszjXJk4YZ7qQRpLeg5Q/znORlQUul0u5GSwLZZ4vvtAgMBAAECggEAOzS7TXAWb0hAoXSMQP7y65M/SMTJ+kPG38YclnvttNOGcFUYVNMIyeGvTlmCCs/BDy6z9GffrvxbcjKz3+moFhohUlHi2LB+/GybaSTOxX8Gk/M/9p+GU2Ku44u/p7M5KagTanMayPPsniUvGxtHCryR4ox4KMEiesxcEGLzxE0wfJ8hlIE5sB8o7Rr5mtlUl4yflH4xWTbyzU8f3+CIIq0UaSELMuVPc4kvgvmSQAbPWMfuexSmdwofVuRuC8MgR+R0R2dA0JiDY38rY9OrQlbIMpL7SLrq/RcNlr4x5mdwxfri/8jZKmf1Ct4tRf32g8a5egB+tydpiElf9in5sQKBgQD/256sZT+8oQBD8sImixrNAQp0KyaNcMG8aBlEdydH2L5KI7v9aNWWSQmgO2rGQrhgEO6b/Vb5TPYEnLb3cmWTQl9/aWue4zv4aEucVpQFfSVKMkIbDaZ6C7e0Bx4r4+3geuQk+aqnz94kTXrmajYSdPTO8U5fYuh4zQxf42QiPwKBgQCQfyiHFp1Jyyf33ZDLX4g5Uf4Be2YPl29uIeuKA+QRSD2O1SyVpXQJ1RsaB2QWQDJW0T25tPV+Sxr1lW49qwDd13nQxN6dJhgiTu1Ajw5fQUV8jTfPzP5UPoyFnmttS26TbajftM+AFWQNxyK2dZL1+lDRSBfuf66UJP7YRxC+0wKBgB1ps+4BnbgjkhI9oBAqQaR7kCYWp9HuunbcSLtUL1HVGtsPDJdvK86hCtg+vGGaXfO9PpD0KMF0FXwmP+pmXkL1iGDvT43udYBzDO4mDdIrttO2v164zQCOxEmP3+oPw9LgjcWqZfHwtYra1VwTra+lflcj6myQxETO3bNQQrylAoGBAIPoQOQJJe7+kW75ibvOavoAuoM51fK3yJZeSroEUUfhjah9PCUkFCkBrKr3Qkv5sLqGavXDlzQCdgroremH2NOrZ93ISnxlETUInhcIQRN972QDOobtWOMo8njsJJ9yc/AL9FS7p40CegMF3c94x37bUrmqOYaUcpd0qlxfi47XAoGABjbXt+PmOtkxntvj00B9Y/cv67aPvyxWUqnRrg3Z+aFodMdFsq6IOCwdCkhjDzEOo+dtS/ZctITIpNLRcfw8uc5f/osM//T/2mu8GmcgWUDaeP6BpS9BEPhkG4x48H9V6SP46l5Y1xW6OQQSEkVs7lVP3A+DwBZjsgwDmdBUQAs=";
    @Keep
    @SophixEntry(CpbApplication.class)
    static class RealApplicationStub {}

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//         如果需要使用MultiDex，需要在此处调用。
         MultiDex.install(this);
        initSophix();
    }

    private void initSophix() {
        String appVersion = "1.0.0";
        final SophixManager instance = SophixManager.getInstance();
        instance.setContext(this)
                .setAppVersion(appVersion)
                .setSecretMetaData(ALI_SOPHIX_APP_KEY,ALI_SOPHIX_APP_SECRET,ALI_SOPHIX_APP_RSA)
                .setEnableDebug(true)
                .setEnableFullLog()
                .setPatchLoadStatusStub((mode, code, info, handlePatchVersion) -> {
                    if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                        Log.e(TAG, "sophix load patch success!");
                    } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                        // 如果需要在后台重启，建议此处用SharePreference保存状态。
                        Log.e(TAG, "sophix preload patch success. restart app to make effect.");
                    }
                    Log.e(TAG, mode+"----"+ code +"----"+info+"----"+"handlePatchVersion---"+handlePatchVersion );
                }).initialize();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //code 必填（代理）  normal 选填（官方）
        List<String> tags = new ArrayList<>();
        tags.add("code");
        SophixManager.getInstance().setTags(tags);
        SophixManager.getInstance().queryAndLoadNewPatch();
    }
}
