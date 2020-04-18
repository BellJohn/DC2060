package com.reachout.blog;

import com.reachout.models.Post;
import com.reachout.blog.PostReader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import java.util.ArrayList;

/**
 * Testing the PostReader class.
 * 
 * @author Jordan
 * 
 */
public class PostReaderTest {

    /**
     * Tests to see if the correct file objects are created when given
     * a valid direcotry containing a single file.
     */
    @Test
    public void testReadSingleFile() {
        PostReader pr = new PostReader("PostReaderTestFiles/SingleFileTest/");
        ArrayList<Post> posts = pr.readPosts();

        assertEquals("Title", posts.get(0).getTitle());
        assertEquals("Author", posts.get(0).getAuthor());
        assertEquals("Date", posts.get(0).getDate());
        assertEquals("Test blog post content.", posts.get(0).getContent());

        assertEquals(1, posts.size());
    }

    /**
     * Test to ensure that blog posts with multiple paragraphs (more lines
     * of content after the first four) have paragraph spacing added 
     * appropriately
     * 
     */
    @Test
    public void testMultilineContent() {
        PostReader pr = new PostReader("PostReaderTestFiles/MultilineContentTest/");
        ArrayList<Post> posts = pr.readPosts();

        assertEquals("Title", posts.get(0).getTitle());
        assertEquals("Author", posts.get(0).getAuthor());
        assertEquals("Date", posts.get(0).getDate());
        assertEquals("Content<br/><br/>Content 2<br/><br/>Content 3<br/><br/>Content 4", posts.get(0).getContent());

        assertEquals("title", posts.get(1).getTitle());
        assertEquals("author", posts.get(1).getAuthor());
        assertEquals("date", posts.get(1).getDate());
        assertEquals("content<br/><br/>content 2<br/><br/>content 3<br/><br/>content 4", posts.get(1).getContent());

        assertEquals(2, posts.size());
    }

    /**
     * Tests to see if the correct file objects are created when given
     * a valid directory containing multiple files.
     */
    @Test
    public void testReadMultipleFiles() {
        PostReader pr = new PostReader("PostReaderTestFiles/MultipleFileTest/");
        ArrayList<Post> posts = pr.readPosts();

        assertEquals("Test Title", posts.get(0).getTitle());
        assertEquals("Test Author", posts.get(0).getAuthor());
        assertEquals("Test Date", posts.get(0).getDate());
        assertEquals("Test blog post content. This is just simple text at the moment.", posts.get(0).getContent());

        assertEquals("Title", posts.get(1).getTitle());
        assertEquals("Mr Author", posts.get(1).getAuthor());
        assertEquals("6th Jan 1920", posts.get(1).getDate());
        assertEquals("This is the test content.", posts.get(1).getContent());

        assertEquals(2, posts.size());
    }

    /**
     * Tests to ensure that an empty collection of posts is returned when
     * given a directory which contains no files.
     */
    @Test
    public void testEmptyDirectory() {
        PostReader pr = new PostReader("PostReaderTestFiles/EmptyDirTest/");
        ArrayList<Post> posts = pr.readPosts();

        assertEquals(0, posts.size());
    }

    /**
     * Tests to ensure that an empty collection of posts is returned when
     * given a directory which doesn't exist.
     */
    @Test
    public void testNotValidDirectory() {
        PostReader pr = new PostReader("PostReaderTestFiles/NonValidDirectoryTest/");
        ArrayList<Post> posts = pr.readPosts();

        assertEquals(0, posts.size());
    }

    /**
     * Tests to ensure that an empty collection of posts is returned when
     * given the name of a direcotry which exists outside of resources/
     */
    @Test
    public void testExternalDirectory() {
        PostReader pr = new PostReader("WEB-INF/jsp/");
        ArrayList<Post> posts = pr.readPosts();

        assertEquals(0, posts.size());
    }

    /**
     * Tests to ensure that files containing the wrong 
     * post format are ignored. 
     */
    @Test
    public void testIncorrectFileFormat() {
        PostReader pr = new PostReader("PostReaderTestFiles/IncorrectFileTest/");
        ArrayList<Post> posts = pr.readPosts();

        assertEquals(0, posts.size());
    }

}