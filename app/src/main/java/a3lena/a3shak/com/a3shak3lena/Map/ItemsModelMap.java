package a3lena.a3shak.com.a3shak3lena.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import a3lena.a3shak.com.a3shak3lena.util.Constatns;

public class ItemsModelMap {

    private String Id;
    private String Name;
    private String Descr;
    private String Logo;
    private String FacebookLink;
    private String YoutubeLink;
    private String TwitterLink;
    private String WebLink;
    private String ShowCommite;
    private String CategoryId;
    private String Latitude;
    private String Longitude;
    private String WorkingHours;
    private String Points;
    private String Phone;
    private ArrayList<String> DealershipImages;

    public ItemsModelMap(JSONObject object) {
        try {
            Id = object.getString("Id");
            Name = object.getString("Name");
            Descr = object.getString("Descr");
            Logo =  object.getString("Logo");
            FacebookLink = object.getString("FacebookLink");
            YoutubeLink = object.getString("YoutubeLink");
            TwitterLink = object.getString("TwitterLink");
            WebLink = object.getString("WebLink");
            ShowCommite = object.getString("ShowCommite");
            Phone = object.getString("Phone");
            CategoryId = object.getString("CategoryId");
            Latitude = object.getString("Latitude");
            Longitude = object.getString("Longitude");
            WorkingHours = object.getString("WorkingHours");
            Points = object.getString("Points");
            DealershipImages = new ArrayList<>();
            JSONArray jsonArray2 = new JSONArray(object.getString("DealershipImages"));
            for (int k = 1; k < jsonArray2.length(); k++) {
                DealershipImages.add(jsonArray2.get(k).toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescr() {
        return Descr;
    }

    public void setDescr(String descr) {
        Descr = descr;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getFacebookLink() {
        return FacebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        FacebookLink = facebookLink;
    }

    public String getYoutubeLink() {
        return YoutubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        YoutubeLink = youtubeLink;
    }

    public String getTwitterLink() {
        return TwitterLink;
    }

    public void setTwitterLink(String twitterLink) {
        TwitterLink = twitterLink;
    }

    public String getShowCommite() {
        return ShowCommite;
    }

    public void setShowCommite(String showCommite) {
        ShowCommite = showCommite;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getWorkingHours() {
        return WorkingHours;
    }

    public void setWorkingHours(String workingHours) {
        WorkingHours = workingHours;
    }

    public String getPoints() {
        return Points;
    }

    public void setPoints(String points) {
        Points = points;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public ArrayList<String> getDealershipImages() {
        return DealershipImages;
    }

    public void setDealershipImages(ArrayList<String> dealershipImages) {
        DealershipImages = dealershipImages;
    }

    public String getWebLink() {
        return WebLink;
    }

    public void setWebLink(String webLink) {
        WebLink = webLink;
    }
}



