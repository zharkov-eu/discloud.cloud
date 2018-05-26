package ru.discloud.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.discloud.user.domain.Client;
import ru.discloud.user.domain.Payment;

public interface PaymentService {
  Page<Payment> findAll(Pageable pageable);

  Page<Payment> findByClient(Client client, Pageable pageable);

  Payment findById(Long id);

  Payment createPayment();

  void capturePayment();
}
