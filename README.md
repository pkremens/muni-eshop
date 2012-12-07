run jboss
mvn clean package -PnoTest jboss-as:deploy
localhost:8080/web/index.html

http://www.mariopareja.com/blog/archive/2010/01/11/how-to-push-a-new-local-branch-to-a-remote.aspx // git branches help

mvn <goal> -PnoTest: to run without tests
mvn <goal> -Parq-jbossas-managed: to run with managed instance of JBoss AS
mvn <goal> -Parq-jbossas-remote: to run with remote instance of JBoss AS (DEFAULT MODE]

run specific test: 
mvn clean test -Parq-jbossas-managed -Dtest=cz.fi.muni.eshop.test.dummy.translate.TranslateTest -DfailIfNoTests=false

mvn surefire-report:report

mvn clean install package jboss-as:redeploy -PnoTest

changing log level globaly: 
for FILE in $(find . | grep "\.java") ; do sed -i 's/FINE/INFO/g' $FILE; done
for FILE in $(find . | grep "\.java") ; do sed -i 's/log.fine/log.info/g' $FILE; done


PROBLEM SOLVING:
WELD-000072 Managed bean declaring a passivating scope must be passivation capable
solution = missing: implements Serializable


16:50:12,428 ERROR [org.jboss.msc.service.fail] (MSC service thread 1-3) MSC00001: Failed to start service jboss.deployment.unit."muni-esho.ear".WeldStartService: org.jboss.msc.service.StartException in service jboss.deployment.unit."muni-esho.ear".WeldStartService: Failed to start service
	at org.jboss.msc.service.ServiceControllerImpl$StartTask.run(ServiceControllerImpl.java:1767) [jboss-msc-1.0.2.GA.jar:1.0.2.GA]
	at java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:886) [rt.jar:1.6.0_31]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:908) [rt.jar:1.6.0_31]
	at java.lang.Thread.run(Thread.java:662) [rt.jar:1.6.0_31]
Caused by: org.jboss.weld.exceptions.DeploymentException: WELD-001408 Unsatisfied dependencies for type [BasketBean] with qualifiers [@Default] at injection point [[field] @Inject private cz.fi.muni.eshop.controller.OrderController.basket]
	at org.jboss.weld.bootstrap.Validator.validateInjectionPoint(Validator.java:311)
	at org.jboss.weld.bootstrap.Validator.validateInjectionPoint(Validator.java:280)
	at org.jboss.weld.bootstrap.Validator.validateBean(Validator.java:143)
	at org.jboss.weld.bootstrap.Validator.validateRIBean(Validator.java:163)
	at org.jboss.weld.bootstrap.Validator.validateBeans(Validator.java:382)
	at org.jboss.weld.bootstrap.Validator.validateDeployment(Validator.java:367)
	at org.jboss.weld.bootstrap.WeldBootstrap.validateBeans(WeldBootstrap.java:379)
	at org.jboss.as.weld.WeldStartService.start(WeldStartService.java:56)
	at org.jboss.msc.service.ServiceControllerImpl$StartTask.startService(ServiceControllerImpl.java:1811) [jboss-msc-1.0.2.GA.jar:1.0.2.GA]
	at org.jboss.msc.service.ServiceControllerImpl$StartTask.run(ServiceControllerImpl.java:1746) [jboss-msc-1.0.2.GA.jar:1.0.2.GA]
	... 3 more
Tady byla chyba v tom, ze sem @Injektoval do OrderControlleru primo BasketBeanu a ne jen jeji rozhrani BasketManager. Ale sou pripady kdy fakt nevim proc to 1408-cku vyhodi, tak snad me to ze sem to aspon jednou vyresil pomuze..
