package br.com.domain.enums;

import br.com.application.exception.InvalidRoleException;

import java.util.Arrays;
import java.util.Optional;

public enum RoleEnum {
    ADMIN, CLIENT;

    public static RoleEnum from(String value) {
        return Optional.ofNullable(value)
                .map(String::toUpperCase)
                .flatMap(v -> Arrays.stream(values())
                        .filter(r -> r.name().equals(v))
                        .findFirst())
                .orElseThrow(() -> new InvalidRoleException("Role inválida: " + value));
    }
}
