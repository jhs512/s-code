package com.sbs.demo5.domain.genFile.controller;

import com.sbs.demo5.domain.genFile.entity.GenFile;
import com.sbs.demo5.domain.genFile.service.GenFileService;
import com.sbs.demo5.standard.util.Ut;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/genFile")
public class GenFileController {
    private final GenFileService genFileService;

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable long id, HttpServletRequest request) throws FileNotFoundException {
        GenFile genFile = genFileService.findById(id).get();
        String filePath = genFile.getFilePath();

        Resource resource = new InputStreamResource(new FileInputStream(filePath));

        String contentType = request.getServletContext().getMimeType(new File(filePath).getAbsolutePath());

        if (contentType == null) contentType = "application/octet-stream";

        String fileName = Ut.url.encode(genFile.getOriginFileName()).replaceAll("\\+", " ");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType(contentType)).body(resource);
    }
}
