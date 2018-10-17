package com.comp30023.spain_itproject;

import com.comp30023.spain_itproject.domain.ChatMessage;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.network.AccountService;
import com.comp30023.spain_itproject.network.BadRequestException;
import com.comp30023.spain_itproject.network.NoConnectionException;
import com.comp30023.spain_itproject.network.RetrofitClientInstance;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class MyNotificationSendingService extends NotificationSendingService {

    private static MyNotificationSendingService instance;
    public static MyNotificationSendingService getInstance() {
        if (instance == null) {
            instance = new MyNotificationSendingService();
        }
        return instance;
    }

    private static AccountService service;
    private void checkService() {
        if (service == null) {
            service = RetrofitClientInstance.getRetrofitInstance().create(AccountService.class);
        }
    }

    /**
     * Send a help message to all associated carers
     * @param requester The dependent making the request
     * @param message The text body of the message
     * @throws BadRequestException Thrown if the dependent is not registered
     * @throws NoConnectionException Thrown if there is an issue while connecting to the server
     */
    @Override
    public void sendHelp(DependentUser requester, String message)
            throws BadRequestException, NoConnectionException {

        checkService();

        Call<ResponseBody> call = service.requestHelp(requester.getId(), message);

        executeCallNoResponse(call);
    }

    @Override
    public void sendChat(ChatMessage message) throws BadRequestException, NoConnectionException {
        checkService();

        Call<ResponseBody> call = service.sendChat(message.getSenderId(), message.getReceiverId(), message.getMessage());

        executeCallNoResponse(call);
    }
}
