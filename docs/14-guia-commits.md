# 14 — Guia de commits e colaboração

> Padrão obrigatório para todas as pessoas que alterarem o SeniorLink.

## 1. Regra principal

Cada commit deve responder a uma única pergunta:

**O que mudou?**

Não misture correção de bug, nova tela, atualização de documentação e limpeza de arquivos em um único commit sem necessidade.

## 2. Formato

Usamos Conventional Commits:

`tipo: descrição curta em português`

Exemplos:

`feat: adiciona cadastro de contato de confiança`
`fix: corrige leitura da localização do idoso`
`docs: atualiza modelo do Firebase`
`refactor: separa lógica de autenticação da UI`
`test: adiciona teste para login`
`chore: atualiza dependências do Android`
`security: restringe acesso ao perfil do idoso`

### Tipos permitidos

| Tipo | Uso |
|---|---|
| `feat` | nova funcionalidade |
| `fix` | correção de bug |
| `refactor` | mudança interna sem alterar o comportamento pretendido |
| `docs` | documentação |
| `test` | testes |
| `chore` | manutenção, configuração ou dependências |
| `security` | correção ou melhoria de segurança |
| `perf` | melhoria de desempenho |
| `build` | Gradle, versões ou processo de build |

Não inventar novos tipos sem alinhar com a equipe.

## 3. Descrição

A descrição deve:

- começar com verbo;
- ser curta;
- explicar o resultado da mudança;
- estar em português;
- não terminar com ponto.

Bom:

`fix: corrige caminho da localização no Firebase`

Ruim:

`fix: bug`

Ruim:

`Fix: Corrige o problema da localização do Firebase.`

## 4. Um commit = uma intenção

### Bom

`feat: adiciona tela de cadastro`

depois:

`fix: valida telefone no cadastro`

depois:

`docs: registra fluxo de cadastro`

### Evitar

`feat: adiciona cadastro, muda cores, corrige Firebase, apaga arquivos e atualiza README`

## 5. Branches

Não trabalhar diretamente na `main`.

Padrão:

`tipo/nome-curto-da-tarefa`

Exemplos:

`feat/cadastro-idoso`
`fix/localizacao-firebase`
`refactor/repositorio-firebase`
`security/regras-firebase`
`docs/guia-commits`

## 6. Fluxo da equipe

1. Atualizar a `main` local.
2. Criar uma branch.
3. Fazer a alteração.
4. Testar.
5. Atualizar documentação relacionada.
6. Criar commit.
7. Fazer push.
8. Abrir Pull Request.
9. Revisar.
10. Só então integrar na `main`.

## 7. Comandos padrão

### Atualizar a main

```powershell
git checkout main
git pull origin main
```

### Criar branch

```powershell
git checkout -b fix/localizacao-firebase
```

### Conferir alterações

```powershell
git status
git diff
```

### Commit

```powershell
git add .
git commit -m "fix: corrige leitura da localizacao"
```

### Enviar

```powershell
git push -u origin fix/localizacao-firebase
```

## 8. Antes do commit

Todo integrante deve conferir:

- código compila;
- imports não utilizados foram removidos;
- não existem chaves ou senhas no código;
- documentação foi atualizada quando necessário;
- caminho do Firebase está correto;
- logs sensíveis estão protegidos;
- nenhuma alteração não relacionada foi incluída.

## 9. Antes do Pull Request

Executar, no mínimo:

```powershell
.gradlew.bat assembleDebug
```

E, quando fizer sentido:

```powershell
.gradlew.bat test
.gradlew.bat connectedAndroidTest
```

Se algum comando não puder ser executado, o PR deve informar o motivo.

## 10. Pull Request

Título:

`tipo: descrição da mudança`

O corpo do PR deve informar:

### O que mudou

Descrever objetivamente.

### Por que mudou

Explicar o problema.

### Como testar

Informar passos reproduzíveis.

### Documentação

Informar quais documentos foram atualizados.

### Pendências

Informar o que ainda não está resolvido.

## 11. Regra para conflitos

Nunca resolver conflito aceitando tudo automaticamente.

Procedimento:

1. atualizar a branch com a `main`;
2. entender cada conflito;
3. preservar a regra correta do projeto;
4. executar os testes;
5. revisar o diff completo.

## 12. Commits de documentação

Quando uma alteração muda comportamento, a documentação precisa acompanhar.

Exemplo:

`fix: corrige caminho da localizacao`

e, quando necessário:

`docs: atualiza modelo do Firebase`

A documentação não deve afirmar que uma funcionalidade está concluída quando o código ainda está incompleto.

## 13. Commits de segurança

Usar `security` para mudanças diretamente relacionadas a:

- autenticação;
- autorização;
- Security Rules;
- exposição de dados;
- credenciais;
- armazenamento de dados sensíveis;
- logs sensíveis;
- backup de dados.

Nunca colocar segredo em commit, comentário de PR ou documentação.

## 14. Mensagens proibidas

Evitar:

`update`
`teste`
`mudanças`
`arrumei`
`final`
`final2`
`correcao definitiva`
`versao nova`

Essas mensagens não explicam o histórico técnico.

## 15. Rebase e merge

A equipe deve preferir Pull Request com squash quando a branch tiver muitos commits pequenos de tentativa.

Commits de trabalho como:

`fix: tenta corrigir`
`fix: corrige de novo`
`teste: ajuste`

podem ser reorganizados antes da integração.

O histórico da `main` deve permanecer legível.

## 16. Checklist do desenvolvedor

- [ ] Branch criada a partir da main atual.
- [ ] Mudança focada em uma tarefa.
- [ ] Código revisado.
- [ ] Teste executado.
- [ ] Documentação atualizada.
- [ ] Sem segredo no diff.
- [ ] Commit segue o padrão.
- [ ] Push feito para a branch.
- [ ] Pull Request aberto.

## 17. Regra definitiva

Se outra pessoa olhar o histórico daqui a três meses, ela deve conseguir entender:

**o que mudou, por que mudou e como a mudança foi validada.**
