package ismail.cozy.my.kaodiminternship;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Service {

    private String url;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("image_thumb_url")
    @Expose
    private ImageListUrl imageListUrls;

    public void setImageListUrls(ImageListUrl imageListUrls) {
        this.imageListUrls = imageListUrls;
    }

    public ImageListUrl getImageListUrls() {
        return imageListUrls;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
