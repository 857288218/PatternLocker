package com.github.ihsg.demo.util;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by hsg on 14/10/2017.
 */

public class PatternHelper {
    public static final int MAX_SIZE = 4;
    public static final int MAX_TIMES = 5;
    private static final String GESTURE_PWD_KEY = "gesture_pwd_key";

    private String message;
    private String storagePwd;
    private String tmpPwd;
    private int times;
    private boolean isFinish;
    private boolean isOk;

    public void validateForSetting(List<Integer> hitIndexList) {
        this.isFinish = false;
        this.isOk = false;

        if ((hitIndexList == null) || (hitIndexList.size() < MAX_SIZE)) {
//            this.tmpPwd = null;
            this.message = getSizeErrorMsg();
            return;
        }

        //1. draw first time
        if (TextUtils.isEmpty(this.tmpPwd)) {
            this.tmpPwd = convert2String(hitIndexList);
            this.message = getReDrawMsg();
            this.isOk = true;
            return;
        }

        //2. draw second times
        if (this.tmpPwd.equals(convert2String(hitIndexList))) {
            this.message = getSettingSuccessMsg();
            saveToStorage(this.tmpPwd);
            this.isOk = true;
            this.isFinish = true;
        } else {
//            this.tmpPwd = null;
            this.message = getDiffPreErrorMsg();
        }
    }

    public void validateForChecking(List<Integer> hitIndexList) {
        this.isOk = false;

        if ((hitIndexList == null) || (hitIndexList.size() < MAX_SIZE)) {
            this.times++;
            this.isFinish = this.times > MAX_SIZE;
            this.message = getPwdErrorMsg();
            return;
        }

        this.storagePwd = getFromStorage();
        if (!TextUtils.isEmpty(this.storagePwd) && this.storagePwd.equals(convert2String(hitIndexList))) {
            this.message = getCheckingSuccessMsg();
            this.isOk = true;
        } else {
            this.times++;
            this.isFinish = this.times > MAX_SIZE;
            this.message = getPwdErrorMsg();
        }
    }

    public String getMessage() {
        return this.message;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public boolean isOk() {
        return isOk;
    }

    //设置手势密码时是否处于二次确认密码状态
    public boolean isTwiceConfirmStatus() {
        if (isOk && message.equals(getReDrawMsg()) && tmpPwd != null) {
            return true;
        }
        return false;
    }

    //设置手势密码时重设手势密码
    public void resetPwd() {
        tmpPwd = null;
        isOk = true;
        message = "绘制解锁图案";
    }

    private String getReDrawMsg() {
        return "再次绘制解锁图案";
    }

    private String getSettingSuccessMsg() {
        return "设置成功";
    }

    private String getCheckingSuccessMsg() {
        return "解锁成功！";
    }

    private String getSizeErrorMsg() {
        return String.format("最少需要连接%d个点", MAX_SIZE);
    }

    private String getDiffPreErrorMsg() {
        return "与上次绘制不一致，请重新绘制";
    }

    private String getPwdErrorMsg() {
        return String.format("密码错误，还剩%d次机会", getRemainTimes());
    }

    private String convert2String(List<Integer> hitIndexList) {
        return hitIndexList.toString();
    }

    private void saveToStorage(String gesturePwd) {
        final String encryptPwd = SecurityUtil.encrypt(gesturePwd);
        SharedPreferencesUtil.getInstance().saveString(GESTURE_PWD_KEY, encryptPwd);
    }

    private String getFromStorage() {
        final String result = SharedPreferencesUtil.getInstance().getString(GESTURE_PWD_KEY);
        return SecurityUtil.decrypt(result);
    }

    private int getRemainTimes() {
        return (times < 5) ? (MAX_TIMES - times) : 0;
    }
}
