package com.ejemplo.inventario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record ProductoResponseDTO(

        @Schema(example = "1")
        Long id,

        @Schema(example = "Teclado Mecánico RGB")
        String nombre,

        @Schema(example = "1250.50")
        BigDecimal precio,

        @Schema(example = "15")
        Integer stock
) {}