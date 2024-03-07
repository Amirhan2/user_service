package school.faang.user_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import school.faang.user_service.dto.UserDto;
import school.faang.user_service.entity.User;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mapping(source = "mentees", target = "menteesIds", qualifiedByName = "map")
    @Mapping(source = "mentors", target = "mentorsIds", qualifiedByName = "map")
    UserDto toDto(User user);
//    List<UserDto> toDto (List<User> users);

    @Mapping(target = "mentees", ignore = true)
    @Mapping(target = "mentors", ignore = true)
    User toEntity(UserDto userDto);


    @Named("map")
    default List<Long> map(List<User> users){
        return users.stream().map(User::getId).toList();
    }


    default List<Long> mapMentorsToIds(User user){
        return user.getMentors().stream().map(User::getId).toList();
    }

    //Not sure should you I send a request to db, if yes than why
//    default List<User> mapMentorsIdsToEntity(UserDto userDto){
//        userDto.getMentorsIds(). // here I should send a request to db to find a User with this id
//    }

}
