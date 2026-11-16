# 12 — Auditoria de segurança

> Revisão do código e da configuração versionada em 15/11/2026.

## Resultado

O SeniorLink possui uma camada funcional de Authentication, Realtime Database, localização, lembretes e emergência, mas ainda não deve ser considerado pronto para produção.

Esta auditoria registra tanto correções já aplicadas quanto riscos que dependem do backend Firebase.

## Correções aplicadas

### Segredos

- `local.defaults.properties` não contém mais uma chave real do Maps;
- `secrets.properties` permanece local;
- `.gitignore` cobre credenciais e certificados;
- a chave que foi encontrada no histórico foi removida das referências de branch do repositório;
- o valor real não é mantido no estado atual do repositório;
- a revogação/rotação no Google Cloud ainda precisa ser concluída fora do GitHub.

### Perfil e banco

- perfil do Idoso alinhado ao Realtime Database;
- dependência do Cloud Firestore removida;
- persistência remota usa `usuarios/{idosoId}`;
- preferências locais são persistidas em SharedPreferences.

### PIN

- o PIN não é salvo em SharedPreferences;
- o PIN ainda existe no Realtime Database;
- o login do Idoso ainda consulta esse PIN;
- essa estratégia é temporária e não representa autenticação de produção.

### Localização

- produção usa `localizacoes/{auth.uid}` quando há Authentication;
- debug pode usar `id_idoso` local;
- `idoso_teste` permanece somente como fallback de debug;
- Home e tela de localização utilizam o mesmo repositório;
- logs de coordenadas detalhados ficam restritos ao debug.

### Lembretes

- o `idosoId` acompanha o alarme;
- a conclusão do alarme pode atualizar o lembrete do idoso correto.

### Serviço de emergência

- listeners são removidos em `onDestroy`;
- chamadas repetidas de `onStartCommand` não registram novamente o mesmo monitoramento.

### Backup

- SharedPreferences foi excluído do backup automático.

## Riscos ainda existentes

1. Security Rules do Realtime Database não estão versionadas.
2. O vínculo Responsável → Idoso ainda utiliza compatibilidades do protótipo e precisa migrar para UID.
3. O vínculo Cuidador → Idoso ainda precisa de autorização por UID.
4. O perfil de acesso escolhido na UI ainda não é uma autorização de backend.
5. O PIN remoto ainda é utilizado no login do Idoso.
6. `idoso_teste` ainda existe para debug.
7. Operações por `idosoId` dependem das regras do servidor.
8. A antiga chave do Maps encontrada no histórico precisa ser revogada/rotacionada no Google Cloud.
9. A retenção de localização e emergência ainda não está definida.
10. O acesso cruzado entre contas ainda precisa de teste formal.

## Critério para produção

O SeniorLink não deve ser considerado pronto enquanto:

- a antiga chave exposta for revogada/rotacionada no Google Cloud;
- as Security Rules não limitarem leitura e escrita;
- os vínculos não forem baseados em UID;
- o PIN não tiver sido removido do modelo remoto;
- o acesso cruzado não tiver sido testado;
- a retenção de localização não estiver definida.

## Próxima prioridade

A próxima grande etapa de segurança é:

```text
auth.uid
   ↓
perfil autenticado
   ↓
vínculo Responsável/Cuidador
   ↓
idoso autorizado
```

Somente depois disso o fluxo remoto deve ser tratado como produção.
