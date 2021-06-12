package com.yeopcp.admsvc.api.controller;

import com.yeopcp.admsvc.api.dto.CurrencyDto;
import com.yeopcp.admsvc.appService.CurrencyAppService;
import com.yeopcp.admsvc.domain.util.ErrStack;
import com.yeopcp.admsvc.persistence.dao.CurrencyDao;
import com.yeopcp.admsvc.util.exception.BadRequestException;
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

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class CurrencyController {

    @Autowired
    private CurrencyAppService currencyAppService;

    @Operation(summary = "Get all currencies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed all currencies",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CurrencyDao.class))}),
            @ApiResponse(responseCode = "404", description = "Currencies not found",
                    content = @Content)})
    @GetMapping("/currency/all")
    public List<CurrencyDto> getAllCurrencies() {
        Validation<ErrStack, fj.data.List<CurrencyDto>> dtoV = currencyAppService.getCurrencies();
        if(dtoV.isFail()) {
            throw new BadRequestException(dtoV.f().toString());
        }
        return dtoV.success().toJavaList();
    }

    @Operation(summary = "Add a currency")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currency added"),
            @ApiResponse(responseCode = "401", description = "Unable to add currency")})
    @PostMapping("/currency/add")
    public ResponseEntity<Object> addCurrency(@Parameter(description = "Currency item", required = true) @RequestBody CurrencyDto curDto) {
        final Validation<ErrStack, CurrencyDto> dtoV = currencyAppService.addCurrency(curDto);

        if(dtoV.isFail()) {
            throw new BadRequestException(dtoV.f().toString());
        }

        HashMap<String, String> result = new HashMap<>();
        result.put("message", "Currency saved");
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a currency by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currency updated"),
            @ApiResponse(responseCode = "401", description = "Unable to update currency")})
    @PostMapping("/currency/update/{curId}")
    public ResponseEntity<Object> updateCurrencyById(@PathVariable UUID curId,
                                                     @Parameter(description = "New currency item details", required = true)
                                                     @RequestBody CurrencyDto curDto) {
        final Validation<ErrStack, CurrencyDto> dtoV = currencyAppService.updateCurrencyV(curDto, curId);

        if(dtoV.isFail()) {
            throw new BadRequestException(dtoV.f().toString());
        }

        HashMap<String, String> result = new HashMap<>();
        result.put("message", "Currency updated");
        return new ResponseEntity<Object>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a currency by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currency deleted"),
            @ApiResponse(responseCode = "401", description = "Unable to delete currency")})
    @DeleteMapping("/currency/delete")
    public ResponseEntity<Object> deleteCurrencyById(@Parameter(description = "Currency item with Id Only", required = true)
                                                     @RequestBody CurrencyDto curDto) {
        final Validation<ErrStack, CurrencyDto> dtoV = currencyAppService.deleteCurrencyByIdV(curDto);

        if(dtoV.isFail()) {
            throw new BadRequestException(dtoV.f().toString());
        }

        HashMap<String, String> result = new HashMap<>();
        result.put("message", "Currency deleted");
        return new ResponseEntity<Object>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a currency by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currency retrieved"),
            @ApiResponse(responseCode = "401", description = "Unable to retrieve currency")})
    @GetMapping("/currency/{id}")
    public ResponseEntity<Object> getCurrencyById(@Parameter(description = "Currency id", required = true)
                                                     @PathVariable UUID id) {
        final Validation<ErrStack, CurrencyDto> dtoV = currencyAppService.getCurrencyByIdV(id);

        if(dtoV.isFail()) {
            throw new BadRequestException(dtoV.f().toString());
        }

        return new ResponseEntity<Object>(dtoV.success(), HttpStatus.OK);
    }

}
