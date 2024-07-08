package school.faang.user_service.service.recommendation;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import school.faang.user_service.repository.recommendation.RecommendationRequestRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

public class RecommendationRequestRepositoryValidatorTest {
    @InjectMocks
    private RecommendationRequestRepositoryValidator recommendationRequestRepositoryValidator;

    @Mock
    private RecommendationRequestRepository recommendationRequestRepository;

    private Long id;

    @BeforeEach
    public void setUp() {
        id = 1L;

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateId() {
        when(recommendationRequestRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> recommendationRequestRepositoryValidator.validateId(id));
    }
}
