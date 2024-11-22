package Controllers;

import Data.UsuarioDAO;
import Models.UsuarioModel;

import java.sql.SQLException;

public class UserController {
private static UsuarioDAO user;
    public UserController() {
        user = new UsuarioDAO();
    }

    public static boolean login(String name, String pass) {
        return user.login(name,pass);
    }

    public static boolean register(String name, String pass) throws SQLException {
        return user.register(name, pass);
    }

    public void depositar(int id, double monto, int idCajero) {
        if (monto > 0){
            if (user.depositar(id, monto, idCajero)){
                UsuarioModel.getInstance().changeSaldo(monto,"add");
                System.out.println("Se depositaron: " + monto);
            }
        }else {
            System.out.println("Monto mayor a 0 obligatoriamente");
        }
    }

    public boolean retirar(int id, double monto, int idCajero) {
        if (monto > 0){
            if (UsuarioModel.getInstance().getSaldo() > monto) {
                if (user.retirar(id, monto, idCajero)) {
                    UsuarioModel.getInstance().changeSaldo(monto,"remove");
                    return true;
                } else {
                    System.out.println("Error al retirar");
                }
            } else {
                System.out.println("No puede retirar mas de " + UsuarioModel.getInstance().getSaldo() + " (saldo actual)");
            }
        } else {
            System.out.println("Monto mayor a 0 obligatoriamente");
        }
        return false;
    }

    public void transferir(int id, double monto, int idReceptor, int idCajero) {
        if (monto > 0){
            if (UsuarioModel.getInstance().getSaldo() > monto) {
                if (user.transferir(id, monto, idReceptor, idCajero)) {
                    UsuarioModel.getInstance().changeSaldo(monto,"remove");
                } else {
                    System.out.println("Error al realizar la transferencia");
                }
            }else {
                System.out.println("No puede transferir mas de " + UsuarioModel.getInstance().getSaldo() + " (saldo actual)");
            }
        }else {
            System.out.println("Monto mayor a 0 obligatoriamente");
        }
    }

    public double obtenerSaldo(int id) {
        double saldo = user.saldoUsuario(id);
        UsuarioModel.getInstance().inicializarUsuario(UsuarioModel.getInstance().getNombre(), id, saldo);
        return saldo;
    }
}
