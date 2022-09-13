package com.chavaillaz.browser;

import lombok.Data;

@Data
public class MavenCentralData {

    private String artifact;
    private String lastVersion;

    public MavenCentralData(String artifact) {
        this.artifact = artifact;
    }

}
