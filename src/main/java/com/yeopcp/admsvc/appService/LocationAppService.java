package com.yeopcp.admsvc.appService;

import com.yeopcp.admsvc.api.dto.LocationDto;
import com.yeopcp.admsvc.domain.entity.Location;
import com.yeopcp.admsvc.domain.service.LocationService;
import com.yeopcp.admsvc.persistence.dao.LocationDao;
import com.yeopcp.admsvc.persistence.repository.LocationRepository;
import com.yeopcp.admsvc.domain.util.ErrStack;
import fj.*;
import fj.Semigroup;
import fj.data.Option;
import fj.data.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class LocationAppService {

    private final LocationRepository locationRepository;

    protected final F<UUID, Option<LocationDao>> getLocationDaoF = this::getLocation0;
    protected final F<Location, Validation<ErrStack, LocationDto>> convertLocationToDtoVF = LocationAppService::toDtoV;
    protected final F<LocationDto, Validation<ErrStack, Location>> convertDtoToLocationF = LocationAppService::fromDtoV;
    protected final F2<Location, LocationDao, LocationDao> convertLocationToDaoF = LocationAppService::toDao;
    protected final F2<Location, LocationDao, LocationDto> saveChangesInLocationF = this::saveChangesInLocation;
    protected final F<Option<LocationDao>, Validation<ErrStack, LocationDto>> performLocationDeleteF = this::performLocationDelete;
    protected final F<Option<LocationDao>, Validation<ErrStack, LocationDto>> validateLocationObtainByIdF = this::validationLocationObtainById;

    protected final F<Option<LocationDao>, Validation<ErrStack, LocationDto>> deleteLocationF =
            (dao) -> performLocationDeleteF.f(dao);

    protected final F3<Location, Date, UUID, Validation<ErrStack, LocationDto>> performLocationUpdateF = this::performLocationUpdate;


    public Validation<ErrStack, fj.data.List<LocationDto>> getLocations() {

        final Semigroup<ErrStack> semigroup = Semigroup.firstSemigroup();

        return fj.data.List.iterableList(locationRepository.findAll())
                                .traverseValidation(semigroup, LocationAppService::fromDaoV)
                                .map(LocationAppService::toDto);
    }


    public static Validation<ErrStack, Location> fromDaoV(final LocationDao locationDao) {
        return Validation.success(new Location(
                Option.fromNull(locationDao.getId()),
                Option.fromNull(locationDao.getCreateAt()),
                Option.fromNull(locationDao.getUpdatedAt()),
                locationDao.getKey(),
                locationDao.getLabel(),
                locationDao.getCountryCode(),
                locationDao.getTeamLocation()
        ));
    }

    public LocationDao findByLabel(String label) { return locationRepository.findByLabel(label); }

    public LocationDao save(LocationDao location) { return locationRepository.save(location); }

    public void deleteByKey(String key) { locationRepository.deleteByKey(key); }


    protected final F<LocationDto, Validation<ErrStack, LocationDto>> addLocationF =
            (locationDto) -> convertDtoToLocationF.f(locationDto).map(loc ->
                    saveChangesInLocationF.f(loc, convertLocationToDaoF.f(loc, null)));


    public Validation<ErrStack, LocationDto> addLocation(final LocationDto locationDto) {
        return addLocationF.f(locationDto);
    }

    private LocationDto saveChangesInLocation(final Location location, final LocationDao locDao) {
        locationRepository.save(locDao);
        return LocationAppService.toDto(location);
    }

    public static LocationDao toDao(final Location location, final LocationDao dao) {
        LocationDao locDao = dao;

        if(locDao == null) {
            locDao = new LocationDao();
        }

        locDao.setKey(location.getKey());
        locDao.setLabel(location.getLabel());
        locDao.setCountryCode(location.getCountryCode());
        locDao.setTeamLocation(location.getTeamLocation());

        return locDao;
    }

    public static Validation<ErrStack, Location> fromDtoV(LocationDto dto) {

        final Validation<ErrStack, String> keyV = Validation.condition(dto.getKey() != null, ErrStack.of("Location key is required"), dto.getKey());
        final Validation<ErrStack, String> labelV = Validation.condition(dto.getLocationLabel() != null, ErrStack.of("Location label is required"), dto.getLocationLabel());
        final Validation<ErrStack, String> countryCodeV = Validation.condition(dto.getCountryCode() != null, ErrStack.of("Country code is required"), dto.getCountryCode());
        final Validation<ErrStack, Boolean> teamLocationV = Validation.condition(dto.isTeamLocation != null, ErrStack.of("is Team Location is required"), dto.isTeamLocation);

        ErrStack errStack = new ErrStack()
                .append(keyV)
                .append(labelV)
                .append(countryCodeV)
                .append(teamLocationV);

        if(errStack.isFail())
            return Validation.fail(errStack);

        return Validation.success(new Location(
                Option.fromNull(dto.getId()),
                Option.fromNull(dto.getCreatedAt()),
                Option.fromNull(dto.getUpdatedAt()),
                keyV.success(),
                labelV.success(),
                countryCodeV.success(),
                teamLocationV.success()
        ));
    }

    public static Validation<ErrStack, LocationDto> toDtoV(final Location location) {
        return Validation.success(new LocationDto(location.getID().toNull(), location.getCreatedAt().toNull(), location.getUpdatedAt().toNull(),
                location.getKey(), location.getLabel(), location.getCountryCode(), location.getTeamLocation()));
    }


    public static fj.data.List<LocationDto> toDto(final fj.data.List<Location> locations) {
        return locations.map(LocationAppService::toDto);
    }

    public static LocationDto toDto(final Location location) {
        return new LocationDto(location.getID().toNull(), location.getCreatedAt().toNull(), location.getUpdatedAt().toNull(), location.getKey(), location.getLabel(),
                                location.getCountryCode(), location.getTeamLocation());
    }

    public Option<LocationDao> getLocation0(UUID locId) {
        return locationRepository.findById(locId)
                .map(Option::fromNull).orElse(Option.none());
    }

    public Validation<ErrStack, LocationDto> updateLocationV(final LocationDto newlocDto, final UUID locId) {
        return updateLocationF.f(getLocationDaoF.f(locId), newlocDto);
    }

    protected Validation<ErrStack, Location> getLocationV(Option<LocationDao> locationDao0) {
        return locationDao0.toValidation(new ErrStack("location not found"))
                .bind(locationDao -> fromDaoV(locationDao));
    }

    protected final F2<LocationDto, Location, Validation<ErrStack, Location>> updateLocationDmF = LocationService::updateLocationV;
    protected final F2<Option<LocationDao>, LocationDto, Validation<ErrStack, LocationDto>> updateLocationF =
            (locDao0, locDto) -> getLocationV(locDao0)
                    .bind(loc ->
                            fromDtoV(locDto).bind(newLoc ->
                                    updateLocationDmF.f(locDto, loc)).map(updatedloc ->
                                    saveChangesInLocationF.f(updatedloc, toDao(updatedloc, locDao0.some()))));

    protected final F4<Option<LocationDao>, LocationDto, Date, UUID, Validation<ErrStack, LocationDto>> updateLocationPropertyF =
            (currDao, newDto, dateNow, locId) -> convertDtoToLocationF.f(newDto).bind(loc ->
                    performLocationUpdateF.f(loc, dateNow, locId));


    public Validation<ErrStack, LocationDto> deleteLocationByIdV(final LocationDto locDto) {
        return deleteLocationF.f(
                getLocationDaoF.f(locDto.getId())
        );
    }

    protected Validation<ErrStack, LocationDto> performLocationDelete(final Option<LocationDao> locDao) {
        if(locDao.isNone()) {
            return Validation.fail(ErrStack.of("Location Id doesn't exist"));
        }
        locationRepository.delete(locDao.toNull());
        return fromDaoV(locDao.toNull()).bind(loc -> convertLocationToDtoVF.f(loc));
    }

    public Validation<ErrStack, LocationDto> getLocationByIdV(final UUID locId) {
        return validateLocationObtainByIdF.f(getLocationDaoF.f(locId));
    }

    protected Validation<ErrStack, LocationDto> performLocationUpdate(final Location newLoc, final Date now, final UUID currId) {
        final Optional<LocationDao> daoO = locationRepository.findById(currId).map(currLoc -> {
                                currLoc.setUpdatedAt(newLoc.getUpdatedAt().toNull());
                                currLoc.setKey(newLoc.getKey());
                                currLoc.setLabel(newLoc.getLabel());
                                currLoc.setCountryCode(newLoc.getCountryCode());
                                currLoc.setTeamLocation(newLoc.getTeamLocation());
                                return locationRepository.save(currLoc);
                            });

        return fromDaoV(daoO.get()).bind(loc -> convertLocationToDtoVF.f(loc));

    }

    protected Validation<ErrStack, LocationDto> validationLocationObtainById(final Option<LocationDao> locDao) {
        if(locDao.isNone()) {
            return Validation.fail(ErrStack.of("Location Id doesn't exist"));
        }
        return fromDaoV(locDao.toNull()).bind(loc -> convertLocationToDtoVF.f(loc));
    }
}
