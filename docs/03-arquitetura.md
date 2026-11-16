# 03 — Arquitetura

## Visão atual

A arquitetura atual está em transição entre uma base de telas Compose centralizada e uma estrutura com autenticação e repositórios.

```
UI
 ↓
ViewModel
 ↓
Repository
 ↓
Firebase / serviços externos
```

## Estrutura

```
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
├── model/
└── ui/
    ├── components/
    ├── screens/
    └── theme/
```

## Autenticação

Firebase Authentication é a autoridade de identidade para contas autenticadas.

A UI ainda permite selecionar o perfil de acesso no fluxo de entrada. Isso é apenas navegação e não pode ser tratado como mecanismo de segurança.

## Dados remotos

O arquivo `FirestoreRepository.kt` atualmente utiliza Firebase Realtime Database apesar do nome. Isso é uma dívida técnica de nomenclatura.

## Localização

`LocationRepository`:

- usa Fused Location Provider;
- atualiza a posição periodicamente;
- usa o UID autenticado como caminho em produção;
- mantém `idoso_teste` somente para debug;
- não grava diagnóstico em produção;
- limita logs detalhados de localização ao debug.

## Segurança

A autorização final deve acontecer no Firebase Security Rules.

Modelo desejado:

```
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

Nunca confiar apenas em:

- botão da UI;
- rota da `MainActivity`;
- `idosoId` enviado pela tela;
- e-mail enviado pelo cliente.

