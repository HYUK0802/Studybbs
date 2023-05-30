package com.jhkim.studywebbbs.controllers;

import com.jhkim.studywebbbs.entities.ArticlesEntity;
import com.jhkim.studywebbbs.entities.AttachmentsEntity;
import com.jhkim.studywebbbs.entities.CommentEntity;
import com.jhkim.studywebbbs.entities.ImageEntity;
import com.jhkim.studywebbbs.services.ArticleService;
import org.checkerframework.checker.units.qual.C;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
    public ArticleControllers(ArticleService articleService) {
        this.articleService = articleService;
    }

    @RequestMapping(value = "read",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getRead(@RequestParam(value = "index") int index) {
        ModelAndView modelAndView = new ModelAndView("article/read");
        ArticlesEntity article = this.articleService.readArticle(index);
        modelAndView.addObject("article", article);
        if (article != null) {
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

    @RequestMapping(value = "write",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postWrite(HttpServletRequest request,
                                  ArticlesEntity article,
                                  @RequestParam(value = "files", required = false) MultipartFile[] files) throws IOException {
        if (files == null) {
            files = new MultipartFile[0];
        }
        boolean result = this.articleService.putArticle(request, article, files);
        ModelAndView modelAndView = new ModelAndView();
        if (result) {
            modelAndView.setViewName("redirect:/article/read?index=" + article.getIndex());
        } else {
            modelAndView.setViewName("article/write");
            modelAndView.addObject("result", result);
        }
        return modelAndView;
    }

    @RequestMapping(value = "uploadImage",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)

    @ResponseBody
    public String postUploadImage(HttpServletRequest request,
                                  @RequestParam(value = "upload") MultipartFile file) throws IOException {
        ImageEntity image = this.articleService.putImage(request, file);
        JSONObject responseObject = new JSONObject() {{
            put("url", "/article/downloadImage?index=" + image.getIndex());
        }};

        return responseObject.toString();
    }

    @RequestMapping(value = "downloadImage", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDownloadImage(@RequestParam(value = "index") int index) {
        ImageEntity file = this.articleService.getImage(index);
        ResponseEntity<byte[]> response;
        if (file == null) {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(file.getSize());
            headers.setContentType(MediaType.parseMediaType(file.getContentType()));
            response = new ResponseEntity<>(file.getData(), headers, HttpStatus.OK);
        }
        return response;
    }

    @RequestMapping(value = "download", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDownload(@RequestParam(value = "index") int index) {
        AttachmentsEntity attachment = this.articleService.getAttachment(index);
        ResponseEntity<byte[]> response;
        if (attachment == null) {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(attachment.getFileSize());
            headers.setContentType(MediaType.parseMediaType(attachment.getFileContentType()));
            response = new ResponseEntity<>(attachment.getFileData(), headers, HttpStatus.OK);
        }
        return response;
    }

    @RequestMapping(value = "comment",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CommentEntity[] getComment(@RequestParam(value = "articleIndex") int articleIndex) {

        return this.articleService.getCommentsOf(articleIndex);
    }

    @RequestMapping(value = "comment",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postComment(HttpServletRequest request,
                              CommentEntity comment) {
        boolean result = this.articleService.putComment(request, comment);
        return String.valueOf(result);
    }

    @RequestMapping(value = "comment",
            method = RequestMethod.DELETE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String deleteComment(CommentEntity comment) {
        boolean result = this.articleService.deleteComment(comment);
        return String.valueOf(result);
    }

}

