package ru.mukhutdinov.demo.controller.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.mukhutdinov.demo.domain.PostOffice;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class MailingInfoResponse {
    private String status;
    private List<PostOffice> offices;

}
