# Documentação do SeniorLink

Esta pasta é a fonte de verdade do projeto.

## Como usar

Antes de implementar uma funcionalidade, consulte:

1. `01-visao-produto.md` para entender o problema.
2. `02-estado-atual.md` para saber o que realmente existe.
3. `03-arquitetura.md` para respeitar a estrutura técnica.
4. `05-requisitos-funcionais.md` para validar o comportamento esperado.
5. `06-roadmap.md` para saber a próxima prioridade.

Depois de implementar:

- atualizar o estado atual;
- marcar o requisito como feito;
- registrar testes;
- registrar decisões novas no documento de decisões;
- adicionar uma entrada no diário de desenvolvimento.

A documentação deve descrever o que o código realmente faz, não apenas o que pretendemos fazer.

COMO USAR O CODIGO:

1. Pré-requisitos de Instalação

Certifique-se de ter as seguintes ferramentas instaladas na sua máquina antes de abrir o projeto:

Android Studio (versão mais recente recomendada, ex: Iguana, Jellyfish ou Ladybug).

Java Development Kit (JDK 17 ou superior) (geralmente já embutido no Android Studio).

Android SDK (API mínima compatível: 24 | SDK Alvo: API 34+).

2. Clonando o Repositório

Abra o seu terminal (ou Git Bash) e clone o repositório oficial:

git clone https://github.com/SEU_USUARIO/SeniorLink.git
cd SeniorLink


3. Configuração Crucial: Firebase (google-services.json)

O aplicativo utiliza Firebase Realtime Database, Firestore e Firebase Auth para sincronização em tempo real e autenticação.

Como o arquivo de chaves google-services.json contém dados sensíveis da API do Google, ele pode não estar exposto publicamente no repositório principal.

Solicite o arquivo google-services.json atualizado com o líder do projeto ou acesse o console do Firebase do SeniorLink.

Cole o arquivo na pasta correta do projeto local:
app/google-services.json

Atenção: Se esse arquivo não estiver na pasta app/, o projeto apresentará erros de compilação relacionados aos serviços do Google.

4. Abrindo e Executando no Android Studio

Abra o Android Studio.

Selecione Open (Abrir) e navegue até a pasta raiz do projeto SeniorLink.

Aguarde o Gradle sincronizar todas as dependências automaticamente (pode levar alguns minutos na primeira vez).

Caso ocorra algum erro de dependência, clique em File > Sync Project with Gradle Files ou execute um Clean Project seguido de Rebuild Project.

Conecte um dispositivo físico via USB (com a Depuração USB ativada) ou abra um Emulador Android (AVD) com API 30 ou superior.

Clique no botão de Play (Run 'app') verde na barra superior.

5. Arquitetura e Correções Recentes Implementadas

Para quem for continuar o desenvolvimento ou correções:

Gerenciamento de Estado de Telas (MainActivity): Utiliza rememberSaveable para evitar que o app volte para a tela inicial ao girar o dispositivo.

Sistema de Alarmes e Emergências (AlarmeDisparoEmergenciaActivity & AlertaBackgroundService):

O serviço em segundo plano monitora o Realtime Database com controle rigoroso de transição de estado (!ultimoEstado), eliminando loops infinitos.

O alarme possui timeout de segurança de 3 minutos e encerra automaticamente ao receber o sinal de resolução.

Painel do Responsável (PainelResponsavelScreen): Conta com monitoramento em tempo real de múltiplos idosos vinculados e botão instantâneo de "PARAR ALARME" sincronizado globalmente.


## Segurança

A revisão de segurança mais recente está em [12 — Auditoria de segurança](12-auditoria-seguranca.md).

Antes de mexer em autenticação, localização, banco, permissões ou dados pessoais, consulte essa auditoria e as pendências registradas.
