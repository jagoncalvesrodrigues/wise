package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Sudadera;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Sudadera entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SudaderaRepository extends JpaRepository<Sudadera, Long> {}
