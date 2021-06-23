```
z  [http-nio-18088-exec-10] [TID:973b4ba009d84fb199fe646118dda9f4] c.m.b.m.base.service.impl.PersonServiceImpl - PersonServiceImpl addPersonInfo:PersonBean(id=null, uuid=5df0bebbb6974d5a8a36e55d, name=刘新丽, nameSpell=null, imageUri=_ZzEwMF9mb3jldmVyQnVja2V0_917697a4fda047baa2a2b86887c8d472, type=1, code=null, identifyNum=null, orgId=1, companyId=1, birthday=null, sex=null, visitStartTime=null, visitEndTime=null, visitReason=null, visited=null, visitType=null, email=null, phone=null, cardNum=null, password=null, entryTime=null, postion=null, ext=null, gmtModified=null, gmtCreate=null, groupList=[AddGroupBean(groupId=292, groupUuid=TKJT_tkpass_test_5)], uniqueIdentify=null, visitFirm=null, visitNumPlate=null, vstNull=false, vetNull=false, etNull=false, bdNull=false, autoCode=false, codeNull=false)
```

```
2021-Jun-04 19:01:08.171 WARN  [http-nio-18088-exec-10] [TID:973b4ba009d84fb199fe646118dda9f4] c.m.e.p.s.c.BaseControllerExceptionHandlerAdvice - 执行出现异常
com.megvii.middle_end.common.domain.exception.BusinessException: UUID已存在
        at com.megvii.bbu.middle_end.base.service.impl.PersonServiceImpl.dealUnwiqException(PersonServiceImpl.java:488) ~[classes!/:1.0.2-main-test-SNAPSHOT]
        at com.megvii.bbu.middle_end.base.service.impl.PersonServiceImpl.addPersonInfo(PersonServiceImpl.java:253) ~[classes!/:1.0.2-main-test-SNAPSHOT]
        at com.megvii.bbu.middle_end.base.service.impl.PersonServiceImpl$$FastClassBySpringCGLIB$$77f32f5e.invoke(<generated>) ~[classes!/:1.0.2-main-test-SNAPSHOT]
        at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204) ~[spring-core-5.0.7.RELEASE.jar!/:5.0.7.RELEASE]
        at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:746) ~[spring-aop-5.0.7.RELEASE.jar!/:5.0.7.RELEASE]
        at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163) ~[spring-aop-5.0.7.RELEASE.jar!/:5.0.7.RELEASE]
        at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:294) ~[spring-tx-5.0.7.RELEASE.jar!/:5.0.7.RELEASE]
        at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:98) ~[spring-tx-5.0.7.RELEASE.jar!/:5.0.7.RELEASE]
        at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:185) ~[spring-aop-5.0.7.RELEASE.jar!/:5.0.7.RELEASE]

```



