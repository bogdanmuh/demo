package ru.mukhutdinov.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mukhutdinov.demo.domain.Moving;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovingRepository extends JpaRepository<Moving, Long> {

    @Query("From Moving  as M Where M.mailing.id = :mailing " +
            "AND  M.from.index = :from " +
            "AND  M.to.index = :to")
    Optional<Moving> getMails(@Param("mailing") String mailing, @Param("from") String from, @Param("to") String to);

    @Query("From Moving  as M Where M.mailing.id = :mailing ")
    List<Moving> findByMailing(@Param("mailing") String mailing);

}
