package com.masters.chat.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.ERROR;

import org.mapstruct.Builder;
import org.mapstruct.MapperConfig;

@MapperConfig(
        componentModel = SPRING,
        unmappedTargetPolicy = ERROR,
        builder = @Builder(disableBuilder = true)
)
public interface CommonMapperConfig {
}
