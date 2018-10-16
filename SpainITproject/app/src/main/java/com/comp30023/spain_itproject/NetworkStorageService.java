package com.comp30023.spain_itproject;

import com.google.firebase.storage.UploadTask;

import java.io.File;

public abstract class NetworkStorageService {

    public abstract void getFile();

    public abstract UploadTask putFile(String currentUserId, File file);

}
