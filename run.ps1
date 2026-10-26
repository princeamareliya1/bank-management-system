Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "  Compiling and Running Bank Management System...       " -ForegroundColor Cyan
Write-Host "========================================================" -ForegroundColor Cyan

if (-not (Test-Path "bin")) {
    New-Item -ItemType Directory -Path "bin" | Out-Null
}

javac -d bin -sourcepath src src/com/bank/Main.java

if ($LASTEXITCODE -eq 0) {
    java -cp bin com.bank.Main
} else {
    Write-Host "`n[ERROR] Compilation failed!" -ForegroundColor Red
}
