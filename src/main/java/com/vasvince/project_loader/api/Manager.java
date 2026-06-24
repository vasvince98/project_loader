package com.vasvince.project_loader.api;

import com.vasvince.project_loader.Folder;

public interface Manager {
    void archive(Folder folder);
    void download(Folder folder);
}
