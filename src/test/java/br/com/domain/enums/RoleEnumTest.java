package br.com.domain.enums;

import br.com.application.exception.InvalidRoleException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleEnumTest {

    @Test
    void fromShouldReturnAdminForAdminString() {
        RoleEnum result = RoleEnum.from("admin");
        assertEquals(RoleEnum.ADMIN, result);
    }

    @Test
    void fromShouldReturnClientForClientString() {
        RoleEnum result = RoleEnum.from("CLIENT");
        assertEquals(RoleEnum.CLIENT, result);
    }

    @Test
    void fromShouldBeCaseInsensitive() {
        assertEquals(RoleEnum.ADMIN, RoleEnum.from("AdMiN"));
        assertEquals(RoleEnum.CLIENT, RoleEnum.from("cLiEnT"));
    }

    @Test
    void fromShouldThrowExceptionForInvalidRole() {
        InvalidRoleException ex = assertThrows(InvalidRoleException.class,
                () -> RoleEnum.from("invalidRole"));
        assertTrue(ex.getMessage().contains("Role inválida"));
    }

    @Test
    void fromShouldThrowExceptionForNull() {
        InvalidRoleException ex = assertThrows(InvalidRoleException.class,
                () -> RoleEnum.from(null));
        assertTrue(ex.getMessage().contains("Role inválida"));
    }
}
