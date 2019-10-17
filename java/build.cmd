@echo on
javac -source 1.6 -target 1.6 com\uipath\xplatform\*.java 
del xplatformutil.jar
jar -cfe xplatformutil.jar com.uipath.xplatform.LabelAppender com\uipath\xplatform\*
