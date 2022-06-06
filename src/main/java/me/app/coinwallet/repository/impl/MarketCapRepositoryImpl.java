package me.app.coinwallet.repository.impl;

import me.app.coinwallet.entity.MarketCap;
import me.app.coinwallet.entity.QMarketCap;
import me.app.coinwallet.repository.custom.MarketCapCustomRepository;

import java.util.List;

public class MarketCapRepositoryImpl extends AbstractRepositoryImpl<MarketCap> implements MarketCapCustomRepository {
    @Override
    public List<MarketCap> getAll() {
        return selectFrom(QMarketCap.marketCap1).fetch();
    }

    @Override
    public List<MarketCap> getSome(int count) {
        return selectFrom(QMarketCap.marketCap1)
                .orderBy(QMarketCap.marketCap1.marketCapRank.asc())
                .limit(count).fetch();
    }
}
