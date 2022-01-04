package com.yeopcp.admsvc.admin.domain.service;

import com.yeopcp.admsvc.admin.api.dto.CurrencyDto;
import com.yeopcp.admsvc.admin.domain.entity.Currency;
import com.yeopcp.admsvc.admin.domain.util.ErrStack;
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
