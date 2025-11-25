# compile-all.ps1
Write-Host "=================================" -ForegroundColor Cyan
Write-Host "   COMPILATION COMPLÈTE SGU" -ForegroundColor Cyan
Write-Host "=================================" -ForegroundColor Cyan

# Créer bin/
if (!(Test-Path "bin")) {
    New-Item -ItemType Directory -Path "bin" | Out-Null
}

# Fonction pour compiler avec gestion d'erreur
function Compile-Java {
    param($files, $description)
    Write-Host "`n📦 Compilation: $description" -ForegroundColor Yellow
    javac -d bin -cp bin $files
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ $description OK" -ForegroundColor Green
        return $true
    } else {
        Write-Host "❌ Erreur: $description" -ForegroundColor Red
        return $false
    }
}

# Ordre de compilation
$success = $true

# 1. Entités de base
$success = $success -and (Compile-Java "src/model/entities/User.java" "User (classe de base)")

# 2. Entités dérivées
$success = $success -and (Compile-Java "src/model/entities/Student.java" "Student")
$success = $success -and (Compile-Java "src/model/entities/Professor.java" "Professor")
$success = $success -and (Compile-Java "src/model/entities/ViceDean.java" "ViceDean")

# 3. Autres entités
$success = $success -and (Compile-Java "src/model/entities/Module.java" "Module")
$success = $success -and (Compile-Java "src/model/entities/Grade.java" "Grade")
$success = $success -and (Compile-Java "src/model/entities/Absence.java" "Absence")
$success = $success -and (Compile-Java "src/model/entities/Inscription.java" "Inscription")

# 4. DataManager
$success = $success -and (Compile-Java "src/model/dao/DataManager.java" "DataManager")

# 5. Observers (si présents)
if (Test-Path "src/model/observers") {
    $success = $success -and (Compile-Java "src/model/observers/*.java" "Observers")
}

# 6. Stratégies
$success = $success -and (Compile-Java "src/strategy/*.java" "Stratégies")

# 7. Vues (si présentes)
if (Test-Path "src/view") {
    $success = $success -and (Compile-Java "src/view/*.java" "Vues")
}

# 8. Contrôleurs (si présents)
if (Test-Path "src/controller") {
    $success = $success -and (Compile-Java "src/controller/*.java" "Contrôleurs")
}

# 9. Main (si présent)
if (Test-Path "src/Main.java") {
    $success = $success -and (Compile-Java "src/Main.java" "Main")
}

# Résumé
Write-Host "`n=================================" -ForegroundColor Cyan
if ($success) {
    Write-Host "✅ COMPILATION RÉUSSIE !" -ForegroundColor Green
    Write-Host "`nPour exécuter: java -cp bin Main" -ForegroundColor Yellow
} else {
    Write-Host "❌ ERREURS DE COMPILATION" -ForegroundColor Red
}
Write-Host "=================================" -ForegroundColor Cyan