package com.example.Advanced_server_Ostrogotskaya.service;

import com.example.Advanced_server_Ostrogotskaya.dto.response.common.CustomSuccessResponse;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    UrlResource getFile(String fileName);

    CustomSuccessResponse<String> uploadFile(MultipartFile file);
}
