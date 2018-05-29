package com.stb.api.listeners;

/**
 * Created by DUY on 7/26/2017.
 */

public interface ApiResponse<T> {
    void onSuccess(T result);
    void onError(T error);


}
