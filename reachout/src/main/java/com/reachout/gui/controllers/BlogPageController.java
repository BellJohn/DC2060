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

/**
 * Controller for the blog page, responsible for creating and 
 * calling the relevant classes to create the blog posts.
 * 
 * @author Jordan
 * 
 */
@Controller
@RequestMapping("/blog")
public class BlogPageController {

    public final Logger logger = LogManager.getLogger(BlogPageController.class);

    private static final String VIEW_NAME = "blog";
    private static final String BLOGS_FILE_PATH = "blogposts/"; //Relative from the resources folder

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {

		logger.debug("Reached Blog Page Controller");
		ModelAndView mv = new ModelAndView(VIEW_NAME);
        mv.addObject("currentPage", VIEW_NAME);
        
        //Create a new instance of type PostReader, read the posts and then add them to the page
        PostReader pr = new PostReader(BLOGS_FILE_PATH);
        ArrayList<Post> blogPosts = pr.readPosts();
        mv.addObject("blogPosts", blogPosts);

		return mv;
	}
}
