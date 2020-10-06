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


    private static final String ALI_SOPHIX_APP_KEY = "31438044";
    private static final String ALI_SOPHIX_APP_SECRET = "6db86041d72c84ddff7ee92efce4e171";
    private static final String ALI_SOPHIX_APP_RSA = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDgPQr8GuRh+FplljiN/WkDyGqQzLjUhf9DVAR+vZNEn/nNQu0uktjx00BoleYdjsn5B9ogs6FCWOUesMIYL2HkwymhFCLteCDsbH2iYfhb8UxBLlG4+AP0CfsajOtQWxZzq5QSgDtLtYm2Ogk5YDIVyV78zxBmNZ+17C/75i+WeGLakaDqn+FyzhQkj/iKEdmAgzdPiaexblfLZkrZy+/bcF+ehfI3Zw6MLB+/1vFbV2tVbusVCpnNvYvF9tLIKvIxrrSBbjEJoTiV5mHnv9Ehxu09CTMIShPexHp1l+Hg2lMgwq+MJMWkWjHjaM12Wio4ll5kAR95Liof+eRbMh4tAgMBAAECggEBAJtESU3lRQw9TkKXMNPIiyWOaZOX3cdyfRzemTo1BDr5Vpv1YovkLInN0ARa/vqqe1mPQU9xchChwwxsMglTDcK8DCA39DNgAdQTDy8YDUOEoltjujyMpxgcrR1mRC3g1HwFa0MgPW6f5hoit25eGHATfk0pB17udIcgYt1WNs9FgqnEAhc+Qq1fBxS2mJDbhI/GZtauECdzrug/JDExOItjg9JdoW/EfvP4VU4Tz/w9gkTM3gm/pZFPHK1X6z7lN152CBw4OP6bDTl2TXfVLT51TbbTs83hwOR+ygA9uMaib1cGW1+oedRHURoY9G++eLlSWEwI0XSI7M8KgkiMgkECgYEA9hGle74hckCY/aTGEC6LwQQ4FObK5Gfmle3570rqgPn4jAycgh68kzQ5gbtH1tPuGknSOdNoc+u1okSKPAEe/CbU6TbVVnD4bk+FIk8w3gccg5k4LRmf2JhcLyS+uaGftNBPz1ifH/wmaO9nIpie+brV8Av8yaSinRz1L/vWYlECgYEA6UnYvs3RfidLAIh6biyNoVIyn/OueLMe8EEeDYk4T9JPMjGFUNuZMr32+fswIXY/Xr2qF3JJUSzTecI01PmI0IX+fO/ZGL+SwvMTi2SHrW35c/fxqR34bBr56vpVbOOlTAbL8Ihv2JgrLgKrCoXjzf/l2McLUQh4ypOJkBNXix0CgYEA1Yf0qZARaBsEaD8fnRU3M3Tc0c6BTHmZUIvlcrD5FJlXBPlAq23OyJLuQFTrf1IDcg6TfpilMMrfoIOHzZBPehKcehCaStvXyan55w7/Yfyb0tsYM5Lj8kos3MAvPdkyRO7sAwttco0WK8NsJUp50k4+roNIhxb2Ag8J+ey5WjECgYEAytxRYxIc0sOeX9dOB1Is0pRku1yuYfz4sVkepgyl8BtkjLQtRrO+0zYHdzerkd1+m4b4Sq0x/impt55eTLi09Qcf1krHENM97tJrM3J23SPdoK+B8FrEdFU8781BzY9jZOogG4i5IbKpEbh2PY9SGgclOs1TFOL2Dsoe/C8lmC0CgYB7+FAMx28+xgW0m+zMpVhtkRLHNNtSVJsqSx11LuVc8mz8fFBvmPRakaGvmlowFa9qy4ZUVH3AGk/xlR0bj/NZltCny/8cOiX+mQXXVIIlNOZqmdhW77S7JFBtUt2QTL1vel3cte2/1CF9CDJW6DHNRENAHSZiHbfaXbxp1P+LTw==";
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
        tags.add("normal");
        SophixManager.getInstance().setTags(tags);
        SophixManager.getInstance().queryAndLoadNewPatch();
    }
}
