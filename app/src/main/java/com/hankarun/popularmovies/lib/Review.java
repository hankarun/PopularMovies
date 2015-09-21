package com.hankarun.popularmovies.lib;

import org.json.JSONObject;

public class Review {
    private String id;
    private String author;
    private String content;
    private String url;

    public Review(JSONObject review) throws Exception{
        id = review.getString(StaticTexts.apiId);
        author = review.getString(StaticTexts.apiAuthor);
        content = review.getString(StaticTexts.apiContent);
        url = review.getString(StaticTexts.apiUrl);
    }

    public String getId(){ return id;}
    public String getAuthor(){ return author;}
    public String getContent(){ return content;}
    public String getUrl(){ return url;}
}
