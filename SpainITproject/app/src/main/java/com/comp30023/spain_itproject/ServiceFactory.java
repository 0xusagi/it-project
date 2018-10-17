package com.comp30023.spain_itproject;

import com.comp30023.spain_itproject.firebase.realtime_database.FirebaseChatService;
import com.comp30023.spain_itproject.firebase.realtime_database.FirebaseLocationSharingService;

/**
 * Allows access to the services used for the application
 */
public class ServiceFactory {

    private static ServiceFactory instance;
    public static ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }

    /**
     * @param currentUserId The ID of the logged in user
     * @param currentUserName The name of the logged in user
     * @param chatPartnerId The ID of the other user
     * @return The instance of the chat service between the 2 users
     */
    public ChatService chatService(String currentUserId, String currentUserName, String chatPartnerId) {
        return new FirebaseChatService(currentUserId, currentUserName, chatPartnerId);
    }

    /**
     * @return The selected notification sending service
     */
    public NotificationSendingService notificationSendingService() {
        return MyNotificationSendingService.getInstance();
    }

    /**
     * @return The selected real-time location sharing service
     */
    public RealTimeLocationSharingService realTimeLocationSharingService() {
        return FirebaseLocationSharingService.getInstance();
    }

}
