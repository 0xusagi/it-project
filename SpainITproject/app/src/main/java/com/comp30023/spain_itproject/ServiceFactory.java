package com.comp30023.spain_itproject;

import com.comp30023.spain_itproject.ChatService;
import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.firebase.realtime_database.FirebaseChatService;
import com.comp30023.spain_itproject.firebase.realtime_database.FirebaseLocationSharingService;
import com.comp30023.spain_itproject.firebase.realtime_database.FirebaseValueListenerLiveData;

public class ServiceFactory {

    private static ServiceFactory instance;
    public static ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }


    public ChatService chatService(String currentUserId, boolean isDependent, String chatPartnerId) {
        return new FirebaseChatService(currentUserId, isDependent, chatPartnerId);
    }

    public NotificationSendingService notificationSendingService() {
        return MyNotificationSendingService.getInstance();
    }

    public RealTimeSharingLocationService realTimeSharingLocationService() {
        return FirebaseLocationSharingService.getInstance();
    }

}
