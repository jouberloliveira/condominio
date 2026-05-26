# Condomínio

Sistema de Gestão de Condomínio — Spring Boot 3 + Thymeleaf + PostgreSQL embarcado.

## Pré-requisitos

- **JDK 21** (Temurin/Oracle/Adoptium). `java -version` deve reportar `21.x`.
- **Maven 3.9+** (`mvn -v`). Não há wrapper `mvnw` neste repositório.

> ⚠️ Lombok 1.18.36 (declarado no `pom.xml`) **não compila em JDK 25+**. Use JDK 21.

Setup rápido com SDKMAN:

```bash
curl -s "https://get.sdkman.io" | bash
sdk install java 21-tem
sdk install maven
```

Se já tem múltiplas JDKs instaladas no macOS:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
export PATH="$JAVA_HOME/bin:$PATH"
```

## Rodando

Dentro da pasta do projeto:

```bash
mvn spring-boot:run
```

App sobe em **http://localhost:8080**. Dashboard em `/`, módulos em `/unidades`,
`/moradores`, `/visitantes`, `/reservas`, `/ocorrencias`.

### Porta customizada

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Build + jar standalone

```bash
mvn clean package
java -jar target/condominio-0.0.1-SNAPSHOT.jar
```

### Testes

```bash
mvn test
```

## Banco de dados

PostgreSQL **embarcado** via `io.zonky.test:embedded-postgres` — não precisa instalar
Postgres local. `application.yml` está com `ddl-auto: create-drop`, ou seja, **o schema é
recriado a cada start** e os dados são descartados ao desligar.

## Estrutura

```
src/main/java/br/com/centralit/condominio/
  CondominioApplication.java     # entrypoint @SpringBootApplication
  controller/                    # MVC controllers (Home + 5 entidades)
  service/
  repository/
  entity/
  enums/
  config/EmbeddedPostgresConfig.java
src/main/resources/
  application.yml
  templates/                     # Thymeleaf (layout/base.html + index + 5 CRUDs)
```

## Parar o app

`Ctrl+C` no terminal onde está rodando, ou:

```bash
kill $(lsof -nP -iTCP:8080 -sTCP:LISTEN -t)
```
