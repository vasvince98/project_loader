package com.vasvince.project_loader.enums;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;

import java.util.Collections;
import java.util.List;

public final class DriveEnums {

    private DriveEnums() {}

    public static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    public static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    public static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    public static final List<String> SCOPES =
            Collections.singletonList(DriveScopes.DRIVE);
    public static final String CREDENTIALS_FILE_PATH = "/credentials.json";
}
