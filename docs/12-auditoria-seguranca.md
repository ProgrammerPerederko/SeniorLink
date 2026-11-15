# 12 — Auditoria de segurança

> Revisão realizada em 15/11/2026 sobre o estado publicado do SeniorLink.

## Resultado

O projeto tinha riscos que precisavam de correção antes de uma versão de produção. Esta branch aplica as correções que podem ser feitas com segurança no código e na configuração do aplicativo.

## Correções aplicadas

### Segredos

- `local.defaults.properties` não contém mais chave real do Maps.
- `secrets.properties` continua tratado como arquivo local.
- `.gitignore` foi reforçado para credenciais e certificados locais.

### PIN

- o PIN deixou de ser salvo em `SharedPreferences`.
- o fluxo atual ainda consulta o PIN no Realtime Database; isso continua sendo uma pendência de segurança porque a autorização do banco precisa impedir consultas indevidas.
- a solução definitiva deverá retirar o PIN do documento do usuário e substituir a autenticação por uma estratégia apropriada.

### Localização

- com usuário autenticado, a localização é gravada em `localizacoes/{uid}`.
- `localizacoes/idoso_teste` só é usado em debug.
- gravação diagnóstica do Firebase só ocorre em debug.
- logs detalhados de coordenadas só ocorrem em debug.

### Backup

- `SharedPreferences` foi excluído do backup automático em nuvem e transferência entre dispositivos.

## Riscos ainda existentes

1. A chave do Maps que esteve no histórico do Git precisa ser revogada/rotacionada.
2. As Security Rules do Realtime Database precisam ser revisadas e versionadas.
3. O aplicativo ainda permite escolher o perfil na interface; a autorização real precisa vir do backend.
4. O acesso de responsável/cuidador precisa ser definido por vínculo explícito com o UID autenticado.
5. Operações que recebem `idosoId` precisam ser protegidas pelas regras do servidor.
6. O PIN ainda existe no modelo de dados remoto e é usado no login do idoso.
7. O caminho `idoso_teste` precisa ser removido quando o fluxo autenticado da localização estiver concluído.
8. `FirestoreRepository.kt` usa Realtime Database e deve receber correção de nomenclatura posteriormente.

## Critério para produção

O SeniorLink não deve ser considerado pronto para produção enquanto:

- a chave exposta não for rotacionada;
- leitura/escrita do Realtime Database não estiver protegida por identidade e vínculo;
- o PIN não tiver sido removido do modelo remoto;
- o acesso cruzado entre contas não tiver sido testado.

## Próxima etapa

Implementar a camada de Security Rules e o modelo de vínculo entre:

```
Responsável → Idoso
Cuidador → Idoso
```

usando `auth.uid` como identidade.
