@echo off
echo ========================================================
echo   Compiling and Running Bank Management System...
echo ========================================================

if not exist "bin" mkdir bin

javac -d bin -sourcepath src src/com/bank/Main.java
if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Compilation failed!
    pause
    exit /b %errorlevel%
)

java -cp bin com.bank.Main
pause
