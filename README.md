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
