package com.ll.domain.baseModule.genFile.entity;

import com.ll.global.app.AppConfig;
import com.ll.global.jpa.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {
                        "relId", "relTypeCode", "typeCode", "type2Code", "fileNo"
                }
        ),
        indexes = {
                // 특정 그룹의 데이터들을 불러올 때
                @Index(name = "idx2", columnList = "relTypeCode, typeCode, type2Code")
        }
)
public class GenFile extends BaseEntity {
    private String relTypeCode;
    private long relId;
    private String typeCode;
    private String type2Code;
    private String fileExtTypeCode;
    private String fileExtType2Code;
    private long fileSize;
    private long fileNo;
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
        return "/domain/genFile/download/" + getId();
    }

    public String getFilePath() {
        return AppConfig.getGenFileDirPath() + "/" + getFileDir() + "/" + getFileName();
    }
}
