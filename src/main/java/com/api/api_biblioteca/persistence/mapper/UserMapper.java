package com.api.api_biblioteca.persistence.mapper;

import com.api.api_biblioteca.domain.User;
import com.api.api_biblioteca.persistence.entity.Usuario;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(source = "idUsuario",target = "userId"),
            @Mapping(source = "nombre",target = "name"),
            @Mapping(source = "fechaRegistro",target = "registerDate"),
            @Mapping(source = "email",target = "email"),
            @Mapping(source = "password",target = "password"),
            @Mapping(source = "role",target = "role")
    })
    User toUser(Usuario usuario);
    List<User>toUsers(List<Usuario>usuarios);

    @InheritInverseConfiguration
    Usuario toUsuario(User user);

}