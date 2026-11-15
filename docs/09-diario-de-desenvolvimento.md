# 09 — Diário de desenvolvimento

## 07/10/2026 — Documentação incorporada ao projeto

A documentação foi criada dentro do repositório para acompanhar o desenvolvimento.

## 15/11/2026 — Revisão de segurança

Foram aplicadas correções relacionadas a segredos, PIN local, backup, logs e identificação por UID na localização.

## 15/11/2026 — Revisão geral de código

### Banco e perfil

- perfil do Idoso alinhado ao Firebase Realtime Database;
- remoção da dependência do Cloud Firestore;
- preferências de acessibilidade passaram a ser persistidas localmente;
- documentação do modelo do Firebase consolidada em `docs/13-modelo-firebase.md`.

### Localização

- Home deixou de escrever no caminho antigo `usuarios/{id}/localizacao`;
- `LocationRepository` passou a ser a fonte de gravação;
- caminho padronizado em `localizacoes/{id}`;
- debug pode usar `id_idoso` antes da migração definitiva do Idoso para Authentication.

### Lembretes

- `idosoId` passou a acompanhar o PendingIntent do lembrete;
- a tela do alarme consegue concluir o registro correto;
- o fluxo de cinco minutos de adiamento permanece local.

### Serviço de emergência

- `AlertaBackgroundService` passou a evitar registro duplicado de listeners em chamadas repetidas de `onStartCommand`;
- listeners são removidos e a coleção interna é limpa no encerramento.

### Documentação

Foram atualizados:

- README;
- estado atual;
- arquitetura;
- mapa de telas;
- requisitos;
- roadmap;
- plano de testes;
- decisões técnicas;
- backlog;
- auditoria;
- modelo Firebase;
- guia de commits.

### Pendências

- Security Rules;
- vínculo por UID;
- autenticação definitiva do Idoso;
- remoção do PIN remoto;
- rotação da chave exposta do Maps;
- testes de acesso cruzado;
- política de retenção de localização.
