package system;

//Display games in cart

import entity.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class cartDAO extends gameDAO{

    public List<Game> getGamesInCart(){
        String cartQuery = "SELECT cartGameID FROM Cart WHERE cartID = ?;";
        List<Game> gamesInCart = new ArrayList<>();

        try (Connection conn = DB.getConnection();
             PreparedStatement stCart = conn.prepareStatement(cartQuery)) {

            stCart.setInt(1, Session.getInstance().getUserID());
            ResultSet rsCart = stCart.executeQuery();

            while (rsCart.next()) {
                int gameID = rsCart.getInt("cartGameID");
                Game game = getGameInfo(gameID);

                if (game != null) {
                    gamesInCart.add(game);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeConnection();
        }

        return gamesInCart;
    }

}
