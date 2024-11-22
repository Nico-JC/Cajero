package Models;
public class TransaccionModel {
    private int id;
    private int userID;
    private double monto;
    private String tipo;
    private Integer cajeroID;
    private String nombreOrigen;
    private String nombreReceptor;

    // Constructor
    public TransaccionModel(int id, int userID, double monto, String tipo, int id_cajero, String nombreOrigen, String nombreReceptor) {
        this.id = id;
        this.userID = userID;
        this.monto = monto;
        this.tipo = tipo;
        this.cajeroID = id_cajero;
        this.nombreOrigen = nombreOrigen;
        this.nombreReceptor = nombreReceptor;
    }

    public int getId() {
        return id;
    }

    public int getUserID() {
        return userID;
    }

    public double getMonto() {
        return monto;
    }

    public String getTipo() {
        return tipo;
    }

    public Integer getCajeroID() {
        return cajeroID;
    }

    public String getNombreOrigen() {
        return nombreOrigen;
    }

    public String getNombreReceptor() {
        return nombreReceptor;
    }
}