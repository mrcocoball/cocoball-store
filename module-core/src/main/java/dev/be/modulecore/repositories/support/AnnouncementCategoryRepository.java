package dev.be.modulecore.repositories.support;

import dev.be.modulecore.domain.support.AnnouncementCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementCategoryRepository extends JpaRepository<AnnouncementCategory, Long> {

    void deleteById(Long id);

}
