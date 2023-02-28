package com.dateplanner.admin.consumer.repository;

import com.dateplanner.admin.consumer.entity.Announcement;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    @Override
    @EntityGraph(attributePaths = "announcementCategory")
    List<Announcement> findAll();

    @EntityGraph(attributePaths = "announcementCategory")
    List<Announcement> findByAnnouncementCategory_Id(Long id);

    void deleteById(Long id);

}
