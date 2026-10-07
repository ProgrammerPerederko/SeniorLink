# 08 — Decisões técnicas

## ADR-001 — Manter a base atual

**Decisão:** não reescrever o aplicativo inteiro.

**Motivo:** a base atual já possui identidade visual, onboarding, Home e componentes Compose. Vamos evoluir em incrementos.

## ADR-002 — Documentação dentro do repositório

**Decisão:** a documentação técnica e de produto fica em `docs/`.

**Motivo:** o código e a documentação precisam evoluir juntos e permanecer versionados.

## ADR-003 — MVP local antes do backend

**Decisão:** implementar primeiro persistência e funcionalidades locais.

**Motivo:** permite validar a experiência no aparelho antes de introduzir servidor e sincronização.

## ADR-004 — Não considerar interface como funcionalidade concluída

**Decisão:** uma tela só passa para concluída depois de comportamento e teste.

**Motivo:** evita confundir protótipo visual com produto funcional.

## ADR-005 — Segurança por camadas

**Decisão:** localização, dados médicos e contatos devem ser tratados como dados sensíveis.

**Motivo:** o produto envolve segurança de pessoas e informações pessoais.

## Próximas decisões a registrar

- tecnologia do backend;
- estratégia de autenticação;
- banco de dados;
- mecanismo de sincronização;
- política de retenção de localização;
- modelo de autorização do responsável.
