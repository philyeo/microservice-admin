package com.yeopcp.admsvc.domain.entity;


import fj.data.Option;
import fj.data.Validation;
import lombok.Value;
import lombok.With;

import java.util.Date;
import java.util.UUID;

@Value
public class Location {

    @With
    Option<UUID> ID;

    Option<Date> createdAt;
    Option<Date> updatedAt;

    @With
    String key;
    @With
    String label;
    @With
    String countryCode;
    @With
    Boolean teamLocation;

    public <E> Validation<E, Location> V() { return Validation.success(this); }


}
