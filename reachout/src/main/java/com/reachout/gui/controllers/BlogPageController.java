package com.reachout.gui.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import com.reachout.blog.PostReader;
import com.reachout.models.Post;

@Controller
@RequestMapping("/blog")
public class BlogPageController {

    public final Logger logger = LogManager.getLogger(BlogPageController.class);

    private static final String VIEW_NAME = "blog";

    
	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {

		logger.debug("Reached Blog Page Controller");
		ModelAndView mv = new ModelAndView(VIEW_NAME);
        mv.addObject("currentPage", VIEW_NAME);

        PostReader pr = new PostReader();
        ArrayList<Post> blogPosts = pr.readPosts();
        mv.addObject("blogPosts", blogPosts);

		return mv;
	}
}
