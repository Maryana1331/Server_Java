package com.example.Advanced_server_Ostrogotskaya.service.impl;

import com.example.Advanced_server_Ostrogotskaya.constants.ErrorCodes;
import com.example.Advanced_server_Ostrogotskaya.dto.response.common.CustomSuccessResponse;
import com.example.Advanced_server_Ostrogotskaya.errors.CustomException;
import com.example.Advanced_server_Ostrogotskaya.service.FileService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import static com.example.Advanced_server_Ostrogotskaya.constants.Constants.DOT;
import static com.example.Advanced_server_Ostrogotskaya.constants.Constants.SLASH;
import static com.example.Advanced_server_Ostrogotskaya.constants.Constants.NONE;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${file.path}")
    private String filePath;

    @Value("${baseUrl.UploadFile}")
    private String baseUrlUploadFile;

    @Override
    public CustomSuccessResponse<String> uploadFile(MultipartFile file) {
        File directory = new File(System.getProperty("user.dir") + filePath);
        String originalFileName = file.getOriginalFilename();
        String extension = NONE;
        if (originalFileName != null && originalFileName.lastIndexOf(DOT) > 0) {
            extension = originalFileName.substring(originalFileName.lastIndexOf(DOT));
        } else {
            throw new CustomException(ErrorCodes.UNKNOWN);
        }
        String uniqueFileName = UUID.randomUUID() + extension;
        File newFile = new File(directory, uniqueFileName);
        try {
            if (!newFile.exists()) {
                file.transferTo(newFile);
                return new CustomSuccessResponse<>(baseUrlUploadFile + newFile.getName(), 1, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED);
        }
        return new CustomSuccessResponse<>(baseUrlUploadFile + newFile.getName(), 1, true);
    }

    @Override
    public UrlResource getFile(String fileUrl) {

        String fileName = extractFileNameFromUrl(fileUrl);

        File directory = new File(System.getProperty("user.dir") + filePath);
        File fileToRetrieve = new File(directory, fileName);

        if (fileToRetrieve.exists()) {
            UrlResource resource;
            try {
                resource = new UrlResource(fileToRetrieve.toURI());
            } catch (IOException e) {
                throw new CustomException(ErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED);
            }
            return resource;
        } else {
            throw new CustomException(ErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED);
        }
    }

    public String extractFileNameFromUrl(String fileUrl) {

        return fileUrl.substring(fileUrl.lastIndexOf(SLASH) + 1);
    }

}
