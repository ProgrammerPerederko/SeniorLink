# SeniorLink

Aplicativo Android voltado à segurança, autonomia e inclusão digital da pessoa idosa.

## Objetivo

O SeniorLink combina localização, ficha de informações importantes, lembretes, emergência, ligações rápidas e treinamento para o idoso e para o responsável.

## Estado atual

A base atual utiliza Kotlin + Jetpack Compose, Firebase Authentication, Firebase Realtime Database, localização do dispositivo e integração inicial com Google Maps.

Existem fluxos distintos para:

- Idoso
- Responsável
- Cuidador

A documentação do projeto fica dentro de `docs/` e deve acompanhar o código.

## Documentação

- [Visão do produto](docs/01-visao-produto.md)
- [Estado atual](docs/02-estado-atual.md)
- [Arquitetura](docs/03-arquitetura.md)
- [Mapa de telas](docs/04-mapa-de-telas.md)
- [Requisitos funcionais](docs/05-requisitos-funcionais.md)
- [Roadmap](docs/06-roadmap.md)
- [Plano de testes](docs/07-plano-de-testes.md)
- [Decisões técnicas](docs/08-decisoes-tecnicas.md)
- [Diário de desenvolvimento](docs/09-diario-de-desenvolvimento.md)
- [Backlog atual](docs/10-backlog-atual.md)
- [Auditoria de segurança](docs/12-auditoria-seguranca.md)

## Configuração de segurança

A chave real do Google Maps deve ficar somente em:

```
secrets.properties
```

Esse arquivo não deve ser versionado.

O arquivo versionado `local.defaults.properties` contém apenas:

```properties
MAPS_API_KEY=DEFAULT_API_KEY
```

Uma chave real do Maps foi encontrada anteriormente nesse arquivo e deve ser considerada exposta. Ela precisa ser revogada/rotacionada no Google Cloud. Remover o valor do arquivo atual não apaga a exposição do histórico do Git.

## Regra de trabalho

Toda nova funcionalidade deve passar pelo ciclo:

`Entender problema → documentar decisão → implementar → testar → atualizar documentação → registrar mudança`.

## Tecnologias atuais

- Kotlin
- Jetpack Compose
- Android Gradle Plugin
- Gradle Version Catalog
- Java/JVM 17
- Firebase Authentication
- Firebase Realtime Database
- Firebase Firestore
- Firebase Cloud Messaging
- Firebase Storage
- Google Maps Compose
- Google Play Services Location

## Repositório

https://github.com/ProgrammerPerederko/SeniorLink
