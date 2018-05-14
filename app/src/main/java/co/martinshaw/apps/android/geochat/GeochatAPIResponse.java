package co.martinshaw.apps.android.geochat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by martin on 13/05/2018.
 */

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
    List<T> data;

    public GeochatAPIResponse (Integer status, String error_msg, List<T> data) {
        if(status == 200) {this.isError = false;}

        this.status = status;
        this.error_msg = error_msg;
        this.data = data;
    }

    public Integer getStatus() {return this.status;}
    public String getErrorMsg() {return this.error_msg;}
    public List<T> getData() {return this.data;}
}
