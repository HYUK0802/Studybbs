package com.jhkim.studywebbbs.controllers;

import com.jhkim.studywebbbs.entities.ArticlesEntity;
import com.jhkim.studywebbbs.services.ArticleService;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

// CKEditor5 builder
@Controller
@RequestMapping(value = "/article")
public class ArticleControllers {
    private ArticleService articleService;

    @Autowired
    public ArticleControllers(ArticleService articleService){
        this.articleService = articleService;
    }

    @RequestMapping(value = "read",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getRead(@RequestParam(value = "index")int index) {
        ModelAndView modelAndView = new ModelAndView("article/read");
        ArticlesEntity article = this.articleService.readArticle(index);
        modelAndView.addObject("article",article);
        if(article != null){
            modelAndView.addObject("attachments", this.articleService.getAttachmentsOf(article));
        }
        return modelAndView;
    }


    @RequestMapping(value = "write",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getWrite() {
        ModelAndView modelAndView = new ModelAndView("article/write");
        return modelAndView;
    }
    @RequestMapping(value="write",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postWrite(HttpServletRequest request,
                                  ArticlesEntity article,
                                  @RequestParam(value = "files", required = false)MultipartFile[] files) throws IOException {
        if(files == null) {
            files = new MultipartFile[0];
        }
        boolean result = this.articleService.putArticle(request, article, files);
        ModelAndView modelAndView = new ModelAndView();
        if (result) {
            modelAndView.setViewName("redirect:/article/read?index=" + article.getIndex());
        }else {
            modelAndView.setViewName("article/write");
            modelAndView.addObject("result", result);
        }
        return modelAndView;
    }
}


