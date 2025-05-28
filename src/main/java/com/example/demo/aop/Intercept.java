package com.example.demo.aop;

import java.time.Duration;
import java.time.Instant;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.config.KafkaProducer;
import com.example.demo.logging.AuditTable;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Configuration
public class Intercept {
	
	@Autowired
	private KafkaProducer kafkaProducer;
	
	@Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restControllerMethods() {}
	
	@Around("restControllerMethods()")
	private Object aroundController(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = null;
		AuditTable auditTable = new AuditTable();
		ObjectMapper mapper = new ObjectMapper();
		String corelationIdStr = beforeExecution();
		Instant startTime = Instant.now();
		String strRequest=null;
		auditTable.setCorelationId(corelationIdStr);
		for(Object request : joinPoint.getArgs()) {
			try {
				strRequest = mapper.writeValueAsString(request);
			}
			catch(Exception ex) {
				throw ex;
			}
		}
		auditTable.setRequest(strRequest);
		try {
			result = joinPoint.proceed();
			auditTable.setResponse(mapper.writeValueAsString(result));
		}
		catch(Exception ex) {
			throw ex;
		}
		Instant endTime = Instant.now();
		auditTable.setDuration(Duration.between(startTime, endTime).toMillis());
		
		auditTable.setUrl(getUrl());
		System.out.println(auditTable.toString());
		
		kafkaProducer.sendMessage(mapper.writeValueAsString(auditTable));
		return result;  
		
	}
	
	
	private String beforeExecution() {
	ServletRequestAttributes attributes = 
		    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

		HttpServletRequest request = attributes.getRequest();
		String coreleationIdstr= null;
	if(null==request.getHeader("corelationId")) {
		coreleationIdstr = "Custom"+" "+Long.toString(System.currentTimeMillis());
		return coreleationIdstr;
	}
	return request.getHeader("corelationId");
	}
	
	private String getUrl() {
		ServletRequestAttributes attributes = 
			    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			
		String url = null;
			if (attributes != null) {
			    HttpServletRequest request = attributes.getRequest();

			    // Build the full request URL
			    url = request.getRequestURL().toString(); // e.g., https://example.com/api/users
			    String queryString = request.getQueryString();   // e.g., ?id=123

			    if (queryString != null) {
			        url += "?" + queryString;
			    }

			    
			}
			return url;
	}

}
