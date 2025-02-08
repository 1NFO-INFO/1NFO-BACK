package com.example.INFO.global.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate //application에 @EnableJpaAuditing 이거 작성해야함. 위에 @EntityListeners도.
    @Column(updatable = false)
    private LocalDateTime createdTime;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedTime;
}