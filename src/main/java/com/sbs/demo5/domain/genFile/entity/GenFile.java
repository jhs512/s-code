package com.sbs.demo5.domain.genFile.entity;

import com.sbs.demo5.base.app.AppConfig;
import com.sbs.demo5.base.jpa.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
@Table(indexes = {
        @Index(name = "idx1", columnList = "relId,relTypeCode,typeCode,type2Code")
})
public class GenFile extends BaseEntity {
    private String relTypeCode;
    private long relId;
    private String typeCode;
    private String type2Code;
    private String fileExtTypeCode;
    private String fileExtType2Code;
    private long fileSize;
    private int fileNo;
    private String fileExt;
    private String fileDir;
    private String originFileName;

    public String getFileName() {
        return getId() + "." + getFileExt();
    }

    public String getUrl() {
        return "/gen/" + getFileDir() + "/" + getFileName();
    }

    public String getDownloadUrl() {
        return "/usr/genFile/download/" + getId();
    }

    public String getFilePath() {
        return AppConfig.getGenFileDirPath() + "/" + getFileDir() + "/" + getFileName();
    }
}
