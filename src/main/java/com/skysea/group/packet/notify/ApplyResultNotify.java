package com.skysea.group.packet.notify;

/**
 * 申请处理结果通知。
 * Created by apple on 14-9-23.
 */
public class ApplyResultNotify extends HasOperatorNotify {
    private boolean result;
    public ApplyResultNotify() {
        super(Type.MEMBER_APPLY_TO_JOIN_RESULT);
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
