package com.yeopcp.admsvc.domain.entity;

import fj.data.Option;
import fj.data.Validation;
import lombok.Value;
import lombok.With;

import java.util.Date;
import java.util.UUID;

@Value
public class Currency {

    @With
    Option<UUID> id;

    Option<Date> createdAt;
    Option<Date> updatedAt;

    @With
    String key;
    @With
    String label;

    public <E> Validation<E, Currency> V() { return Validation.success(this); }

}
