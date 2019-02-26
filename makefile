jar_lib = ".:/Path/to/mysql-connector-java-8.0.14.jar:"

run:	
	javac -cp $(jar_lib) sampleMySQLCon.java
	java -cp $(jar_lib) sampleMySQLCon

clean:
	rm *.class
	clear
