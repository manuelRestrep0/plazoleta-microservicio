package com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.adapter;

import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.entity.PedidoEntity;
import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.entity.PedidoPlatoEntity;
import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.mapper.IPedidoEntityMapper;
import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.mapper.IPedidoPlatoEntityMapper;
import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.repository.IPedidoPlatoRepository;
import com.pragma.plazoletamicroservice.adapters.driven.jpa.mysql.repository.IPedidoRepository;
import com.pragma.plazoletamicroservice.domain.model.Pedido;
import com.pragma.plazoletamicroservice.domain.model.PedidoPlato;
import com.pragma.plazoletamicroservice.domain.spi.IPedidoPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PedidoMysqlAdapter implements IPedidoPersistencePort {

    private final IPedidoRepository pedidoRepository;
    private final IPedidoEntityMapper pedidoEntityMapper;
    private final IPedidoPlatoRepository pedidoPlatoRepository;
    private final IPedidoPlatoEntityMapper pedidoPlatoEntityMapper;

    @Override
    public void guardarPedido(Pedido pedido, List<PedidoPlato> platosPedidos) {
        PedidoEntity pedidoEntity = pedidoEntityMapper.toEntity(pedido);
        pedidoRepository.save(pedidoEntity);
        List<PedidoPlatoEntity> platosEntity = new ArrayList<>();
        platosPedidos.forEach(pedidoPlato -> platosEntity.add(pedidoPlatoEntityMapper.toEntity(pedidoPlato)));
        platosEntity.forEach(pedidoPlatoEntity -> pedidoPlatoEntity.setIdPedido(pedidoRepository.findById(pedidoEntity.getId()).get()));
        pedidoPlatoRepository.saveAll(platosEntity);
    }

    @Override
    public Boolean verificarPedidoCliente(Long idCliente) {
        return pedidoRepository.existsByIdCliente(idCliente);
    }

    @Override
    public List<Page<Pedido>> obtenerPedidos(Long id, String estado, int elementos) {
        List<Page<Pedido>> paginas = new ArrayList<>();
        int numeroPagina = 0;
        Page<Pedido> pagina;
        do{
            Pageable pageable = PageRequest.of(numeroPagina,elementos);
            pagina = pedidoRepository.findAllByIdRestaurante_IdAndAndEstado(id,estado,pageable).map(pedidoEntityMapper::toPedido);
            paginas.add(pagina);
            numeroPagina++;
        } while (pagina.hasNext());
        return paginas;
    }
}