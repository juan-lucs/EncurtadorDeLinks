# Encurtador de Links

API em **Spring Boot** para encurtar URLs e contabilizar acessos, com uma interface web simples para criar o link e consultar as estatísticas.

Projeto feito para o desafio técnico de estágio em desenvolvimento

## Funcionalidades

- Encurtar uma URL (`http`/`https`) e receber um código curto.
- Redirecionar do link curto para a URL original, contando cada acesso.
- Consultar estatísticas do link: cliques, data de criação e horários dos acessos.
- Interface web: criar o link, copiar, e ver as estatísticas do link criado.
- Validação da URL e erros padronizados (`400` para URL inválida, `404` para código inexistente).

## Tecnologias

- Java 21
- Spring Boot 4.1.1 (Web MVC, Data JPA, Validation)
- Banco H2 em memória
- Lombok
- Maven (wrapper incluído, não precisa instalar o Maven)
- Front-end em HTML, CSS e JavaScript puro, servido pelo próprio Spring Boot

## Como rodar

**Pré-requisito:** JDK 21 instalado.

```bash
git clone https://github.com/juan-lucs/EncurtadorDeLinks.git
cd EncurtadorDeLinks
./mvnw spring-boot:run        # no Windows: mvnw.cmd spring-boot:run
```

Depois abra **http://localhost:8080** no navegador.

Outras opções:

```bash
# Gerar o .jar e rodar
./mvnw clean package
java -jar target/encurtador-de-links-api-0.0.1-SNAPSHOT.jar

# Rodar em outra porta (a interface funciona em qualquer porta)
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081

# Rodar os testes
./mvnw test
```

> O banco é **em memória**: os links somem quando a aplicação é reiniciada.

## Como usar a interface

1. Cole uma URL (ex.: `https://www.example.com`) e clique em **Encurtar**.
2. A tela mostra o link curto, o número de cliques, a data de criação e os 10 acessos mais recentes.
3. Abra o link curto em outra aba. Ao voltar para a tela, as estatísticas atualizam sozinhas (ou use o botão **Atualizar**).
4. O botão **Copiar link** copia o link curto.


## API

| Método | Rota | O que faz | Conta clique? |
|--------|------|-----------|---------------|
| `POST` | `/link` | Cria um link curto | não |
| `GET`  | `/link/{codigo}` | Redireciona (HTTP 302) para a URL original | **sim** |
| `GET`  | `/link/{codigo}/status` | Devolve as estatísticas do link | não |

### Criar um link

```bash
curl -X POST http://localhost:8080/link \
  -H "Content-Type: application/json" \
  -d '{"url": "https://www.example.com/uma/pagina/bem/longa"}'
```

Resposta:

```json
{
  "url": "https://www.example.com/uma/pagina/bem/longa",
  "codigo": "aB3xZ",
  "dataCriacao": "2026-09-20",
  "cliques": 0,
  "ultimosAcessos": []
}
```

O link curto é `http://localhost:8080/link/aB3xZ`.

### Acessar o link curto (redireciona e conta 1 clique)

```bash
curl -i http://localhost:8080/link/aB3xZ
```

### Consultar as estatísticas (não conta clique)

```bash
curl http://localhost:8080/link/aB3xZ/status
```

### Erros

| Situação | Status |
|----------|--------|
| URL vazia, sem `http`/`https` ou sem host | `400 Bad Request` |
| Código de link inexistente (`/link/{codigo}` ou `/link/{codigo}/status`) | `404 Not Found` |

Os erros seguem o formato `ProblemDetail` do Spring (`title`, `status`, `detail`). Exemplo:

```bash
curl -i -X POST http://localhost:8080/link \
  -H "Content-Type: application/json" \
  -d '{"url": "ftp://exemplo.com"}'
```

## Estrutura do projeto

```
src/main/java/com/juanlucas/encurtador_de_links_api/
├── Controller/   # rotas HTTP (LinkController)
├── Service/      # regras de negócio (LinkService)
├── Repository/   # acesso ao banco (Spring Data JPA)
├── Model/
│   ├── Entity/   # Link e EstatisticaLink
│   └── DTO/      # CriarLinkRequest (entrada) e SaidaLinkRequest (saída)
└── Exception/    # exceções e GlobalExceptionHandler

src/main/resources/
├── application.yaml
└── static/       # interface web (index.html, script.js, style.css)
```

## Decisões técnicas

- **Só aceitar `http`/`https` com host:** além do `@NotBlank` no DTO, o service rejeita (com `400`) qualquer outro esquema (`ftp:`, `javascript:`, `file:`) e URLs sem host. Assim não guardo links que não fazem sentido nem redireciono o usuário para esquemas perigosos.
- **DTO em vez de expor a entidade:** `CriarLinkRequest` (entrada) e `SaidaLinkRequest` (saída). A API não depende da estrutura do banco, e a resposta traz só o que interessa (URL, código, data, cliques e acessos).
- **`EstatisticaLink` separada de `Link`** (relação 1:1): separa "o link" (URL e código) de "o uso do link" (cliques, data de criação e horários dos acessos). Assim as estatísticas podem evoluir sem mexer na entidade principal.
- **Código aleatório de 5 caracteres:** letras e números (62⁵ ≈ 916 milhões de combinações). É curto para caber num link e não é sequencial, então não dá para percorrer os links em ordem. Se o código já existir, gera outro (a limitação dessa checagem está em "O que ficou de fora").
- **`ddl-auto: update` + H2 em memória:** o Hibernate cria e atualiza as tabelas a partir das entidades, e o H2 permite rodar o projeto sem instalar banco. Em produção usaria um banco persistente e migrations.

## O que ficou de fora (e por quê)

Como o teste permite autonomia de escopo, priorizei ter o fluxo principal completo e organizado (encurtar → redirecionar → contar → consultar), com validação e tratamento de erros. O que não entrou:

| Item | Situação e motivo |
|------|-------------------|
| Testes automatizados | Existe só o teste `contextLoads` gerado pelo Spring Initializr. Testei o fluxo manualmente (interface e `curl`). Próximo passo: testes do service e do controller (MockMvc). |
| Contagem de cliques atômica | Hoje lê o valor, soma 1 e salva; acessos simultâneos ao mesmo link podem perder contagem. Solução: `UPDATE` atômico ou lock. |
| Limite da lista de acessos | `ultimosAcessos` guarda todos os acessos e a API devolve a lista inteira (a tela mostra só os 10 mais recentes). Em produção seria paginada ou limitada. |
| Geração do código | A checagem de colisão carrega todos os códigos existentes e a coluna não tem restrição `unique`; não escala. Solução: `unique` no banco + consulta `existsByCodigo`. |
| Persistência | H2 em memória, então os dados somem ao reiniciar. Trocaria por um banco persistente (ex.: PostgreSQL). |
| Consulta na interface de links antigos | A tela mostra as estatísticas do link recém-criado. Para outro código, use a API (`/link/{codigo}/status`). |
| Autenticação, expiração de links e código personalizado | Não eram pedidos pelo teste; preferi caprichar no básico. |
| Docker e deploy | Fora do escopo; a execução é local. |

## Uso de IA

O teste incentiva o uso consciente de IA, então registro como usei:

- **Front-end (`src/main/resources/static`)**: gerado com IA. Meu foco neste desafio foi o backend com Spring Boot. Testei o fluxo completo da interface contra a API.
- **Backend**: a lógica (camadas, entidades, validação e tratamento de erros) foi escrita por mim. Usei IA como apoio de pesquisa e para tirar dúvidas.
- **Revisão e README**: usei o Claude para revisar o projeto e para redigir este README a partir do código, que depois revisei. A revisão apontou bugs que corrigi: a rota `/status` retornava erro 500 para código inexistente, faltavam parênteses na validação da URL e o front chamava a rota de redirecionamento, que inflava o contador de cliques.
- **Conversas**: os links estão no arquivo [conversas ia](conversas%20ia).

## Autor

[@juan-lucs](https://github.com/juan-lucs)