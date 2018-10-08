package com.managers;

import com.utilities.constants.AppConstant;
import com.utilities.constants.SharesPrefConstants;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

import static com.utilities.constants.ApiConstants.ROOT_API;

public class ConnectionManagers {

    public static OkHttpClient getHeader(final String authorizationValue) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(
                        new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                Request request = null;
                                if (authorizationValue != null) {
                                    Request original = chain.request();
                                    Request.Builder requestBuilder = original.newBuilder()
                                            .addHeader("Authorization", "bearer " + authorizationValue);
                                    request = requestBuilder.build();
                                }
                                return chain.proceed(request);
                            }
                        })
                .build();
        return okClient;

    }

    public static void doRequest(final String type, String apiRoute, Map<String, String> Params, ApiCallResponseRetrofit apiCallResponse) {
        switch (type) {
            case AppConstant.GET:
                GET(apiRoute, Params, apiCallResponse);
                break;
            case AppConstant.POST:
                POST(apiRoute, Params, apiCallResponse);
                break;
        }
    }

    public static void doRequestText(final String type, String apiRoute, Map<String, String> Params, ApiCallResponseString apiCallResponse) {
        switch (type) {
            case AppConstant.GET:
            case AppConstant.GET_TEXT:
                getText(apiRoute, apiCallResponse);
                break;
            case AppConstant.POST:
            case AppConstant.POST_TEXT:
                postText(apiRoute, Params, apiCallResponse);
                break;
        }
    }

    public static void getText(String url, final ApiCallResponseString callResponse) {
        String Token = PreferencesManager.getInstance().getStringValue(SharesPrefConstants.ACCESS_TOKEN);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROOT_API)
                .client(getHeader(Token))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService service = retrofit.create(APIService.class);
        Call<ResponseBody> call = service.getText(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    callResponse.onSuccess(response.code(), response.body().string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callResponse.onFailure(call.request().body().hashCode(), t.toString());
            }
        });

    }

    public static void GET(String URl, Map<String, String> Params, final ApiCallResponseRetrofit callResponse) {
        String Token = PreferencesManager.getInstance().getStringValue(SharesPrefConstants.ACCESS_TOKEN);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROOT_API)
                .client(getHeader(Token))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService service = retrofit.create(APIService.class);
        Call<ResponseBody> call = service.GET(URl, Params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.code() == 200)
                        callResponse.onSuccess(response.body().string(), response.message());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callResponse.onFailure(t.toString());
            }
        });

    }

    public static void postText(String URl, Map<String, String> Params, final ApiCallResponseString callResponse) {
        String Token = PreferencesManager.getInstance().getStringValue(SharesPrefConstants.ACCESS_TOKEN);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROOT_API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getHeader(Token))
                .build();
        APIService service = retrofit.create(APIService.class);
        Call<ResponseBody> call = service.POST_FormUrlEncoded(URl, Params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    callResponse.onSuccess(response.code(), response.body().string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    callResponse.onFailure(-1992, t.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void POST(String URl, Map<String, String> Params, final ApiCallResponseRetrofit callResponse) {
        String Token = PreferencesManager.getInstance().getStringValue(SharesPrefConstants.ACCESS_TOKEN);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROOT_API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getHeader(Token))
                .build();
        APIService service = retrofit.create(APIService.class);
        Call<ResponseBody> call = service.POST_FormUrlEncoded(URl, Params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
//                    if (response.code() == 200)
                    callResponse.onSuccess(response.body().string(), response.message());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callResponse.onFailure(t.toString());
            }
        });
    }


    public interface APIService {
        public static String BASE_URL = ROOT_API;
//                "X-Header-ENC:2b5f32cfd8ba73e32073eda28e44cb50",

        @Headers({"X-Header-AGENT:android"})
        @GET
        public Call<ResponseBody> GET(@Url String url, @QueryMap Map<String, String> params);

        @Headers({"X-Header-AGENT:android"})
        @GET
        public Call<ResponseBody> getText(@Url String url);


        @Headers({"Content-Type:application/x-www-form-urlencoded", "X-Header-AGENT:android"})
        @POST
        @FormUrlEncoded
        public Call<ResponseBody> POST_FormUrlEncoded(@Url String url, @FieldMap Map<String, String> params);
    }

}
