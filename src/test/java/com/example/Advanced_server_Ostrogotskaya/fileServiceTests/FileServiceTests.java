package com.example.Advanced_server_Ostrogotskaya.fileServiceTests;

import com.example.Advanced_server_Ostrogotskaya.constants.ErrorCodes;
import com.example.Advanced_server_Ostrogotskaya.dto.response.common.CustomSuccessResponse;
import com.example.Advanced_server_Ostrogotskaya.errors.CustomException;
import com.example.Advanced_server_Ostrogotskaya.service.impl.FileServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class FileServiceTests {

    @InjectMocks
    private FileServiceImpl fileService;

    private final String filePath = "/src/main/resources/files/";

    private final String baseUrlUploadFile = "http://localhost:8080/api/v1/file/";

    @BeforeEach
    void setUp() {
        fileService = new FileServiceImpl(filePath, baseUrlUploadFile);
        new File(System.getProperty("user.dir") + filePath).mkdirs();
    }

    @Test
    @DisplayName("Should upload file successfully")
    void testUploadFile_Success() {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        String originalFileName = "testfile.txt";
        Mockito.when(file.getOriginalFilename()).thenReturn(originalFileName);

        CustomSuccessResponse<String> response = fileService.uploadFile(file);

        Assertions.assertNotNull(response);
        assertTrue(response.getSuccess());
        assertTrue(response.getData().contains(baseUrlUploadFile));
        assertTrue(response.getData().endsWith(".txt"));
    }

    @Test
    @DisplayName("Should throw CustomException for unknown file type")
    void testUploadFile_UnknownFileType() {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        Mockito.when(file.getOriginalFilename()).thenReturn("testfile");

        CustomException exception = assertThrows(CustomException.class, () -> {
            fileService.uploadFile(file);
        });

        assertEquals(ErrorCodes.UNKNOWN, exception.getErrorCode());
    }

    @Test
    @DisplayName("Should handle IOException during file transfer")
    void testUploadFile_IOException() throws IOException {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        Mockito.when(file.getOriginalFilename()).thenReturn("testfile.txt");
        Mockito.doThrow(new IOException()).when(file).transferTo(any(File.class));

        CustomException exception = assertThrows(CustomException.class, () -> {
            fileService.uploadFile(file);
        });

        assertEquals(ErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED, exception.getErrorCode());
    }

    @Test
    @DisplayName("Should return UrlResource when file exists")
    void testGetFile_FileExists() throws Exception {
        String testFileName = "testfile.txt";
        File testFile = new File(System.getProperty("user.dir") + filePath + testFileName);
        testFile.createNewFile();
        UrlResource resource = fileService.getFile(baseUrlUploadFile + testFileName);

        assertNotNull(resource);
        assertTrue(resource.exists());

        testFile.delete();
    }

    @Test
    @DisplayName("Should throw CustomException when file does not exist")
    void testGetFile_FileDoesNotExist() {

        CustomException exception = assertThrows(CustomException.class, () -> {
            fileService.getFile(baseUrlUploadFile + "nonexistent.txt");
        });

        assertEquals(ErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED, exception.getErrorCode());
    }

    @Test
    @DisplayName("Should extract file name from URL")
    void testExtractFileNameFromUrl() {
        String fileUrl = baseUrlUploadFile + "testfile.txt";
        String fileName = fileService.extractFileNameFromUrl(fileUrl);

        assertEquals("testfile.txt", fileName);
    }
}
