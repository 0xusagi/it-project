package com.comp30023.spain_itproject.domain;

import java.io.Serializable;

/**
 * Requires an implementing class to have a name String that can be retrieved, and is Serializable
 */
public interface DisplayName extends Serializable {

    public String getDisplayName();

}
