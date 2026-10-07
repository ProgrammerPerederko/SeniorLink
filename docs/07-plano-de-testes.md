# 07 — Plano de testes

## Teste de abertura

**Objetivo:** garantir que o aplicativo inicia sem crash.

Passos:
1. Instalar APK.
2. Abrir SeniorLink.
3. Avançar no onboarding.
4. Chegar à Home.

Resultado esperado: nenhuma Activity fecha inesperadamente.

## Teste de navegação

Validar:

- Home → Perfil
- Home → Localização
- Home → Lembretes
- Home → Aprender
- Home → Emergência
- retorno para Home

## Teste de perfil

Validar:

- inserir dados;
- salvar;
- fechar tela;
- abrir novamente;
- verificar persistência.

## Teste de localização

Validar:

- permissão concedida;
- permissão negada;
- GPS desligado;
- GPS ligado;
- leitura de posição;
- compartilhamento.

## Teste de lembretes

Validar:

- criação;
- edição;
- exclusão;
- ativação/desativação;
- notificação;
- repetição diária.

## Teste de emergência

Validar:

- sem contato;
- com contato;
- confirmação;
- abertura do discador;
- compartilhamento de localização.

## Teste de acessibilidade

Validar:

- fonte maior;
- contraste;
- áreas de toque;
- leitura de rótulos;
- compreensão da navegação.

## Critério de aceite do MVP

Uma funcionalidade é considerada pronta quando passa no caminho feliz e nos principais caminhos de erro, sem crash, e o resultado é documentado.
