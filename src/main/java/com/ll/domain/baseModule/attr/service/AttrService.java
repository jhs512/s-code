package com.ll.domain.baseModule.attr.service;

import com.ll.domain.baseModule.attr.entity.Attr;
import com.ll.domain.baseModule.attr.repository.AttrRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AttrService {
    private final AttrRepository attrRepository;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    @Getter
    private static class VarName {
        private String relTypeCode;
        private long relId;
        private String typeCode;
        private String type2Code;

        public VarName(String varName) {
            if (varName == null || varName.isBlank()) {
                throw new IllegalArgumentException("varName is null or blank");
            }

            if (varName.startsWith("system__")) {
                varName = "system__0__" + varName;
            }

            String[] varNameBits = varName.split("__");

            if (varNameBits.length != 4) {
                throw new IllegalArgumentException("varName is must be 4 bits separated by '__'");
            }

            this.relTypeCode = varNameBits[0];
            this.relId = Long.parseLong(varNameBits[1]);
            this.typeCode = varNameBits[2];
            this.type2Code = varNameBits[3];
        }
    }

    // 조회
    public String get(String varName, String defaultValue) {
        Attr attr = findAttr(varName);

        if (attr == null) {
            return defaultValue;
        }

        if (attr.getExpireDate() != null && attr.getExpireDate().isBefore(LocalDateTime.now())) {
            return defaultValue;
        }

        return attr.getVal();
    }

    private Attr findAttr(String varName) {
        return findAttr(new VarName(varName));
    }

    private Attr findAttr(VarName _varName) {
        return attrRepository
                .findByRelTypeCodeAndRelIdAndTypeCodeAndType2Code(
                        _varName.getRelTypeCode(),
                        _varName.getRelId(),
                        _varName.getTypeCode(),
                        _varName.getType2Code()
                )
                .orElse(null);
    }

    public long getAsLong(String varName, long defaultValue) {
        String value = get(varName, "");

        if (value.isBlank()) {
            return defaultValue;
        }

        return Long.parseLong(value);
    }

    public boolean getAsBoolean(String varName, boolean defaultValue) {
        String value = get(varName, "");

        if (value.isBlank()) {
            return defaultValue;
        }

        if (value.equals("true")) {
            return true;
        } else return value.equals("1");
    }

    public LocalDateTime getAsLocalDatetime(String varName, LocalDateTime defaultValue) {
        String value = get(varName, "");

        if (value.isBlank()) {
            return defaultValue;
        }

        return LocalDateTime.parse(value, dateTimeFormatter);
    }

    // 명령
    // String, expireDate 없음
    @Transactional
    public void set(String varName, String value) {
        set(varName, value, null);
    }

    // String, expireDate 있음
    @Transactional
    public void set(String varName, String value, LocalDateTime expireDate) {
        _set(varName, value, expireDate);
    }

    // long, expireDate 없음
    @Transactional
    public void set(String varName, long value) {
        set(varName, String.valueOf(value), null);
    }

    // long, expireDate 있음
    @Transactional
    public void set(String varName, long value, LocalDateTime expireDate) {
        _set(varName, String.valueOf(value), expireDate);
    }

    // boolean, expireDate 없음
    @Transactional
    public void set(String varName, boolean value) {
        set(varName, String.valueOf(value), null);
    }

    // boolean, expireDate 있음
    @Transactional
    public void set(String varName, boolean value, LocalDateTime expireDate) {
        _set(varName, String.valueOf(value), expireDate);
    }

    // LocalDateTime, expireDate 없음
    @Transactional
    public void set(String varName, LocalDateTime value) {
        set(varName, value, null);
    }

    // LocalDateTime, expireDate 있음
    @Transactional
    public void set(String varName, LocalDateTime value, LocalDateTime expireDate) {
        _set(varName, value.format(dateTimeFormatter), expireDate);
    }

    private void _set(String varName, String value, LocalDateTime expireDate) {
        VarName _varName = new VarName(varName);

        _set(_varName, value, expireDate);
    }

    private void _set(VarName varName, String value, LocalDateTime expireDate) {
        Attr attr = findAttr(varName);

        if (attr == null) {
            attr = Attr
                    .builder()
                    .relTypeCode(varName.getRelTypeCode())
                    .relId(varName.getRelId())
                    .typeCode(varName.getTypeCode())
                    .type2Code(varName.getType2Code())
                    .build();
        }

        attr.setVal(value);
        attr.setExpireDate(expireDate);

        attrRepository.save(attr);
    }
}