package com.clientesapi.repository;

import com.clientesapi.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByCpf(String cpf);
    boolean existsByIdNotAndCpf(Long id, String cpf);

}
