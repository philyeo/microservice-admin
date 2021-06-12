package com.yeopcp.admsvc.api.dto;

import com.yeopcp.admsvc.domain.entity.Location;
import javafx.beans.binding.BooleanExpression;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

public class LocationDto {

    @Getter
    final public UUID id;

    @Getter
    @Setter
    final public Date createdAt;

    @Getter
    @Setter
    final public Date updatedAt;

    @Getter
    @Setter
    final public String key;

    @Getter
    @Setter
    final public String locationLabel;

    @Getter
    @Setter
    final public String countryCode;

    @Getter
    @Setter
    final public Boolean isTeamLocation;

    public LocationDto(UUID id, Date createdAt, Date updatedAt, String key, String locationLabel, String countryCode, Boolean isTeamLocation) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.key = key;
        this.locationLabel = locationLabel;
        this.countryCode = countryCode;
        this.isTeamLocation = isTeamLocation;
    }

}
