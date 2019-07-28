package ismail.cozy.my.kaodiminternship;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageListUrl {
    @SerializedName("sm")
    @Expose
    private String url;

    public String getUrl(){
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
