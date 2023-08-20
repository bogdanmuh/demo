package ru.mukhutdinov.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mukhutdinov.demo.domain.PostOffice;

@Repository
public interface PostOfficeRepository extends JpaRepository<PostOffice, Long> {
    Boolean existsByIndex(Integer index);
}
