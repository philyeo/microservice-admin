package com.yeopcp.admsvc.admin.persistence.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Entity
@Table(name="LOCATIONS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
public class LocationDao extends AuditModelDao {

    @Column(name="KEY")
    @NotNull
    @Getter
    @Setter
    private String key;

    @Column(name="LABEL")
    @NotNull
    @Getter
    @Setter
    private String label;

    @Column(name="COUNTRY_CODE")
    @NotNull
    @Getter
    @Setter
    private String countryCode;

    @Column(name="IS_TEAM_LOCATION")
    @Getter
    @Setter
    private Boolean teamLocation;

    @Transient
    @JsonIgnore
    private SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");


    public LocationDao(String key, String locationLabel, String countryCode, Boolean teamLocation) throws ParseException {
//        this.id = id;
//        this.setCreateAt(format.parse(createdDate));
//        this.setUpdatedAt(format.parse(updatedDate));
        this.key = key;
        this.label = locationLabel;
        this.countryCode = countryCode;
        this.teamLocation = teamLocation;
    }

}
