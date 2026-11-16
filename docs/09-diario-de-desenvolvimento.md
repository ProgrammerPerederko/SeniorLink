`# 09 — Diário de desenvolvimento

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
- documentação de segurança criada.

## 15/11/2026 — Correção da integração do Realtime Database

### Problemas encontrados

1. `PerfilVinculadoFirestore` já não possuía o campo `pin`, mas `ResponsavelScreens.kt` ainda tentava exibi-lo e preenchê-lo na instância do modelo.
2. `LocationRepository` gravava a localização em `localizacoes/{uid}`, enquanto o painel do responsável lia `usuarios/{idosoId}/localizacao`.
3. O fluxo de emergência repetia a escrita da localização no caminho antigo.
4. O repositório usava `__unauthenticated__` como pseudo-UID em operações autenticadas.
5. A consulta do responsável carregava `usuarios` inteiro antes de filtrar pelo e-mail.

### Correções aplicadas no código

- removidas as referências quebradas ao campo `pin`;
- localização padronizada em `localizacoes/{id}`;
- emergência atualizada para usar o mesmo caminho de localização;
- fallback de debug passou a usar o `id_idoso` salvo localmente quando disponível;
- operações do repositório que exigem autenticação deixam de usar pseudo-UID;
- consulta de idosos do responsável passou a usar `orderByChild("emailResponsavel")`;
- adicionadas validações para operações que exigem sessão autenticada;
- criado `docs/11-modelo-firebase.md` como referência do banco.

### Pendências

- Security Rules;
- vínculo por UID entre Responsável/Cuidador e Idoso;
- autenticação definitiva do Idoso;
- remoção do PIN remoto;
- rotação da chave do Maps exposta no histórico;
- teste de acesso cruzado entre contas.
