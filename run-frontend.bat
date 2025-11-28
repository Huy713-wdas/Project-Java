@echo off
REM Start the frontend dev server (React / CRA)
cd /d "%~dp0frontend"
if not exist node_modules (
  echo Installing npm dependencies...
  call npm install
)
echo Starting frontend on http://localhost:3000 ...
call npm start

