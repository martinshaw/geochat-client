package co.martinshaw.apps.android.geochat;

/**
 * Created by martin on 13/05/2018.
 */

public class GeochatAPIResponse {

    Integer status;
    String error_msg;
    Object data;

    public GeochatAPIResponse () {

    }

    public Integer getStatus () {return this.status;}
    public String getErrorMsg () {return this.error_msg;}
    public Object getData () {return this.data;}
}
