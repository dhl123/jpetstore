package com.software.jpetstore.logger;

import com.software.jpetstore.domain.Account;
import com.software.jpetstore.domain.Cart;
import com.software.jpetstore.domain.CartItem;
import com.software.jpetstore.service.LogService;
import org.apache.ibatis.binding.MapperMethod;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Aspect
@Service
public class LogAspect {
    @Autowired
    LogService logService;
    private final static Logger logger= LoggerFactory.getLogger(LogAspect.class);
    @Pointcut("execution(* com.software.jpetstore.controller..*(..))")
    public void log(){

    }
    @Around("log()")
    public Object Interceptor(ProceedingJoinPoint pjp){
        //long beginTime = System.currentTimeMillis();

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod(); //获取被拦截的方法
        String methodName = method.getName(); //获取被拦截的方法名
        Object result = null;
        String username="";


        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        url=url+";"+queryString;


        Account account=(Account)request.getSession().getAttribute("account");
        if (account != null) {
            String cartitems="";
            Cart cart=(Cart)request.getSession().getAttribute("cart");
            List<CartItem> items=cart.getCartItemList();
            for(CartItem item:items){
                cartitems=cartitems+item.getItem().getItemId()+";";
            }
            username = account.getUsername();
            logService.insertLog(username,url,cartitems);

        }

        try {
            result=pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return  result;
    }



}
