package com.jhkim.studywebbbs.services;


import com.jhkim.studywebbbs.entities.ArticlesEntity;
import com.jhkim.studywebbbs.entities.AttachmentsEntity;
import com.jhkim.studywebbbs.mappers.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

@Service
public class ArticleService {
    private final ArticleMapper articleMapper;
    @Autowired
    public ArticleService(ArticleMapper articleMapper){
        this.articleMapper = articleMapper;
    }

    public ArticlesEntity readArticle(int index){
        ArticlesEntity article = this.articleMapper.selectArticleByIndex(index);
        if (article != null && !article.isDeleted()){
            article.setView(article.getView() + 1);
            this.articleMapper.updateArticle(article);
        }
        return article;
    }

    public AttachmentsEntity[] getAttachmentsOf(ArticlesEntity article) {

        return this.articleMapper.selectAttachmentsByArticleIndexNoData(article.getIndex());
    }
    @Transactional
    public boolean putArticle(HttpServletRequest request, ArticlesEntity article, MultipartFile[] files) throws IOException {
        article.setView(0)
                .setCreatedAt(new Date())
                .setClientIp(request.getRemoteAddr())
                .setClientUa(request.getHeader("User-Agent"))
                .setDeleted(false);
        if(this.articleMapper.insertArticle(article) < 1){
            return false;
        }
        int inserted = 0;
        for (MultipartFile file : files) {
            AttachmentsEntity attachment = new AttachmentsEntity()
                    .setArticleIndex(article.getIndex())
                    .setFileData(file.getBytes())
                    .setFileContentType(file.getContentType())
                    .setFileSize(file.getSize())
                    .setFileName(file.getOriginalFilename());
            inserted += this.articleMapper.insertAttachment(attachment);
        }
        return inserted == files.length;
    }



}
