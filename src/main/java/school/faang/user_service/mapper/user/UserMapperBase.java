package school.faang.user_service.mapper.user;

import school.faang.user_service.entity.Country;

public interface UserMapperBase {
    default Country getCountry(String title) {
        return Country.builder()
                .title(title)
                .build();
    }
}