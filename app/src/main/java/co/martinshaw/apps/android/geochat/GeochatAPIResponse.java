/*
 * GeochatAPIResponse
 * C:/Users/martin/Android_Projects/GeoChat/app/src/main/java/co/martinshaw/apps/android/geochat/GeochatAPIResponse.java
 *
 * Project: GeoChat
 * Module: app
 * Last Modified: 21/05/18 10:51 <martin>
 * Last Compilation: 21/05/18 10:51
 *
 * Copyright (c) 2018. Martin David Shaw. All rights reserved.
 */

package co.martinshaw.apps.android.geochat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeochatAPIResponse<T> {
    public boolean isError = true;

    @SerializedName("status")
    @Expose
    public Integer status;

    @SerializedName("error_msg")
    @Expose
    public String error_msg;

    @SerializedName("data")
    @Expose
    T data;

    public GeochatAPIResponse (Integer status, String error_msg, T data) {
        if(status == 200) {this.isError = false;}

        this.status = status;
        this.error_msg = error_msg;
        this.data = data;
    }

    public Integer getStatus() {return this.status;}
    public String getErrorMsg() {return this.error_msg;}
    public T getData() {return this.data;}
}
