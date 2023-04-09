package system;


//1.Search by keyword
//2.Get game info by type

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

public class storeDAO extends gameDAO{

    //limit is the number of games to fetch and offset is the starting point for fetching games
    public List<Game> getGamesByType(String type, int limit, int offset) {

        //Click button "ALL" will show all games
        String query;
        if ("ALL".equalsIgnoreCase(type)) {
            query = "SELECT GameID FROM Game LIMIT ? OFFSET ?;";
        } else {
            query = "SELECT GameID FROM Game WHERE Type = ? LIMIT ? OFFSET ?;";
        }
        List<Game> gamesByType = new ArrayList<>();

        try (Connection conn = DB.getConnection();
             PreparedStatement stGames = conn.prepareStatement(query)) {

            if ("ALL".equalsIgnoreCase(type)) {
                stGames.setInt(1, limit);
                stGames.setInt(2, offset);
            } else {
                stGames.setString(1, type);
                stGames.setInt(2, limit);
                stGames.setInt(3, offset);
            }
            ResultSet rsGames = stGames.executeQuery();

            while (rsGames.next()) {
                int gameID = rsGames.getInt("GameID");
                Game game = getGameInfo(gameID);

                if (game != null) {
                    gamesByType.add(game);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeConnection();
        }

        return gamesByType;
    }

    //Search games
    public List<Game> searchGames(String input){
        String query = "SELECT DISTINCT g.GameID FROM Game g " +
                "JOIN Keyword k ON g.GameID = k.IdGame_keyword " +
                "WHERE g.Description LIKE ? OR k.Keyword LIKE ?;";

        List<Game> gamesByKeyword = new ArrayList<>();

        try (Connection conn = DB.getConnection();
             PreparedStatement stGames = conn.prepareStatement(query)) {

            String keywordPattern = "%" + input + "%";
            stGames.setString(1, keywordPattern);
            stGames.setString(2, keywordPattern);
            ResultSet rsGames = stGames.executeQuery();

            while (rsGames.next()) {
                int gameID = rsGames.getInt("GameID");
                Game game = getGameInfo(gameID);

                if (game != null) {
                    gamesByKeyword.add(game);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeConnection();
        }

        return gamesByKeyword;
    }
}
