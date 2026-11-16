# SeniorLink

Aplicativo Android voltado à segurança, autonomia e inclusão digital da pessoa idosa.

## Objetivo

O SeniorLink combina localização, ficha de informações importantes, lembretes, emergência, ligações rápidas e treinamento para o idoso, responsável e cuidador.

## Estado técnico atual

A aplicação utiliza Kotlin + Jetpack Compose, Firebase Authentication, Firebase Realtime Database, Fused Location Provider e Google Maps Compose. A integração do Google Maps usa uma chave configurada localmente; nenhuma chave pessoal é mantida no repositório.

O fluxo principal é controlado por `MainActivity` e `SplashScreen`. O projeto ainda contém algumas Activities/XML antigos de onboarding que não são usados pelo launcher atual; eles são mantidos como legado até uma limpeza dedicada.

### Perfis atuais

- Idoso
- Responsável
- Cuidador

### Backend atual

O banco operacional é **Firebase Realtime Database**.

O Firebase Cloud Firestore não participa mais do código atual e foi removido das dependências do aplicativo.

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
- [Entrega de interfaces — 07/10/2026](docs/11-entrega-interfaces-2026-10-07.md)
- [Modelo do Firebase](docs/13-modelo-firebase.md)
- [Auditoria de segurança](docs/12-auditoria-seguranca.md)
- [Guia de commits e colaboração](docs/14-guia-commits.md)

## Configuração local

O projeto precisa de:

- JDK 17;
- Android Studio compatível com compileSdk 37;
- `app/google-services.json` do projeto Firebase;
- `secrets.properties` local, somente quando o usuário quiser fornecer sua própria chave do Maps.

`secrets.properties` não deve ser versionado.

O arquivo versionado `local.defaults.properties` contém somente:

```properties
MAPS_API_KEY=DEFAULT_API_KEY
```

A chave antiga que apareceu no histórico foi removida das referências de branch e excluída no Google Cloud. O repositório atual mantém somente placeholders; cada ambiente pode fornecer sua própria chave localmente.

## Estrutura principal

```text
app/src/main/java/com/seniorlink/app/
├── MainActivity.kt
├── auth/
├── data/
├── location/
├── alarm/
├── services/
├── model/
└── ui/
```

> `data/FirestoreRepository.kt` possui nome histórico, mas usa Firebase Realtime Database. A nomenclatura será corrigida em uma mudança dedicada para evitar alterações desnecessárias nesta revisão.

## Regra de trabalho

Toda alteração deve seguir:

`entender → implementar → testar → documentar → commit → pull request`.

Veja o [Guia de commits e colaboração](docs/14-guia-commits.md) antes de criar commits.

## Repositório

https://github.com/ProgrammerPerederko/SeniorLink
