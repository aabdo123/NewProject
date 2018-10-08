package com.managers;

/**
 * Created by malikabuqaoud on 1/5/17.
 */

public interface ApiCallResponseRetrofit {

    void onSuccess(Object responseObject,String responseMessage);

    void onFailure(String errorResponse);


}