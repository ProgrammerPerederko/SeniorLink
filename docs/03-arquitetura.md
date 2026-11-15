`# 03 — Arquitetura

## Visão atual

O SeniorLink usa uma arquitetura simples em camadas, ainda em evolução:

```text
Compose UI
   ↓
ViewModel / estado da tela
   ↓
Repository
   ↓
Firebase / serviços do Android
```

O objetivo é manter a UI sem conhecimento desnecessário dos detalhes do Firebase e do provedor de localização.

## Estrutura principal

```text
app/src/main/java/com/seniorlink/app/
├── MainActivity.kt
├── auth/
│   ├── AuthRepository.kt
│   └── AuthViewModel.kt
├── data/
│   └── FirestoreRepository.kt
├── location/
│   └── LocationRepository.kt
├── alarm/
├── services/
├── model/
└── ui/
    ├── components/
    ├── screens/
    └── theme/
```

## Autenticação

`AuthRepository` é a camada de acesso ao Firebase Authentication.

Responsabilidades:

- login por e-mail/senha;
- cadastro;
- Google Sign-In;
- recuperação de senha;
- logout.

`AuthViewModel` expõe o estado de carregamento e mensagens de erro para a UI.

### Limitação atual

A seleção visual de perfil ocorre na UI.

Ela define o fluxo de navegação, mas **não é autorização**.

O backend deve validar a identidade e o relacionamento do usuário.

## Realtime Database

O arquivo `data/FirestoreRepository.kt` possui nome histórico, mas usa exclusivamente Firebase Realtime Database.

Principais responsabilidades:

- consultas de idosos;
- atualização de ficha;
- lembretes;
- contatos;
- ranking;
- emergências.

A estrutura dos nós está em [11 — Modelo do Firebase](11-modelo-firebase.md).

## Localização

`LocationRepository` concentra:

- `FusedLocationProviderClient`;
- atualização periódica;
- última posição disponível;
- publicação no Realtime Database;
- diagnóstico de debug.

### Regra de identidade

Em produção, a referência é:

```text
localizacoes/{auth.uid}
```

No debug, o código atual aceita o `id_idoso` salvo localmente para manter o fluxo de teste por PIN funcionando.

Essa compatibilidade deve desaparecer quando o Idoso passar a possuir uma identidade Firebase própria.

## Localização do Responsável

O painel do responsável lê diretamente:

```text
localizacoes/{idosoId}
```

Isso corrige a divergência anterior em que o produtor gravava em `localizacoes` enquanto a tela tentava ler em `usuarios/{id}/localizacao`.

## Emergência

O evento de emergência usa:

```text
emergencias/{idosoId}
usuarios/{idosoId}/emergencia_ativa
```

A localização continua centralizada em:

```text
localizacoes/{idosoId}
```

## Segurança

A autorização deve ser imposta no Firebase Realtime Database Rules.

Modelo esperado:

```text
auth.uid
   ↓
perfil autenticado
   ↓
vínculo autorizado
   ↓
idoso
   ├── localização
   ├── lembretes
   ├── contatos
   └── emergências
```

Nunca confiar exclusivamente em:

- botão da UI;
- rota;
- e-mail enviado pelo cliente;
- `idosoId` enviado pela tela;
- campo de perfil escolhido localmente.
