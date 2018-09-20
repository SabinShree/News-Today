package datacontainer;

public class NewsItems {
    private String imageUrl;
    private String newsName;
    private String description;
    private String newsTime;
    private String author;
    private String url;

    public NewsItems(String imageUrl, String newsName, String description, String newsTime, String author, String url) {
        this.imageUrl = imageUrl;
        this.newsName = newsName;
        this.url = url;
        this.newsTime = newsTime;
        this.author = author;
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getNewsName() {
        return newsName;
    }

    public String getNewsTime() {
        return newsTime;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "NewsItems{" +
                "imageUrl='" + imageUrl + '\'' +
                ", newsName='" + newsName + '\'' +
                ", description='" + description + '\'' +
                ", newsTime='" + newsTime + '\'' +
                ", author='" + author + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
