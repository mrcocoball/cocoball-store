package com.dateplanner.place.entity;

import com.dateplanner.constant.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity implements Persistable<String> {

    @Id
    @Column(name = "category_id")
    private String id;

    private String categoryName;

    private Category(String id) {
        this.id = id;
    }

    public static Category of(String id) {
        return new Category(id);
    }

    // persistable 재정의

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }

}
