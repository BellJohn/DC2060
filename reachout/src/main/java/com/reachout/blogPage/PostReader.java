package com.reachout.blog;

import com.reachout.models.Post;
import java.util.ArrayList;
import java.util.Arrays;
import java.net.URL;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException; 

/**
 * Used for converting all files from a specified directory into instances of class Post.
 * 
 * @author Jordan
 *
 */
public class PostReader {

    private String directory;

    /**
     * 
     * @param directory
     */
    public PostReader(String directory) {
        this.directory = directory;
    }

    /**
     * Convert all of the files from the given directory to instances
     * of class Post, and return them in an ordered array. 
     * 
     */
    public ArrayList<Post> readPosts() {

        ArrayList<Post> posts = new ArrayList();

        //Read each file into a file object, and return the empty array if none were found. 
        File[] files = getFilesFromDirectory();
        if (files == null || files.length == 0) {
            return posts;
        }

        //Sort files lexicographically
        Arrays.sort(files);

        try {  
            for(File file : files) {
                BufferedReader br = new BufferedReader(new FileReader(file)); 

                //Read each of the relevant lines into the relevant variable, then create the object
                String title = br.readLine();
                String author = br.readLine();
                String date = br.readLine();
                String content = br.readLine();

                //Ensure that all fields have appropriate values before adding to posts
                if (checkFileContent(title, author, date, content)) {
                    Post p = new Post(title, author, date, content);
                    posts.add(p);
                }
            }

        } catch (IOException e) {
            //This shouldn't ever be reached, but report errors and return the array
            e.printStackTrace();
            return posts;
        }  

        return posts;
    }

    /**
     * Create an array containing all files within the
     * given directory.
     * 
     */
    public File[] getFilesFromDirectory() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(directory);

        //Check to see if directory nonexistant or empty
        if (url == null) {
            return null;
        }

        //Return array of all files found on path
        String path = url.getPath();
        return new File(path).listFiles();
    }

    /**
     * Returns whether or not all blog post fields contain valid
     * content.
     * 
     */
    public Boolean checkFileContent(String title, String author, String date, String content) {

        //Check if any fields are null. If they are, return false.
        if (title == null || author == null || date == null || content == null) {
            return false;
        }

        //Check to ensure that no fields just contain spaces or newlines.
        if (title.matches("\\s*") || author.matches("\\s*") || date.matches("\\s*") || content.matches("\\s*")) {
            return false;
        }

        return true;
    }
}