package school.faang.user_service.util.premium;

import school.faang.user_service.dto.payment.PaymentResponseDto;
import school.faang.user_service.dto.premium.PremiumResponseDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.payment.PaymentStatus;
import school.faang.user_service.entity.premium.Premium;

import java.time.LocalDateTime;
import java.util.List;

public class PremiumFabric {

    public static Premium getPremium(long id, User user, LocalDateTime startDate, LocalDateTime endDate) {
        return Premium
                .builder()
                .id(id)
                .user(user)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public static Premium getPremium(long id, LocalDateTime startDate, LocalDateTime endDate) {
        return Premium
                .builder()
                .id(id)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public static User getUser(long id, Premium premium) {
        return User
                .builder()
                .id(id)
                .premiums(List.of(premium))
                .build();
    }

    public static User getUser(long userId) {
        return User
                .builder()
                .id(userId)
                .build();
    }

    public static PremiumResponseDto getResponsePremiumDto(Long id, Long userId,
                                                           LocalDateTime startDate, LocalDateTime endDate) {
        return PremiumResponseDto
                .builder()
                .id(id)
                .userId(userId)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public static PaymentResponseDto getPaymentResponse(PaymentStatus status, String message) {
        return PaymentResponseDto
                .builder()
                .status(status)
                .message(message)
                .build();
    }
}