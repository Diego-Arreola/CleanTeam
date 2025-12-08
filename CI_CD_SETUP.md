# Configuración de GitHub Actions y SonarCloud

## 📋 Archivos Creados

1. **`.github/workflows/build-and-test.yml`** - Workflow de GitHub Actions
2. **`sonar-project.properties`** - Configuración de SonarQube
3. **`pom.xml`** - Actualizado con plugins JaCoCo y SonarQube
4. **`src/test/resources/application-test.properties`** - Configuración de tests con H2 en memoria

## ⚙️ Configuración Requerida

### 1. Crear `SONAR_TOKEN` en GitHub

1. Accede a [SonarCloud](https://sonarcloud.io/)
2. Inicia sesión con tu cuenta (Diego-Arreola)
3. Ve a **My Account → Security**
4. Genera un nuevo token
5. En tu repositorio de GitHub:
   - Ve a **Settings → Secrets and variables → Actions**
   - Click en **New repository secret**
   - Nombre: `SONAR_TOKEN`
   - Valor: Pega el token generado en SonarCloud

### 2. Configurar el Proyecto en SonarCloud

1. Abre [SonarCloud](https://sonarcloud.io/)
2. Ve a **+ → Analyze new project**
3. Selecciona el repositorio `CleanTeam`
4. Asegúrate que el **project key** sea: `Diego-Arreola_CleanTeam`
5. La **organización** debe ser: `diego-arreola`

## 🔄 Flujo de CI/CD

El workflow se ejecutará automáticamente en los siguientes casos:

- **Push a `main` o `develop`**
- **Pull Request a `main` o `develop`**

### Pasos que ejecuta:

1. ✅ Checkout del código (con historial completo para mejor análisis)
2. ✅ Setup de Java 17 y Maven
3. ✅ Build del proyecto y ejecución de tests con JUnit (usando perfil `test` con H2)
4. ✅ Generación de reporte de cobertura con JaCoCo
5. ✅ Análisis con SonarCloud

## 📊 Configuración de Base de Datos para Tests

Los tests usan **H2 Database** (en memoria) automáticamente:
- Archivo de configuración: `src/test/resources/application-test.properties`
- Base de datos: **SQLite en memoria** (no requiere instalación)
- Se crea y destruye automáticamente para cada ejecución de tests
- No hay dependencias externas en GitHub Actions

**Perfil activado en tests:** `test`

## 📊 Resultados

Después de ejecutar el workflow, podrás ver:

- **En GitHub**: Estado del build en tu PR o commit
- **En SonarCloud**: 
  - Análisis de código
  - Cobertura de tests (actualmente 50% mínimo)
  - Detección de bugs y vulnerabilidades
  - Quality Gate

## 📈 Métricas de Cobertura

El proyecto está configurado para:
- **Mínimo 50% de cobertura de líneas** en packages
- Reporte de JaCoCo en `target/site/jacoco/jacoco.xml`
- Reporte de tests en `target/surefire-reports`

## 🔧 Comandos Locales

Para ejecutar el análisis localmente:

```bash
# Build y tests
mvn clean verify

# Solo tests
mvn test

# Tests con cobertura
mvn clean test jacoco:report

# Análisis con SonarQube local
mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=<TOKEN>
```

## 📝 Notas Importantes

- El token `SONAR_TOKEN` es sensible y no debe compartirse
- Los workflow solo se ejecutan en push/PR a ramas especificadas
- El workflow no se ejecutará hasta que hagas push del workflow file a GitHub
- Actualmente hay 22 tests unitarios que se ejecutan automáticamente

## 🐛 Troubleshooting

### "SONAR_TOKEN not found"
- Asegúrate de haber creado el secret en GitHub
- Verifica que el nombre sea exactamente `SONAR_TOKEN`

### "Project key not found"
- Asegúrate de crear el proyecto en SonarCloud primero
- Verifica que el nombre coincida con: `Diego-Arreola_CleanTeam`

### "No coverage reports found"
- Asegúrate de que JaCoCo esté correctamente configurado en pom.xml
- Ejecuta `mvn clean verify` para generar los reportes
