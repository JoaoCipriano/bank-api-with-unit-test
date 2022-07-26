package com.api.bank.domain.wallet;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletModel toModel(WalletEntity entity);
}
