package com.reachout.blogpage;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PostReaderTest {

    //Pass in files path, get back array list with objects of each file
    @Test
    public void testRead() {
        PostReader pr = new PostReader("../resources/blogposts/testblog");
        ArrayList<Post> posts = pr.readPosts();
    }

    //Pass in incorrect path, get back appropriate exception

    //Somethin

}