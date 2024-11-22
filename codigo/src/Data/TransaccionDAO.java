package Data;

import Models.TransaccionModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransaccionDAO {
    private Connection connection;

    public TransaccionDAO(Connection connection) {
        this.connection = connection;
    }

    public List<TransaccionModel> obtenerTodasLasTransacciones() {
        List<TransaccionModel> transacciones = new ArrayList<>();
        String sql = "SELECT m.id, m.userID, m.monto, m.tipo, m.id_cajero, u.nombre as nombreUsuario, u2.nombre as nombreReceptor " +
                "FROM movimiento m " +
                "JOIN usuario u ON m.userID = u.id " +
                "LEFT JOIN usuario u2 ON m.receptor = u2.id ";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TransaccionModel transaccion = new TransaccionModel(
                        rs.getInt("id"),
                        rs.getInt("userID"),
                        rs.getDouble("monto"),
                        rs.getString("tipo"),
                        rs.getInt("id_cajero"),
                        rs.getString("nombreUsuario"),
                        rs.getString("nombreReceptor")
                );
                transacciones.add(transaccion);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las transacciones: " + e.getMessage());
        }
        return transacciones;
    }
    public List<TransaccionModel> obtenerMovimientosUsuario(int userId) {
        List<TransaccionModel> transacciones = new ArrayList<>();
        String sql = "SELECT m.id, m.userID, m.monto, m.tipo, m.id_cajero, " +
                "u1.nombre as nombreOrigen, u2.nombre as nombreReceptor " +
                "FROM movimiento m " +
                "JOIN usuario u1 ON m.userID = u1.id " +
                "LEFT JOIN usuario u2 ON m.receptor = u2.id " +
                "WHERE m.userID = ? OR m.receptor = ? ";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TransaccionModel transaccion = new TransaccionModel(
                        rs.getInt("id"),
                        rs.getInt("userID"),
                        rs.getDouble("monto"),
                        rs.getString("tipo"),
                        rs.getInt("id_cajero"),
                        rs.getString("nombreOrigen"),
                        rs.getString("nombreReceptor")
                );
                transacciones.add(transaccion);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los movimientos del usuario: " + e.getMessage());
        }
        return transacciones;
    }
}
