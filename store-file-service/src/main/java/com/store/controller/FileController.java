package com.store.controller;

import com.store.common.resultvo.ResultVO;
import com.store.domain.vo.FileInfoVO;
import com.store.domain.vo.FileSignedUrlVO;
import com.store.domain.vo.FileUploadVO;
import com.store.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@Tag(name = "File Management", description = "File upload and OSS access APIs")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload file to Aliyun OSS")
    public ResultVO<FileUploadVO> upload(@RequestPart("file") MultipartFile file,
                                         @RequestParam(value = "businessType", defaultValue = "common") String businessType) {
        return fileService.upload(file, businessType);
    }

    @GetMapping("/{fileId}")
    @Operation(summary = "Get file metadata")
    public ResultVO<FileInfoVO> getFileInfo(@PathVariable("fileId") Long fileId) {
        return fileService.getFileInfo(fileId);
    }

    @GetMapping("/{fileId}/signed-url")
    @Operation(summary = "Generate signed OSS URL")
    public ResultVO<FileSignedUrlVO> getSignedUrl(@PathVariable("fileId") Long fileId,
                                                  @RequestParam(value = "expireSeconds", required = false) Long expireSeconds) {
        return fileService.getSignedUrl(fileId, expireSeconds);
    }
}
