package ru.mukhutdinov.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "postOffice")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostOffice {
    @Id
    @Column(length = 6)
    private Integer index;

    private String name;

    private String address;

}
