package com.jhkim.studywebbbs.mappers;

import com.jhkim.studywebbbs.entities.ArticlesEntity;
import com.jhkim.studywebbbs.entities.AttachmentsEntity;
import com.jhkim.studywebbbs.entities.CommentEntity;
import com.jhkim.studywebbbs.entities.ImageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleMapper {
    int insertArticle(ArticlesEntity article);

    int insertAttachment(AttachmentsEntity attachment);
    int insertComment(CommentEntity comment);

    int insertImage(ImageEntity image);

    ArticlesEntity selectArticleByIndex(@Param("index")int index);
    AttachmentsEntity[] selectAttachmentsByArticleIndexNoData(@Param(value = "articleIndex")int articleIndex);

    AttachmentsEntity selectAttachment(@Param(value = "index") int index);

    CommentEntity[] selectCommentsByArticleIndex(@Param(value = "articleIndex")int articleIndex);
    CommentEntity selectComment(@Param(value = "index")int index);
    ImageEntity selectImage(@Param("index") int index);
    int updateArticle(ArticlesEntity article);

    int updateComment(CommentEntity comment);
}
