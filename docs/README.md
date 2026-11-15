# Documentação do SeniorLink

Esta pasta é a fonte de verdade do projeto.

A regra principal é: **documentar o comportamento real do código**. Funcionalidades planejadas não devem ser descritas como concluídas.

## Ordem recomendada

1. `01-visao-produto.md` — problema e objetivo.
2. `02-estado-atual.md` — o que existe hoje.
3. `03-arquitetura.md` — estrutura técnica.
4. `04-mapa-de-telas.md` — navegação atual.
5. `05-requisitos-funcionais.md` — requisitos e estado de implementação.
6. `13-modelo-firebase.md` — contrato atual do Realtime Database.
7. `12-auditoria-seguranca.md` — riscos e pendências.
8. `06-roadmap.md` — sequência de trabalho.
9. `08-decisoes-tecnicas.md` — decisões arquiteturais.
10. `07-plano-de-testes.md` — validação.
11. `09-diario-de-desenvolvimento.md` — histórico.
12. `10-backlog-atual.md` — tarefas abertas.
13. `14-guia-commits.md` — padrão de commits, branches e PRs.

## Mapa dos principais arquivos

| Arquivo | Responsabilidade |
|---|---|
| `MainActivity.kt` | Navegação principal e estado de sessão |
| `auth/AuthRepository.kt` | Firebase Authentication |
| `auth/AuthViewModel.kt` | Estado de autenticação |
| `data/FirestoreRepository.kt` | Realtime Database; nome histórico |
| `location/LocationRepository.kt` | GPS e publicação de localização |
| `model/IdosoState.kt` | Perfil local, preferências e persistência de perfil no Realtime Database |
| `ui/screens/AccessScreens.kt` | Login e cadastro |
| `ui/screens/SplashScreen.kt` | Onboarding Compose ativo |
| `ui/screens/FeatureScreens.kt` | Perfil, localização, lembretes e emergência |
| `ui/screens/ResponsavelScreens.kt` | Painel do responsável |
| `ui/screens/CuidadorScreens.kt` | Painel do cuidador |
| `alarm/LembreteAlarmManager.kt` | Agendamento e receiver de lembretes |
| `alarm/AlarmeDisparoActivity.kt` | Tela de lembrete na tela bloqueada |
| `alarm/AlarmeDisparoEmergenciaActivity.kt` | Tela de alerta de emergência |
| `services/AlertaBackgroundService.kt` | Monitoramento de emergência em segundo plano |
| `app/google-services.json` | Configuração Firebase |
| `local.defaults.properties` | Fallback não secreto do Maps |
| `secrets.properties` | Segredos locais; nunca versionar |

## Banco

O backend operacional atual é o Firebase Realtime Database.

O modelo está documentado em [13 — Modelo do Firebase](13-modelo-firebase.md).

## Checklist antes de abrir o projeto

- [ ] JDK 17 configurado.
- [ ] `app/google-services.json` presente.
- [ ] `secrets.properties` presente.
- [ ] `MAPS_API_KEY` configurada localmente.
- [ ] Android SDK 37 disponível.

## Checklist depois de alterar código

1. testar o fluxo alterado;
2. atualizar o estado atual quando necessário;
3. atualizar requisitos/roadmap se o status mudou;
4. registrar decisão arquitetural quando aplicável;
5. registrar o diário;
6. atualizar o modelo Firebase se o banco mudou;
7. atualizar a auditoria se houver impacto de segurança;
8. criar commit seguindo `docs/14-guia-commits.md`.

## Segurança

Leia `docs/12-auditoria-seguranca.md` antes de alterar autenticação, localização, banco, permissões, backup ou dados pessoais.
