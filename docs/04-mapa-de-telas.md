# 04 — Mapa de telas

## Fluxo ativo

```text
MainActivity
   ↓
Onboarding Compose
   ├── Pular
   ├── Próximo
   └── Começar
          ↓
        Login
       ↙  ↓  ↘
  Idoso Responsável Cuidador
    ↓       ↓       ↓
  Home    Painel   Área do cuidador
```

## Idoso

```text
Home
├── Perfil
├── Localização
├── Lembretes
│   └── Novo lembrete
├── Contatos
├── Emergência
├── Aprender
├── Jogos
└── Configurações
    ├── Perfil
    ├── Contatos
    └── Treinamento
```

## Responsável

```text
Painel do Responsável
├── Idosos vinculados
├── Localização
├── Agenda e lembretes
├── Ficha clínica
├── Contatos
└── Treinamento
```

## Cuidador

```text
Área do cuidador
├── Agenda
├── Localização
├── Emergência
└── Orientações
```

## Componentes ativos

| Tela/Componente | Arquivo | Estado |
|---|---|---|
| Onboarding | `ui/screens/SplashScreen.kt` | ativo |
| Login/Cadastro | `ui/screens/AccessScreens.kt` | ativo |
| Home | `ui/screens/HomeScreen.kt` | ativo |
| Localização | `ui/screens/FeatureScreens.kt` | ativo |
| Lembretes | `ui/screens/FeatureScreens.kt` | ativo |
| Emergência | `ui/screens/FeatureScreens.kt` | ativo |
| Painel Responsável | `ui/screens/ResponsavelScreens.kt` | ativo |
| Cuidador | `ui/screens/CuidadorScreens.kt` | ativo |

## Legado

Os seguintes arquivos antigos não são usados pelo launcher atual:

- `OnboardingActivity.kt`;
- `SplashActivity.kt`;
- `activity_onboarding2.kt`;
- `activity_onboarding3.kt`;
- `activity_onboarding4.kt`;
- XMLs antigos de onboarding/splash.

Eles permanecem apenas como legado até uma limpeza específica.

## Critério de conclusão

Uma tela só pode ser marcada como concluída quando:

- navegação funciona;
- estados vazio e erro foram tratados;
- persistência necessária está funcionando;
- permissões necessárias foram tratadas;
- teste foi executado;
- documentação foi atualizada.
