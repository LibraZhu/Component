package com.libra.app;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**
 * Created by libra on 2017/12/12.
 */

interface ApiService {
    @GET("/LibraZhu/Library/blob/master/Switch")
    Flowable<ResponseBody> getReadme();
}
