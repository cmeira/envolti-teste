package com.ms.order.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms.order.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
