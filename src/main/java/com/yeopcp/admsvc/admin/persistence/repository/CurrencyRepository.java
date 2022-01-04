package com.yeopcp.admsvc.admin.persistence.repository;

import com.yeopcp.admsvc.admin.persistence.dao.CurrencyDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyDao, UUID> {

    CurrencyDao findByLabel(String key);

    void deleteByKey(String key);

}
