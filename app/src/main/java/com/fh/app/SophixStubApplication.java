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


    private static final String ALI_SOPHIX_APP_KEY = "31251497";
    private static final String ALI_SOPHIX_APP_SECRET = "59bb015f9f71331a3d8d4580777bc639";
    private static final String ALI_SOPHIX_APP_RSA = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDiBzR4XTKoL3zZSTlEUnpb6ca2K0xMvfTC/003bTKznd/i06ueBa1X6g52zXcEvRzyGqCjsKcO2UIqk8+1XSTNdMBu3+OLcYRMcVm5qq3pGE+nDyIEe0E0Q2FE3PboeX29JjNMPmpRimHEtDcNNTlzpD2DrnynWEWgtmlzYVuGUyW12vq6yFYFOOCgTQY6UoBq2M3qwzPM8cQNzGwer2mwR5RxR5TVG2rjqQBiHVmOOWqMdmspYkIZAxQ/l3gIrdJ0xoonOERW0FlWkz1O3jOqkSlI54bMTNRJIb2CamqZwbSxUzZ1U0YQFUygwQlEvJO8fXcVy2M5skkGXA+uiCOTAgMBAAECggEAH+tSjpjiuXuTTwAXNT3Mh8RYyosMyF2e/hadqfEi0ti9sEOiHbvBggPMrRxQ7XtjZhE6lEMzJFasEGU1VPBlbRpKvp2hfaS3QTBUsnKXAq7rOLVbYXSifaXyS45E1AmsJxUI84Bw9Tjsm6GzkIrupRURltICSoOduZdpBuE90pcRZcnvi0q6thGRv3eY7BUaeBDbJmYLdFqB6sG895x8N6/8lKYysUs7tYjtZyBGz4kbYaLss3ILZL/cKoJBYc/UpC1Zwas1TSBja4ROwvIhvMbDu2Ylk0vvX+rRGqVwPsoz3by4OWTXzGBGKqhe7qPXTftjTn9DEfxZtCLgpHysmQKBgQD5JgfoGVFvba7hGJp1I/FKBf3AEGPjHm0l2TKAq1x3bju7JhB+yOPubJetXm8gNKXVjUeatsuQhcqaUBE8/oZZYs7hauCgC6bJh2a4kNeHChJri8pgl1fjShfRKVZc9juSTd9dMv3024czwWSZNKXSsdv6QWSi26aFQUd7DIr4rQKBgQDoPmjm1mZGzTFvAHRm/yTegtvLg/uE8w/3GYfrlEFi8WjdSXqKioSFbwA0OfzIQBpuKxJsza61JiW5hHc2wBm9nP2NfUbhVOE901Sebp12MXN4HLZyGHrUETThNy3O9AgtSdmkJbrv+Mc6rQDyicZRdos2dsghD/nLOYsrEcHVPwKBgQD0YpWG2K7EefX73cJD/MmFLnNEnc8czI+LfIzJIU1F6N62KBQuCKdrCyGMcQDXukygOEBwRbVPQa0FyFvgVFk7/GupOMi0tDkZnLLpf0mpr/t5elk8fbc7v0W1aLLJ5M1yKOhb5tSsBVmNLrmafcXZ8ZQw2Uv5YzLnqqyOyjLW0QKBgQCN0a6VZ89E5QCO3CQ40Q1eo2I8wPfSk+rn5btObFYRaYGzv68I8tVB9iqVueNXFW4OYFb8Ag3xqL3Yr79su5n8Y+WhhSLHbuGss2Q06y9UgZYVftMakUBQR8GW1e8vNW3Y2gQBbNjeuXhPqF2vvdpQ3KbeN2K2bd8H7rfBMTLAWQKBgQCaPcri04KSpsLRqC+cXeddJUZ2GRiPq8A6sesnwBqZ+NjRzIt847HQqT1pfYjpX4RyRVikXCKj9OWskij/oqH5ceyHmPFYk+rUq9vnWXnYP3wya/wtRJpV4EstxZUa1jCWvUDBzSfSltHrGKL9gRij4S7kiz2j8Mdm8yzJtOeCCA==";
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
//        List<String> tags = new ArrayList<>();
//        tags.add("normal");
//        SophixManager.getInstance().setTags(tags);
//        SophixManager.getInstance().queryAndLoadNewPatch();
    }
}
