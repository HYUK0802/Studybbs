package com.jhkim.studywebbbs.entities;

import java.util.Objects;

public class AttachmentsEntity {
    private int index;
    private int articleIndex;
    private String fileName;
    private long fileSize;
    private String fileContentType;
    private byte[]  fileData;

    public int getIndex() {
        return index;
    }

    public AttachmentsEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public int getArticleIndex() {
        return articleIndex;
    }

    public AttachmentsEntity setArticleIndex(int articleIndex) {
        this.articleIndex = articleIndex;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public AttachmentsEntity setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public long getFileSize() {
        return fileSize;
    }

    public AttachmentsEntity setFileSize(long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public AttachmentsEntity setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
        return this;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public AttachmentsEntity setFileData(byte[] fileData) {
        this.fileData = fileData;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttachmentsEntity that = (AttachmentsEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
