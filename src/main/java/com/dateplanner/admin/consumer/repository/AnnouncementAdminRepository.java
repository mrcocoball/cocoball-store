package com.dateplanner.admin.consumer.repository;

import com.dateplanner.admin.consumer.entity.Announcement;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnnouncementAdminRepository extends JpaRepository<Announcement, Long> {

    @EntityGraph(attributePaths = "announcementCategory")
    @Override
    List<Announcement> findAll();

    @EntityGraph(attributePaths = "announcementCategory")
    @Override
    Optional<Announcement> findById(Long id);

    @EntityGraph(attributePaths = "announcementCategory")
    List<Announcement> findByAnnouncementCategory_Id(Long id);

    void deleteById(Long id);

}
