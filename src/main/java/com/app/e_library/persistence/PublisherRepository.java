package com.app.e_library.persistence;

import com.app.e_library.persistence.entity.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<PublisherEntity, Long> {

    PublisherEntity getPublisherEntityByPublisherName(String name);
}
