`# 11 — Modelo de dados do Firebase

> Documento de referência do Firebase usado pelo SeniorLink. Atualizado em 15/11/2026.

## Objetivo

Este documento descreve os nós efetivamente utilizados pelo código do aplicativo.

O projeto utiliza **Firebase Authentication** para identidade das contas autenticadas e **Firebase Realtime Database** para dados operacionais em tempo real.

> Importante: as Firebase Security Rules não estão versionadas neste repositório. A estrutura abaixo documenta o contrato esperado pelo código; a autorização efetiva depende das regras configuradas no projeto Firebase.

## 1. Identidade

O Firebase Authentication é usado atualmente para:

- Responsável;
- Cuidador;
- recuperação de senha;
- login com Google.

O fluxo do Idoso ainda utiliza um PIN consultado no Realtime Database. Esse fluxo é legado e permanece como pendência de segurança.

## 2. Estrutura atual do Realtime Database

`@text
usuarios/
└── {idosoId}/
    ├── nome
    ├── pin
    ├── emailResponsavel
    ├── tipo
    ├── cadastroCompleto
    ├── idade
    ├── tipoSanguineo
    ├── condicoesMedicas
    ├── alergias
    ├── medicamentos
    ├── endereco
    ├── telefone
    ├── lembretes/
    │   └── {lembreteId}/
    │       ├── titulo
    │       ├── horario
    │       ├── categoria
    │       └── concluido
    ├── contatos/
    │   └── {contatoId}/
    │       ├── nome
    │       ├── parentesco
    │       └── telefone
    └── emergencia_ativa/
        ├── ativo
        ├── idosoId
        ├── nomeIdoso
        ├── latitude
        ├── longitude
        └── timestamp
`@

### Observações sobre `usuarios/{idosoId}`

O `idosoId` é atualmente um ID gerado pelo Realtime Database no cadastro do idoso.

O campo `emailResponsavel` é usado pelo aplicativo para localizar idosos vinculados ao responsável. A implementação já limita a consulta ao e-mail da sessão autenticada, mas o modelo definitivo deverá migrar para vínculo por UID.

O campo `pin` ainda existe porque o login do idoso depende dele. Ele não deve ser considerado solução de autenticação para produção.

## 3. Localização

`@text
localizacoes/
└── {identificador}/
    ├── latitude
    ├── longitude
    ├── precisao
    └── timestamp
`@

### Regra de escrita implementada no aplicativo

Em produção:

`@text
localizacoes/{auth.uid}
`@

O código utiliza o UID da sessão autenticada.

Em debug, quando o fluxo atual do idoso ainda não possui Firebase Authentication, o aplicativo pode utilizar o ID salvo localmente em `seniorlink_prefs/id_idoso`.

Somente quando esse ID local não existir o debug cai para:

`@text
localizacoes/idoso_teste
`@

Essa compatibilidade existe exclusivamente para testes e deve ser retirada quando o login do idoso for migrado para uma identidade Firebase.

## 4. Emergências

`@text
emergencias/
└── {idosoId}/
    ├── ativo
    ├── idosoId
    ├── nomeIdoso
    ├── latitude
    ├── longitude
    └── timestamp
`@

O aplicativo também mantém um espelho em:

`@text
usuarios/{idosoId}/emergencia_ativa/
`@

Isso atende às rotinas atuais que escutam o estado de emergência em mais de um ponto do aplicativo.

## 5. Ranking

`@text
ranking_idosos/
└── {uid}/
    ├── nome
    ├── avatar
    └── pontos
`@

O ranking usa o UID da conta autenticada quando disponível.

## 6. Diagnóstico

Durante debug:

`@text
diagnostico/
└── localizacao/
    ├── status
    └── timestamp
`@

A gravação diagnóstica é bloqueada pelo código em builds que não são de debug.

## 7. Regras de acesso esperadas

O modelo de autorização que deve ser implementado nas Security Rules é:

`@text
auth.uid
   │
   ├── próprio perfil
   │
   └── vínculos autorizados
          │
          └── idoso
               ├── localização
               ├── lembretes
               ├── contatos
               └── emergência
`@

A UI, o e-mail informado pelo cliente e o `idosoId` recebido por uma tela **não são suficientes para autorizar acesso**.

## 8. Arquivos que implementam o modelo

| Arquivo | Responsabilidade |
|---|---|
| `app/src/main/java/com/seniorlink/app/data/FirestoreRepository.kt` | Repositório do Realtime Database; o nome do arquivo é histórico e ainda precisa ser renomeado |
| `app/src/main/java/com/seniorlink/app/location/LocationRepository.kt` | Captura GPS e grava localização |
| `app/src/main/java/com/seniorlink/app/ui/screens/FeatureScreens.kt` | Perfil, localização, lembretes e emergência |
| `app/src/main/java/com/seniorlink/app/ui/screens/ResponsavelScreens.kt` | Painel do responsável, vínculo e acompanhamento |
| `app/src/main/java/com/seniorlink/app/auth/AuthRepository.kt` | Firebase Authentication |
| `app/src/main/java/com/seniorlink/app/auth/AuthViewModel.kt` | Estado das operações de autenticação |
| `app/google-services.json` | Configuração do projeto Firebase para o aplicativo Android |

## 9. Pendências obrigatórias

1. Versionar a Security Rules de forma controlada e revisar permissões.
2. Criar vínculo Responsável → Idoso por UID.
3. Criar vínculo Cuidador → Idoso por UID.
4. Retirar o PIN do documento remoto.
5. Substituir o login do idoso por autenticação apropriada.
6. Remover o fallback `idoso_teste`.
7. Testar leitura e escrita entre duas contas diferentes.
8. Definir política de retenção para dados de localização e emergência.
