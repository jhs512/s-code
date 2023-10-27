package com.sbs.demo5.domain.baseModule.genFile.controller;

import com.sbs.demo5.domain.baseModule.genFile.entity.GenFile;
import com.sbs.demo5.domain.baseModule.genFile.service.GenFileService;
import com.sbs.demo5.global.rq.Rq;
import com.sbs.demo5.global.rsData.RsData;
import com.sbs.demo5.standard.util.Ut;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/genFile")
@Validated
public class GenFileController {
    private final Rq rq;
    private final GenFileService genFileService;

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable long id, HttpServletRequest request) throws FileNotFoundException {
        GenFile genFile = genFileService.findById(id).get();
        String filePath = genFile.getFilePath();

        Resource resource = new InputStreamResource(new FileInputStream(filePath));

        String contentType = request.getServletContext().getMimeType(new File(filePath).getAbsolutePath());

        if (contentType == null) contentType = "application/octet-stream";

        String fileName = Ut.url.encode(genFile.getOriginFileName()).replace("%20", " ");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType(contentType)).body(resource);
    }

    // 위 주석문을 처리할 수 있는 액션 만들어줘
    @PostMapping("/temp")
    @ResponseBody
    public RsData<String> temp(@RequestParam("file") MultipartFile file) {
        GenFile savedFile = genFileService.saveTempFile(rq.getMember(), file);

        return RsData.of("S-1", "임시 파일이 생성되었습니다.", savedFile.getUrl());
    }

    @Scheduled(cron = "0 0 4 * * ?")
    public void removeOldTempFiles() {
        genFileService.removeOldTempFiles();
    }
}
