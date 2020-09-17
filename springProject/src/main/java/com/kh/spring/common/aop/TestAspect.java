package com.kh.spring.common.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.sun.org.glassfish.gmbal.ParameterNames;

// IOC(제어 반전) :  개발자가 제어하지 않고 Spring이 제어
// -> IOC를 하기 위한 조건 : Bean으로 등록되어 있어야 IOC가 가능함.
@Aspect // 공통 관심사가 작성된 클래스임을 지정(AOP에 필요한 Ponintcut, Advice가 작성된 클래스)
@Component // 이 클래스를 bean으로 등록하겠다
public class TestAspect {
	
	// ** 필수 **
	// Advice : 공통되는 부분(공통 관심사)을 별도로 빼내어 작성한 메소드(코드)
	// JoinPoint : Adivce가 적용될 수 있는 모든 시점(관점, 메소드)
				// -> JoinPoint 중 Pointcut이 지정되기 때문에
				// 	  Pointcut 후보 라고도 함.
	// Pointcut : JoinPoint 중에서 실제로 Advice가 적용되는 관점
				// -> 어느 부분에 Advice를 적용할 것인지 필터링함.
	
	// Weaving : Pointcut으로 지정된 시점(관점)에 Advice를 끼워 넣는 작업
	
	
	// Pointcut 표현식 작성 방법
	// **외우기** execution([접근제한자] 리턴타입  패키지명+클래스명  메소드명([매개변수])) : 
				//[접근제한자] 생략시 모든 제한자 의미
	
	// execution : Advice를 적용할 메소드를 지정
			//(지정된 메소드가 실행되기 전/후에 advice가 수행됨)  (어떤 메소드가 실행(호출)되는 시점)
	
	
	// "execution(*(모든 리턴타입) com.kh.spring..(하위 모든 패키지)*(아무값이나 컨트롤러로 끝나는 클래스)Controller.*(모든 메소드)(..)(0개 이상의 매개변수))"
	// -> com.kh.spring 하위에 있는 모든 Controller로 끝나는 클래스 중 메소드명, 반환값, 매개변수 개수 상관 없이 모든 메소드가 수행되는 시점.
	
	// * : 모든, 아무 값을 의미
	// . : 하위(아래)
	// .. : 하위 모든 패키지, 0개 이상의 매개변수
	
	
	// @Pointcut("Pointcut 표현식") : 표현식을 미리 작성한 후  필요한 어드바이스에서 해당 어노테이션이 붙은 메소드를 호출하여 사용 가능
	@Pointcut("execution(* com.kh.spring..*Controller.*(..))")
	public void controllerPointcuts(){}
	
	
	
	
	// "execution(* com.kh.spring..*Controller.*(..))" : 포인트컷 표현식이다 (클래스 명이 컨트롤러로 끝나는 모든 메소드)
	// @Before() : Pointcut으로 지정된 메소드가 실행되기 전에 수행할 advice를 지정하는 advice 수행 시점  (5개 시점 중 1)
//	@Before("execution(* com.kh.spring..*Controller.*(..))") 
	//@Before("controllerPointcuts()")
	public void startLinePrint() {
		// 컨트롤러에 메소드가 실행되기 전(before)
		// "----------- 요청 처리 시작 ----------------" 구문을 출력하는 advice
		System.out.println("----------- 요청 처리 시작 ----------------");
		
	}
	
	// @After() : Pointcut으로 지정된 메소드가 실행된 후 수행할 advice를 지정하는 advice 수행 시점
	//@After("execution(* com.kh.spring..*Controller.*(..))")
	//@After("CommonPointcut.ctrlPc()") // 다른 클래스에 존재하는 Pointcut 사용
	public void endtLinePrint() {
		// 컨트롤러에 메소드가 실행된 후
		// "----------- 요청 처리 시작 ----------------" 구문을 출력하는 advice
		System.out.println("----------- 요청 처리 종료 ----------------");
		
	}
	
}
