# 09 — Diário de desenvolvimento

## 07/10/2026 — Documentação incorporada ao projeto

A documentação foi criada dentro do repositório para acompanhar o desenvolvimento.

## 15/11/2026 — Revisão de segurança

### Objetivo

Revisar o código publicado e corrigir riscos que podiam ser tratados diretamente no aplicativo sem alterar ainda as Security Rules do Firebase.

### Alterações

- removida a chave real do Maps do `local.defaults.properties`;
- reforçado o `.gitignore`;
- removido armazenamento local do PIN;
- desabilitado backup automático das SharedPreferences;
- limitado logs detalhados de localização ao debug;
- limitado gravação diagnóstica ao debug;
- localização autenticada passou a utilizar UID;
- `idoso_teste` ficou restrito ao debug;
- documentação atualizada com as pendências de autorização.

### Pendências

- rotação da chave exposta no histórico;
- Security Rules;
- vínculo entre perfis;
- autenticação segura do idoso;
- remoção do PIN do banco;
- revisão de acessos por `idosoId`.

### Próximo passo

Fazer a camada de segurança do Firebase e testar acesso cruzado entre contas.

