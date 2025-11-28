@echo off
REM Simple runner for the Spring Boot backend
cd /d "%~dp0demo"
echo Starting Spring Boot backend...
if exist mvnw.cmd (
  call mvnw.cmd spring-boot:run
) else (
  call mvnw spring-boot:run
)

