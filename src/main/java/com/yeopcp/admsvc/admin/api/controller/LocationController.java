package com.yeopcp.admsvc.admin.api.controller;


import com.yeopcp.admsvc.admin.api.dto.LocationDto;
import com.yeopcp.admsvc.admin.appService.LocationAppService;
import com.yeopcp.admsvc.admin.persistence.dao.LocationDao;
import com.yeopcp.admsvc.admin.persistence.repository.LocationRepository;
import com.yeopcp.admsvc.admin.domain.util.ErrStack;
import com.yeopcp.admsvc.admin.util.exception.BadRequestException;
import fj.data.Validation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class LocationController {

    @Autowired
    private LocationAppService locationAppService;

    @Operation(summary = "Get all locations")
    @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Listed all location",
                content = { @Content(mediaType = "application/json",
                      schema = @Schema(implementation = LocationDao.class)) }),
          @ApiResponse(responseCode = "404", description = "Locations not found",
                content = @Content)})
    @GetMapping("/locations/all")
    public List<LocationDto> getAllLocations() {
        Validation<ErrStack, fj.data.List<LocationDto>> dtoV = locationAppService.getLocations();
        if(dtoV.isFail()) {
            throw new BadRequestException(dtoV.f().toString());
        }
        return dtoV.success().toJavaList();
    }

    @Operation(summary = "Add a location")
    @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Location added"),
          @ApiResponse(responseCode = "401", description = "Unable to add location")})
    @PostMapping("/locations/add")
    public ResponseEntity<Object> addLocation(@Parameter(description = "Location item", required = true) @RequestBody LocationDto locDto) {
        final Validation<ErrStack, LocationDto> dtoV = locationAppService.addLocation(locDto);

        if (dtoV.isFail()) {
            throw new BadRequestException(dtoV.fail().toString());
        }

        HashMap<String, String> result = new HashMap<>();
        result.put("message", "Location saved");
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a location by Id")
    @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Location updated"),
          @ApiResponse(responseCode = "401", description = "Unable to update location")})
    @PatchMapping("/locations/update/{locId}")
    public ResponseEntity updateLocationById(@PathVariable UUID locId, @Parameter(description = "Location item", required = true) @RequestBody LocationDto locDto) {
        final Validation<ErrStack, LocationDto> dtoV = locationAppService.updateLocationV(locDto, locId);

        if (dtoV.isFail()) {
            throw new BadRequestException(dtoV.fail().toString());
        }

        HashMap<String, String> result = new HashMap<>();
        result.put("message", "Location updated");
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a location by Id")
    @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Location deleted"),
          @ApiResponse(responseCode = "401", description = "Unable to delete location")})
    @DeleteMapping("/locations/delete")
    public ResponseEntity deleteLocationById(@Parameter(description = "Location item with Id ONLY", required = true) @RequestBody LocationDto locDto) {
        final Validation<ErrStack, LocationDto> dtoV = locationAppService.deleteLocationByIdV(locDto);

        if (dtoV.isFail()) {
            throw new BadRequestException(dtoV.fail().toString());
        }

        HashMap<String, String> result = new HashMap<>();
        result.put("message", "Location deleted");
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a location by Id")
    @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Location retrieved"),
          @ApiResponse(responseCode = "401", description = "Unable to retrieve location")})
    @GetMapping("/locations/{id}")
    public ResponseEntity getLocationById(@Parameter(description = "Location ID", required = true) @PathVariable UUID id) {
        final Validation<ErrStack, LocationDto> dtoV = locationAppService.getLocationByIdV(id);

        if (dtoV.isFail()) {
            throw new BadRequestException(dtoV.fail().toString());
        }

        return new ResponseEntity<Object>(dtoV.success(), HttpStatus.CREATED);
    }


}
