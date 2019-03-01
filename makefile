LIB = ".:/Users/Alan-Liu/desktop/chilling_monsters/chillingM_code/lib/mysql-connector-java-8.0.15.jar:"

SRC = "MySQLCon.java"
TARGET = "MySQLCon"

compile:	
	javac -cp $(LIB) $(SRC)

run: compile
	java -cp $(LIB) $(TARGET)

clean:
	rm *.class
	clear
