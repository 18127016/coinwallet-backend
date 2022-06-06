package me.app.coinwallet.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.app.coinwallet.service.QueryChartEvent;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "MARKETCAP")
public class MarketCap extends AbstractEntity {
    @Id
    @GeneratedValue
    Long id;

    @Column(name = "coin_id")
    private String coinId;

    @Column
    private String name;
    @Column
    private String symbol;
    @Column
    private Float fluctuation;
    @Column
    private Double high;
    @Column
    private Double low;
    @Column(name = "total_volume")
    private  Double totalVolume;
    @Column
    private  String image;
    @Column(name = "cap_time_stamp")
    @UpdateTimestamp
    private Date capTimeStamp;
    @Column(name = "current_price")
    private Double currentPrice;
    @Column(name = "market_cap")
    private Long marketCap;
    @Column(name = "market_cap_rank")
    private Integer marketCapRank;

    @OneToOne(mappedBy = "marketCap", cascade = CascadeType.ALL)
    private Chart chart;

    public void queryChart(){
        registerEvent(new QueryChartEvent(this));
    }

    public MarketCap(Long id,String coinId, String name, String symbol, Float fluctuation, Double high, Double low, Double totalVolume, String image, Date capTimeStamp, Double currentPrice, Long marketCap, Integer marketCapRank) {
        this.id = id;
        this.coinId=coinId;
        this.name = name;
        this.symbol = symbol;
        this.fluctuation = fluctuation;
        this.high = high;
        this.low = low;
        this.totalVolume = totalVolume;
        this.image = image;
        this.capTimeStamp = capTimeStamp;
        this.currentPrice = currentPrice;
        this.marketCap = marketCap;
        this.marketCapRank = marketCapRank;
    }
}
