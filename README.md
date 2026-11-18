# SeniorLink

> Aplicativo Android para a **segurança, autonomia e inclusão digital da pessoa idosa**, conectando o idoso, seus responsáveis e cuidadores.

![Status](https://img.shields.io/badge/status-MVP-orange)
![Plataforma](https://img.shields.io/badge/plataforma-Android-3DDC84)
![Kotlin](https://img.shields.io/badge/Kotlin-Jetpack%20Compose-7F52FF)

O SeniorLink reúne em um só lugar localização, ficha de informações importantes, lembretes, botão de emergência, ligações rápidas e treinamento para uso do celular.

## Sumário

- [Funcionalidades](#funcionalidades)
- [Perfis de uso](#perfis-de-uso)
- [Tecnologias](#tecnologias)
- [Documentação](#documentação)
- [Como executar](#como-executar)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Como contribuir](#como-contribuir)

## Funcionalidades

- **Localização** do idoso com Fused Location Provider e Google Maps.
- **Ficha de informações importantes** (dados de saúde e contatos de confiança).
- **Lembretes** com alarmes para medicações e compromissos.
- **Emergência**: alerta com tela dedicada para acionamento rápido.
- **Ligações rápidas** para contatos de confiança.
- **Jogos e treinamento** para inclusão digital do idoso.

> Algumas funcionalidades ainda estão em desenvolvimento. Consulte o [roadmap](docs/06-roadmap.md) e o [backlog atual](docs/10-backlog-atual.md).

## Perfis de uso

| Perfil | Descrição |
|---|---|
| **Idoso** | Usuário principal do app, com interface simplificada. |
| **Responsável** | Acompanha a localização e as informações do idoso. |
| **Cuidador** | Apoia o dia a dia do idoso com acesso às informações definidas pelo responsável. |

## Tecnologias

- **Kotlin** e **Jetpack Compose** para a interface.
- **Firebase Authentication** para login.
- **Firebase Realtime Database** como banco de dados operacional.
- **Fused Location Provider** e **Google Maps Compose** para localização.

> O Cloud Firestore não é mais usado. Ele foi removido das dependências do aplicativo.

## Documentação

| Documento | Conteúdo |
|---|---|
| [Visão do produto](docs/01-visao-produto.md) | Problema, público e proposta de valor |
| [Estado atual](docs/02-estado-atual.md) | O que já está implementado |
| [Arquitetura](docs/03-arquitetura.md) | Organização técnica do app |
| [Mapa de telas](docs/04-mapa-de-telas.md) | Fluxo entre as telas |
| [Requisitos funcionais](docs/05-requisitos-funcionais.md) | O que o sistema deve fazer |
| [Roadmap](docs/06-roadmap.md) | Próximas etapas |
| [Plano de testes](docs/07-plano-de-testes.md) | Estratégia e casos de teste |
| [Decisões técnicas](docs/08-decisoes-tecnicas.md) | Justificativas das escolhas técnicas |
| [Diário de desenvolvimento](docs/09-diario-de-desenvolvimento.md) | Histórico de progresso |
| [Backlog atual](docs/10-backlog-atual.md) | Tarefas pendentes |
| [Entrega de interfaces (07/10/2026)](docs/11-entrega-interfaces-2026-10-07.md) | Entrega das telas |
| [Auditoria de segurança](docs/12-auditoria-seguranca.md) | Revisão de segurança |
| [Modelo do Firebase](docs/13-modelo-firebase.md) | Estrutura dos dados no Realtime Database |
| [Guia de commits e colaboração](docs/14-guia-commits.md) | Padrões de commits e pull requests |
| [Organização das branches](docs/15-organizacao-branches.md) | Branch oficial, arquivos históricos e fluxo de contribuição |

## Como executar

### Pré-requisitos

- **JDK 17**
- **Android Studio** compatível com `compileSdk 37`
- Um projeto Firebase configurado para o aplicativo Android `com.seniorlink.app`

### Passo a passo

1. Clone o repositório:
   ```bash
   git clone https://github.com/ProgrammerPerederko/SeniorLink.git
   ```
2. Baixe o arquivo `google-services.json` nas configurações do seu aplicativo Android no Firebase Console.
3. Coloque o arquivo localmente em `app/google-services.json`. Esse arquivo é ignorado pelo Git e não deve ser enviado ao repositório.
4. (Opcional) Crie um arquivo `secrets.properties` na raiz com sua chave restrita do Google Maps:
   ```properties
   MAPS_API_KEY=SUA_CHAVE_AQUI
   ```
   Sem essa chave, o mapa não será carregado. O arquivo `secrets.properties` também não deve ser versionado.
5. Abra o projeto no Android Studio e aguarde a sincronização do Gradle.
6. Execute o app em um emulador ou dispositivo físico.

> Os arquivos de configuração Firebase e as chaves locais são fornecidos por cada desenvolvedor no próprio ambiente. Não publique credenciais, tokens ou arquivos de configuração privados.

## Estrutura do projeto

```text
app/src/main/java/com/seniorlink/app/
├── MainActivity.kt   # Ponto de entrada e fluxo principal
├── auth/             # Autenticação (AuthRepository, AuthViewModel)
├── data/             # Acesso ao Firebase Realtime Database
├── location/         # Localização do dispositivo
├── alarm/            # Lembretes, alarmes e alertas de emergência
├── services/         # Serviços em segundo plano
├── model/            # Modelos de dados
└── ui/               # Telas (screens), componentes e tema
```
