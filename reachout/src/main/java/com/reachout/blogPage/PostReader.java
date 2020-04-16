package com.reachout.blog;

import com.reachout.models.Post;
import java.util.ArrayList;
import java.util.Arrays;
import java.net.URL;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException; 


public class PostReader {

    private String folder;

    public PostReader(String folder) {
        this.folder = folder;
    }

    public ArrayList<Post> readPosts() {
        ArrayList<Post> posts = new ArrayList();

        File[] files = getFilesFromFolder();
        //Ensure that some files have been found, otherwise return empty array
        if (files == null || files.length == 0) {
            return posts;
        }

        //Sort files lexicographically, then convert each file into a post object
        Arrays.sort(files);
        try {  
            for(File file : files) {
                BufferedReader br = new BufferedReader(new FileReader(file)); 

                String title = br.readLine();
                String author = br.readLine();
                String date = br.readLine();
                String content = br.readLine();
                Post p = new Post(title, author, date, content);

                posts.add(p);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }  

        return posts;
    }

    public File[] getFilesFromFolder() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folder);

        //Check to see if folder nonexistant or empty
        if (url == null) {
            return null;
        }

        //Return array of all files found on path
        String path = url.getPath();
        return new File(path).listFiles();
    }
}