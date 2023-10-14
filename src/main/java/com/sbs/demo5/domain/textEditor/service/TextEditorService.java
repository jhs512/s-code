package com.sbs.demo5.domain.textEditor.service;

import com.sbs.demo5.base.jpa.baseEntity.BaseEntity;
import com.sbs.demo5.domain.genFile.service.GenFileService;
import com.sbs.demo5.domain.textEditor.standard.TextEditorPost;
import com.sbs.demo5.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TextEditorService {
    private final GenFileService genFileService;

    @Transactional
    public void updateTempGenFilesToInBody(TextEditorPost post) {
        Map<String, String> urlsMap = new HashMap<>();

        String newBody = Ut.str.replace(post.getBody(), "\\(/gen/temp_member/([^)]+)\\?type=temp\\)", (String url) -> {
            url = "/gen/temp_member/" + url;
            String newUrl = genFileService.tempToFile(url, (BaseEntity) post, "common", "inBody", 0).getUrl();
            urlsMap.put(url, newUrl);
            return "(" + newUrl + ")";
        });

        post.setBody(newBody);

        String newBodyHtml = Ut.str.replace(post.getBodyHtml(), "=\"/gen/temp_member/([^\" ]+)\\?type=temp\"", (String url) -> {
            url = "/gen/temp_member/" + url;
            String newUrl = urlsMap.get(url);
            return "=\"" + newUrl + "\"";
        });

        post.setBodyHtml(newBodyHtml);
    }
}
