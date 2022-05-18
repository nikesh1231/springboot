package com.evbackend.service;

import static io.smallrye.mutiny.converters.uni.UniReactorConverters.toMono;

import org.springframework.stereotype.Service;

import com.evbackend.commands.CreateAddressCommand;
import com.evbackend.model.address.Address;
import com.evbackend.repository.AddressRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public Mono<Address> create(CreateAddressCommand address) {
        return this.addressRepository.create(
                        Address.builder()
                                .streetAddress(address.getStreetAddress())
                                .postCode(address.getPostCode())
                                .longitude(address.getLongitude())
                                .latitude(address.getLatitude())
                                .build()
                )
                .convert().with(toMono());
    }

}
