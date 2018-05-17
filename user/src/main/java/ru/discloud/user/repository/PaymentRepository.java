package ru.discloud.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.discloud.user.domain.Client;
import ru.discloud.user.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Page<Payment> findAllByClient(Client client, Pageable pageable);
}
