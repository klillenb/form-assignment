package com.klillenb.formapi.repository;

import com.klillenb.formapi.model.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FormRepository extends JpaRepository<Form, Long> {

    Optional<Form> findFirst();
}
