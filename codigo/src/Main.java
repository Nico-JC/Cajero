import Controllers.UserController;
import Data.CajeroDAO;
import Data.TransaccionDAO;
import DataBase.DB;
import Models.CajeroModel;
import Models.TransaccionModel;
import Models.UsuarioModel;
import Data.UsuarioDAO;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static UsuarioDAO usuarioDAO;
    static Scanner sc = new Scanner(System.in);
    boolean seguir = true;
    static UserController uc = new UserController();
    static CajeroDAO cajeroDAO = new CajeroDAO();



    public static void main(String[] args) {

        try{
            DB con = new DB();
            con.getConnection();

            usuarioDAO = new UsuarioDAO();

            boolean inicio = true;
            do {
                System.out.println("\n1. Registrarse");
                System.out.println("2. Iniciar Sesión");
                System.out.println("3. Salir");
                System.out.print("Seleccione una opción: ");

                int opcion = sc.nextInt();
                sc.nextLine();

                String nombre = "" ;
                String contraseña = "";

                switch (opcion) {
                    case 1:
                        boolean registroExitoso = false;
                        while (!registroExitoso) {
                            System.out.print("Ingrese nombre de usuario: ");
                            nombre = sc.nextLine();

                            // Verificar si el usuario ya existe
                            if (usuarioDAO.existeUsuario(nombre)) {
                                System.out.println("El nombre de usuario ya está en uso. Por favor, elija otro.");
                                continue;
                            }

                            System.out.print("Ingrese contraseña: ");
                            contraseña = sc.nextLine();

                            // registrar contraseña en la bd
                            if (uc.register(nombre, contraseña)) {
                                System.out.println("Registro exitoso, ya puede loguearse");
                                registroExitoso = true; // salir del bulce
                            } else {
                                System.out.println("Error al registrarse, intente más tarde");
                                System.out.print("¿Desea intentar de nuevo? (s/n): ");
                                String respuesta = sc.nextLine();
                                if (!respuesta.toLowerCase().equals("s")) {
                                    break;
                                }
                            }
                        }
                        break;
                    case 2:
                        System.out.print("Ingrese nombre de usuario: ");
                        nombre = sc.nextLine();
                        System.out.print("Ingrese contraseña: ");
                        contraseña = sc.nextLine();
                        // comparo las credenciales en la bd y si coinciden ingresa al sistema
                        if (uc.login(nombre, contraseña)){
                            System.out.println("Bienvendo");
                            // en caso de ingresar al sistema mostramos las opciones
                            menu2();
                        }else {
                            System.out.println("Credenciales invalidas ");
                        }
                        break;
                    case 3:
                        System.out.println("¡Hasta luego!");
                        return;
                    default:
                        System.out.println("Opción no válida");
                }
            }while (inicio == true);
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }
    private static void menu2() throws SQLException {
        boolean continuar = true;
        TransaccionDAO transaccionDAO = new TransaccionDAO(new DB().getConnection());
        do {
            // si es empleado
            if (UsuarioModel.getInstance().isEmployed(UsuarioModel.getInstance().getId())){
                System.out.println("1. Ver transacciones\n2. Salir");
                int opcion = sc.nextInt();

                switch (opcion) {
                    case 1:
                        List<TransaccionModel> transacciones = transaccionDAO.obtenerTodasLasTransacciones();
                        System.out.println("\n=== TODAS LAS TRANSACCIONES ===");
                        System.out.println("ID | Usuario | Tipo | Monto | Cajero | Receptor");
                        System.out.println("----------------------------------------");

                        for (TransaccionModel transaction : transacciones) {
                            System.out.printf("%d | %s | %s | $%.2f | %d | %s%n",
                                    transaction.getId(),
                                    transaction.getNombreOrigen(),
                                    transaction.getTipo(),
                                    transaction.getMonto(),
                                    transaction.getCajeroID(),
                                    transaction.getNombreReceptor() != null ? transaction.getNombreReceptor() : "N/A"
                            );
                        }
                        break;
                    case 2:
                        System.out.println("Sesión cerrada.");
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            }else {

                System.out.println("1. Ver saldo\n2. Depositar\n3. Retirar\n4. Transferir\n5. Ver Movimientos\n6. Cerrar sesión");
                int opcion = sc.nextInt();

                switch (opcion) {
                    case 1:
                        // obtenemos el saldo desde la instancia del usuario
                        System.out.println("Saldo: " + UsuarioModel.getInstance().getSaldo());
                        break;
                    case 2:
                        System.out.print("Monto a depositar: ");
                        double deposito = sc.nextDouble();
                        // hacemos un deposito en la cuenta del usuario
                        uc.depositar(UsuarioModel.getInstance().getId(), deposito, 1);
                        // sumamos el efectivo al cajero
                        cajeroDAO.modificarEfectivo(CajeroModel.getId_cajero(), deposito, "deposito");

                        break;
                    case 3:
                        System.out.print("Monto a retirar: ");
                        double retiro = sc.nextDouble();
                        // Verificar que el cajero tenga efectivo
                        double efectivoDisponible = cajeroDAO.obtenerEfectivoDisponible(1);
                        System.out.println("Efectivo disponible antes del retiro: " + efectivoDisponible);

                        if (efectivoDisponible >= retiro) {
                            // Retiramos el monto del saldo del usuario
                            if (uc.retirar(UsuarioModel.getInstance().getId(), retiro, 1)) {
                                // Restamos el efectivo del saldo del cajero
                                cajeroDAO.modificarEfectivo(1, retiro, "retiro");

                                // Verificamos el nuevo saldo
                                System.out.println("Efectivo disponible después del retiro: " +
                                        cajeroDAO.obtenerEfectivoDisponible(1));
                                System.out.println("Retiro exitoso");
                            } else {
                                System.out.println("No se pudo realizar el retiro");
                            }
                        } else {
                            System.out.println("El cajero no tiene suficiente efectivo disponible");
                        }
                        break;
                    case 4:
                        UsuarioDAO dao = new UsuarioDAO();
                        List<UsuarioModel> listaUsuarios = dao.obtenerUsuarios();

                        for (UsuarioModel usuario : listaUsuarios) {
                            System.out.println("ID: " + usuario.getId() + ", Nombre: " + usuario.getNombre());
                        }
                        System.out.print("Seleccione el ID del destinatario: ");
                        sc.nextLine();
                        int idReceptor = sc.nextInt();
                        if (usuarioDAO.existeUsuarioPorId(idReceptor)) {
                            System.out.print("Monto a transferir: ");
                            double transferencia = sc.nextDouble();
                            uc.transferir(UsuarioModel.getInstance().getId(), transferencia, idReceptor, 1);
                        }
                        break;
                    case 5:
                        // ver movimientos del usuario
                        List<TransaccionModel> misMovimientos = transaccionDAO.obtenerMovimientosUsuario(UsuarioModel.getInstance().getId());
                        System.out.println("\n=== MIS MOVIMIENTOS ===");
                        System.out.println("Tipo | Monto | Detalles");
                        System.out.println("-------------------------");

                        for (TransaccionModel mov : misMovimientos) {
                            String detalles = "";
                            if (mov.getTipo().toLowerCase().contains("transferencia")) {
                                // si el userID es el mismo que el usuario actual, es una transferencia enviada
                                if (mov.getUserID() == UsuarioModel.getInstance().getId()) {
                                    detalles = "Enviado a: " +
                                            (mov.getNombreReceptor() != null ? mov.getNombreReceptor() : "N/A");
                                }
                                // si el userID es diferente, es una transferencia recibida
                                else {
                                    detalles = "Recibido de: " +
                                            (mov.getNombreOrigen() != null ? mov.getNombreOrigen() : "N/A");
                                }
                            }

                            System.out.printf("%s | $%.2f | %s%n",
                                    mov.getTipo(),
                                    mov.getMonto(),
                                    detalles
                            );
                        }
                        break;
                    case 6:
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            }
        } while(continuar);
    }
}
