package a3lena.a3shak.com.a3shak3lena.Dealers.Items;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Created by aldaboubi on 8/16/2017.
 */
//Descr-Logo-FacebookLink-YoutubeLink-TwitterLink-ShowCommite-CategoryId-Latitude-Longitude-WorkingHours-Points-DealershipImages

public class ItemsModel extends ArrayList<String> {

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


    public ArrayList<String> getDealershipImages() {
        return DealershipImages;
    }

    public void setDealershipImages(ArrayList<String> dealershipImages) {
        DealershipImages = dealershipImages;
    }

    @Override
    public Stream<String> parallelStream() {
        return null;
    }

    public JSONObject toJson(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", getId());
            jsonObject.put("name", getName());
            jsonObject.put("descr", getDescr());
            jsonObject.put("logo", getLogo());
            jsonObject.put("mobile", getPhone());
            jsonObject.put("fblink", getFacebookLink());
            jsonObject.put("youtubelink", getYoutubeLink());
            jsonObject.put("twitterlink", getTwitterLink());
            jsonObject.put("weblink", getTwitterLink());
            jsonObject.put("showcommite", getShowCommite());
            jsonObject.put("catid", getCategoryId());
            jsonObject.put("latitude", getLatitude());
            jsonObject.put("longitude", getLongitude());
            jsonObject.put("workinghours", getWorkingHours());
            jsonObject.put("points", getPoints());
            jsonObject.put("dealershipimages", getDealershipImages());


            return jsonObject;
        }catch (Exception e){
            return null;
        }

    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getWebLink() {
        return WebLink;
    }

    public void setWebLink(String webLink) {
        WebLink = webLink;
    }
}



