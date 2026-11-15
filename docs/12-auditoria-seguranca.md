`# 12 — Auditoria de segurança

> Revisão do código e da configuração versionada em 15/11/2026.

## Resultado

O projeto possui uma camada funcional de Firebase, mas ainda não deve ser considerado pronto para produção.

Esta revisão concentra-se em correções que podem ser aplicadas no aplicativo sem assumir que as Security Rules do console já estejam corretas.

## Correções aplicadas

### Segredos

- `local.defaults.properties` não contém mais uma chave real do Maps.
- `secrets.properties` continua sendo arquivo local.
- `.gitignore` foi reforçado para credenciais e certificados.
- A chave que já apareceu no histórico continua sendo tratada como exposta e precisa ser rotacionada/revogada.

### PIN

- o PIN deixou de ser salvo em `SharedPreferences`;
- o fluxo atual ainda mantém o PIN no Realtime Database para compatibilidade;
- o login do Idoso continua consultando o PIN diretamente no banco;
- essa estratégia é temporária e não pode ser usada como autenticação de produção.

### Identidade e repositório

- operações do repositório que dependem de usuário autenticado não usam mais o pseudo-UID `__unauthenticated__`;
- consultas de idosos do responsável são filtradas pelo e-mail da sessão autenticada;
- operações de atualização de ficha verificam a existência de sessão.

Essas validações melhoram o comportamento do cliente, mas **não substituem Security Rules**.

### Localização

- em produção, a localização utiliza `localizacoes/{auth.uid}`;
- em debug, o fluxo atual por PIN pode usar `id_idoso` local para testar `localizacoes/{id}`;
- `idoso_teste` permanece somente como fallback de debug;
- gravação diagnóstica ocorre somente em debug;
- logs detalhados de coordenadas ficam limitados ao debug;
- o painel do responsável agora lê o mesmo caminho que o produtor utiliza.

### Emergência

- a localização da emergência usa o nó padronizado `localizacoes/{idosoId}`;
- o estado do alerta continua disponível em `emergencias/{idosoId}`;
- o espelho `usuarios/{idosoId}/emergencia_ativa` continua para compatibilidade com as telas e serviços atuais.

### Backup

- `SharedPreferences` foi excluído do backup automático em nuvem e transferência entre dispositivos.

## Riscos ainda existentes

1. A chave do Maps exposta no histórico precisa ser revogada/rotacionada.
2. As Security Rules do Realtime Database precisam ser revisadas e versionadas.
3. A seleção de perfil ainda é um mecanismo de navegação, não de autorização.
4. Responsável e Cuidador precisam de vínculos explícitos com idosos usando UID.
5. Operações que recebem `idosoId` precisam ser protegidas no servidor.
6. O PIN ainda existe no modelo remoto.
7. O Idoso ainda não possui uma identidade Firebase equivalente ao fluxo de e-mail/senha.
8. `idoso_teste` precisa ser removido quando a autenticação definitiva estiver pronta.
9. A retenção de localização e dados de emergência ainda não está definida.
10. O acesso cruzado entre contas ainda precisa de teste formal.

## Critério para produção

O SeniorLink não deve ser considerado pronto para produção enquanto:

- a chave exposta não for rotacionada;
- as Security Rules não limitarem leitura/escrita por identidade e vínculo;
- o PIN não tiver sido removido do modelo remoto;
- os vínculos Responsável/Idoso e Cuidador/Idoso não estiverem definidos no backend;
- o acesso cruzado entre contas não tiver sido testado;
- a política de retenção de localização não estiver definida.

## Próxima etapa

Implementar, testar e versionar a camada de autorização:

`@text
Responsável → Idoso
Cuidador → Idoso
`@

usando `auth.uid` como identidade.
