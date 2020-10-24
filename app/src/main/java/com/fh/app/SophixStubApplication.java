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


    private static final String ALI_SOPHIX_APP_KEY = "31705312";
    private static final String ALI_SOPHIX_APP_SECRET = "b87a264aebf90e179455f5d811d06136";
    private static final String ALI_SOPHIX_APP_RSA = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCD9g0uYSQUvSw4I0mEgB6tkf9OcmKGrpnIHR+IYRjEK03T3iCJKXKxXdPNEpgrTRD5NkE1sW6bzDk44H00/SSckIsWu/We3+uxjsYoH3eOHbYELdWSij8QU9bOGK7uej910fRTl0YmmSscF4qRlEXyOz3hYTw1urd0j7Jxd30+8G98GezrKBNdg95wBB8sgli5+RZMA1siOx9k2Hj4D0bluvIm9g+RVjP7VNW3H4zI1WNyZ4Ls5WrOMooAOzSb/2RsM0XsnoUcr2U1adtfdH7PAoQuUKj1Yqaj6WU97VnY3PJQPhfHf/J+3FAGw8UoDJDlcd+2sKfW6Q6BJJNqAPkVAgMBAAECggEAGWs7/sMXgCcwbV/euXc0cZq94w6FjAv182YovMx3SNJOm5EeE024mhHIyqfaR/0AWADrbrxO4hPkseAMPjjE5KOzJx3tojzf2cP2x6PIE8gFV6i8iw22OB7uMaTvjWBe+QMIXVG2YZj/hNnEB2bYUmC2+sMQJglhbUKAzPqHVagSt+LHQ+j6Ak/OIP1y0MDCbQUWIqEIUuGAi1NNZLCMwFkR62kkYmefDI0QQ5N+cdNuYSBQYqHLfYK5a/bLtDeFNgdEfR2l4iDHU7Z8QsjapSIgxZ2dj60Jjmq9WqnyDoFer07BkLGo2kvIR540op0ybzvE28QvSd5JuTZoGr0CAQKBgQDeZ6x2dFEiAJoZRmRV1gyIxSaY7kgrpAyLCl/E4zhzlb8SKYrt/qF+3CnNJRdgEOyLXKGXEs8Ogm1qlcslspxc3RV5F1xdEuPF8te4IBrXXMfqnGimiRREv5sA9LWmHI2rqbASZweiBcgG7Fe1rmIMcRBYm10uCqPDUvgZQP4HnQKBgQCX5PHcNZ40TCqnS8hEWFWgTjihjKH62c4lIKOuFDthFXZ5sZg8ROqJ4j+KBH2WtZmr3ar12bf6+L5+L2GgXxnVpEceLxdERVx2742Yg7U0TaFVmbnfHJklT0qVfpsNw7PqfWZrRNNLXRTh2wVIPUd0Tc52uxolejspm9bK7YgJ2QKBgQDY18hFLh+0wVV98y38qthJub9qOFlRg39fSVwawVfXClfjQ3Hbnfqjj9GJ3PIvjhCzyDsc6w/Km8xGsYoCVWUy/bPj/qzland+FsnREixZcnLRAnLefKSwawicI/u/2/MdPjMXnjDI/k4KwgC6eS3L56kCW2i8bU9CL/EJ3l5P2QKBgQCCIi/kjjtgavEQK6kEhPm3Qcza+3W7SOeIf5I8DjFhV9YNJM5ctF6Miz4MrRQ8DpH94QgbYdByQLJTwZxOnauwkfIsNgQlBZfWfh490HlquifDAtVwBTxf7rLjqXiGLmjyOUWKGYk9RB4j8lVwMrAJQK/moR4hhbsyRsWbTi4C4QKBgQCcU2kJlQU5yYefVzxN1wLQhx8p7n8O4bZkjuBvMg2NU5ajXiOePro/dIZ3EettcxlZYFg7wTo42LqUSV93GucEnafZywNygcc/z0Bw5Ceh6aGBYRX4I0EkQjKhV42fs7GMmpP3LI5mPiSoKSr01lTiWTaW4gm6Eb/1+f5iFqpCMA==";
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
