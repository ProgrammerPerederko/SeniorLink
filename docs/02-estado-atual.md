# 02 — Estado atual

> Revisão técnica em 15/11/2026.

## Plataforma

- Android;
- Kotlin 2.4.21;
- Jetpack Compose;
- Android Gradle Plugin 9.3.1;
- Gradle 9.6.0;
- Java/JVM 17;
- minSdk 24;
- compileSdk 37;
- targetSdk 37.

## Fluxo ativo

O launcher atual é `MainActivity`.

O onboarding ativo é `ui/screens/SplashScreen.kt`, implementado com Compose `HorizontalPager`.

O projeto ainda contém Activities/XML antigos de onboarding, mas eles não estão declarados no `AndroidManifest.xml` e não participam do launcher. São legado.

## Autenticação

Firebase Authentication está integrado para:

- e-mail e senha;
- cadastro;
- recuperação de senha;
- Google Sign-In;
- logout.

O Idoso ainda possui um fluxo legado por PIN no Realtime Database.

## Banco

O banco operacional é somente o Firebase Realtime Database.

O Cloud Firestore foi removido das dependências e do armazenamento do perfil.

## Perfil

`model/IdosoState.kt` concentra:

- estado do perfil;
- preferências de acessibilidade;
- persistência das preferências;
- leitura e gravação do perfil em `usuarios/{idosoId}` no Realtime Database.

As preferências de fonte, contraste e leitura de voz agora são persistidas localmente.

## Localização

`LocationRepository` usa `FusedLocationProviderClient`.

Em produção autenticada:

`localizacoes/{auth.uid}`

Em debug, o fluxo legado do Idoso pode usar o `id_idoso` salvo localmente; `idoso_teste` permanece somente como fallback.

A Home e a tela dedicada de localização utilizam o mesmo repositório e o mesmo caminho do Firebase.

## Lembretes

Os lembretes ficam em:

`usuarios/{idosoId}/lembretes/{lembreteId}`

O agendamento local também carrega o `idosoId`. Assim, a tela de alarme consegue marcar o registro correto como concluído.

## Emergência

Os alertas são registrados em:

- `emergencias/{idosoId}`;
- `usuarios/{idosoId}/emergencia_ativa`.

A localização usada no alerta segue `localizacoes/{idosoId}`.

O serviço em segundo plano agora impede inicialização duplicada de listeners durante múltiplas chamadas de `onStartCommand`.

## Segurança

Correções relevantes desta revisão:

- perfil migrado de Firestore para Realtime Database;
- dependência do Firestore removida;
- localização da Home alinhada ao `LocationRepository`;
- alarmes vinculados ao `idosoId`;
- listeners duplicados do serviço reduzidos;
- preferências locais persistidas;
- documentação do Firebase consolidada.

## Pendências

Ainda são necessárias:

1. Security Rules;
2. vínculo Responsável → Idoso por UID;
3. vínculo Cuidador → Idoso por UID;
4. autenticação definitiva do Idoso;
5. remoção do PIN remoto;
6. remoção de `idoso_teste`;
7. testes de acesso cruzado;
8. rotação da chave do Maps exposta no histórico;
9. política de retenção de localização.
