package com.jhkim.studywebbbs.entities;

import java.util.Date;
import java.util.Objects;

public class ImageEntity {

    private int index;
    private String name;
    private long size;
    private String contentType;
    private byte[] data;
    private Date createdAt;
    private String clientIp;
    private String clientUa;

    public int getIndex() {
        return index;
    }

    public ImageEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getName() {
        return name;
    }

    public ImageEntity setName(String name) {
        this.name = name;
        return this;
    }

    public long getSize() {
        return size;
    }

    public ImageEntity setSize(long size) {
        this.size = size;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public ImageEntity setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public byte[] getData() {
        return data;
    }

    public ImageEntity setData(byte[] data) {
        this.data = data;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public ImageEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getClientIp() {
        return clientIp;
    }

    public ImageEntity setClientIp(String clientIp) {
        this.clientIp = clientIp;
        return this;
    }

    public String getClientUa() {
        return clientUa;
    }

    public ImageEntity setClientUa(String clientUa) {
        this.clientUa = clientUa;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageEntity that = (ImageEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}

