package com.comp30023.spain_itproject;

import com.comp30023.spain_itproject.domain.ChatMessage;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.network.BadRequestException;
import com.comp30023.spain_itproject.network.ErrorResponse;
import com.comp30023.spain_itproject.network.ErrorUtils;
import com.comp30023.spain_itproject.network.NoConnectionException;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public abstract class NotificationSendingService {

    public abstract void sendHelp(DependentUser helpRequester, String message) throws BadRequestException, NoConnectionException;

    public abstract void sendChat(ChatMessage message) throws BadRequestException, NoConnectionException;

    /**
     * Executes the call and returns the response or throws an error if one occurred during execution
     * @param call The call to be executed
     * @param <T> The type of call that is being made
     * @return The response from the call if successful
     * @throws BadRequestException If the request could not be completed because of invalid input
     * @throws NoConnectionException If the request could not be completed due to a connection issue
     */
    public <T> T executeCallReturnResponse(Call<T> call)
            throws BadRequestException, NoConnectionException {

        try {
            Response<T> response = call.execute();

            if (response.isSuccessful()) {
                return response.body();
            } else {

                ErrorResponse error = ErrorUtils.parseError(response);
                throw new BadRequestException(error);
            }

        } catch (IOException e) {
            throw new NoConnectionException();
        }
    }

    /**
     * Executes a call that does not expect a response or throws an error if one occurred during execution
     * @param call The call to be executed
     * @throws BadRequestException If the request could not be completed because of invalid input
     * @throws NoConnectionException If the request could not be completed due to a connection issue
     */
    public void executeCallNoResponse(Call<ResponseBody> call)
            throws BadRequestException, NoConnectionException {
        try {

            Response<ResponseBody> response = call.execute();

            if (!response.isSuccessful()) {
                ErrorResponse error = ErrorUtils.parseError(response);
                throw new BadRequestException(error);
            }

        } catch (IOException e) {
            throw new NoConnectionException();
        }
    }


}
