package com.ejemplo.inventario.controller;

import com.ejemplo.inventario.dto.ErrorResponseDTO;
import com.ejemplo.inventario.dto.ProductoRequestDTO;
import com.ejemplo.inventario.dto.ProductoResponseDTO;
import com.ejemplo.inventario.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Operaciones CRUD para la gestión del catálogo de productos en inventario")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    //ENDPOINT LISTAR TODOS LOS PRODUCTOS
    @Operation(
            summary = "Listar todos los productos",
            description = "Obtiene la lista completa de productos registrados en PostgreSQL. Devuelve un arreglo vacío si no hay registros."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de productos recuperada correctamente",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listProducts() {
        return ResponseEntity.ok(productoService.findAll());
    }

    //OBTENER LA INFORMACION DE UN PRODUCTO POR SU id
    @Operation(
            summary = "Obtener producto por ID",
            description = "Busca y devuelve el detalle de un producto específico a partir de su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto encontrado",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe un producto con el ID proporcionado",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerProductoPorId(@PathVariable Long id)
    {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    //ENDPOINT PARA CREAR UN PRODUCTO Y GUARDARLO EN LA BASE DE DATOS
    @Operation(
            summary = "Registrar un nuevo producto",
            description = "Valida los datos de entrada y guarda un nuevo producto en la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Producto creado exitosamente",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos (campos vacíos, precio negativo o JSON mal formado)",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crearProducto(
            @Valid @RequestBody ProductoRequestDTO producto) {
        ProductoResponseDTO nuevoProducto = productoService.saveProducto(producto);

        //Que vamos a retornar
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);

    }

    //ENDPOINT PARA ACTUALIZAR UN PRODUCTO EXISTENTE EN LA BD
    @Operation(
            summary = "Actualizar un producto existente",
            description = "Reemplaza el nombre, precio y stock de un producto existente identificado por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto actualizado exitosamente",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos en el cuerpo de la petición",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró el producto a actualizar con el ID indicado",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO requestDTO
    ){
        return ResponseEntity.ok(productoService.updateProducto(id, requestDTO));
    }

    //ELIMINAR PRODUCTO DE LA BASE DE DATOS
    @Operation(
            summary = "Eliminar un producto",
            description = "Elimina permanentemente un producto de la base de datos a partir de su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Producto eliminado correctamente (sin contenido en el cuerpo)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró el producto a eliminar con el ID indicado",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id){
        productoService.deleteProducto(id);

        return  ResponseEntity.noContent().build();
    }
}
