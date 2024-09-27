package school.faang.user_service.repository.promotion;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import school.faang.user_service.entity.promotion.EventPromotion;

import java.util.Optional;

@Repository
public interface EventPromotionRepository extends CrudRepository<EventPromotion, Long> {
}