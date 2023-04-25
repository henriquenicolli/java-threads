package com.example.threads.repository;

import com.example.threads.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("select p from Pedido p where p.id = :id")
    Optional<Pedido> findById(Long id);

}
