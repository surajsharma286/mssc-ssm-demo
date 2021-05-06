package com.suraj.ssm.services;

import com.suraj.ssm.domain.Payment;
import com.suraj.ssm.domain.PaymentEvent;
import com.suraj.ssm.domain.PaymentState;
import com.suraj.ssm.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceImplTest {

    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentRepository paymentRepository;

    Payment payment;

    @BeforeEach
    void setUp() {
        payment = Payment.builder()
                .amount(new BigDecimal("12.99"))
                .build();
    }

    @Transactional
    @Test
    void preAuth() {
        Payment savedPayment = paymentService.newPayment(payment);

        StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());

        Payment preAuthedPayment = paymentRepository.getOne(savedPayment.getId());

        System.out.println(" ---------------  "+sm.getState().getId());
        System.out.println(preAuthedPayment);
        System.out.println(" ---------------  ");
    }

    @Transactional
    @RepeatedTest(10)
    void testAuth() {
        Payment savedPayment = paymentService.newPayment(payment);

        StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());

       if(sm.getState().getId() == PaymentState.PRE_AUTH){
           System.out.println("Payment is Pre Authorized ");
           StateMachine<PaymentState,PaymentEvent> authSm = paymentService.authorizePayment(savedPayment.getId());

           System.out.println("Result of Auth : "+ authSm.getState().getId());
       }else{
           System.out.println("Payment failed Pre-Auth ...");
       }
    }
}