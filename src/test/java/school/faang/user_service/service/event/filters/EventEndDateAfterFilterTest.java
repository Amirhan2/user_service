package school.faang.user_service.service.event.filters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import school.faang.user_service.dto.event.filters.EventFilterDto;
import school.faang.user_service.entity.event.Event;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventEndDateAfterFilterTest {
    @Mock
    private Event event;
    @Mock
    private EventFilterDto filter;
    @InjectMocks
    private EventEndDateAfterFilter eventEndDateAfterFilter;

    @BeforeEach
    void setUp() {
        eventEndDateAfterFilter = new EventEndDateAfterFilter();
    }

    @Nested
    class PositiveTests {
        @Test
        public void testIsApplicable_Success() {
            when(filter.getEndDateAfterPattern()).thenReturn(LocalDateTime.now().plusDays(1));

            boolean result = eventEndDateAfterFilter.isApplicable(filter);
            assertTrue(result);

            verify(filter, atLeastOnce()).getEndDateAfterPattern();
        }

        @Test
        public void testApplyFilter_Success() {
            when(event.getEndDate()).thenReturn(LocalDateTime.now().plusDays(2));
            when(filter.getEndDateAfterPattern()).thenReturn(LocalDateTime.now().plusDays(1));

            boolean result = eventEndDateAfterFilter.applyFilter(event, filter);
            assertTrue(result);

            verify(event, atLeastOnce()).getEndDate();
            verify(filter, atLeastOnce()).getEndDateAfterPattern();
        }
    }

    @Nested
    class NegativeTest {
        @Test
        public void testIsApplicable_NullPattern_AssertFalse() {
            filter.setEndDateAfterPattern(null);

            assertFalse(eventEndDateAfterFilter.isApplicable(filter));
        }

        @Test
        public void testApplyFilter_BeforeDate_AssertFalse() {
            when(event.getEndDate()).thenReturn(LocalDateTime.now().plusDays(1));
            when(filter.getEndDateAfterPattern()).thenReturn(LocalDateTime.now().plusDays(2));

            boolean result = eventEndDateAfterFilter.applyFilter(event, filter);
            assertFalse(result);

            verify(event, atLeastOnce()).getEndDate();
            verify(filter, atLeastOnce()).getEndDateAfterPattern();
        }
    }
}
