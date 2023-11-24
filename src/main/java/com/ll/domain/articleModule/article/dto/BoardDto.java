package com.ll.domain.articleModule.article.dto;

import com.ll.domain.articleModule.board.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardDto {
    private final long id;
    private final LocalDateTime createDate;
    private final LocalDateTime modifyDate;
    private final String name;
    private final String code;
    private final String icon;

    public BoardDto(Board board) {
        this.id = board.getId();
        this.createDate = board.getCreateDate();
        this.modifyDate = board.getModifyDate();
        this.name = board.getName();
        this.code = board.getCode();
        this.icon = board.getIcon();
    }
}
