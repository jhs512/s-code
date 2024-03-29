package com.ll.domain.baseModule.attr.entity;

import com.ll.global.jpa.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(
        // 변수명이 같은 데이터 생성되는것을 막는 역할
        // 특정 변수명으로 검색했을 때 초고속 검색이 되도록
        uniqueConstraints = @UniqueConstraint(
                columnNames = {
                        "relId", "relTypeCode", "typeCode", "type2Code"
                }
        ),
        indexes = {
                // 특정 그룹의 데이터들을 불러올 때
                @Index(name = "idx1", columnList = "relTypeCode, typeCode, type2Code")
        }
)
@Setter
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class Attr extends BaseEntity {
    private String relTypeCode;
    private long relId;
    private String typeCode;
    private String type2Code;
    private String val;
    private LocalDateTime expireDate;
}
