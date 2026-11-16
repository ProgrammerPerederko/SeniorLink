# 11 — Entrega de interfaces — 07/10/2026

## Objetivo

Aproximar o aplicativo do fluxo visual definido no protótipo de 16 telas e corrigir a navegação inicial para refletir o fluxo oficial:

`Splash → Onboarding → Login → Cadastro → Boas-vindas → Home`.

## Alterações realizadas

### Acesso
- criada interface de Login;
- criada interface de Cadastro;
- criada interface de Boas-vindas;
- navegação entre Login e Cadastro;
- fluxo de onboarding direcionado para Login.

### Gestão
- criada interface de Novo Lembrete;
- criada interface de Contatos de Confiança;
- criada interface de Configurações;
- criada interface de Treinamento do Responsável.

### Navegação
- MainActivity agora aceita os extras `abrir_login` e `abrir_inicio`;
- Home ganhou acesso às Configurações;
- Lembretes ganhou acesso à criação de novo lembrete;
- Configurações conecta Perfil, Contatos e Treinamento do responsável.

## O que ainda é interface

As telas novas ainda não são consideradas funcionalidades concluídas. Campos, persistência, permissões, notificações, GPS, chamadas e sincronização serão implementados e testados nas próximas etapas.

## Critério para próxima etapa

Cada interface será convertida em funcionalidade apenas após:
1. comportamento implementado;
2. estado vazio/erro tratado;
3. teste no Samsung;
4. documentação atualizada.
