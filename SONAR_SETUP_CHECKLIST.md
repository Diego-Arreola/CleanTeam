# 🔧 Pasos para Habilita## 📊 ¿Qué sucede después?

1. **GitHub Actions ejecuta:**
   - ✅ Checkout del código
   - ✅ Setup de Java 17
   - ✅ Maven build + tests con JUnit (usando perfil `test` con H2)
   - ✅ Genera reporte de cobertura (JaCoCo)

2. **SonarCloud action analiza:**
   - 📈 Cobertura de tests
   - 🐛 Bugs potenciales
   - 🔒 Vulnerabilidades
   - 📝 Code smells
   - 📊 Quality Gates + SonarCloud

## ✅ Checklist de Configuración

### Paso 1: Generar SONAR_TOKEN
- [ ] Accede a https://sonarcloud.io/
- [ ] Inicia sesión con tu cuenta (Diego-Arreola)
- [ ] Ve a **My Account → Security**
- [ ] Click en **Generate Tokens**
- [ ] Copia el token generado

### Paso 2: Guardar SONAR_TOKEN en GitHub
- [ ] Ve a tu repositorio: https://github.com/Diego-Arreola/CleanTeam
- [ ] **Settings → Secrets and variables → Actions**
- [ ] Click en **New repository secret**
- [ ] Nombre: `SONAR_TOKEN`
- [ ] Valor: (pega el token)
- [ ] Click en **Add secret**

### Paso 3: Hacer Push
- [ ] Ejecuta `git add .`
- [ ] Ejecuta `git commit -m "ci: configure github actions and sonarcloud"`
- [ ] Ejecuta `git push origin main`

## 📊 ¿Qué sucede después?

1. **GitHub Actions ejecuta:**
   - ✅ Checkout del código
   - ✅ Setup de Java 17
   - ✅ Maven build + tests con JUnit
   - ✅ Genera reporte de cobertura (JaCoCo)
   - ✅ Envía análisis a SonarCloud

2. **SonarCloud analiza:**
   - 📈 Cobertura de tests
   - 🐛 Bugs potenciales
   - 🔒 Vulnerabilidades
   - 📝 Code smells
   - 📊 Quality Gate

## 🔍 Monitorear el flujo

### En GitHub:
1. Ve a **Actions** en tu repositorio
2. Verás el workflow `Build, Test and SonarCloud Analysis`
3. Click para ver los detalles en tiempo real

### En SonarCloud:
1. Ve a tu proyecto
2. Verás la cobertura actualizada
3. Métricas en el dashboard

## 🐛 Si algo falla

**Error: "SONAR_TOKEN not found"**
→ Asegúrate de haber creado el secret en GitHub con el nombre exacto `SONAR_TOKEN`

**Error: "Project key not found"**
→ Crea el proyecto en SonarCloud si no existe. Verifica que sea: `Diego-Arreola_CleanTeam`

**Error: "Running CI analysis while Automatic Analysis is enabled"**
→ (Solo si no seguiste el paso 3) Deshabilita Automatic Analysis en SonarCloud

## 📝 Configuración Automática

Todos estos archivos ya están listos:
- ✅ `.github/workflows/build-and-test.yml` - Workflow de GitHub Actions (usa SonarCloud action)
- ✅ `sonar-project.properties` - Configuración de SonarCloud
- ✅ `src/test/resources/application-test.properties` - Config de tests con H2
- ✅ `pom.xml` - Maven configurado con JaCoCo para cobertura

Solo necesitas seguir los 3 pasos arriba.
