package datacontainer;

public class CategoryItems {
    private String imageName;
    private String name;

    public CategoryItems(String imageName, String name) {
        this.imageName = imageName;
        this.name = name;
    }

    public String getImageName() {
        return imageName;
    }

    public String getName() {
        return name;
    }
}
