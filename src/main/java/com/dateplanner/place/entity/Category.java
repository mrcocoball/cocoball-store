package com.dateplanner.place.entity;

import com.dateplanner.constant.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category")
public class Category extends BaseTimeEntity {

    @Id
    @Column(name = "category_id")
    private String id;

    private String categoryName;

    private Category(String id) {
        this.id = id;
    }

    private Category(String id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    public static Category of(String id) {
        return new Category(id);
    }

    public static Category of(String id, String categoryName) { return new Category(id, categoryName); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return id != null && Objects.equals(id, category.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
