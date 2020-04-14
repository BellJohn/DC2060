package com.reachout.blog;

import java.util.ArrayList;
import com.reachout.models.Post;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class PostReader {

    public ArrayList<Post> readPosts() {

        ArrayList<Post> posts = new ArrayList();

        //File file = new File("/bitnami/tomcat/data/ReachOut/blogposts/testblog");
        //BufferedReader br = new BufferedReader(new FileReader(file)); 

        String title = "Title,,,";
        String author = "Author,,,";
        String content = "Blog post content";
        Post p = new Post(title, author, content);
        posts.add(p);

        return posts;

    }

}