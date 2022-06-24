package io.springbatch.springbatchlecture.batch.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class OrderBook {

    private String market;
    private Date timestamp;
    private List<Unit> orderbook_units;
}
