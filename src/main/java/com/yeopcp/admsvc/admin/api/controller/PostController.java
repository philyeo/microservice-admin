package com.yeopcp.admsvc.admin.api.controller;


import com.yeopcp.admsvc.admin.appService.LocationAppService;
import fj.data.Validation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
@CrossOrigin(origins = "*")
public class PostController {

    //    @Autowired
//    private PostAppService postAppService;
//    @Autowired
//    private RestTemplate restTemplate;
//
//    private static String url = "https://jsonplaceholder.typicode.com/posts";
//
//    @GetMapping("/posts/all")
//    public List<Object> getPosts() {
//        Object[] posts = restTemplate.getForObject(url, Object[Arrays.class]);
//        return Arrays.asList(posts);
//    }

}
