package com.ms.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ms.order.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByStatus(String status);
}
