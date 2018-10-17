package com.comp30023.spain_itproject.firebase.realtime_database;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.media.MediaPlayer;
import android.net.Uri;

import com.comp30023.spain_itproject.ChatService;
import com.comp30023.spain_itproject.ServiceFactory;
import com.comp30023.spain_itproject.Clock;
import com.comp30023.spain_itproject.domain.ChatMessage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

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

    private String chatReferenceName;

    /**
     * Creates the references and listeners to a chat room
     * @param currentUserId The currently logged in user
     * @param currentUserName The name of the currently logged in user
     * @param chatPartnerId The user they are messaging
     */
    public FirebaseChatService(String currentUserId, String currentUserName, String chatPartnerId) {
        super(currentUserId, currentUserName, chatPartnerId);

        chatReferenceName = getChatReferenceName(currentUserId, chatPartnerId);

        chatReference = baseReference.child(chatReferenceName);

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
    public void sendMessage(final String message) throws Exception {

        ChatMessage chatMessage = new ChatMessage(getCurrentUserId(), getCurrentUserName(), getChatPartnerId(), message, null);

        //Adds the message as a child to the chat instance
        chatReference.push().setValue(chatMessage);

        //Send the notification
        ServiceFactory.getInstance().notificationSendingService().sendChat(chatMessage);
    }

    /**
     * Send a message that contains a link to an audio file
     * @param message The text body of the message
     * @param audioResourceLink The path to the audio file
     * @throws Exception Thrown when there is a connection issue
     */
    private void sendMessage(String message, String audioResourceLink) throws Exception {

        ChatMessage chatMessage = new ChatMessage(getCurrentUserId(), getCurrentUserName(), getChatPartnerId(), message, audioResourceLink);

        //Adds the message as a child to the chat instance
        chatReference.push().setValue(chatMessage);

        //Send the notification
        ServiceFactory.getInstance().notificationSendingService().sendChat(chatMessage);
    }

    /**
     * Stores the file in FirebaseStorage and then sends the chat partner a message about it
     * @param message The text body of the message the message to be sent
     * @param file The audio file to share
     */
    @Override
    public void sendAudioMessage(final String message, File file) {

        String timeStamp = Clock.getCurrentLocalTimeStamp();

        //Get the reference to where the file will be stored
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("audio_messages").child(chatReferenceName).child(timeStamp);

        Uri uri = Uri.fromFile(file);

        //Upload the file
        UploadTask task = storageReference.putFile(uri);

        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //Send message to the chat partner so that they can access the file
                try {
                    sendMessage(message, storageReference.getPath());

                } catch (Exception e) {

                    //TODO
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Returns the live data that represents the listener for most recent chat message
     * @return
     */
    public LiveData<ChatMessage> getLatestMessageLiveData() {
        return latestMessage;
    }

    /**
     * Play the audio message that is found at the resource link
     * @param resourceLink The path to the audio file in storage
     */
    @Override
    public void playAudioMessage(String resourceLink) {

        //Get the reference to the file
        final StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(resourceLink);

        try {

            //Create a temporary file to store the download to
            final File localFile = File.createTempFile("recording", "3gp");

            //Download the file and play
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    playAudio(localFile.getPath());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Play the audio file over the default speaker
     * @param path The path to the file
     */
    private void playAudio(String path) {

        final MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(path);
            player.prepare();
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    //When finished playing, release the asset
                    player.reset();
                    player.release();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Determines the name for the chat instance between the two users
     * @param currentUserId The logged in user
     * @param partnerId The user they are chatting to
     * @return The name of the chat instance to listen to
     */
    private static String getChatReferenceName(String currentUserId, String partnerId) {

        //Determine the reference of the chat instance
        String reference;

        if (currentUserId.compareTo(partnerId) < 0) {
            reference = currentUserId + "-" + partnerId;
        } else {
            reference = partnerId + "-" + currentUserId;
        }

        return reference;
    }
}
