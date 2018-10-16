package com.comp30023.spain_itproject.firebase.storage;

import com.google.firebase.storage.FirebaseStorage;

public class FirebaseStorageService {

    private static FirebaseStorage storage;

    public static FirebaseStorage getStorage() {
        if (storage == null) {
            storage = FirebaseStorage.getInstance();
        }
        return storage;
    }

}
