package com.store.service;

import com.store.common.resultvo.ResultVO;
import com.store.domain.vo.FileInfoVO;
import com.store.domain.vo.FileSignedUrlVO;
import com.store.domain.vo.FileUploadVO;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    ResultVO<FileUploadVO> upload(MultipartFile file, String businessType);

    ResultVO<FileInfoVO> getFileInfo(Long fileId);

    ResultVO<FileSignedUrlVO> getSignedUrl(Long fileId, Long expireSeconds);
}
