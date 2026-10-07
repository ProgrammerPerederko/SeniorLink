# 03 — Arquitetura

## Visão atual

A arquitetura atual é baseada em uma Activity principal com Compose e uma pequena camada de Activities XML para o fluxo de entrada.

### Fluxo atual

`SplashActivity → Onboarding 2 → Onboarding 3 → Onboarding 4 → MainActivity`

A `MainActivity` mantém um estado simples de tela e direciona para as telas Compose.

## Estrutura

```
app/src/main/java/com/seniorlink/app/
├── MainActivity.kt
├── SplashActivity.kt
├── activity_onboarding2.kt
├── activity_onboarding3.kt
├── activity_onboarding4.kt
└── ui/
    ├── components/
    ├── screens/
    └── theme/
```

## Papel dos principais arquivos

### MainActivity

Controla a tela atual do fluxo Compose e encaminha destinos como:

- inicio
- perfil
- localizacao
- lembretes
- aprender
- emergencia

### SplashActivity e onboarding

São responsáveis pelo fluxo visual inicial e pela transição para a Home.

### ui/screens

Concentra as telas de interface.

### ui/components

Concentra componentes reutilizáveis.

### ui/theme

Concentra cores, tipografia e tema.

## Arquitetura desejada para a próxima fase

Evoluir gradualmente para:

```
UI
 ↓
ViewModel
 ↓
Repository
 ↓
Local data / Remote data
 ↓
Backend e serviços externos
```

A migração deve ser incremental. Não reescrever o projeto inteiro de uma vez.

## Dados

### Primeira fase

Persistência local no aparelho.

### Segunda fase

Backend e autenticação.

### Terceira fase

Sincronização entre idoso e responsável.

## Segurança

Dados médicos, contatos e localização são informações sensíveis. A implementação futura deve restringir acesso e transmissão.
