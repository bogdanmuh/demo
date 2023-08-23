package ru.mukhutdinov.demo.domain.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MovingDto {
    private String from_Name;
    private String from_Adress;
    private Integer from_Index;
    private Date from_from;
    private String to_Name;
    private String to_Adress;
    private Integer to_Index;
    private Date to_Date;

    public MovingDto(String from_Name, String from_Adress, Integer from_Index, Date from_from, String to_Name, String to_Adress, Integer to_Index, Date to_Date) {
        this.from_Name = from_Name;
        this.from_Adress = from_Adress;
        this.from_Index = from_Index;
        this.from_from = from_from;
        this.to_Name = to_Name;
        this.to_Adress = to_Adress;
        this.to_Index = to_Index;
        this.to_Date = to_Date;
    }
}
