package com.yeopcp.admsvc.appService;


import com.yeopcp.admsvc.api.dto.CurrencyDto;
import com.yeopcp.admsvc.domain.entity.Currency;
import com.yeopcp.admsvc.domain.service.CurrencyService;
import com.yeopcp.admsvc.domain.util.ErrStack;
import com.yeopcp.admsvc.persistence.dao.CurrencyDao;
import com.yeopcp.admsvc.persistence.repository.CurrencyRepository;
import fj.*;
import fj.data.Option;
import fj.data.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CurrencyAppService {

    @Autowired
    private CurrencyRepository currencyRepository;

    protected final F<CurrencyDto, Validation<ErrStack, Currency>> convertDtoToCurrencyF = CurrencyAppService::fromDtoV;
    protected final F<Currency, Validation<ErrStack, CurrencyDto>> convertCurrencyToDtoVF = CurrencyAppService::toDtoV;
    protected final F2<Currency, CurrencyDao, CurrencyDto> saveChangesInCurrencyF = this::saveChangesInCurrency;
    protected final F2<Currency, CurrencyDao, CurrencyDao> convertCurrencyToDaoF = CurrencyAppService::toDao;
    protected final F<UUID, Option<CurrencyDao>> getCurrencyDaoF = this::getCurrencyO;
    protected final F<Option<CurrencyDao>, Validation<ErrStack, CurrencyDto>> performCurrencyDeleteF = this::performCurrencyDelete;
    protected final F<Option<CurrencyDao>, Validation<ErrStack, CurrencyDto>> validateCurrencyObtainByIdF = this::validateCurrencyObtainById;

    protected Validation<ErrStack, CurrencyDto> validateCurrencyObtainById(final Option<CurrencyDao> curDao) {
        if(curDao.isNone()) {
            return Validation.fail(ErrStack.of("Currency Id doesn't exist"));
        }
        return fromDaoV(curDao.toNull()).bind(cur -> convertCurrencyToDtoVF.f(cur));
    }

    public static Validation<ErrStack, CurrencyDto> toDtoV(final Currency currency) {
        return Validation.success(new CurrencyDto(currency.getId().toNull(), currency.getCreatedAt().toNull(), currency.getUpdatedAt().toNull(), currency.getKey(), currency.getLabel()));
    }

    protected final F<Option<CurrencyDao>, Validation<ErrStack, CurrencyDto>> deleteCurrencyF =
            (dao) -> performCurrencyDeleteF.f(dao);

    protected Validation<ErrStack, CurrencyDto> performCurrencyDelete(final Option<CurrencyDao> curDao) {
        if(curDao.isNone()) {
            return Validation.fail(ErrStack.of("Currency Id doesn't exist"));
        }
        currencyRepository.delete(curDao.toNull());
        return fromDaoV(curDao.toNull()).bind(cur -> convertCurrencyToDtoVF.f(cur));
    }

    protected Option<CurrencyDao> getCurrencyO(UUID curId) {
        return currencyRepository.findById(curId)
                .map(Option::fromNull).orElse(Option.none());
    }

    public static CurrencyDao toDao(final Currency currency, final CurrencyDao dao) {
        CurrencyDao curDao = dao;

        if(curDao == null) {
            curDao = new CurrencyDao();
        }

        curDao.setKey(currency.getKey());
        curDao.setLabel(currency.getLabel());

        return curDao;
    }

    public static Validation<ErrStack, Currency> fromDtoV(CurrencyDto dto) {
        final Validation<ErrStack, String> keyV = Validation.condition(dto.getKey() != null, ErrStack.of("Currency key is required"), dto.getKey());
        final Validation<ErrStack, String> labelV = Validation.condition(dto.getCurrencyLabel() != null, ErrStack.of("Currency label is required"), dto.getCurrencyLabel());

        ErrStack errStack = new ErrStack()
                .append(keyV)
                .append(labelV);

        if(errStack.isFail())
            return Validation.fail(errStack);

        return Validation.success(new Currency(
                Option.fromNull(dto.getId()),
                Option.fromNull(dto.getCreatedAt()),
                Option.fromNull(dto.getUpdatedAt()),
                keyV.success(),
                labelV.success()
        ));
    }

    public static fj.data.List<CurrencyDto> toDto(final fj.data.List<Currency> currencies) {
        return currencies.map(CurrencyAppService::toDto);
    }

    public static CurrencyDto toDto(final Currency currency) {
        return new CurrencyDto(currency.getId().toNull(), currency.getCreatedAt().toNull(), currency.getUpdatedAt().toNull(), currency.getKey(), currency.getLabel());
    }

    public static Validation<ErrStack, Currency> fromDaoV(final CurrencyDao currencyDao) {

        return Validation.success(new Currency(
                Option.fromNull(currencyDao.getId()),
                Option.fromNull(currencyDao.getCreateAt()),
                Option.fromNull(currencyDao.getUpdatedAt()),
                currencyDao.getKey(),
                currencyDao.getLabel()
        ));
    }

    public Validation<ErrStack, fj.data.List<CurrencyDto>> getCurrencies() {
        final Semigroup<ErrStack> semigroup = Semigroup.firstSemigroup();
        return fj.data.List.iterableList(currencyRepository.findAll())
                .traverseValidation(semigroup, CurrencyAppService::fromDaoV)
                .map(CurrencyAppService::toDto);
    }

    private CurrencyDto saveChangesInCurrency(final Currency currency, final CurrencyDao curDao) {
        currencyRepository.save(curDao);
        return CurrencyAppService.toDto(currency);
    }

    protected final F<CurrencyDto, Validation<ErrStack, CurrencyDto>> addCurrencyF =
            (currencyDto) -> convertDtoToCurrencyF.f(currencyDto).map(cur ->
                    saveChangesInCurrencyF.f(cur, convertCurrencyToDaoF.f(cur, null)));

    public Validation<ErrStack, CurrencyDto> addCurrency(final CurrencyDto currencyDto) {
        return addCurrencyF.f(currencyDto);
    }

    protected final F2<CurrencyDto, Currency, Validation<ErrStack, Currency>> updateCurrencyDmF = CurrencyService::updateCurrencyV;

    protected Validation<ErrStack, Currency> getCurrencyV(Option<CurrencyDao> currencyDao0) {
        return currencyDao0.toValidation(new ErrStack("currency not found"))
                .bind(currencyDao -> fromDaoV(currencyDao));
    }

    protected final F2<Option<CurrencyDao>, CurrencyDto, Validation<ErrStack, CurrencyDto>> updateCurrencyF =
            (curDao0, curDto) -> getCurrencyV(curDao0)
                        .bind(cur ->
                                fromDtoV(curDto).bind(newCur ->
                                        updateCurrencyDmF.f(curDto, cur)).map(updatedCur ->
                                        saveChangesInCurrencyF.f(updatedCur, toDao(updatedCur, curDao0.some()))));

    public Validation<ErrStack, CurrencyDto> updateCurrencyV(final CurrencyDto newcurDto, final UUID curId) {
        return updateCurrencyF.f(getCurrencyDaoF.f(curId), newcurDto);
    }

    public Validation<ErrStack, CurrencyDto> deleteCurrencyByIdV(final CurrencyDto curDto) {
        return deleteCurrencyF.f(getCurrencyDaoF.f(curDto.getId()));
    }

    public Validation<ErrStack, CurrencyDto> getCurrencyByIdV(final UUID curId) {
        return validateCurrencyObtainByIdF.f(getCurrencyDaoF.f(curId));
    }

}
