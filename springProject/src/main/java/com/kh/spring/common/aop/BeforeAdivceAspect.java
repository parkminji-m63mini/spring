package com.kh.spring.common.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BeforeAdivceAspect {
	
	private Logger logger = LoggerFactory.getLogger(BeforeAdivceAspect.class);
	
	
	
	@Before("CommonPointcut.implPc()")
	public void beforeLog(JoinPoint jp) {
		
		// 클래스명
		String className = jp.getTarget().getClass().getSimpleName(); // 클래스명 얻기
		
		// 메소드명
		String methodName = jp.getSignature().getName();
		
		if(logger.isDebugEnabled()) {
			logger.debug("------------------------------------------------------------------------------------------------");
			logger.debug("[Start] : " + className + " - " + methodName + "()");
			logger.debug("[Parameter] : " + Arrays.toString(jp.getArgs()));
			// jp.getArgs() : 대상 메소드의 매개변수를 배열로 반환
			
		}
	}

}
