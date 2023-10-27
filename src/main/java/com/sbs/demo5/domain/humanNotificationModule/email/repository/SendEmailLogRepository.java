package com.sbs.demo5.domain.humanNotificationModule.email.repository;

import com.sbs.demo5.domain.humanNotificationModule.email.entity.SendEmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendEmailLogRepository extends JpaRepository<SendEmailLog, Long> {
}
