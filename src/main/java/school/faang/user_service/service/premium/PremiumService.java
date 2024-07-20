package school.faang.user_service.service.premium;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.faang.user_service.client.PaymentServiceClient;
import school.faang.user_service.dto.PremiumDto;
import school.faang.user_service.dto.client.Currency;
import school.faang.user_service.dto.client.PaymentRequest;
import school.faang.user_service.dto.client.PaymentResponse;
import school.faang.user_service.entity.premium.Premium;
import school.faang.user_service.enums.PremiumPeriod;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.mapper.PremiumMapper;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.repository.premium.PremiumRepository;
import school.faang.user_service.service.user.UserService;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PremiumService {

    private final PremiumRepository premiumRepository;

    private final UserService userService;

    private final PremiumMapper mapper;

    private final PaymentServiceClient serviceClient;

    public PremiumDto buyPremium(long id, PremiumPeriod period) {
        if (premiumRepository.existsByUserId(id)) {
            throw new DataValidationException("Премиум подписка уже оформлена");
        }
        PaymentRequest request = new PaymentRequest(new Random().nextLong(), BigDecimal.valueOf(period.getCost()), Currency.USD);
        PaymentResponse response = serviceClient.sendPayment(request);
        if (response.status().equals("SUCCESS")) {
            LocalDateTime today = LocalDateTime.now();
            Premium premium = Premium.builder().user(userService.findUserById(id))
                    .startDate(today)
                    .endDate(today.plusDays(period.getDays())).build();
            return mapper.toDto(premiumRepository.save(premium));
        } else {
            throw new RuntimeException("Возникла проблема при оплает подписки. Пожалуйста попробуйте оплатить еще раз");
        }
    }
}
