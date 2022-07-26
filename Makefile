c:
	mvn clean

unit-test:
	mvn clean test

mt:
	mvn clean install && (mvn org.pitest:pitest-maven:mutationCoverage || true) && start target/pit-reports/*/index.html

test: unit-test mutation-test

b:
	mvn clean install compile

run:
	mvn spring-boot:run