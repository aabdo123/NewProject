package com.managers;

/**
 * Created by malikabuqaoud on 1/5/17.
 */

public interface ApiCallResponseString {

    void onSuccess(int statusCode, String responseObject);

    void onFailure(int statusCode, String errorResponse);
}
