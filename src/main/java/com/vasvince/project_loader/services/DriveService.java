package com.vasvince.project_loader.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;

import static com.vasvince.project_loader.enums.DriveEnums.*;

public class DriveService {

    private static final Logger logger = LoggerFactory.getLogger(DriveService.class);

    /**
     * Creates an authorized Credential object.
     *
     * @param httpTransport The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport httpTransport)
            throws IOException {
        // Load client secrets.
        InputStream in = DriveService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public List<File> getFilesFromDrive() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                .setApplicationName(APPLICATION_NAME)
                .build();

        FileList result = service.files().list()
                .setQ("'" + "1tSNGm-hpXY132p_WuSgXdcer8A3x3fU5" + "' in parents and trashed=false")
                .setFields("files(id, name, mimeType)")
                .execute();

        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            logger.warn("No projects found in provided drive path");
        } else {
            logger.info("Files:");
            for (File file : files) {
                logger.info("Name: {}, Id: {}", file.getName(), file.getId());
            }
        }
        return files;
    }

}
