package ru.mukhutdinov.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mukhutdinov.demo.domain.Moving;
import ru.mukhutdinov.demo.domain.dto.MovingDto;

import java.util.Date;
import java.util.List;

@Repository
public interface MovingRepository extends JpaRepository<Moving, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE  Moving  as M set M.date_arrival = :date " +
            "Where M.mailing.id = :mailing " +
            "AND  M.from.index = :from " +
            "AND  M.to.index = :to")
    void updateIsComing(@Param("mailing") String mailing, @Param("from") String from, @Param("to") String to, @Param("date") Date date);

    /// отпртимиизировать запросы

   @Query("select new ru.mukhutdinov.demo.domain.dto.MovingDto(" +
            "M.from.name," +
            "M.from.address," +
            "M.from.index,"+
            "M.date_departure,"+
            "M.to.name," +
            "M.to.address," +
            "M.to.index," +
            "M.date_arrival"+
            ") " +
            "From Moving M " +
            "inner join M.from " +
            "where M.mailing.id = :mailing " +
            "order by M.date_arrival")
    //@Query("From Moving  as M Where M.mailing.id = :mailing ")
    List<MovingDto> findByMailing(@Param("mailing") String mailing);

}
