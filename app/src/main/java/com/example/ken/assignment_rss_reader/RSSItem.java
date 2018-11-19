package com.example.ken.assignment_rss_reader;

class RSSItem {

    public String title;
    public String link;
    public String description;
    public String image;
    public String pubdate;




    public RSSItem(String title, String description,String image, String pubdate, String link) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.link = link;
        this.pubdate = pubdate;

    }
}
