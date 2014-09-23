package com.skysea.group.packet.notify;

/**
 * Created by apple on 14-9-23.
 */
public class ApplyResultNotify extends HasOperatorNotify {
    private boolean result;
    ApplyResultNotify() {
        super(Type.MEMBER_APPLY_TO_JOIN_RESULT);
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
