package Data;


import DataBase.DB;
import Models.UsuarioModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private static Connection connection;

    public UsuarioDAO() {
        connection = DB.getConnection();
        if (connection == null) {
            throw new IllegalStateException("Conexión a la base de datos no disponible.");
        }
    }

    public boolean register(String name, String pass) throws SQLException {
        String sql = "INSERT INTO usuario (nombre, contraseña) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, pass);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean login(String name, String pass) {
        String sql = "SELECT * FROM usuario WHERE nombre = ? AND contraseña = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, pass);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UsuarioModel.getInstance().inicializarUsuario(rs.getString("nombre"), rs.getInt("id"), rs.getDouble("saldo"));
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean existeUsuario(String nombre) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE nombre = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public boolean existeUsuarioPorId(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }


    public static boolean depositar(int id, double monto, int idCajero) {
        if (connection == null) {
            throw new IllegalStateException("La conexión a la base de datos no está inicializada.");
        }
        // añadimos el movimiento a la tabla de la bd
        String sql = "INSERT INTO movimiento (userID, monto, tipo, id_cajero) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setDouble(2, monto);
            stmt.setString(3, "Deposito");
            stmt.setInt(4, idCajero);
            if (stmt.executeUpdate() > 0) {
                // actualizaos el saldo del usuario
                sql = "UPDATE usuario SET saldo = saldo + ? WHERE id = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(sql)) {
                    updateStmt.setDouble(1, monto);
                    updateStmt.setInt(2, id);
                    // sumamos efectivo al cajero en la bd
                    if (updateStmt.executeUpdate() > 0){
                        CajeroDAO cdao = new CajeroDAO();
                        cdao.modificarEfectivo(idCajero, monto, "deposito");
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean retirar(int id, double monto, int idCajero) {
        String sql = "INSERT INTO movimiento (userID, monto, tipo, id_cajero) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setDouble(2, monto);
            stmt.setString(3, "Retiro");
            stmt.setInt(4, idCajero);
            if (stmt.executeUpdate() > 0) {
                sql = "UPDATE usuario SET saldo = saldo - ? WHERE id = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(sql)) {
                    updateStmt.setDouble(1, monto);
                    updateStmt.setInt(2, id);
                    if( updateStmt.executeUpdate() > 0){
                        CajeroDAO cdao = new CajeroDAO();
                        cdao.modificarEfectivo(idCajero, monto, "retiro");
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean transferir(int id, double monto, int idReceptor, int idCajero) {
        String sql = "INSERT INTO movimiento (userID, monto, tipo, receptor, id_cajero) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setDouble(2, monto);
            stmt.setString(3, "Transferencia");
            stmt.setInt(4, idReceptor);
            stmt.setInt(5, idCajero);
            if (stmt.executeUpdate() > 0) {
                sql = "UPDATE usuario SET saldo = saldo - ? WHERE id = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(sql)) {
                    updateStmt.setDouble(1, monto);
                    updateStmt.setInt(2, id);
                    if(updateStmt.executeUpdate() > 0){
                        sql = "UPDATE usuario SET saldo = saldo + ? WHERE id = ?";
                        try (PreparedStatement updateStmt2 = connection.prepareStatement(sql)) {
                            updateStmt2.setDouble(1, monto);
                            updateStmt2.setInt(2, idReceptor);
                            return updateStmt2.executeUpdate() > 0;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public double saldoUsuario(int id) {
        String sql = "SELECT saldo FROM usuario WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("saldo");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0.0;
    }


    public List<UsuarioModel> obtenerUsuarios() {
        List<UsuarioModel> usuarios = new ArrayList<>();
        String sql = "SELECT id, nombre FROM usuario";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
             ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                usuarios.add(new UsuarioModel(id, nombre));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    public static boolean isEmployed(int id) {
        String sql = "SELECT COUNT(*) AS total FROM usuario u WHERE u.id = ? AND u.isEmployed = true";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            // movemos el cursor a la primera fila del resultado
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error ejecutando consulta: " + e.getMessage(), e);
        }
        return false;
    }

}