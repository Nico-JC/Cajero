package test;
import Controllers.UserController;
import Data.UsuarioDAO;
import Models.UsuarioModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioControllerTest {

    @Test
    @DisplayName("Prueba de registro")
    void testRegisterSucces() throws SQLException {
        UserController uc = new UserController();
        Assertions.assertEquals(true, uc.register("test", "uno"));
    }

    @Test
    @DisplayName("Error de registro")
    // nombre de usuario ya en uso
    void testRegisterFail() throws SQLException {
        UserController uc = new UserController();
        Assertions.assertEquals(true, uc.register("messi", "1010"));
    }

    @Test
    @DisplayName("Prueba de logueo")
    void testLoginSuccess() {
        UserController uc = new UserController();
        Assertions.assertEquals(true,uc.login("prueba","test"));
    }

    @Test
    @DisplayName("Error de logueo")
    void testLoginFail() {
        // error, el usuario no existe en ls bd
        UserController uc = new UserController();
        Assertions.assertEquals(false,uc.login("asd","123"));
    }
}