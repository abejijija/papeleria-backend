package com.ejemplo.inventario.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Estructura estándar de respuesta de error para toda la API.
 * Los campos nulos no se serializan, por lo que cada handler
 * devuelve solo lo que aplica.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String mensaje,
        String path,
        Map<String, String> detalles
) {
}