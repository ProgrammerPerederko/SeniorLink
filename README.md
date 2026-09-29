# SeniorLink

Aplicativo Android em Kotlin + Jetpack Compose baseado no protótipo de 16 telas.

## Fluxos implementados

- Splash e 3 etapas de onboarding
- Login local com senha armazenada como hash SHA-256
- Cadastro local para idoso ou responsável
- Modo demonstração com dados de exemplo
- Recuperação local de senha
- Tela de boas-vindas pós-login/cadastro
- Home com navegação inferior
- Perfil do idoso com persistência local
- Localização GPS, atualização enquanto a tela está aberta, mapa e compartilhamento
- Lembretes com data, hora, ativação/desativação, exclusão e notificações
- Lembretes diários e reprogramação após reinício/atualização do aplicativo
- Emergência/SOS com confirmação, discador e compartilhamento de localização
- Contatos de confiança: adicionar, editar, excluir e ligar
- Área Aprender com tutoriais
- Configurações: notificações, privacidade, texto maior, conta, contatos, ajuda e logout

## Teste rápido

1. Abra o projeto no Android Studio.
2. Sincronize o Gradle.
3. Execute no aparelho.
4. Na tela de Login, use **Entrar em modo demonstração**.
5. Conceda localização e notificações quando solicitadas.
6. Teste Home, Perfil, Localização, Lembretes, SOS, Contatos, Aprender e Configurações.

### Observação

Esta versão é um MVP local para demonstração/teste. Login, perfil, contatos e lembretes ficam no aparelho. Não há servidor, banco remoto ou sincronização entre aparelhos nesta etapa.