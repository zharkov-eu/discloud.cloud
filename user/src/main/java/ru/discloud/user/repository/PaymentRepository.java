package ru.discloud.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.discloud.user.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
