/*
 * DefaultDialog
 * C:/Users/martin/Android_Projects/GeoChat/app/src/main/java/co/martinshaw/apps/android/geochat/ChatKit/DefaultDialog.java
 *
 * Project: GeoChat
 * Module: app
 * Last Modified: 23/05/18 09:23 <martin>
 * Last Compilation: 23/05/18 09:23
 *
 * Copyright (c) 2018. Martin David Shaw. All rights reserved.
 */

package co.martinshaw.apps.android.geochat.ChatKit;


import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DefaultDialog implements IDialog<Message> {

    String id;
    String dialogPhoto = "http://www.martinshaw.co/me.jpg";
    String dialogName;
    private ArrayList<User> users;
    IMessage lastMessage;
    int unreadCount;

    DefaultDialog() {
    }

    public DefaultDialog(String id, String name, String photo, ArrayList<User> users, Message lastMessage, int unreadCount) {

        this.id = id;
        this.dialogName = name;
        this.dialogPhoto = photo;
        this.users = users;
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDialogPhoto() {
        return dialogPhoto;
    }

    @Override
    public String getDialogName() {
        return dialogName;
    }

    @Override
    public ArrayList<User> getUsers() {
        return users;
    }

    @Override
    public Message getLastMessage() {
        return (Message) lastMessage;
    }

    @Override
    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public int getUnreadCount() {
        return unreadCount;
    }

}
