package com.reachout.blog;

import com.reachout.models.Post;
import java.util.ArrayList;
import java.util.Arrays;
import java.net.URL;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException; 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Used for converting all files from a specified directory into instances of class Post.
 * 
 * @author Jordan
 *
 */
public class PostReader {

    private String directory;
    private static final Logger logger = LogManager.getLogger(PostReader.class);

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

        ArrayList<Post> posts = new ArrayList<>();

        //Read each file into a file object, and return the empty array if none were found. 
        File[] files = getFilesFromDirectory();
        if (files == null || files.length == 0) {
            return posts;
        }

        //Sort files lexicographically
        Arrays.sort(files);

        for(File file : files) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) { 

                //Read each of the relevant lines into the relevant variable, then create the object
                String title = br.readLine();
                String author = br.readLine();
                String date = br.readLine();
                String content = br.readLine();

                //After the first four lines, treat any subsequent lines as new paragraphs of content
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.matches("\\s*")) {
                        content += "<br/><br/>" + line;
                    }
                }

                //Ensure that all fields have appropriate values before adding to posts
                if (checkFileContent(title, author, date, content)) {
                    Post p = new Post(title, author, date, content);
                    posts.add(p);
                }
            } catch (IOException e) {
                //This shouldn't ever be reached, but report errors and return the array
                logger.error("POSTREADER ERROR when reading file '" + file.getName() + "' from directory '" + directory + "'.", e);
                return posts;
            }
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

        //Check to see if file nonexistant or empty
        if (url == null) {
            return new File[] {};
        }

        //Return array of all files found on path
        String path = url.getPath();
        return new File(path).listFiles();
    }

    /**
     * Get a specific file from a given directory
     * 
     * 
     */
    public static String getFilePath(String fileName) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(fileName);
        System.out.println( url);

        //Check to see if file exists
        if (url == null) {
            System.out.println("File can't be found");
        }

        //Return array of all files found on path
        return url.getPath();
        
     
    }
    
    
    
    /**
     * Returns whether or not all blog post fields contain valid
     * content.
     * 
     */
    private Boolean checkFileContent(String title, String author, String date, String content) {

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