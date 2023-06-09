package com.pragma.plazoletamicroservice.configuration;

import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.adapter.CategoriaMysqlAdapter;
import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.adapter.EmplRestMysqlAdapter;
import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.adapter.PedidoDetallesMysqlAdapter;
import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.adapter.PedidoMysqlAdapter;
import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.adapter.PlatoMysqlAdapter;
import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.adapter.RestauranteMysqlAdapter;
import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.mapper.ICategoriaEntityMapper;
import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.mapper.IPedidoEntityMapper;
import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.mapper.IPedidoDetallesEntityMapper;
import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.mapper.IPlatoEntityMapper;
import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.mapper.IRestauranteEntityMapper;
import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.repository.ICategoriaRepository;
import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.repository.IEmplRestRepository;
import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.repository.IPedidoDetallesRepository;
import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.repository.IPedidoRepository;
import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.repository.IPlatoRepository;
import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.repository.IRestauranteRepository;
import com.pragma.plazoletamicroservice.adapters.driving.feign.client.UsuarioFeignClient;
import com.pragma.plazoletamicroservice.adapters.driving.feign.client.UsuarioFeignHandlerImp;
import com.pragma.plazoletamicroservice.adapters.driving.feign.mensajeria.MensajeriaFeignClient;
import com.pragma.plazoletamicroservice.adapters.driving.feign.mensajeria.MensajeriaFiegnHandlerImpl;
import com.pragma.plazoletamicroservice.adapters.driving.feign.trazabilidad.TrazabilidadFeignClient;
import com.pragma.plazoletamicroservice.adapters.driving.feign.trazabilidad.TrazabilidadFeignHandlerImpl;
import com.pragma.plazoletamicroservice.domain.api.IMensajeriaServicePort;
import com.pragma.plazoletamicroservice.domain.api.IPedidoServicePort;
import com.pragma.plazoletamicroservice.domain.api.IPlatoServicePort;
import com.pragma.plazoletamicroservice.domain.api.IRestauranteServicePort;
import com.pragma.plazoletamicroservice.domain.api.IFeignServicePort;
import com.pragma.plazoletamicroservice.domain.api.ITrazabilidadServicePort;
import com.pragma.plazoletamicroservice.domain.spi.ICategoriaPersistencePort;
import com.pragma.plazoletamicroservice.domain.spi.IEmplRestPersistencePort;
import com.pragma.plazoletamicroservice.domain.spi.IPedidoDetallesPersistencePort;
import com.pragma.plazoletamicroservice.domain.spi.IPedidoPersistencePort;
import com.pragma.plazoletamicroservice.domain.spi.IPlatoPersistencePort;
import com.pragma.plazoletamicroservice.domain.spi.IRestaurantePersistencePort;
import com.pragma.plazoletamicroservice.domain.usecase.PedidoUseCase;
import com.pragma.plazoletamicroservice.domain.usecase.PlatoUseCase;
import com.pragma.plazoletamicroservice.domain.usecase.RestauranteUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IRestauranteRepository restauranteRepository;
    private final IRestauranteEntityMapper restauranteEntityMapper;
    private final IPlatoRepository platoRepository;
    private final IPlatoEntityMapper platoEntityMapper;
    private final UsuarioFeignClient usuarioFeignClient;
    private final ICategoriaRepository categoriaRepository;
    private final ICategoriaEntityMapper categoriaEntityMapper;
    private final IPedidoRepository pedidoRepository;
    private final IPedidoEntityMapper pedidoEntityMapper;
    private final IPedidoDetallesRepository pedidoPlatoRepository;
    private final IPedidoDetallesEntityMapper pedidoPlatoEntityMapper;
    private final IEmplRestRepository emplRestRepository;
    private final MensajeriaFeignClient mensajeriaFeignClient;
    private final TrazabilidadFeignClient trazabilidadFeignClient;

    @Bean
    public IRestauranteServicePort restauranteServicePort(){
        return new RestauranteUseCase(restaurantePersistencePort(),emplRestPersistencePort(), feignServicePort());
    }
    @Bean
    public IFeignServicePort feignServicePort(){
        return new UsuarioFeignHandlerImp(usuarioFeignClient);
    }
    @Bean
    public IRestaurantePersistencePort restaurantePersistencePort(){
        return new RestauranteMysqlAdapter(restauranteRepository,restauranteEntityMapper);
    }
    @Bean
    public IEmplRestPersistencePort emplRestPersistencePort(){
        return new EmplRestMysqlAdapter(emplRestRepository);
    }
    @Bean
    public IPlatoServicePort platoServicePort(){
        return new PlatoUseCase(platoPersistencePort(),restaurantePersistencePort(),categoriaPersistencePort(),feignServicePort());
    }
    @Bean
    public IPlatoPersistencePort platoPersistencePort(){
        return new PlatoMysqlAdapter(platoRepository,platoEntityMapper);
    }
    @Bean
    public ICategoriaPersistencePort categoriaPersistencePort(){
        return new CategoriaMysqlAdapter(categoriaRepository,categoriaEntityMapper);
    }
    @Bean
    public IPedidoServicePort pedidoServicePort(){
        return new PedidoUseCase(pedidoPersistencePort(),restaurantePersistencePort(), platoPersistencePort(), pedidoDetallesPersistencePort(), emplRestPersistencePort(), feignServicePort(), mensajeriaServicePort(), trazabilidadServicePort());
    }
    @Bean
    public IPedidoPersistencePort pedidoPersistencePort(){
        return new PedidoMysqlAdapter(pedidoRepository,pedidoEntityMapper);
    }
    @Bean
    IPedidoDetallesPersistencePort pedidoDetallesPersistencePort(){
        return new PedidoDetallesMysqlAdapter(pedidoPlatoRepository,pedidoPlatoEntityMapper);
    }
    @Bean
    public IMensajeriaServicePort mensajeriaServicePort(){
        return new MensajeriaFiegnHandlerImpl(mensajeriaFeignClient);
    }
    @Bean
    public ITrazabilidadServicePort trazabilidadServicePort(){
        return new TrazabilidadFeignHandlerImpl(trazabilidadFeignClient);
    }
}
