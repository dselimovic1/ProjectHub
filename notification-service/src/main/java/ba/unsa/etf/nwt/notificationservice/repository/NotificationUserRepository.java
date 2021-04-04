package ba.unsa.etf.nwt.notificationservice.repository;

import ba.unsa.etf.nwt.notificationservice.model.NotificationUser;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationUserRepository extends PagingAndSortingRepository<NotificationUser, UUID> {
    Optional<NotificationUser> findByNotification_IdAndUserId(UUID notificationId, UUID userId);
}
