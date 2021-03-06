package com.techstore.ecommerce.object.dto.mapper;

import com.techstore.ecommerce.object.dto.request.UserRequest;
import com.techstore.ecommerce.object.dto.response.UserResponse;
import com.techstore.ecommerce.object.entity.jpa.User;
import com.techstore.ecommerce.object.model.Address;
import com.techstore.ecommerce.object.model.AuthenticationInfo;
import com.techstore.ecommerce.object.model.FullName;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "roleName", source = "role.name")
    UserResponse toResponseModel(User user);

    @Mapping(target = "roleName", source = "role.name")
    AuthenticationInfo toAuthInfo(User user);

    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "address", ignore = true)
    User createEntityFromRequest(UserRequest request);

    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "address", ignore = true)
    void update(@MappingTarget User user, UserRequest request);

    @AfterMapping
    default void getPropertiesAsString(@MappingTarget User user, UserRequest request) {
        FullName fullName = request.getFullName();
        String fullNameString = String.format("%s %s", fullName.getFirstName(), fullName.getLastName());
        user.setFullName(fullNameString);

        Address address = request.getAddress();
        String addressString = String.format("%s, %s, %s, %s",
                                             address.getStreet(), address.getWard(),
                                             address.getDistrict(), address.getCity());
        user.setAddress(addressString);
    }
}
