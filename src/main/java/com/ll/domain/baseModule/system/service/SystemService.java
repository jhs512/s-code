package com.ll.domain.baseModule.system.service;

import com.ll.domain.baseModule.attr.service.AttrService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SystemService {
    private final AttrService attrService;

    public void setAllInitDataConfigured(boolean configured) {
        attrService.set("member__1__system__allInitDataConfigured", configured);
    }

    public boolean isAllInitDataConfigured() {
        return attrService.getAsBoolean("member__1__system__allInitDataConfigured", false);
    }

    public void setNotProdInitDataConfigured(boolean configured) {
        attrService.set("system__notProdInitDataConfigured", configured);
    }

    public boolean isNotProdInitDataConfigured() {
        return attrService.getAsBoolean("system__notProdInitDataConfigured", false);
    }
}
