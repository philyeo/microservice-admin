package com.yeopcp.admsvc.admin.domain.service;

import com.yeopcp.admsvc.admin.api.dto.LocationDto;
import com.yeopcp.admsvc.admin.domain.entity.Location;
import com.yeopcp.admsvc.admin.domain.util.ErrStack;
import fj.data.List;
import fj.data.Validation;

public class LocationService {

    public static Validation<ErrStack, List<Location>> getRealLocations(List<Location> locations) {
        return Validation.success(locations.filter(location -> location.getKey() != null));
    }

    public static Validation<ErrStack, Location> updateLocationV(LocationDto locDto, Location location) {
        return Validation.success(location.withKey(locDto.getKey())
              .withLabel(locDto.getLocationLabel())
              .withCountryCode(locDto.getCountryCode())
              .withTeamLocation(locDto.getIsTeamLocation())
        );
    }

}
