package com.yeopcp.admsvc.domain.service;

import com.yeopcp.admsvc.api.dto.CurrencyDto;
import com.yeopcp.admsvc.domain.entity.Currency;
import com.yeopcp.admsvc.domain.util.ErrStack;
import fj.data.Validation;
import fj.data.List;

public class CurrencyService {

    public static Validation<ErrStack, List<Currency>> getRealCurrencies(List<Currency> currencies) {
        return Validation.success(currencies.filter(currency -> currency.getKey() != null));
    }

    public static Validation<ErrStack, Currency> updateCurrencyV(CurrencyDto currencyDto, Currency currency) {
        return Validation.success(currency.withKey(currencyDto.getKey())
                .withLabel(currencyDto.getCurrencyLabel())
        );
    }

}
