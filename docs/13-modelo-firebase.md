# 13 — Modelo do Firebase

> Contrato atual do Firebase usado pelo código do SeniorLink. Atualizado em 15/11/2026.

## 1. Serviços utilizados

### Firebase Authentication

Usado atualmente para contas de:

- Responsável;
- Cuidador;
- login com Google;
- recuperação de senha;
- logout.

O fluxo do Idoso ainda utiliza PIN legado no Realtime Database.

### Firebase Realtime Database

É o backend operacional do aplicativo.

O Cloud Firestore não participa mais da implementação atual.

## 2. Perfil do idoso

Estrutura principal:

```text
usuarios/
└── {idosoId}/
    ├── nome
    ├── pin                    # legado; pendente de remoção
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
    ├── contatos/
    └── emergencia_ativa/
```

O ID do registro do idoso é atualmente gerado pelo Realtime Database no fluxo legado.

## 3. Localização

Estrutura:

```text
localizacoes/
└── {identificador}/
    ├── latitude
    ├── longitude
    ├── precisao
    └── timestamp
```

Em produção, `LocationRepository` usa `auth.uid`.

Durante debug, o fluxo legado por PIN pode usar o `id_idoso` salvo localmente. O fallback `idoso_teste` existe apenas para diagnóstico.

A Home e a tela de localização usam o mesmo padrão de escrita.

## 4. Emergência

Estrutura principal:

```text
emergencias/
└── {idosoId}/
    ├── ativo
    ├── idosoId
    ├── nomeIdoso
    ├── latitude
    ├── longitude
    └── timestamp
```

Existe também o espelho:

```text
usuarios/{idosoId}/emergencia_ativa/
```

O espelho permanece por compatibilidade com as telas e o serviço de emergência.

## 5. Lembretes

```text
usuarios/{idosoId}/lembretes/{lembreteId}/
├── titulo
├── horario
├── categoria
└── concluido
```

O agendamento local do Android recebe também o `idosoId` para que a tela de alarme consiga marcar o lembrete no registro correto.

## 6. Contatos

```text
usuarios/{idosoId}/contatos/{contatoId}/
├── nome
├── parentesco
└── telefone
```

## 7. Ranking

```text
ranking_idosos/{uid}/
├── nome
├── avatar
└── pontos
```

O ranking usa o UID autenticado.

## 8. Diagnóstico

Somente em debug:

```text
diagnostico/localizacao/
├── status
└── timestamp
```

## 9. Arquivos responsáveis

| Arquivo | Papel |
|---|---|
| `data/FirestoreRepository.kt` | acesso ao Realtime Database; nome histórico |
| `location/LocationRepository.kt` | captura e gravação de localização |
| `model/IdosoState.kt` | perfil e preferências |
| `ui/screens/FeatureScreens.kt` | UI de perfil, localização, lembretes e emergência |
| `ui/screens/ResponsavelScreens.kt` | painel e gestão do responsável |
| `ui/screens/CuidadorScreens.kt` | painel do cuidador |
| `auth/AuthRepository.kt` | identidade |

## 10. Segurança

O código cliente não é a camada final de autorização.

As Security Rules devem validar:

```text
auth.uid
   ↓
perfil autenticado
   ↓
vínculo permitido
   ↓
idoso
   ├── localização
   ├── lembretes
   ├── contatos
   └── emergência
```

Enquanto o vínculo por UID não estiver implementado, o e-mail e o `idosoId` continuam sendo compatibilidades do protótipo e não devem ser tratados como autorização definitiva.

## 11. Pendências

- Security Rules versionadas;
- vínculo Responsável → Idoso por UID;
- vínculo Cuidador → Idoso por UID;
- autenticação definitiva do Idoso;
- remoção do PIN remoto;
- remoção de `idoso_teste`;
- política de retenção da localização;
- teste de acesso cruzado.
