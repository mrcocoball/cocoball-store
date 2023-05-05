package dev.be.modulecore.repositories.place;

import dev.be.modulecore.domain.place.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
