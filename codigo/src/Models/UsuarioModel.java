package Models;

import Data.UsuarioDAO;

public class UsuarioModel {
    private static UsuarioModel instance;
    private int id;
    private String nombre;
    private double saldo;


    private UsuarioModel() {
        this.saldo = 0.0;
    }

    public UsuarioModel(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public static UsuarioModel getInstance() {
        if (instance == null) {
            instance = new UsuarioModel();
        }
        return instance;
    }

    public void inicializarUsuario(String nombre, int id, double saldo) {
        this.nombre = nombre;
        this.id = id;
        this.saldo = saldo;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getSaldo() {
        return saldo;
    }

    public void changeSaldo(double saldo, String status) {
        switch (status){
            case "add":
                this.saldo += saldo;
                break;
            case "remove":
                this.saldo -= saldo;
                break;
            default:
                break;
        }
    }

    public boolean isEmployed(int id){
        return UsuarioDAO.isEmployed(id);
    }

}