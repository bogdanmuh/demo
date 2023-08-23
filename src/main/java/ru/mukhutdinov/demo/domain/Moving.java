package ru.mukhutdinov.demo.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "moving")
@Getter
@Setter
@NoArgsConstructor
public class Moving {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mailing_id")
    private Mailing mailing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_id")
    private PostOffice from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_id")
    private PostOffice to;
    private Date date_departure;
    private Date date_arrival;
    @Column(name = "from_id", insertable = false, updatable = false)
    private Integer from_id_fk;

    @Column(name = "to_id", insertable = false, updatable = false)
    private Integer to_id_fk;

    public Moving(Mailing mailing, PostOffice from, PostOffice to, Date date_departure, Date date_arrival) {
        this.mailing = mailing;
        this.from = from;
        this.to = to;
        this.date_departure = date_departure;
        this.date_arrival = date_arrival;
    }


}
