package com.jhkim.studywebbbs.services;


import com.jhkim.studywebbbs.entities.ArticlesEntity;
import com.jhkim.studywebbbs.entities.AttachmentsEntity;
import com.jhkim.studywebbbs.entities.CommentEntity;
import com.jhkim.studywebbbs.entities.ImageEntity;
import com.jhkim.studywebbbs.mappers.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
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

    public ImageEntity putImage(HttpServletRequest request, MultipartFile file) throws IOException {

        ImageEntity image = new ImageEntity()
                .setName(file.getName())
                .setSize(file.getSize())
                .setContentType(file.getContentType())
                .setData(file.getBytes())
                .setCreatedAt(new Date())
                .setClientIp(request.getRemoteAddr())
                .setClientUa(request.getHeader("User-Agent"));
            this.articleMapper.insertImage(image);
            return image;

   }
   public ImageEntity getImage(int index) {
        return this.articleMapper.selectImage(index);
   }
   public  AttachmentsEntity getAttachment(int index) {

        return this.articleMapper.selectAttachment(index);
   }
   public CommentEntity[] getCommentsOf(int articleIndex){
        return this.articleMapper.selectCommentsByArticleIndex(articleIndex);
   }

   public boolean deleteComment(CommentEntity comment) {
        comment = this.articleMapper.selectComment(comment.getIndex());
        if(comment == null) {
            return false;
        }
        comment.setDeleted(true);
        return this.articleMapper.updateComment(comment) >0;
   }

   public boolean putComment(HttpServletRequest request, CommentEntity comment){
        comment.setDeleted(false)
                .setCreatedAt(new Date())
                .setClientIp(request.getRemoteAddr())
                .setClientUa(request.getHeader("User-Agent"));
        return this.articleMapper.insertComment(comment) >0;
   }
}
