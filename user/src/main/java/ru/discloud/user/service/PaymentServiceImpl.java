package ru.discloud.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.discloud.user.domain.Client;
import ru.discloud.user.domain.Payment;
import ru.discloud.user.repository.PaymentRepository;

import javax.persistence.EntityNotFoundException;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Page<Payment> findAll(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }

    @Override
    public Page<Payment> findByClient(Client client, Pageable pageable) {
        return paymentRepository.findAllByClient(client, pageable);
    }

    @Override
    public Payment findById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment '{" + id + "}' not found"));
    }

    @Override
    public Payment createPayment() {
        return null;
    }

    @Override
    public void capturePayment() {

    }
}
