package com.spring.service;

import com.spring.domain.SendEmailRequest;
import org.springframework.stereotype.Service;

@Service
public class MessageGenerator {
    public SendEmailRequest generateEmailRequest() {
        return new SendEmailRequest();
    }
}
