package ru.mukhutdinov.demo.domain.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovingDto {
    private String from_Name;
    private String from_Adress;
    private Integer from_Index;
    private String to_Name;
    private String to_Adress;
    private Integer to_Index;
    private boolean isComing;

    public MovingDto(String from_Name, String from_Adress, Integer from_Index, String to_Name, String to_Adress, Integer to_Index, boolean isComing) {
        this.from_Name = from_Name;
        this.from_Adress = from_Adress;
        this.from_Index = from_Index;
        this.to_Name = to_Name;
        this.to_Adress = to_Adress;
        this.to_Index = to_Index;
        this.isComing = isComing;
    }
}
