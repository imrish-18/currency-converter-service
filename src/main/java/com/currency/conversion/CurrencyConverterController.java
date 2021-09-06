package com.currency.conversion;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RefreshScope
public class CurrencyConverterController {

	@Autowired
	private CurrencyExchangeServiceProxy proxy;
	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConverterBean calculateCurrencyConverterBean(
			@PathVariable String from,
			@PathVariable String to,
			@PathVariable BigDecimal quantity
			) {
		
		HashMap<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from",from);
		uriVariables.put("to",to);
		
		ResponseEntity<CurrencyConverterBean> responseEntity = new RestTemplate().getForEntity
		("http://localhost:8000/currency-exchange/from/{from}/to/{to}", 
				CurrencyConverterBean.class, uriVariables);
		
		CurrencyConverterBean CurrencyConverterBean = responseEntity.getBody();
		
		return new CurrencyConverterBean(CurrencyConverterBean.getId(), 
				from, to, quantity, 
				CurrencyConverterBean.getConversionMultiple(), 
				quantity.multiply(CurrencyConverterBean.getConversionMultiple()), 
				CurrencyConverterBean.getEnvironment()+ " " + "rest template");
		
	}

	@GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConverterBean calculateCurrencyConverterBeanFeign(
			@PathVariable String from,
			@PathVariable String to,
			@PathVariable BigDecimal quantity
			) {
				
		CurrencyConverterBean CurrencyConverterBean = proxy.retrieveExchangeValue(from, to);
		
		return new CurrencyConverterBean(CurrencyConverterBean.getId(), 
				from, to, quantity, 
				CurrencyConverterBean.getConversionMultiple(), 
				quantity.multiply(CurrencyConverterBean.getConversionMultiple()), 
				CurrencyConverterBean.getEnvironment()+ " " + "");
		
	}
}
