package com.example.API.StudentManagementService.practise;

import org.springframework.stereotype.Component;

@Component
public class RazorpayPaymentService implements PaymentService {
    @Override
    public String pay() {
        String payment = "RazorpayPay";
        System.out.println("Payment From " + payment);
        return payment;
    }
}
