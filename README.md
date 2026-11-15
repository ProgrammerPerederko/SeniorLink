`# SeniorLink

Aplicativo Android voltado à segurança, autonomia e inclusão digital da pessoa idosa.

## Objetivo

O SeniorLink combina localização, ficha de informações importantes, lembretes, emergência, ligações rápidas e treinamento para o idoso e para o responsável.

## Estado atual

A base atual utiliza:

- Kotlin + Jetpack Compose;
- Firebase Authentication;
- Firebase Realtime Database;
- Fused Location Provider;
- Google Maps Compose;
- alarmes e notificações do Android.

Existem fluxos distintos para:

- Idoso;
- Responsável;
- Cuidador.

A documentação do projeto fica em `docs/` e deve refletir o comportamento real do código.

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
- [Modelo do Firebase](docs/11-modelo-firebase.md)
- [Auditoria de segurança](docs/12-auditoria-seguranca.md)

## Firebase e segredos

O aplicativo precisa do arquivo:

```text
app/google-services.json
```

Esse arquivo deve corresponder ao projeto Firebase usado pelo SeniorLink.

A chave real do Google Maps deve ficar somente em:

```text
secrets.properties
```

Esse arquivo não deve ser versionado.

O arquivo versionado `local.defaults.properties` contém somente:

```properties
MAPS_API_KEY=DEFAULT_API_KEY
```

Uma chave real do Maps foi encontrada anteriormente no histórico do Git e deve ser considerada exposta. Ela precisa ser revogada/rotacionada no Google Cloud. Remover o valor do arquivo atual não elimina a exposição do histórico.

## Estrutura principal do código

```text
app/src/main/java/com/seniorlink/app/
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
    ├── screens/
    └── theme/
```

> `FirestoreRepository.kt` usa Realtime Database apesar do nome histórico. A renomeação é uma melhoria técnica pendente.

## Configuração local

Use JDK 17.

O projeto define:

- minSdk 24;
- compileSdk 37;
- targetSdk 37;
- Java/JVM 17.

No Android Studio:

1. abra a pasta raiz do projeto;
2. aguarde a sincronização do Gradle;
3. confirme que `app/google-services.json` existe;
4. mantenha a chave real do Maps em `secrets.properties`;
5. execute o app em um dispositivo ou emulador com localização habilitada.

## Regra de trabalho

Toda nova funcionalidade deve passar pelo ciclo:

`Entender problema → documentar decisão → implementar → testar → atualizar documentação → registrar mudança`.

## Segurança

A documentação de segurança e as pendências estão em [12 — Auditoria de segurança](docs/12-auditoria-seguranca.md).

Antes de alterar autenticação, localização, banco, permissões ou dados pessoais, consulte também o [Modelo do Firebase](docs/11-modelo-firebase.md).

## Repositório

https://github.com/ProgrammerPerederko/SeniorLink
