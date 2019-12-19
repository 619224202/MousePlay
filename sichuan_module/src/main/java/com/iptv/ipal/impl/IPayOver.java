package com.iptv.ipal.impl;

public interface IPayOver {
    //订购成功
    public void paySuccess(int propId);
    //订购失败
    public void payFailed(int propId);
}
