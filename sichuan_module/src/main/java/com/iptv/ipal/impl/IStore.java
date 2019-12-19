package com.iptv.ipal.impl;

public interface IStore {
    public boolean store(final String loginAccount, final String userToken,int payIndex,String packageName);
}
