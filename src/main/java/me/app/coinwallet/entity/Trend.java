package me.app.coinwallet.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TREND")
public class Trend extends AbstractEntity{
    @Id
    @GeneratedValue
    Long id;

    @OneToOne(targetEntity = MarketCap.class)
    @JoinColumn(name = "cap_id",referencedColumnName = "id")
    private MarketCap marketCap;

    public Trend( MarketCap marketCap) {
        this.marketCap = marketCap;
    }

    public Trend(Long id, MarketCap marketCap) {
        this(marketCap);
        this.id = id;
    }
}
