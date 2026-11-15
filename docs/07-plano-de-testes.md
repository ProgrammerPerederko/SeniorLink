# 07 — Plano de testes

## 1. Build

Na raiz do projeto:

```powershell
.\gradlew.bat assembleDebug
```

Resultado esperado: APK de debug gerado sem erro de compilação.

## 2. Testes unitários

```powershell
.\gradlew.bat test
```

Executar quando houver alteração em regras de negócio, modelos ou repositórios.

## 3. Testes instrumentados

Quando houver dispositivo/emulador:

```powershell
.\gradlew.bat connectedAndroidTest
```

## 4. Inicialização

Validar:

1. abrir o app;
2. navegar pelo onboarding;
3. entrar no login;
4. abrir a Home;
5. voltar e avançar entre telas.

Resultado esperado: nenhum crash.

## 5. Autenticação

Validar:

- cadastro do responsável;
- login;
- recuperação de senha;
- login com Google;
- logout;
- cadastro/login do cuidador;
- fluxo legado por PIN do idoso.

## 6. Perfil

Validar:

- leitura;
- alteração;
- salvamento;
- fechamento;
- reabertura;
- persistência após reiniciar o app.

## 7. Localização

Validar:

- permissão concedida;
- permissão negada;
- GPS indisponível;
- GPS disponível;
- escrita em `localizacoes/{id}`;
- leitura pelo responsável;
- atualização do mapa;
- encerramento do rastreamento.

## 8. Lembretes

Validar:

- criação;
- persistência;
- agendamento;
- abertura na tela bloqueada;
- concluir;
- adiar cinco minutos;
- excluir;
- reiniciar o aplicativo.

Também verificar se o lembrete concluído foi alterado no `idosoId` correto.

## 9. Emergência

Validar:

- confirmação;
- gravação do alerta;
- localização;
- painel do responsável;
- alerta em segundo plano;
- parada do alarme;
- encerramento do estado.

## 10. Contatos

Validar:

- criar;
- listar;
- ligar;
- excluir;
- estado vazio.

## 11. Acessibilidade

Validar:

- fonte normal;
- fonte grande;
- fonte extra;
- alto contraste;
- tamanhos de toque;
- leitura de textos críticos.

## 12. Segurança

Usar duas contas diferentes:

- responsável A não pode acessar dados do idoso B;
- cuidador A não pode acessar dados fora do vínculo;
- alterar manualmente um `idosoId` não deve liberar dados;
- usuário deslogado não deve gravar dados protegidos.

## 13. Critério de aceite

Uma funcionalidade só é considerada pronta quando:

- caminho feliz passa;
- principais erros passam;
- não há crash;
- documentação foi atualizada;
- teste executado está registrado no PR.
