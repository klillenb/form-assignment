package com.klillenb.formapi.repository;

import com.klillenb.formapi.model.Form;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FormRepository extends JpaRepository<Form, Long> {

    Optional<Form> findFirstByOrderByIdAsc();
}
