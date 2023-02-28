package com.dateplanner.admin.consumer.repository;

import com.dateplanner.admin.consumer.entity.AnnouncementCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementCategoryRepository extends JpaRepository<AnnouncementCategory, Long> {
}
