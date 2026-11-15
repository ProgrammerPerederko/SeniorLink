# 08 — Decisões técnicas

## ADR-001 — Evolução incremental

**Decisão:** manter a base atual e evoluir por etapas.

## ADR-002 — Documentação versionada

**Decisão:** manter a documentação técnica e de produto em `docs/`.

## ADR-003 — Realtime Database como backend atual

**Decisão:** utilizar Firebase Authentication + Firebase Realtime Database.

**Motivo:** os fluxos de localização, lembretes, contatos e emergência já utilizam o Realtime Database.

**Resultado:** a persistência do perfil foi alinhada ao mesmo banco e a dependência do Cloud Firestore foi removida.

## ADR-004 — Segurança por camadas

**Decisão:** localização, dados médicos, contatos e emergência são dados sensíveis.

A UI nunca deve ser tratada como autorização.

## ADR-005 — Segredos fora do código

**Decisão:** a chave real do Maps fica somente em `secrets.properties`.

**Pendência:** a chave anteriormente exposta no histórico precisa ser revogada/rotacionada.

## ADR-006 — UI não é autorização

A seleção de perfil e as rotas da `MainActivity` controlam experiência, não segurança.

A autorização definitiva deverá ficar no backend.

## ADR-007 — UID para identidade autenticada

Quando houver Firebase Authentication, o UID é a identidade remota preferencial.

A localização autenticada usa `localizacoes/{auth.uid}`.

## ADR-008 — Debug separado de produção

`idoso_teste`, diagnóstico detalhado e logs de coordenadas detalhados são restritos ao debug.

## ADR-009 — Preferências locais

A fonte, o alto contraste e a leitura de voz são preferências locais e agora são persistidas em SharedPreferences.

## ADR-010 — Dono explícito dos alarmes

O agendamento do lembrete leva o `idosoId` junto ao PendingIntent.

**Motivo:** a tela de alarme precisa saber qual registro remoto deve ser atualizado quando o usuário concluir o lembrete.

## ADR-011 — Evitar listeners duplicados no serviço

`AlertaBackgroundService` não deve registrar o mesmo conjunto de listeners novamente quando `onStartCommand` for chamado mais de uma vez.

## Pendências arquiteturais

- Security Rules por UID;
- vínculo Responsável ↔ Idoso;
- autorização de Cuidador;
- autenticação segura do Idoso;
- remoção do PIN;
- remoção de `idoso_teste`;
- retenção da localização;
- auditoria de acesso.
