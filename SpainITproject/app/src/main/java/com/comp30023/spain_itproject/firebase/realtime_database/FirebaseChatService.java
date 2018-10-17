package com.comp30023.spain_itproject.firebase.realtime_database;

import android.annotation.SuppressLint;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;

import com.comp30023.spain_itproject.ChatService;
import com.comp30023.spain_itproject.ServiceFactory;
import com.comp30023.spain_itproject.domain.ChatMessage;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.network.BadRequestException;
import com.comp30023.spain_itproject.network.NoConnectionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A chat service utilising Firebase's RealTimeDatabase
 */
public class FirebaseChatService extends ChatService {

    private static final FirebaseDatabase database = MyFirebaseDatabase.getDatabase();

    //Reference to where all chat instances are stored
    private static final DatabaseReference baseReference = database.getReference().child("chats");

    //Database reference to the chat between the currentUser and chatPartner
    private DatabaseReference chatReference;

    //LiveData representing the most recently received chat message
    private LiveData<ChatMessage> latestMessage;

    //private LiveData<List<ChatMessage>> pastMessages;


    /**
     * Creates the references and listeners to a chat room
     * @param currentUserId The currently logged in user
     * @param chatPartnerId The user they are messaging
     */
    public FirebaseChatService(String currentUserId, String chatPartnerId) {
        super(currentUserId, chatPartnerId);

        String referenceName = getChatReferenceName(currentUserId, chatPartnerId);

        chatReference = baseReference.child(referenceName);

        //Set the listener for incoming children (messages) of the chatReference
        LiveData<DataSnapshot> latestMessageSnapshot = new FirebaseChildListenerLiveData(chatReference);

        //Map the incoming message to the latestMessage
        latestMessage = Transformations.map(latestMessageSnapshot, new Function<DataSnapshot, ChatMessage>() {
            @Override
            public ChatMessage apply(DataSnapshot snapshot) {
                return snapshot.getValue(ChatMessage.class);
            }
        });

        /*pastMessages = Transformations.map(new FirebaseSingleValueListenerLiveData(chatReference), new Function<DataSnapshot, List<ChatMessage>>() {
            @Override
            public List<ChatMessage> apply(DataSnapshot snapshot) {

                List<ChatMessage> messages = new ArrayList<ChatMessage>();

                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        messages.add(child.getValue(ChatMessage.class));
                    }
                }

                return messages;
            }
        });*/
    }

    /*public LiveData<List<ChatMessage>> getMessageHistory() {

        return pastMessages;
    }*/

    /**
     * Sends the chat message
     * Makes network call to send notification, cannot be executed on UI thread
     * @param message The message to be sent
     */
    public void sendMessage(final ChatMessage message) throws Exception {

        //Adds the message as a child to the chat instance
        chatReference.push().setValue(message);

        //Send the notification
        ServiceFactory.getInstance().notificationSendingService().sendChat(message);
    }

    /**
     * Returns the live data that represents the listener for most recent chat message
     * @return
     */
    public LiveData<ChatMessage> getLatestMessageLiveData() {
        return latestMessage;
    }

    /**
     * Determines the name for the chat instance between the two users
     * @param currentUserId The logged in user
     * @param partnerId The user they are chatting to
     * @return The name of the chat instance to listen to
     */
    private static String getChatReferenceName(String currentUserId, String partnerId) {

        //Determine the reference from the agreed naming convention (DependentUser.id-CarerUser.id)
        String reference;

        if (currentUserId.compareTo(partnerId) < 0) {
            reference = currentUserId + "-" + partnerId;
        } else {
            reference = partnerId + "-" + currentUserId;
        }

        return reference;
    }

}
