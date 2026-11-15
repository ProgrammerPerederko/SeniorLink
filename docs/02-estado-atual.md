`# 02 — Estado atual

> Revisão da base do repositório e das integrações em 15/11/2026.

## 1. Plataforma

O SeniorLink é um aplicativo Android em Kotlin + Jetpack Compose.

A `MainActivity` concentra a navegação atual e ainda existem Activities/recursos XML específicos para alarmes e compatibilidade.

### Configuração atual

| Item | Valor |
|---|---|
| namespace | `com.seniorlink.app` |
| applicationId | `com.seniorlink.app` |
| minSdk | 24 |
| compileSdk | 37 |
| targetSdk | 37 |
| Kotlin | 2.4.21 |
| Android Gradle Plugin | 9.3.1 |
| Gradle | 9.6.0 |
| Compose BOM | 2026.02.01 |
| Java/JVM | 17 |

## 2. Autenticação

O Firebase Authentication está integrado para:

- login por e-mail e senha;
- cadastro por e-mail e senha;
- recuperação de senha;
- login com Google;
- logout.

### Exceção atual: Idoso

O Idoso ainda utiliza um PIN de quatro dígitos consultado no Realtime Database.

Esse fluxo funciona como compatibilidade com o protótipo atual, mas **não representa a autenticação definitiva**.

## 3. Realtime Database

O Realtime Database é usado para:

- perfis;
- vínculo atual do responsável com idosos;
- lembretes;
- contatos;
- ranking;
- localização;
- emergências.

O modelo completo está documentado em [11 — Modelo do Firebase](11-modelo-firebase.md).

## 4. Localização

`LocationRepository` usa `FusedLocationProviderClient`.

### Produção

Quando existe sessão Firebase autenticada:

`@text
localizacoes/{auth.uid}
`@

### Debug

Quando o fluxo atual do idoso ainda não possui Firebase Authentication, o aplicativo pode usar o `id_idoso` salvo em `SharedPreferences` para testar o caminho:

`@text
localizacoes/{id_idoso}
`@

Se não existir esse identificador, o fallback é:

`@text
localizacoes/idoso_teste
`@

Esse fallback é exclusivo para debug.

## 5. Google Maps

A tela de localização utiliza Google Maps Compose e apresenta:

- marcador da posição atual;
- atualização de câmera;
- controles de zoom;
- leitura de precisão;
- status de rastreamento.

A chave real deve ficar em `secrets.properties`.

## 6. Emergência

O fluxo de emergência registra o evento em:

`@text
emergencias/{idosoId}
`@

e também mantém um espelho em:

`@text
usuarios/{idosoId}/emergencia_ativa
`@

A localização usada pelo painel é padronizada em:

`@text
localizacoes/{idosoId}
`@

## 7. Correções aplicadas nesta revisão

- corrigida a referência a um campo `pin` que havia sido removido do modelo de perfil;
- corrigido o caminho de leitura da localização do painel do responsável;
- corrigido o caminho da localização usado pelo fluxo de emergência;
- evitado o uso do identificador artificial `__unauthenticated__` nas gravações do repositório;
- consulta de idosos do responsável filtrada no Realtime Database pelo e-mail autenticado;
- validações adicionais para operações que exigem sessão autenticada;
- documentação do modelo Firebase criada e vinculada ao projeto.

## 8. Segurança ainda pendente

1. revogar/rotacionar a chave do Maps exposta no histórico;
2. revisar e versionar as Security Rules;
3. criar vínculo Responsável → Idoso por UID;
4. criar vínculo Cuidador → Idoso por UID;
5. retirar o PIN do documento remoto;
6. substituir o login do idoso por autenticação apropriada;
7. remover `idoso_teste`;
8. revisar todos os acessos que recebem `idosoId` da UI;
9. executar testes reais de acesso cruzado;
10. definir retenção de dados de localização.

## Regra de segurança

A tela, a rota da `MainActivity`, o e-mail recebido pela UI e o `idosoId` não substituem as Security Rules.
