package com.hankarun.popularmovies.lib;

import org.json.JSONObject;

public class Video {
    private String id;
    private String key;
    private String name;
    private String site;
    private String type;

    public Video(JSONObject video) throws Exception{
        id = video.getString(StaticTexts.apiId);
        key = video.getString(StaticTexts.apiKey);
        name = video.getString(StaticTexts.apiName);
        site = video.getString(StaticTexts.apiSite);
        type = video.getString(StaticTexts.apiType);
    }

    public String getId(){ return id;}
    public String getKey(){ return key;}
    public String getName(){ return name;}
    public String getSite(){ return site;}
    public String getType(){ return type;}

    public String getUrl(){ return StaticTexts.mYoutubeBaseUrl+key;}
}
