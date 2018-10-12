package com.comp30023.spain_itproject;

import com.comp30023.spain_itproject.ChatService;
import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.firebase.realtime_database.FirebaseChatService;

public class ServiceFactory {

    private static ServiceFactory instance;
    public static ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }


    public ChatService createChatService(User currentUser, User chatPartner) {
        return new FirebaseChatService(currentUser, chatPartner);
    }

    public NotificationSendingService notificationSendingService() {
        return MyNotificationSendingService.getInstance();
    }

}
