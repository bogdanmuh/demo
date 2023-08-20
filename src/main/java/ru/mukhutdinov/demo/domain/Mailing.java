package ru.mukhutdinov.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name = "mailing")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Mailing {
    @Id
    private Long id;
    private TypeMailing type;
    @Column(length = 6)
    private Integer recipientIndex;
    private String recipientName;
    private String recipientAddress;

}
