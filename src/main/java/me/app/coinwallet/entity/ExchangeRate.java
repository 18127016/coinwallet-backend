package me.app.coinwallet.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "EXCHANGE_RATES")
public class ExchangeRate extends AbstractEntity {
    @Column
    String name;
    @Column
    String unit;
    @Column
    Double value;

    @Id @GeneratedValue
    Long id;

    public ExchangeRate(String name, String unit, Double value) {
        this.name = name;
        this.unit = unit;
        this.value = value;
    }

    public ExchangeRate(Long id,String name, String unit, Double value) {
        this(name, unit, value);
        this.id=id;
    }

}
