# 10 — Backlog atual

> Revisado em 16/11/2026. A ordem abaixo considera primeiro o que impede uma entrega confiável e segura.

## P0 — bloqueadores de estabilidade e entrega

- [ ] Fazer o Android CI concluir assembleDebug no GitHub Actions
- [ ] Fazer o Android CI concluir test
- [ ] Validar o ambiente CI com JDK 17 + AGP 9.3.1 + Gradle 9.6
- [ ] Registrar no PR a evidência de build/teste aprovado
- [ ] Testes automatizados dos fluxos críticos
- [ ] Testes no dispositivo físico dos fluxos de localização e emergência
- [ ] Edição completa de lembretes
- [ ] Repetição configurável dos lembretes

## P1 — segurança

- [ ] Security Rules do Realtime Database
- [ ] Vínculo Responsável → Idoso por UID
- [ ] Vínculo Cuidador → Idoso por UID
- [ ] Autenticação definitiva do Idoso
- [ ] Remover PIN remoto
- [ ] Remover idoso_teste
- [ ] Testar acesso cruzado
- [ ] Rotacionar chave do Maps exposta no histórico
- [ ] Definir retenção de localização
- [ ] Auditoria de acesso aos dados sensíveis

## P1 — acessibilidade

- [x] Escala de fonte
- [x] Alto contraste
- [x] Persistência das preferências
- [ ] Completar leitura por voz nas funções críticas
- [ ] Teste formal de acessibilidade no aparelho
- [ ] Revisão de tamanhos de toque

## P2 — qualidade técnica

- [ ] Testes unitários
- [ ] Testes instrumentados
- [ ] Testes offline/online
- [ ] Testes de permissões
- [ ] Testes de localização
- [ ] Testes de lembretes
- [ ] Testes de emergência
- [ ] Testes de acesso cruzado
- [ ] Validar APK de debug gerado pelo CI
- [ ] Criar artefato de APK no pipeline após o build ficar verde

## P3 — produto

- [ ] Treinamento avançado do idoso
- [ ] Central de ajuda
- [ ] Histórico de localização com retenção definida
- [ ] Logs de segurança
- [ ] Telemetria de erros
- [ ] Publicação em loja

## Regra de prioridade

Segurança crítica → estabilidade do build → qualidade/testes → acessibilidade → funcionalidades novas → refinamento.

## Critério para mover uma tarefa para concluída

Uma tarefa só deve ser marcada como [x] quando houver:

1. implementação no código;
2. validação correspondente;
3. documentação atualizada quando aplicável;
4. evidência no commit/PR.
