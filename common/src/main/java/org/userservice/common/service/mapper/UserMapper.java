package org.userservice.common.service.mapper;

import org.mapstruct.Mapper;
import org.userservice.common.config.MapperConfig;
import org.userservice.common.model.dto.CreateRequestUserDto;
import org.userservice.common.model.dto.UserDto;
import org.userservice.common.model.entity.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UserDto mapToDto(User user);

    User mapToModel(CreateRequestUserDto userDto);
}
