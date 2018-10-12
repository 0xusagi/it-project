package com.comp30023.spain_itproject;

import com.comp30023.spain_itproject.ChatService;
import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.firebase.realtime_database.FirebaseChatService;

public class ServiceFactory {

    public static ChatService createChatService(User currentUser, User chatPartner) {
        return new FirebaseChatService(currentUser, chatPartner);
    }

}
