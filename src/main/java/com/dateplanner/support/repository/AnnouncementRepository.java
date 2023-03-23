package com.dateplanner.support.repository;

import com.dateplanner.admin.consumer.entity.Announcement;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    @EntityGraph(attributePaths = "announcementCategory")
    @Override
    Optional<Announcement> findById(Long id);

}
