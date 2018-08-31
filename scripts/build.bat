@echo off

rem =============== Please do not modify the following content ===============
rem go to script directory
cd "%~dp0"

cd ..

echo "==== starting to build rwm ===="

call mvn clean package -DskipTests

echo "==== building rwm finished ===="

pause