package com.example.Advanced_server_Ostrogotskaya.controller;

import com.example.Advanced_server_Ostrogotskaya.dto.response.common.CustomSuccessResponse;
import com.example.Advanced_server_Ostrogotskaya.service.impl.FileServiceImpl;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileServiceImpl fileServiceImpl;

    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomSuccessResponse<String>> uploadFile(@RequestParam ("file")
                                                                        @NotNull MultipartFile file) {
        return ResponseEntity.ok().body(fileServiceImpl.uploadFile(file));
    }

    @GetMapping(value = "/{fileName}")
    public ResponseEntity<UrlResource> getFile(@PathVariable String fileName) {
        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(fileServiceImpl.getFile(fileName));
    }
}
