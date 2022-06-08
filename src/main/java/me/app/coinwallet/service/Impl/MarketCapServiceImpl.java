package me.app.coinwallet.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.app.coinwallet.constant.Constant;
import me.app.coinwallet.entity.MarketCap;
import me.app.coinwallet.entity.Trend;
import me.app.coinwallet.jackson.TrendJson;
import me.app.coinwallet.repository.MarketCapRepository;
import me.app.coinwallet.service.ChartService;
import me.app.coinwallet.service.MarketCapService;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
public class MarketCapServiceImpl implements MarketCapService {
    private final MarketCapRepository marketCapRepository;
    private final ChartService chartService;
    private final OkHttpClient client= Constant.HTTP_CLIENT;
    private final ObjectMapper objectMapper;

    private final static String HOST_PREFIX = "https://api.coingecko.com/api/v3/";
    private final static HttpUrl MARKET_CAP_URL = HttpUrl.parse(HOST_PREFIX + "coins/markets");
    private final static HttpUrl TREND_URL=HttpUrl.parse(HOST_PREFIX+"search/trending");
    private final static String CURRENCY_QUERY_PARAM = "vs_currency";
    private final static String ORDER_QUERY_PARAM = "order";
    private final static String PAGE_CAP_QUERY_PARAM = "per_page";
    private final static String PAGE_NUM_QUERY_PARAM = "page";
    private final static String SPARKLINE_QUERY_PARAM = "sparkline";
    private final static String IDS_QUERY_PARAM="ids";
    private final static MediaType MEDIA_TYPE=MediaType.get("application/json");

    public HttpUrl url(final String currency,final List<String> ids) {
        HttpUrl.Builder builder = MARKET_CAP_URL.newBuilder();
        builder.addQueryParameter(CURRENCY_QUERY_PARAM, currency);
        builder.addQueryParameter(ORDER_QUERY_PARAM, "market_cap_desc");
        builder.addQueryParameter(PAGE_CAP_QUERY_PARAM, "50");
        builder.addQueryParameter(PAGE_NUM_QUERY_PARAM, "1");
        builder.addQueryParameter(SPARKLINE_QUERY_PARAM, "false");
        builder.addQueryParameter(IDS_QUERY_PARAM, ids!=null?String.join(",",ids):null);
        return builder.build();
    }

    public HttpUrl trendUrl(){
        HttpUrl.Builder builder = TREND_URL.newBuilder();
        return builder.build();
    }

    public List<MarketCap> parseMarketCap(final String jsonSource) throws IOException {

        return objectMapper.readValue(jsonSource, new TypeReference<List<MarketCap>>() {
        });
    }
    public TrendJson parseTrend(final String jsonSource) throws IOException{
        return objectMapper.readValue(jsonSource, TrendJson.class);
    }

    public void getTrendFromSource(){
        final Request.Builder request= new Request.Builder();
        request.url(trendUrl());
        final Headers.Builder headers= new Headers.Builder();
        headers.add("Accept",MEDIA_TYPE.toString());
        request.headers(headers.build());
        final OkHttpClient.Builder httpClientBuilder = client.newBuilder();
        httpClientBuilder.connectionSpecs(Collections.singletonList(ConnectionSpec.RESTRICTED_TLS));
        final Call call = httpClientBuilder.build().newCall(request.build());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(final Call call, final Response response) {
                try {
                    if (response.isSuccessful()) {
                        TrendJson data = parseTrend(response.body().string());
                        HttpUrl url=url("usd",data.getCoins());
                        getMarketCapFromSource(url,true);
                    }
                } catch (final IOException x) {

                }
            }
            @Override
            public void onFailure(final Call call, final IOException x) {
//                log.warn("problem fetching exchange rates from " + marketCapHost.url(), x);
//                Log.e("HD","Market cap request failed");
            }
        });
    }

    private void getMarketCapFromSource(HttpUrl url, boolean isTrend) {

        final Request.Builder request = new Request.Builder();
        request.url(url);
        final Headers.Builder headers = new Headers.Builder();
//        headers.add("User-Agent", userAgent);
        headers.add("Accept", MEDIA_TYPE.toString());
        request.headers(headers.build());
        final OkHttpClient.Builder httpClientBuilder = client.newBuilder();
        httpClientBuilder.connectionSpecs(Collections.singletonList(ConnectionSpec.RESTRICTED_TLS));
        final Call call = httpClientBuilder.build().newCall(request.build());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(final Call call, final Response response) {
                try {
                    if (response.isSuccessful()) {
                        List<MarketCap> data = parseMarketCap(response.body().string());
                        marketCapRepository.deleteAll();
//                        marketCapRepository.saveAll(data);
                        data.forEach(cap->{
                            cap.queryChart();
                            if(isTrend){
                                cap.setTrend(new Trend(cap));
                            }
                            marketCapRepository.save(cap);
//                            chartService.load(cap);
                        });
                    }
                } catch (final IOException x) {

                }
            }

            @Override
            public void onFailure(final Call call, final IOException x) {
//                log.warn("problem fetching exchange rates from " + marketCapHost.url(), x);
//                Log.e("HD","Market cap request failed");
            }
        });
    }


    @Override
    public void load() {
        getMarketCapFromSource(url("usd",null),false);
    }

    @Override
    public void loadTrend() {
        getTrendFromSource();
    }

    @Override
    public List<MarketCap> getTrend() {
        return marketCapRepository.getTrend();
    }

    @Override
    public List<MarketCap> getAllCap() {
        return marketCapRepository.getAll();
    }

    @Override
    public List<MarketCap> getSomeCap(int count) {
        return marketCapRepository.getSome(count);
    }

}
