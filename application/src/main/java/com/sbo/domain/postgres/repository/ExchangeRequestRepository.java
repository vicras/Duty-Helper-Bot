package com.sbo.domain.postgres.repository;

import com.sbo.domain.postgres.entity.ExchangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Long> {
}
