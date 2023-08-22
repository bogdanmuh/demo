package ru.mukhutdinov.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mukhutdinov.demo.domain.Moving;
import ru.mukhutdinov.demo.domain.dto.MovingDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovingRepository extends JpaRepository<Moving, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE  Moving  as M set M.isComing = true " +
            "Where M.mailing.id = :mailing " +
            "AND  M.from.index = :from " +
            "AND  M.to.index = :to")
    void updateIsComing(@Param("mailing") String mailing, @Param("from") String from, @Param("to") String to);

    /// отпртимиизировать запросы

   @Query("select new ru.mukhutdinov.demo.domain.dto.MovingDto(" +
            "M.from.name," +
            "M.from.address," +
            "M.from.index,"+
            "M.to.name," +
            "M.to.address," +
            "M.to.index," +
            "M.isComing" +
            ") " +
            "From Moving M " +
            "inner join M.from " +
            "where M.mailing.id = :mailing ")
    //@Query("From Moving  as M Where M.mailing.id = :mailing ")
    List<MovingDto> findByMailing(@Param("mailing") String mailing);

}
