package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.URIScheme;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.core.io.Resource;
@Configuration
public class CustomRestTemplateConfiguration {
	

    @Value("${trust.store}")
    private Resource trustStore;

    @Value("${trust.store.password}")
    private String trustStorePassword;


    @Bean
    public RestTemplate restTemplate() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException,
      CertificateException, MalformedURLException, IOException {
    	
    	SSLContext sslContext = new SSLContextBuilder()
    	         .loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray()).build();
    	System.out.println("trustStore ==>"+trustStore.getURL());
    	
    	System.out.println("trustStorePassword => "+trustStorePassword);
				
    		Registry<ConnectionSocketFactory> socketRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
    			.register(URIScheme.HTTPS.getId(), new SSLConnectionSocketFactory(sslContext))
    			.register(URIScheme.HTTP.getId(), new PlainConnectionSocketFactory())
    			.build();

    		HttpClient httpClient = HttpClientBuilder.create()
    			.setConnectionManager(new PoolingHttpClientConnectionManager(socketRegistry))
    			.setConnectionManagerShared(true)
    			.build();

            ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
           // RestTemplate restTemplate = new RestTemplate(requestFactory);
        
        return new RestTemplate(requestFactory);
    }
}