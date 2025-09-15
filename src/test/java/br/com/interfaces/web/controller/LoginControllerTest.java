package br.com.interfaces.web.controller;

import br.com.domain.enums.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest {

    private LoginController controller;
    private Model model;

    @BeforeEach
    void setUp() {
        controller = new LoginController();
        model = new ConcurrentModel();
    }

    @Test
    void loginPageShouldReturnLoginViewWithDefaultRole() {
        String view = controller.loginPage("CLIENT", null, model);

        assertEquals("login/login", view);
        assertEquals(RoleEnum.CLIENT.name(), model.getAttribute("role"));
        assertNull(model.getAttribute("error"));
    }

    @Test
    void loginPageShouldReturnLoginViewWithCustomRoleAndError() {
        String view = controller.loginPage("ADMIN", "Login de administrador inválido", model);

        assertEquals("login/login", view);
        assertEquals(RoleEnum.ADMIN.name(), model.getAttribute("role"));
        assertEquals("Login de administrador inválido", model.getAttribute("error"));
    }

    @Test
    void loginPageShouldThrowInvalidRoleExceptionForInvalidRole() {
        Exception ex = assertThrows(RuntimeException.class, () -> {
            controller.loginPage("INVALID_ROLE", null, model);
        });

        assertTrue(ex.getMessage().contains("Role inválida"));
    }
}
