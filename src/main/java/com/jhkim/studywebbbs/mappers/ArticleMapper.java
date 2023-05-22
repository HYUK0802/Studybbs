package com.jhkim.studywebbbs.mappers;

import com.jhkim.studywebbbs.entities.ArticlesEntity;
import com.jhkim.studywebbbs.entities.AttachmentsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleMapper {
    int insertArticle(ArticlesEntity article);

    int insertAttachment(AttachmentsEntity attachment);

    ArticlesEntity selectArticleByIndex(@Param("index")int index);
    AttachmentsEntity[] selectAttachmentsByArticleIndexNoData(@Param(value = "articleIndex")int articleIndex);

    int updateArticle(ArticlesEntity article);
}
