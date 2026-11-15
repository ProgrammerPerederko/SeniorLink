`# 06 — Roadmap

## Fase 0 — Base

- [x] Projeto Android abre no Android Studio
- [x] Gradle sincroniza
- [x] Home e telas principais
- [x] Documentação versionada

## Fase 1 — Interfaces

- [x] Login
- [x] Cadastro
- [x] Contatos
- [x] Configurações
- [x] Treinamento
- [x] Fluxos de Idoso, Responsável e Cuidador

## Fase 2 — Integrações

- [x] Firebase Authentication por e-mail/senha
- [x] Recuperação de senha
- [x] Login com Google
- [x] Firebase Realtime Database
- [x] GPS
- [x] Google Maps inicial
- [x] Integração de lembretes e emergências
- [x] Sincronização da localização com caminho padronizado
- [x] Documentação do modelo de dados Firebase

## Fase 3 — Segurança

- [x] Remover chave real do fallback versionado
- [x] Proteger arquivos locais de credenciais no Git
- [x] Não armazenar PIN do idoso em SharedPreferences
- [x] Impedir backup automático das SharedPreferences
- [x] Limitar logs de coordenadas ao debug
- [x] Usar UID autenticado para localização em produção
- [x] Evitar escrita com identificador artificial para usuário não autenticado
- [ ] Revogar/rotacionar chave do Maps exposta no histórico
- [ ] Definir Security Rules do Realtime Database
- [ ] Restringir leituras/escritas por auth.uid
- [ ] Criar vínculo Responsável ↔ Idoso por UID
- [ ] Criar autorização específica do Cuidador
- [ ] Substituir autenticação por PIN consultado diretamente no banco
- [ ] Retirar PIN do documento do usuário
- [ ] Remover `idoso_teste` do fluxo final
- [ ] Revisar todo acesso por `idosoId`
- [ ] Testar acesso cruzado entre contas

## Fase 4 — Qualidade

- [ ] Testes unitários para regras de negócio
- [ ] Testes instrumentados
- [ ] Testes em aparelho físico
- [ ] Testes offline/online
- [ ] Testes de permissões
- [ ] Testes de emergência
- [ ] Testes de localização
- [ ] Teste de rotação de tela
- [ ] Teste de encerramento e retomada do aplicativo

## Fase 5 — Entrega

- [ ] README final
- [ ] Manual do idoso
- [ ] Manual do responsável
- [ ] Manual técnico
- [ ] APK de demonstração
- [ ] Checklist final
