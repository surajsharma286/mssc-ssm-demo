package com.suraj.ssm.config.guard;

import com.suraj.ssm.domain.PaymentEvent;
import com.suraj.ssm.domain.PaymentState;
import com.suraj.ssm.services.PaymentServiceImpl;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Component
public class PaymentIdGuard implements Guard<PaymentState, PaymentEvent> {
    @Override
    public boolean evaluate(StateContext<PaymentState, PaymentEvent> stateContext) {
        return stateContext.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER)!=null;
    }
}
