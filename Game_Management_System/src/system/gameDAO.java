package system;

import entity.Game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class gameDAO {
    public Game getGameInfo(int gameID){
        String gameQuery = "SELECT GameName, Description, Price, Type FROM Games WHERE GameID = ?;";
        String imageQuery = "SELECT Image FROM Image WHERE IdGame_image = ?;";
        String keywordQuery = "SELECT Keyword FROM Keyword WHERE IdGame_keyword = ?;";

        Game game = null;

        try (Connection conn = DB.getConnection();
             PreparedStatement stGame = conn.prepareStatement(gameQuery);
             PreparedStatement stImage = conn.prepareStatement(imageQuery);
             PreparedStatement stKeyword = conn.prepareStatement(keywordQuery)) {

            stGame.setInt(1, gameID);
            ResultSet rsGame = stGame.executeQuery();

            if (rsGame.next()) {
                String gameName = rsGame.getString("GameName");
                String description = rsGame.getString("Description");
                double price = rsGame.getDouble("Price");
                String type = rsGame.getString("Type");

                stImage.setInt(1, gameID);
                ResultSet rsImage = stImage.executeQuery();
                List<BufferedImage> images = new ArrayList<>();

                while (rsImage.next()) {
                    byte[] imageBytes = rsImage.getBytes("Image");
                    images.add(ImageIO.read(new ByteArrayInputStream(imageBytes)));
                }

                stKeyword.setInt(1, gameID);
                ResultSet rsKeyword = stKeyword.executeQuery();
                List<String> keywords = new ArrayList<>();

                while (rsKeyword.next()) {
                    String keyword = rsKeyword.getString("Keyword");
                    keywords.add(keyword);
                }

                game = new Game(gameID, gameName, description, price, type, images, keywords);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            DB.closeConnection();
        }

        return game;
    }
}
