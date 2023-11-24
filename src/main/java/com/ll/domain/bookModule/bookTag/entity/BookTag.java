package com.ll.domain.bookModule.bookTag.entity;

import com.ll.domain.baseModule.document.standard.DocumentTag;
import com.ll.domain.bookModule.book.entity.Book;
import com.ll.domain.memberModule.member.entity.Member;
import com.ll.global.jpa.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Setter
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class BookTag extends BaseEntity implements DocumentTag {
    @ManyToOne
    private Member author;

    @ManyToOne
    private Book book;

    private String content;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookTag that = (BookTag) o;

        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (book != null ? book.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}
