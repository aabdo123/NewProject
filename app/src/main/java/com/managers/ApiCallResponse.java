package com.managers;

/**
 * Created by malikabuqaoud on 1/5/17.
 */

public interface ApiCallResponse {

    void onSuccess(int statusCode, Object responseObject);

    void onFailure(int statusCode, String errorResponse);
}
