package com.currency.conversion;


import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name="currecny-exchange-service1",url="http")
//@FeignClient(name="currency-exchange-service1")
@RibbonClient(name="currency-exchange-service1")
public interface CurrencyExchangeServiceProxy {
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyConverterBean retrieveExchangeValue(
			@PathVariable String from,
			@PathVariable String to);
}
