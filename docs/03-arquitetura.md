# 03 — Arquitetura

## Visão

O projeto segue uma arquitetura simples por camadas:

```text
Compose UI
   ↓
estado da tela / ViewModel
   ↓
Repository
   ↓
Firebase / serviços Android
```

## Estrutura

```text
app/src/main/java/com/seniorlink/app/
├── MainActivity.kt
├── auth/
│   ├── AuthRepository.kt
│   └── AuthViewModel.kt
├── data/
│   └── FirestoreRepository.kt
├── location/
│   └── LocationRepository.kt
├── alarm/
├── services/
├── model/
└── ui/
    ├── components/
    ├── screens/
    └── theme/
```

> `FirestoreRepository.kt` é um nome histórico. A implementação usa Realtime Database.

## Navegação

`MainActivity` controla as rotas por estado.

O fluxo inicial é:

```text
MainActivity
   ↓
SplashScreen / onboarding Compose
   ↓
Login
   ↓
Cadastro ou Home
```

O launcher não usa `SplashActivity` nem `OnboardingActivity`.

## Autenticação

`AuthRepository` concentra Firebase Authentication.

`AuthViewModel` expõe o estado das operações para Compose.

A seleção de perfil na UI define o fluxo de navegação, mas não é autorização de backend.

## Dados

O banco operacional é Firebase Realtime Database.

Principais áreas:

- `usuarios`;
- `localizacoes`;
- `emergencias`;
- `ranking_idosos`;
- `diagnostico`.

O contrato está em [13 — Modelo do Firebase](13-modelo-firebase.md).

## Perfil

`model/IdosoState.kt` usa Realtime Database para o perfil e SharedPreferences para preferências locais.

O perfil remoto mantém o registro existente e usa `updateChildren`, preservando campos como vínculo e PIN legado.

## Localização

`LocationRepository` é a única camada responsável por publicar localização.

Em produção:

```text
localizacoes/{auth.uid}
```

Em debug do fluxo legado:

```text
localizacoes/{id_idoso}
```

ou, apenas como fallback:

```text
localizacoes/idoso_teste
```

## Lembretes

`LembreteScheduler` cria o PendingIntent do lembrete e agora transporta também o `idosoId`.

`AlarmeDisparoActivity` usa esse identificador para concluir o lembrete no registro correto.

## Emergência

`AlertaBackgroundService` monitora os registros autorizados de emergência.

O serviço possui proteção para não registrar listeners novamente quando `onStartCommand` for chamado múltiplas vezes.

## Segurança

A autorização final deve ser feita no Firebase Security Rules.

O Android não deve ser tratado como fonte definitiva de autorização.

`idosoId`, e-mail e perfil selecionado pela UI são identificadores de fluxo até que o vínculo por UID seja implementado no backend.
