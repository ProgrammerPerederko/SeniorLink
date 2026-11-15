# 08 — Decisões técnicas

## ADR-001 — Evolução incremental

**Decisão:** manter a base atual e evoluir por etapas.

## ADR-002 — Documentação versionada

**Decisão:** manter a documentação técnica e de produto em `docs/`.

## ADR-003 — Firebase como base atual

**Decisão:** utilizar Firebase Authentication e Realtime Database na fase atual.

## ADR-004 — Segurança por camadas

**Decisão:** localização, dados médicos e contatos são tratados como dados sensíveis.

## ADR-005 — Segredos fora do código

**Decisão:** chave real do Maps fica somente em `secrets.properties`.

**Resultado:** `local.defaults.properties` agora contém somente fallback.

**Pendência:** a chave que já esteve no histórico precisa ser rotacionada/revogada.

## ADR-006 — UI não é autorização

**Decisão:** seleção de perfil e rotas no Android não são mecanismos de segurança.

**Motivo:** qualquer cliente potencialmente controlado pelo usuário pode alterar o fluxo local.

## ADR-007 — UID como identidade remota

**Decisão:** quando houver usuário autenticado, dados de localização devem ser armazenados sob o UID.

**Motivo:** reduz o risco de usar um identificador fixo compartilhado.

## ADR-008 — Debug separado de produção

**Decisão:** caminhos e diagnósticos de teste só podem ser usados no debug.

**Resultado:** `idoso_teste`, diagnósticos e logs detalhados foram limitados ao modo debug na camada de localização.

## ADR-009 — Backup de preferências

**Decisão:** não fazer backup automático das SharedPreferences do aplicativo.

**Motivo:** as preferências podem conter identificadores e dados relacionados à sessão.

## Pendências

- Security Rules por UID;
- vínculo Responsável ↔ Idoso;
- autorização de Cuidador;
- estratégia segura para autenticação do Idoso;
- remoção do PIN do banco;
- política de retenção de localização;
- auditoria de acessos.
