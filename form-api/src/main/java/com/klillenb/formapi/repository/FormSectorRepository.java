package com.klillenb.formapi.repository;

import com.klillenb.formapi.model.FormSector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FormSectorRepository extends JpaRepository<FormSector, Long> {

    @Query("select fs.sector.id from FormSector fs where fs.form.id = :formId")
    List<Long> findSectorIdsByFormId(Long formId);

    void deleteByFormId(Long formId);
}
