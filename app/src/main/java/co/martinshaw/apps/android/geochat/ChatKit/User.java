/*
 * User
 * C:/Users/martin/Android_Projects/GeoChat/app/src/main/java/co/martinshaw/apps/android/geochat/ChatKit/User.java
 *
 * Project: GeoChat
 * Module: app
 * Last Modified: 23/05/18 09:39 <martin>
 * Last Compilation: 23/05/18 09:39
 *
 * Copyright (c) 2018. Martin David Shaw. All rights reserved.
 */

package co.martinshaw.apps.android.geochat.ChatKit;


import com.stfalcon.chatkit.commons.models.IUser;

public class User implements IUser {

    private String id;
    private String name;
    private String avatar;
    private boolean online;

    public User(String id, String name, String avatar, boolean online) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.online = online;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    public boolean isOnline() {
        return online;
    }

}
