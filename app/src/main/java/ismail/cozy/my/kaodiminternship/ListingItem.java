package ismail.cozy.my.kaodiminternship;

public class ListingItem {
    private String name;
    private Boolean isHeader = false;
    private Boolean isFooter = false;
    private String imgUrl;

    public ListingItem(String name, Boolean isHeader){
        this.name = name;
        this.isHeader = isHeader;
    }

    public ListingItem(String name, String imgUrl){
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public ListingItem(Boolean isFooter){
        this.isFooter = isFooter;
    }

    public String getName() {
        return name;
    }

    public Boolean getHeader() {
        return isHeader;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Boolean getFooter() {
        return isFooter;
    }
}
