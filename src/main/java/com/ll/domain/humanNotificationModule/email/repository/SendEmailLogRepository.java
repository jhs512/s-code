package com.ll.domain.humanNotificationModule.email.repository;

import com.ll.domain.humanNotificationModule.email.entity.SendEmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendEmailLogRepository extends JpaRepository<SendEmailLog, Long> {
}
