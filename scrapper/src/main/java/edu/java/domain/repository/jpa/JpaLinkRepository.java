package edu.java.domain.repository.jpa;

import edu.java.domain.model.jpa.LinkEntity;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLinkRepository extends JpaRepository<LinkEntity, Long> {

    Optional<LinkEntity> findByUrl(URI url);

    List<LinkEntity> findByOrderByLastCheckedAtAsc(Limit limit);

}
