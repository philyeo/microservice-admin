package com.yeopcp.admsvc.persistence.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Entity
@Table(name = "CURRENCY")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
public class CurrencyDao extends AuditModelDao {

    @Column(name = "KEY")
    @NotNull
    @Getter
    @Setter
    private String key;

    @Column(name = "LABEL")
    @NotNull
    @Getter
    @Setter
    private String label;

    @Transient
    @JsonIgnore
    private SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

    public CurrencyDao(String key, String currencyLabel) throws ParseException {
        this.key = key;
        this.label = currencyLabel;
    }

}
