package com.appsdeveloperblog.ws.emailnotification.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.appsdeveloperblog.ws.core.ProductCreatedEvent;
import com.appsdeveloperblog.ws.emailnotification.error.NonRetryableException;
import com.appsdeveloperblog.ws.emailnotification.error.RetryableException;

@Component
@KafkaListener(topics = "product-created-events-topic")
public class ProductCreatedEventHandler {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private RestTemplate restTemplate;

	public ProductCreatedEventHandler(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@KafkaHandler
	public void handle(ProductCreatedEvent productCreatedEvent) {

		LOGGER.info("Received a new event: " + productCreatedEvent.getTitle());

		String requestUrl = "http://localhost:8082/response/200";

		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);

			if (response.getStatusCode().value() == HttpStatus.OK.value()) {
				LOGGER.info("Received response from remote server : {}", response.getBody());
			}

		} catch (ResourceAccessException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			throw new RetryableException(e);
		}catch(HttpServerErrorException ex) {
			LOGGER.error(ex.getMessage());
			throw new NonRetryableException(ex);
		}catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new NonRetryableException(e);
		}

	}

}
