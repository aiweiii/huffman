compile:
	javac -d classes/ App.java

run:
	java -cp classes/ App

runfile:
	java -cp classes/ App > test.txt

clear:
	rm -rf ./*.class