package com.example.flightmanager.mapper;

import com.example.flightmanager.dto.FlightDTO;
import com.example.flightmanager.model.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FlightMapper {

    FlightMapper INSTANCE = Mappers.getMapper(FlightMapper.class);

    FlightDTO entityToDto(Flight flight);
    Flight dtoToEntity(FlightDTO flightDTO);
}
