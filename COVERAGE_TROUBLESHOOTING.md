# 🔍 Troubleshooting: Coverage Report

## Verificar que JaCoCo genera el reporte

### Localmente:

```bash
# Ejecutar tests con cobertura
mvn clean verify -Dspring.profiles.active=test

# Verificar que el archivo existe
ls -la target/site/jacoco/jacoco.xml
ls -la target/jacoco.exec

# Si el archivo existe, deberías ver algo como:
# -rw-r--r-- ... 12345 Dec  8 23:45 target/site/jacoco/jacoco.xml
```

### En GitHub Actions:

El workflow ejecuta:
```yaml
mvn clean verify -DskipITs -Dspring.profiles.active=test
```

Esto debe generar:
- ✅ `target/site/jacoco/jacoco.xml` (reporte XML)
- ✅ `target/jacoco.exec` (datos en bruto)
- ✅ `target/surefire-reports/` (reportes de tests)

## Si SonarCloud no lee la cobertura

### Checklist:

1. **¿Se generó el reporte JaCoCo?**
   ```bash
   mvn clean verify -Dspring.profiles.active=test
   cat target/site/jacoco/jacoco.xml | head -20
   ```
   Debe mostrar XML con datos de cobertura.

2. **¿Está configurado correctamente en sonar-project.properties?**
   ```properties
   sonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
   ```

3. **¿Tiene los tests configurados con el perfil test?**
   - MandarinplayerApplicationTests debe tener `@ActiveProfiles("test")`
   - application-test.properties debe existir con BD H2

4. **¿El workflow ejecuta verify (no test)?**
   ```yaml
   run: mvn clean verify -DskipITs -Dspring.profiles.active=test
   ```

## Estructura del XML esperado

```xml
<?xml version="1.0" encoding="UTF-8"?>
<report name="MandarinPlayer">
  <package name="com/cleanteam/mandarinplayer/auth">
    <class name="JwtUtils">
      <method name="generateToken">
        <!-- Datos de cobertura -->
      </method>
    </class>
  </package>
</report>
```

Si ves esto, JaCoCo está funcionando correctamente.

## Regenerar reporte en SonarCloud

1. Ve a tu proyecto en SonarCloud
2. Administration → Re-scan this project
3. O simplemente haz push nuevamente a GitHub

SonarCloud debería detectar el nuevo reporte en el siguiente análisis.
