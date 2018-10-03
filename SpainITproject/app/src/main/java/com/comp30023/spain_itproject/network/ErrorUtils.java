package com.comp30023.spain_itproject.network;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Used to convert server response messages into ErrorResponse object
 * Adapted from: https://futurestud.io/tutorials/retrofit-2-simple-error-handling
 */
public class ErrorUtils {

    /**
     * Converts the unsuccessful response into an ErrorResponse object
     * @param response
     * @return
     */
    public static ErrorResponse parseError(Response<?> response) {
        Converter<ResponseBody, ErrorResponse> converter =
                RetrofitClientInstance.getRetrofitInstance()
                        .responseBodyConverter(ErrorResponse.class, new Annotation[0]);

        ErrorResponse error = null;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ErrorResponse();
        }

        return error;
    }
}
