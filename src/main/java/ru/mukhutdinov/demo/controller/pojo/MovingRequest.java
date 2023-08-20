package ru.mukhutdinov.demo.controller.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MovingRequest {
    private String mailing;
    private String from;
    private String to;

}
