package com.yeopcp.admsvc.admin.persistence.repository;

import com.yeopcp.admsvc.admin.persistence.dao.LocationDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<LocationDao, UUID> {

    LocationDao findByLabel(String key);

    void deleteByKey(String key);
}