package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Accesorio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Accesorio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccesorioRepository extends JpaRepository<Accesorio, Long> {}
