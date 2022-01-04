package com.yeopcp.admsvc.admin;

import com.yeopcp.admsvc.admin.persistence.dao.CurrencyDao;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyDaoRepository extends JpaRepository<CurrencyDao, UUID> {

}