package com.vasvince.project_loader.api;

import com.vasvince.project_loader.Project;

public interface Manager {
    void archive(Project project);
    void download(Project project);
}
