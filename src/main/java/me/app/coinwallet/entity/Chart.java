package me.app.coinwallet.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "CHART")
public class Chart extends AbstractEntity{

    @Id
    @GeneratedValue
    Long id;

    @OneToOne(targetEntity = MarketCap.class)
    @JoinColumn(name = "cap_id",referencedColumnName = "id")
    MarketCap marketCap;

    @Column(name = "point_list")
    String pointList;

    @Column
    Integer size;

    public Chart(Long id, MarketCap marketCap, String pointList, Integer size) {
        this(marketCap, pointList, size);
        this.id = id;
    }

    public Chart(MarketCap marketCap, String pointList, Integer size) {
        this.marketCap = marketCap;
        this.pointList = pointList;
        this.size = size;
    }
}
