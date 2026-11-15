`# Documentação do SeniorLink

Esta pasta é a fonte de verdade do projeto.

A regra principal é simples: **a documentação deve descrever o que o código realmente faz hoje**. Recursos planejados, mas não implementados, devem ficar identificados como pendentes.

## Ordem recomendada de leitura

1. `01-visao-produto.md` — problema, público e objetivo.
2. `02-estado-atual.md` — estado real do aplicativo.
3. `03-arquitetura.md` — organização técnica.
4. `04-mapa-de-telas.md` — fluxos e navegação.
5. `05-requisitos-funcionais.md` — requisitos do produto.
6. `11-modelo-firebase.md` — estrutura dos dados remotos.
7. `12-auditoria-seguranca.md` — riscos, correções e pendências.
8. `06-roadmap.md` — sequência de entrega.
9. `08-decisoes-tecnicas.md` — decisões arquiteturais.
10. `07-plano-de-testes.md` — critérios de validação.
11. `09-diario-de-desenvolvimento.md` — histórico das mudanças.
12. `10-backlog-atual.md` — tarefas abertas.

## Mapa dos principais arquivos de código

| Arquivo | Responsabilidade |
|---|---|
| `MainActivity.kt` | Navegação raiz e seleção das telas |
| `auth/AuthRepository.kt` | Login, cadastro, Google, recuperação de senha e logout |
| `auth/AuthViewModel.kt` | Estado das operações de autenticação |
| `data/FirestoreRepository.kt` | Acesso ao Firebase Realtime Database; nome histórico |
| `location/LocationRepository.kt` | Captura e publicação da localização |
| `ui/screens/AccessScreens.kt` | Login e cadastro |
| `ui/screens/FeatureScreens.kt` | Perfil, localização, lembretes, emergência e outras funções |
| `ui/screens/ResponsavelScreens.kt` | Painel e gestão do responsável |
| `app/google-services.json` | Configuração do projeto Firebase |
| `local.defaults.properties` | Fallback não secreto para o Maps |
| `secrets.properties` | Chaves locais; não versionar |

## Firebase

O projeto utiliza Firebase Authentication e Firebase Realtime Database.

A estrutura dos nós e os caminhos esperados estão documentados em [11 — Modelo do Firebase](11-modelo-firebase.md).

As Security Rules **não são substituídas pela lógica do Android**. Qualquer acesso a dados sensíveis deve ser autorizado no backend.

## Configuração local

Pré-requisitos principais:

- Android Studio atual;
- JDK 17;
- Android SDK compatível com compileSdk 37;
- `app/google-services.json` do projeto SeniorLink;
- `secrets.properties` local contendo a chave real do Maps.

O repositório oficial é:

`@text
git clone https://github.com/ProgrammerPerederko/SeniorLink.git
cd SeniorLink
`@

Depois, abra a pasta raiz no Android Studio.

## Checklist antes de executar

- [ ] `app/google-services.json` está presente.
- [ ] `secrets.properties` existe localmente.
- [ ] `MAPS_API_KEY` está configurada localmente.
- [ ] dispositivo/emulador está disponível.
- [ ] permissão de localização pode ser concedida durante o teste.

## Checklist depois de implementar uma mudança

1. corrigir ou adicionar o código;
2. testar o fluxo;
3. atualizar `02-estado-atual.md`;
4. atualizar `05-requisitos-funcionais.md` quando um requisito mudar de estado;
5. registrar decisões em `08-decisoes-tecnicas.md`;
6. registrar a mudança em `09-diario-de-desenvolvimento.md`;
7. atualizar `11-modelo-firebase.md` quando a estrutura remota mudar;
8. revisar `12-auditoria-seguranca.md` quando houver impacto de segurança.

## Segurança

Leia [12 — Auditoria de segurança](12-auditoria-seguranca.md) antes de alterar autenticação, localização, banco, permissões, backup ou dados pessoais.
