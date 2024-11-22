package Data;

import Models.CajeroModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DataBase.DB;


public class CajeroDAO {
    private Connection connection;

    public CajeroDAO() {
        connection = DB.getConnection();
        if (connection == null) {
            throw new IllegalStateException("Conexión a la base de datos no disponible.");
        }
    }

    public static ArrayList<CajeroModel> getAllCajeros(Connection connection){
        ArrayList<CajeroModel> lista = new ArrayList<>();
        String sql = "SELECT * FROM cajero";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CajeroModel cajero = new CajeroModel(
                            rs.getInt("id_cajero"),
                            rs.getInt("efectivo_disponible")
                    );
                    lista.add(cajero);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public double obtenerEfectivoDisponible(int idCajero) {
        String sql = "SELECT efectivo_disponible FROM cajero WHERE id_cajero = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCajero);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("efectivo_disponible");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener efectivo disponible: " + e.getMessage());
        }
        throw new RuntimeException("No se encontró el cajero especificado");
    }


    public void modificarEfectivo(int idCajero, double monto, String tipo) {
        String sql = "";
        String tipoLower = tipo.toLowerCase().trim();
        System.out.println("Tipo después de toLowerCase y trim: '" + tipoLower + "'");

        if (tipoLower.equalsIgnoreCase("retiro")) {
            sql = "UPDATE cajero SET efectivo_disponible = efectivo_disponible - ? WHERE id_cajero = ?";
        }else {
                sql = "UPDATE cajero SET efectivo_disponible = efectivo_disponible + ? WHERE id_cajero = ?";
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, monto);
            stmt.setInt(2, idCajero);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al modificar el efectivo del cajero: " + e.getMessage());
        }
    }
}