package edu.udacity.android.gridviewexample2;

public class MovieInfo {
    private final String name;
    private final int imageId;

    public MovieInfo(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
