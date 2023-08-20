package ru.mukhutdinov.demo.controller.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MailingRequest {
    private String id;
    private String type;
    private String recipientIndex;
    private String recipientName;
    private String recipientAddress;

}
