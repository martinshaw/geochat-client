/*
 * Message
 * C:/Users/martin/Android_Projects/GeoChat/app/src/main/java/co/martinshaw/apps/android/geochat/Message.java
 *
 * Project: GeoChat
 * Module: app
 * Last Modified: 23/05/18 09:58 <martin>
 * Last Compilation: 23/05/18 09:58
 *
 * Copyright (c) 2018. Martin David Shaw. All rights reserved.
 */

package co.martinshaw.apps.android.geochat;


class Message {

    int id;

    double origin_lat;
    double origin_long;
    double recipient_lat;
    double recipient_long;
    int user_id;
    int session_id;
    int is_anonymaus;
    String message_type;
    String contents;
    String contents_extra;
    String created_at;
    String updated_at;
    int active;
    String first_name;
    String last_name;


    Message (String _contents){
        contents = _contents;
    }


    public boolean isAnonymaus() {
        return is_anonymaus == 1;
    }

    public boolean isActive(){
        return active == 1;
    }

}
