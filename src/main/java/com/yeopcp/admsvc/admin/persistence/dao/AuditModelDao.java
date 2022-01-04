package com.yeopcp.admsvc.admin.persistence.dao;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
      value = {"createdAt", "updatedAt", "version"},
      allowGetters = true
)
public abstract class AuditModelDao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    public UUID id;

    @Version
    private int version;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    @LastModifiedDate
    @Getter
    @Setter
    private Date createAt;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    @LastModifiedDate
    @Getter
    @Setter
    private Date updatedAt;

}