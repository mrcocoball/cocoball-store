package dev.be.modulecore.repositories.support;

import dev.be.modulecore.domain.support.Announcement;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    @EntityGraph(attributePaths = "announcementCategory")
    @Override
    Optional<Announcement> findById(Long id);

}
