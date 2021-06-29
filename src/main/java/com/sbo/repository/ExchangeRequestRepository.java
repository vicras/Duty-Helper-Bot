package com.sbo.repository;

import com.sbo.entity.ExchangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Long> {
}
