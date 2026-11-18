# Organização das branches após a limpeza do histórico

> Atualizado em 17/11/2026.

## Branch oficial

A branch `main` é a única fonte oficial de código e documentação do SeniorLink. Novas alterações devem partir dela por meio de branches curtas e Pull Requests.

## Branches de trabalho antigas

Após a reescrita do histórico para remover arquivos de configuração sensíveis, três branches de trabalho antigas ficaram sem ancestral comum com `main`. Por isso, não devem ser mescladas diretamente.

Os conteúdos originais foram preservados nestas branches de arquivo:

- `archive/refactor-revisao-geral-2026-11-15`
- `archive/fix-firebase-documentacao-2026-11-15`
- `archive/security-revisao-2026-11-15`

Elas são referências históricas para consulta e recuperação seletiva de alterações. Não são branches de desenvolvimento e não devem ser usadas como base de novos commits.

Os nomes antigos das três branches de trabalho foram alinhados ao commit atual de `main` para eliminar divergências acidentais e evitar propostas de merge entre históricos independentes.

## Procedimento para novas alterações

1. Atualize a `main`.
2. Crie uma branch a partir de `main`, por exemplo `fix/nome-da-correcao` ou `docs/nome-da-documentacao`.
3. Faça uma alteração pequena e específica.
4. Execute os testes aplicáveis.
5. Abra um Pull Request para `main`.
6. Revise o diff e os resultados de CI antes de fazer merge.

## Atenção às branches de arquivo

Não faça merge integral das branches `archive/*`. Se encontrar nelas uma correção útil, compare o arquivo com a versão atual da `main` e reaplique somente a alteração necessária em uma nova branch baseada na `main`.

A preservação dessas referências não significa que o conteúdo arquivado foi validado ou que está pronto para produção.
