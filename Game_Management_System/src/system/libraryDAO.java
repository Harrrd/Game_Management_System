package system;

//Display games in library

import entity.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class libraryDAO extends gameDAO{

    public List<Game> getGamesInLibrary(){
        String libraryQuery = "SELECT libGameID FROM Library WHERE LibraryID = ?;";
        List<Game> gamesInLibrary = new ArrayList<>();

        try (Connection conn = DB.getConnection();
             PreparedStatement stLibrary = conn.prepareStatement(libraryQuery)) {

            stLibrary.setInt(1, Session.getInstance().getUserID());
            ResultSet rsLibrary = stLibrary.executeQuery();

            while (rsLibrary.next()) {
                int gameID = rsLibrary.getInt("libGameID");
                Game game = getGameInfo(gameID);

                if (game != null) {
                    gamesInLibrary.add(game);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeConnection();
        }

        return gamesInLibrary;
    }
}
