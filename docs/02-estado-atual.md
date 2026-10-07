# 02 — Estado atual

> Levantamento da base existente no repositório `main` em 07/10/2026.

## Estrutura atual

O projeto é um aplicativo Android em Kotlin + Jetpack Compose, com algumas Activities e layouts XML usados pelo fluxo de Splash/Onboarding.

## Configuração observada

- namespace/applicationId: `com.seniorlink.app`
- minSdk: 24
- targetSdk: 37
- compileSdk: 37
- Kotlin: 2.2.10
- Android Gradle Plugin: 9.4.1
- Compose BOM: 2026.02.01
- Java source/target: 11

## Entrada do aplicativo

O Manifest atual define `SplashActivity` como launcher.

A `MainActivity` é aberta como Activity interna após o fluxo inicial.

## Telas existentes na base

- Splash XML
- Onboarding 2 XML
- Onboarding 3 XML
- Onboarding 4 XML
- SplashScreen Compose
- HomeScreen Compose
- PerfilScreen Compose
- LocalizacaoScreen Compose
- LembretesScreen Compose
- AprenderScreen Compose
- EmergenciaScreen Compose

## O que está pronto como interface

A base já apresenta visual do SeniorLink, Home com cartões de acesso, navegação entre Home e telas secundárias, onboarding visual e componentes Compose reutilizáveis.

## O que ainda é protótipo

As telas de Perfil, Localização, Lembretes e Emergência ainda contêm comportamentos demonstrativos. O código atual dessas telas inclui textos que explicitam que persistência, GPS, notificações e chamadas reais ainda não estão implementados.

A Home também possui saudação fixa com o nome Maria.

## Lacunas prioritárias

1. Login e cadastro.
2. Persistência de perfil.
3. Localização real e compartilhamento com responsável.
4. Lembretes persistentes e notificações.
5. Emergência e ligação rápida.
6. Contatos de confiança.
7. Configurações e acessibilidade.
8. Treinamento do responsável.
9. Sincronização entre aparelhos.
10. Backend/autenticação para uma versão conectada.

## Regra

Não marcar uma funcionalidade como concluída apenas porque existe uma tela. Ela só será considerada concluída quando existir comportamento testado e documentado.
