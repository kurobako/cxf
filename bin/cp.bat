rem @echo off
rem Helper bat file for appending values to classpath
rem 

if  "%1" == "" goto usage 

set CLASSPATH=%CLASSPATH%;%1
goto end 

:usage
echo cp env-var old-path new-value

:end