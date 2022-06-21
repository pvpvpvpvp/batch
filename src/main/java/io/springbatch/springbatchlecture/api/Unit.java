package io.springbatch.springbatchlecture.api;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Unit {
    private String ask_price;
    private String bid_price;
    private String ask_size;
    private String bid_size;
}
