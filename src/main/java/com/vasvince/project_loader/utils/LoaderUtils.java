package com.vasvince.project_loader.utils;

import com.vasvince.project_loader.Folder;

import java.io.File;
import java.util.Arrays;

public final class LoaderUtils {

    private LoaderUtils() {}

    public static boolean isProjectFolder(Folder folder) {
        File dir = new File(folder.getPath().toString());

        File[] files = dir.listFiles();
        if (files == null) {
            return false;
        }

        return Arrays.stream(files)
                .anyMatch(file -> file.getName().toLowerCase().endsWith(".logicx"));
    }
}
