package ru.timuruktus.projectposter;


import java.util.ArrayList;

public class Post {

    public String text,title,imageURL;
    public ArrayList<String> tags;

    public Post(){}

    public Post(String text, String title, String imageURL, ArrayList<String> tags){
        this.text = text;
        this.title = title;
        this.imageURL = imageURL;
        this.tags = tags;
    }
}
