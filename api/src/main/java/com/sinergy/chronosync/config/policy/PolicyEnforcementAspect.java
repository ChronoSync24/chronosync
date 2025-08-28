package com.sinergy.chronosync.config.policy;

import com.sinergy.chronosync.service.SecurityContextService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspect that enforces CRUD policies on controller methods annotated with {@link EnforcePolicy}.
 * <p>
 * Intercepts the method call, determines the corresponding policy bean based on the
 * entity and operation specified in the annotation, and validates access according to
 * the current user's role before proceeding with the method execution.
 */
@AllArgsConstructor
@Aspect
@Component
public class PolicyEnforcementAspect {

	private final SecurityContextService securityContextService;
	private final ApplicationContext applicationContext;

	/**
	 * Intercepts methods annotated with {@link EnforcePolicy} and enforces the corresponding CRUD policy.
	 *
	 * <p>The method:
	 * <ul>
	 *     <li>Determines the request argument (if any) from the intercepted method.</li>
	 *     <li>Resolves the appropriate policy bean based on the entity and operation in the annotation.</li>
	 *     <li>Validates access using the current user's role via the resolved policy.</li>
	 *     <li>Proceeds with the original method execution if validation passes.</li>
	 * </ul>
	 *
	 * @param pjp           {@link ProceedingJoinPoint} the join point representing the intercepted method call
	 * @param enforcePolicy {@link EnforcePolicy} annotation of the intercepted method
	 * @return {@link Object} the result of the original method execution
	 * @throws Throwable if the original method or validation throws an exception
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Around("@annotation(enforcePolicy)")
	public Object enforce(ProceedingJoinPoint pjp, EnforcePolicy enforcePolicy) throws Throwable {

		Object request = Arrays.stream(pjp.getArgs())
			.filter(arg -> !(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse))
			.findFirst()
			.orElse(null);

		String beanName = enforcePolicy.entity().getSimpleName().toLowerCase() +
			enforcePolicy.operation().name().charAt(0) +
			enforcePolicy.operation().name().substring(1).toLowerCase() +
			"Policy";

		CrudPolicy policy = applicationContext.getBean(beanName, CrudPolicy.class);
		policy.validate(securityContextService.getAuthUserRole(), request);

		return pjp.proceed();
	}
}

