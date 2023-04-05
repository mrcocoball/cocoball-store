package dev.be.modulecore.repositories.support;

import dev.be.modulecore.domain.support.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnnouncementAdminRepository extends JpaRepository<Announcement, Long> {

    @EntityGraph(attributePaths = "announcementCategory")
    @Override
    Page<Announcement> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "announcementCategory")
    @Override
    Optional<Announcement> findById(Long id);

    @EntityGraph(attributePaths = "announcementCategory")
    List<Announcement> findByAnnouncementCategory_Id(Long id);

    void deleteById(Long id);

}
