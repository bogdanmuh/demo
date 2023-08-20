package ru.mukhutdinov.demo.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne
    @JoinColumn(name = "from_id")
    private PostOffice from;

    @ManyToOne
    @JoinColumn(name = "to_id")
    private PostOffice to;

    private boolean isComing;

    public Moving(Mailing mailing, PostOffice from, PostOffice to, boolean isComing) {
        this.mailing = mailing;
        this.from = from;
        this.to = to;
        this.isComing = isComing;
    }
}
