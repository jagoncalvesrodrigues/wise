package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Camiseta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Camiseta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CamisetaRepository extends JpaRepository<Camiseta, Long> {}
