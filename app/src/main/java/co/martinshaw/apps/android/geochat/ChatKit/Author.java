/*
 * Author
 * C:/Users/martin/Android_Projects/GeoChat/app/src/main/java/co/martinshaw/apps/android/geochat/ChatKit/Author.java
 *
 * Project: GeoChat
 * Module: app
 * Last Modified: 23/05/18 09:12 <martin>
 * Last Compilation: 23/05/18 09:23
 *
 * Copyright (c) 2018. Martin David Shaw. All rights reserved.
 */

package co.martinshaw.apps.android.geochat.ChatKit;


import com.stfalcon.chatkit.commons.models.IUser;

public class Author implements IUser {

    String id;
    String name;
    String avatar = null;

    Author () {

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
}