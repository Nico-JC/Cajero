package Controllers;

import DataBase.DB;
import Data.CajeroDAO;
import Models.CajeroModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class CajeroController {
    private Connection connection;

    public CajeroController() throws SQLException {
        connection = DB.getConnection();
    }

    public ArrayList<CajeroModel> getAllCajeros(){
        return CajeroDAO.getAllCajeros(connection);
    }

    public boolean saveChange(int id, String status, double monto){

        String sql = "UPDATE cajero SET cantidad_dinero = ? WHERE id_cajero = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(2, monto);
            stmt.setInt(3, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean create(){
        String sql = "INSERT INTO cajero (cantidad_dinero) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, 0);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}