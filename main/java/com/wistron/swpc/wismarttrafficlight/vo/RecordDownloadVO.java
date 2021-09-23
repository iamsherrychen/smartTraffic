package com.wistron.swpc.wismarttrafficlight.vo;

import java.io.File;

public class RecordDownloadVO {

    private File zipFile;

    private Boolean hasArchive;

    public File getZipFile() {
        return zipFile;
    }

    public void setZipFile(File zipFile) {
        this.zipFile = zipFile;
    }

    public Boolean getHasArchive() {
        return hasArchive;
    }

    public void setHasArchive(Boolean hasArchive) {
        this.hasArchive = hasArchive;
    }
}
