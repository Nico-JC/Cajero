package Models;

import java.sql.SQLException;

public class CajeroModel {
    private static int id_cajero;
    private int efectivo_disponible;

    public CajeroModel(int id, int dinero) throws SQLException {
        this.id_cajero = id;
        this.efectivo_disponible = dinero;
    }

    public static int getId_cajero() {
        return id_cajero;
    }

    public int getEfectivo_disponible() {
        return efectivo_disponible;
    }
}
