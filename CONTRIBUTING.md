# Contribuindo com o SeniorLink

O SeniorLink usa o fluxo:

`branch → código → teste → documentação → commit → Pull Request → revisão → main`

## Antes de começar

```powershell
git checkout main
git pull origin main
git checkout -b fix/minha-tarefa
```

## Commits

Siga `docs/14-guia-commits.md`.

Exemplos:

- `feat: adiciona cadastro de contato`
- `fix: corrige leitura da localização`
- `security: restringe acesso ao perfil`
- `docs: atualiza modelo do Firebase`

## Validação

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat test
```

Para testes instrumentados:

```powershell
.\gradlew.bat connectedAndroidTest
```

## Pull Request

Todo PR deve explicar:

- o problema;
- a solução;
- como testar;
- documentação alterada;
- pendências;
- impacto de segurança.

Use o template em `.github/PULL_REQUEST_TEMPLATE.md`.

## Regras

Não:

- trabalhar diretamente na `main`;
- colocar senha, chave ou token em commit;
- ignorar falhas de build;
- marcar documentação como concluída sem implementação;
- alterar Security Rules sem registrar a mudança.

Quando uma alteração muda o banco, atualize `docs/13-modelo-firebase.md`.

Quando uma alteração tem impacto de segurança, atualize `docs/12-auditoria-seguranca.md`.
