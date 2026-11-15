# 02 — Estado atual

> Revisão da base do repositório em 15/11/2026.

## Estrutura atual

Aplicativo Android em Kotlin + Jetpack Compose, com uma `MainActivity` centralizando o fluxo atual e Activities/layouts XML legados ainda presentes no projeto.

## Configuração

- namespace/applicationId: `com.seniorlink.app`
- minSdk: 24
- targetSdk: 37
- compileSdk: 37
- Kotlin: 2.4.21
- Android Gradle Plugin: 9.3.1
- Gradle: 9.6.0
- Compose BOM: 2026.02.01
- Java/JVM: 17

## Integrações

### Firebase Authentication

O projeto possui:

- login por e-mail e senha;
- cadastro por e-mail e senha;
- recuperação de senha;
- login com Google;
- logout.

### Realtime Database

O projeto utiliza o Realtime Database para usuários, lembretes, contatos, ranking, localização e emergências.

### Localização

`LocationRepository` usa `FusedLocationProviderClient`.

Em debug, o projeto ainda possui o caminho de teste `localizacoes/idoso_teste` para preservar os testes atuais.

Em build de produção, a localização só é persistida quando existe um usuário autenticado, usando o UID como identificador.

### Google Maps

A tela de localização utiliza Google Maps Compose com marcador e atualização de câmera.

## Correções de segurança aplicadas nesta revisão

- chave real removida de `local.defaults.properties`;
- proteção adicional para arquivos de credenciais no `.gitignore`;
- PIN removido do armazenamento em `SharedPreferences`;
- backup automático de `SharedPreferences` desabilitado;
- logs detalhados de coordenadas limitados ao modo debug;
- gravação diagnóstica do Firebase limitada ao modo debug;
- localização de produção vinculada ao UID autenticado.

## Pendências de segurança

Ainda são necessárias:

1. rotação da chave do Maps exposta no histórico;
2. Security Rules do Realtime Database por `auth.uid`;
3. modelo de vínculo Responsável ↔ Idoso;
4. autorização específica do Cuidador;
5. substituição do login do idoso por PIN consultável diretamente no banco;
6. remoção do PIN armazenado no próprio registro do usuário;
7. remoção definitiva de `idoso_teste` quando o fluxo autenticado estiver concluído;
8. revisão de todos os acessos que recebem `idosoId` da UI;
9. revisão de backup e permissões para a versão final;
10. testes de acesso entre contas.

## Regra

Interface não é autorização. A decisão final de acesso deve estar nas regras do backend.
