package system;

import entity.Game;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class adminDAO {
    public boolean deleteGamer(int gamerID){

        //Delete gamer information including info in User, Gamer, Cart and Library
        String userQuery = "DELETE FROM User WHERE UserID = ?";
        String gamerQuery = "DELETE FROM Gamer WHERE GamerID = ?";
        String cartQuery = "DELETE FROM Cart WHERE CartID = ?";
        String libraryQuery = "DELETE FROM Library WHERE LibraryID = ?";

        try(Connection conn = DB.getConnection();
            PreparedStatement stUser = conn.prepareStatement(userQuery);
            PreparedStatement stGamer = conn.prepareStatement(gamerQuery);
            PreparedStatement stCart = conn.prepareStatement(cartQuery);
            PreparedStatement stLibrary = conn.prepareStatement(libraryQuery)){

            stCart.setInt(1, gamerID);
            stLibrary.setInt(1, gamerID);
            stCart.executeUpdate();
            stLibrary.executeUpdate();

            stUser.setInt(1, gamerID);
            stGamer.setInt(1, gamerID);
            int gamerRows = stGamer.executeUpdate();
            int userRows = stUser.executeUpdate();

            if(userRows > 0 && gamerRows > 0){
                return true;
            }

        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeConnection();
        }

        return false;
    }

    public boolean addGame(Game game){

        //Game info is seperated in three tables, Game, Image and Keyword
        //Game.GameID = Image.IdGame_image, Game.GameID = Keyword.IdGame_keyword
        //GameID, ImageID and KeywordID are automatically generated
        String gameQuery = "INSERT INTO Game (GameName, Description, Price, Type) VALUES (?, ?, ?, ?);";
        String imageQuery = "INSERT INTO Image (IdGame_image, Image) VALUES (?, ?);";
        String keywordQuery = "INSERT INTO Keyword (IdGame_keyword, Keyword) VALUES (?, ?);";

        try(Connection conn = DB.getConnection();
            PreparedStatement stGame = conn.prepareStatement(gameQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            PreparedStatement stImage = conn.prepareStatement(imageQuery);
            PreparedStatement stKeyword = conn.prepareStatement(keywordQuery)){

            //Make sure that all the information related to a game is inserted into the database
            conn.setAutoCommit(false);

            stGame.setString(1, game.getGameName());
            stGame.setString(2, game.getDescription());
            stGame.setDouble(3, game.getPrice());
            stGame.setString(4, game.getType());

            int gameRowsAffected = stGame.executeUpdate();

            if(gameRowsAffected > 0){
                //Get generated GameID
                ResultSet rs = stGame.getGeneratedKeys();
                if(rs.next()){
                    int gameID = rs.getInt(1);

                    //Insert images
                    for(BufferedImage image : game.getImages()){
                        stImage.setInt(1, gameID);

                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        ImageIO.write(image, "png", os);
                        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

                        stImage.setBinaryStream(2, is);
                        stImage.addBatch();
                    }
                    stImage.executeBatch();

                    //Insert keywords
                    for(String keyword : game.getKeywords()){
                        stKeyword.setInt(1, gameID);
                        stKeyword.setString(2, keyword);
                        stKeyword.addBatch();
                    }
                    stKeyword.executeBatch();

                    conn.commit();
                    return true;
                }
            }
        }catch (SQLException | IOException e) {
            e.printStackTrace();
            try {
                if (DB.getConnection() != null) {
                    DB.getConnection().rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (DB.getConnection() != null) {
                    DB.getConnection().setAutoCommit(true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            DB.closeConnection();
        }

        return false;

    }

    public boolean deleteGame(int gameID){
        String gameQuery = "DELETE FROM Game WHERE GameID = ?;";
        String keywordQuery = "DELETE FROM Keyword WHERE IdGame_keyword = ?;";
        String imageQuery = "DELETE FROM Image WHERE IdGame_image = ?;";

        try (Connection con = DB.getConnection();
             PreparedStatement stGame = con.prepareStatement(gameQuery);
             PreparedStatement stKeyword = con.prepareStatement(keywordQuery);
             PreparedStatement stImage = con.prepareStatement(imageQuery)) {

            con.setAutoCommit(false);

            // Delete game
            stGame.setInt(1, gameID);
            int gameRows = stGame.executeUpdate();

            // Delete keywords
            stKeyword.setInt(1, gameID);
            stKeyword.executeUpdate();

            // Delete images
            stImage.setInt(1, gameID);
            stImage.executeUpdate();

            con.commit();

            if (gameRows > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (DB.getConnection() != null) {
                    DB.getConnection().rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            DB.closeConnection();
        }

        return false;
    }

    //Update a game can be divided into updateGameInfo, updateImage and updateKeyword
    //updateImage equals addImage and deleteImage, so as updateKeyword
    public boolean updateGame(int gameID, Game game) {
        String gameQuery = "UPDATE Game SET Description = ?, Price = ?, Type = ? WHERE GameID = ?;";

        try (Connection conn = DB.getConnection();
             PreparedStatement st = conn.prepareStatement(gameQuery)) {

            // Set query parameters
            st.setString(1, game.getDescription());
            st.setDouble(2, game.getPrice());
            st.setString(3, game.getType());
            st.setInt(4, gameID);

            // Execute the query
            int affectedRows = st.executeUpdate();

            // If at least one row is affected, the game information was updated successfully
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeConnection();
        }

        return false;
    }

    public boolean addImage(int gameID, BufferedImage image) {
        String query = "INSERT INTO Image (IdGame_image, Image) VALUES (?, ?);";

        try (Connection conn = DB.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {

            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bs);
            byte[] imageBytes = bs.toByteArray();

            st.setInt(1, gameID);
            st.setBytes(2, imageBytes);
            st.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            DB.closeConnection();
        }

        return false;
    }

    public boolean deleteImage(int imageID) {
        String query = "DELETE FROM Image WHERE ImageID = ?;";

        try (Connection conn = DB.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {

            st.setInt(1, imageID);
            int affectedRows = st.executeUpdate();

            if (affectedRows > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeConnection();
        }

        return false;
    }

    public boolean addKeyword(int gameID, String keyword) {
        String query = "INSERT INTO Keyword (IdGame_keyword, Keyword) VALUES (?, ?);";

        try (Connection conn = DB.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {

            st.setInt(1, gameID);
            st.setString(2, keyword);
            st.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeConnection();
        }

        return false;
    }

    public boolean deleteKeyword(int keywordID) {
        String query = "DELETE FROM Keyword WHERE KeywordID = ?;";

        try (Connection conn = DB.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {

            st.setInt(1, keywordID);
            int affectedRows = st.executeUpdate();

            if (affectedRows > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeConnection();
        }

        return false;
    }
}
