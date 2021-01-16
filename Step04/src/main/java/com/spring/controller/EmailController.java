package com.spring.controller;

import com.spring.domain.SendEmailRequest;
import com.spring.service.EmailServiceClient;
import com.spring.service.MessageGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

@Controller
@RequestMapping("/email")
public class EmailController {

    @Inject
    private EmailServiceClient emailServiceClient;
    @Inject
    private MessageGenerator messageGenerator;

    @RequestMapping(value = "/sendEmail")
    @ResponseBody
    public String sendEmail() {
        SendEmailRequest sendEmailRequest = messageGenerator.generateEmailRequest();
        emailServiceClient.sendEmail(sendEmailRequest);
        return "Success";
    }
}
