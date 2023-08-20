package ru.mukhutdinov.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mukhutdinov.demo.domain.Mailing;

import java.util.Optional;

@Repository
public interface MailingRepository extends JpaRepository<Mailing, Long> {

    boolean existsById(Long id);

    Optional<Mailing> findById(Long id);

}
