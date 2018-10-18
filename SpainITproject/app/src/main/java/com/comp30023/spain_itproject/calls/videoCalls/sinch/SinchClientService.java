package com.comp30023.spain_itproject.calls.videoCalls.sinch;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.comp30023.spain_itproject.ui.LoginSharedPreference;
import com.comp30023.spain_itproject.ui.calls.IncomingVideoCallActivity;
import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.video.VideoController;

public class SinchClientService extends Service {
    public static final String CALL_ID = "Call_ID";
    public static final String CALLER_USER_ID="CallerUserId";

    private final String APP_KEY = "29522bfe-2ee2-44d4-bff0-4af14c6b6801";
    private final String APP_SECRET = "3ev38vtThEekNXamuYZq+g==";
    private final String HOSTNAME = "clientapi.sinch.com";


    private String userId;

    private SinchClient client;
    private SinchServiceInterface serviceInterface = new SinchServiceInterface();

    @Override
    public void onCreate() {
        super.onCreate();
        String userId = LoginSharedPreference.getId(getApplicationContext());

        if (userId != null) {
            start(userId);
        }
    }

    @Override
    public void onDestroy() {
        if (client != null && client.isStarted()) {
            client.stopListeningOnActiveConnection();
            client.terminate();
        }

        super.onDestroy();
    }

    /**
     * Start the client service to be able to make calls and listen to incoming calls
     * @param userId mongodb id of the user
     */
    private void start(String userId) {
        if (client == null) {
            this.userId = userId;

            // Create the client
            client = Sinch.getSinchClientBuilder()
                    .context(getApplicationContext())
                    .userId(userId)
                    .applicationKey(APP_KEY)
                    .applicationSecret(APP_SECRET)
                    .environmentHost(HOSTNAME)
                    .build();

            // Set the capabilites
            client.setSupportCalling(true);
            client.startListeningOnActiveConnection();

            // Add listeners
            addSinchClientListener();
            addCallClientListener();

            // Start the client
            client.start();
        }
    }

    /**
     * Stop the Sinch client and stop listening to calls
     */
    private void stop() {
        if (client != null) {
            client.stopListeningOnActiveConnection();
            client.terminate();
            client = null;
        }
    }

    /**
     * Add a client listener to the client
     */
    private void addSinchClientListener() {
        client.addSinchClientListener(new SinchClientListener() {
            @Override
            public void onClientStarted(SinchClient sinchClient) {
                Log.d(SinchClientService.class.getSimpleName(), "Sinch Client Started");
            }

            @Override
            public void onClientStopped(SinchClient sinchClient) {
                Log.d(SinchClientService.class.getSimpleName(), "Sinch Client Stopped");
            }

            @Override
            public void onClientFailed(SinchClient sinchClient, SinchError sinchError) {
                client.terminate();
                client = null;
            }

            @Override
            public void onRegistrationCredentialsRequired(SinchClient sinchClient, ClientRegistration clientRegistration) {

            }

            @Override
            public void onLogMessage(int i, String s, String s1) {
                switch (i) {
                    case Log.DEBUG:
                        Log.d(s, s1);
                        break;
                    case Log.ERROR:
                        Log.e(s, s1);
                        break;
                    case Log.INFO:
                        Log.i(s, s1);
                        break;
                    case Log.VERBOSE:
                        Log.v(s, s1);
                        break;
                    case Log.WARN:
                        Log.w(s, s1);
                        break;
                }
            }
        });
    }

    /**
     * Add a call listener to the client
     */
    private void addCallClientListener() {
        client.getCallClient().addCallClientListener(new CallClientListener() {
            @Override
            public void onIncomingCall(CallClient callClient, Call call) {
                Intent intent = new Intent(SinchClientService.this, IncomingVideoCallActivity.class);
                intent.putExtra(CALL_ID, call.getCallId());
                intent.putExtra(CALLER_USER_ID, call.getRemoteUserId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                SinchClientService.this.startActivity(intent);
            }
        });
    }

    /**
     * Check whether the client is started
     * @return
     */
    public boolean isStarted() {
        return (client != null && client.isStarted());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceInterface;
    }

    public class SinchServiceInterface extends Binder {

        /**
         * Make a video call to the intended user
         * @param userId
         * @return
         */
        public Call callUserVideo(String userId) {
            return client.getCallClient().callUserVideo(userId);
        }

        /**
         * Make a internet voice call to the intended user
         * @param userId
         * @return
         */
        public Call callUserVoice(String userId) {
            return client.getCallClient().callUser(userId);
        }

        public String getUserName() {
            return userId;
        }

        public boolean isStarted() {
            return SinchClientService.this.isStarted();
        }

        public void startClient(String userName) {
            start(userName);
        }

        public void stopClient() {
            stop();
        }

        public Call getCall(String callId) {
            return client.getCallClient().getCall(callId);
        }

        public VideoController getVideoController() {
            if (!isStarted()) {
                return null;
            }
            return client.getVideoController();
        }

        public AudioController getAudioController() {
            if (!isStarted()) {
                return null;
            }
            return client.getAudioController();
        }
    }


}
