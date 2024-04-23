package com.example.flightmanager.mapper;

import com.example.flightmanager.dto.PassengerDTO;
import com.example.flightmanager.model.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PassengerMapper {

    PassengerMapper INSTANCE = Mappers.getMapper(PassengerMapper.class);

    PassengerDTO entityToDto(Passenger passenger);
    Passenger dtoToEntity(PassengerDTO passengerDTO);
}
