create sequence hibernate_sequence;

create table MARKETCAP(
                          id bigint primary key ,
                          coin_id varchar(50),
                          name varchar(50),
                          symbol varchar(50),
                          image varchar(250),
                          fluctuation float,
                          high double precision,
                          low double precision,
                          total_volume double precision,
                          cap_time_stamp timestamp default CURRENT_TIMESTAMP,
                          current_price double precision,
                          market_cap bigint,
                          market_cap_rank integer,
                              version integer default 0
);
create table CHART(
    id bigint primary key,
    cap_id bigint,
    point_list text,
    size integer,
    constraint fk_chart foreign key (cap_id) references MARKETCAP (id),
    version integer default 0
);

