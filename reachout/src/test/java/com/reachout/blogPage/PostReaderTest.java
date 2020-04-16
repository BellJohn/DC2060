package com.reachout.blog;

import com.reachout.models.Post;
import com.reachout.blog.PostReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


public class PostReaderTest {

    @Test
    public void testRead() {
        PostReader pr = new PostReader("blogposts/");
        ArrayList<Post> posts = pr.readPosts();

        assertEquals("Test Blog Post Title", posts.get(0).getTitle());
        assertEquals("Test Author", posts.get(0).getAuthor());
        assertEquals("Test blog post content. This is just simple text at the moment.", posts.get(0).getContent());

        assertEquals("Test", posts.get(1).getTitle());
        assertEquals("Blog", posts.get(1).getAuthor());
        assertEquals("Two", posts.get(1).getContent());

        assertEquals(2, posts.size());
    }

    @Test
    public void testEmptyDirectory() {
        PostReader pr = new PostReader("blogposts-empty-dir/");
        ArrayList<Post> posts = pr.readPosts();
        assertTrue(posts.size() == 0);
    }

    @Test
    public void testNotValidDirectory() {
        PostReader pr = new PostReader("noFilesFoundHere/");
        ArrayList<Post> posts = pr.readPosts();
        assertTrue(posts.size() == 0);
    }

}