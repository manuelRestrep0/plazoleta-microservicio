package com.pragma.plazoletamicroservice.domain.spi;

import com.pragma.plazoletamicroservice.domain.model.Restaurante;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IRestaurantePersistencePort {

    void crearRestaurante(Restaurante restaurante);

    Optional<Restaurante> obtenerRestaurante(Long id);

    List<Page<Restaurante>> obtenerRestaurantes(int elementos);
    Boolean validarExistenciaRestaurante(String nit);
}
