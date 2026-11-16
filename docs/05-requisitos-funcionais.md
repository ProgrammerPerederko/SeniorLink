# 05 — Requisitos funcionais

## RF01 — Localização

**Estado:** parcialmente implementado.

O aplicativo captura GPS, publica a localização no Realtime Database e exibe a posição em mapa.

Pendências: autorização por vínculo, política de retenção e testes de acesso cruzado.

## RF02 — Ficha do idoso

**Estado:** implementado na camada atual.

O perfil pode armazenar:

- nome;
- idade;
- tipo sanguíneo;
- alergias;
- condições médicas;
- medicamentos;
- endereço;
- telefone de emergência.

Pendência: proteção final pelas Security Rules.

## RF03 — Lembretes

**Estado:** parcialmente implementado.

Existe:

- criação;
- leitura;
- conclusão;
- exclusão;
- agendamento local;
- tela de alarme;
- adiamento de cinco minutos.

Pendências de produto:

- edição;
- repetição configurável;
- sincronização mais completa;
- testes instrumentados.

## RF04 — Emergência

**Estado:** parcialmente implementado.

Existe:

- confirmação;
- gravação no Realtime Database;
- localização;
- painel do responsável;
- monitoramento em segundo plano;
- tela de alerta.

Pendência: autorização final por vínculo e testes em cenário real.

## RF05 — Ligação rápida

**Estado:** implementado na interface atual.

Os contatos usam o discador do Android.

## RF06 — Contatos de confiança

**Estado:** parcialmente implementado.

Existe criação, leitura, exclusão e ligação.

Pendência: autorização por vínculo do responsável/cuidador.

## RF07 — Treinamento do idoso

**Estado:** interface inicial.

Existem tutoriais básicos; o conteúdo será expandido.

## RF08 — Treinamento do responsável

**Estado:** interface inicial.

Existe uma sequência de orientações básicas.

## RF09 — Perfil

**Estado:** implementado na camada atual.

O perfil remoto utiliza Realtime Database e as preferências locais são persistidas em SharedPreferences.

## RF10 — Acessibilidade

**Estado:** parcialmente implementado.

Existe:

- escala de fonte;
- alto contraste;
- opção de leitura em voz alta.

A persistência das preferências foi corrigida nesta revisão.

Pendência: completar o uso da leitura de voz nas funções críticas e realizar teste de acessibilidade no aparelho.

## RNF01 — Usabilidade

**Estado:** em validação.

## RNF02 — Privacidade

**Estado:** pendente de conclusão.

A implementação cliente possui medidas de segurança, mas as Security Rules e os vínculos de acesso ainda precisam ser concluídos.

## RNF03 — Confiabilidade

**Estado:** parcialmente implementado.

Permissões e erros básicos possuem tratamento, mas ainda faltam testes instrumentados e testes offline/online.

## RNF04 — Desempenho

**Estado:** em validação.

A interface principal não deve bloquear durante operações de rede.
