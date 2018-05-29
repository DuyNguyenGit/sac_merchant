package com.sacombank.merchants.view.impl;

/**
 * Created by DUY on 8/13/2017.
 */

interface FragmentListener {
    <T> void goHomePageWithData(T data);

    void gotoChangePassword(String s);
}
