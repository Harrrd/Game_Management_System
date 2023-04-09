package entity;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private int GameID;
    private String GameName;
    private String Description;
    private double Price;
    private String Type;
    private List<BufferedImage> Images;
    private List<String> Keywords;

    public Game(){
    }

    public Game(int gameID, String gameName, String description, double price, String type, List<BufferedImage> images, List<String> keywords) {

        GameID = gameID;
        GameName = gameName;
        Description = description;
        Price = price;
        Type = type;
        Images = new ArrayList<>(images);
        Keywords = new ArrayList<>(keywords);
    }

    public int getGameID() {
        return GameID;
    }

    public void setGameID(int gameID) {
        GameID = gameID;
    }

    public String getGameName() {
        return GameName;
    }

    public void setGameName(String gameName) {
        GameName = gameName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public List<BufferedImage> getImages() {
        return Images;
    }

    public void setImages(List<BufferedImage> images) {
        Images = images;
    }

    public List<String> getKeywords() {
        return Keywords;
    }

    public void setKeywords(List<String> keywords) {
        Keywords = keywords;
    }
}
