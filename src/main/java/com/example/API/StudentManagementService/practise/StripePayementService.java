package com.example.API.StudentManagementService.practise;

import org.springframework.stereotype.Component;

@Component
public class StripePayementService implements PaymentService {
    @Override
    public String pay() {
        String payment = "StripePayment";
        System.out.println("Payment From " + payment);
        return payment;
    }
}
