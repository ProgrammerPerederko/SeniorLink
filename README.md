# SeniorLink

Aplicativo Android voltado à segurança, autonomia e inclusão digital da pessoa idosa.

## Objetivo

O SeniorLink combina localização, ficha de informações importantes, lembretes, emergência, ligações rápidas e treinamento para o idoso e para o responsável.

## Estado atual

Esta branch contém a base do projeto Android desenvolvido em Kotlin + Jetpack Compose, com Splash, onboarding, Home e telas de Perfil, Localização, Lembretes, Aprender e Emergência.

A documentação do projeto fica dentro de `docs/` e deve ser atualizada junto com o código.

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

## Regra de trabalho

Toda nova funcionalidade deve passar por este ciclo:

`Entender problema → documentar decisão → implementar → testar → atualizar documentação → registrar mudança`.

## Tecnologias atuais

- Kotlin
- Jetpack Compose
- Android Gradle Plugin
- Gradle Version Catalog
- Android SDK
- AppCompat para as Activities XML existentes

## Repositório

https://github.com/ProgrammerPerederko/SeniorLink
