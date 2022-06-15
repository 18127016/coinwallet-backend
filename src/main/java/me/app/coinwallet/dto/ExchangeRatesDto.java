package me.app.coinwallet.dto;

import lombok.Data;

@Data
public class ExchangeRatesDto {
    String name;
    String unit;
    Double value;
}
