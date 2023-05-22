package com.jhkim.studywebbbs.entities;

import java.util.Date;
import java.util.Objects;

public class ArticlesEntity {

    private int index;
    private String title;
    private String content;
    private int view;
    private Date createdAt;
    private boolean isDeleted;
    private String clientIp;
    private String clientUa;

    public int getIndex() {
        return index;
    }

    public ArticlesEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ArticlesEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ArticlesEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public int getView() {
        return view;
    }

    public ArticlesEntity setView(int view) {
        this.view = view;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public ArticlesEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public ArticlesEntity setDeleted(boolean deleted) {
        isDeleted = deleted;
        return this;
    }

    public String getClientIp() {
        return clientIp;
    }

    public ArticlesEntity setClientIp(String clientIp) {
        this.clientIp = clientIp;
        return this;
    }

    public String getClientUa() {
        return clientUa;
    }

    public ArticlesEntity setClientUa(String clientUa) {
        this.clientUa = clientUa;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticlesEntity that = (ArticlesEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
