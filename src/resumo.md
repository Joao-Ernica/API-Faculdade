# API - Documentação

## Visão Geral
Esta é uma API REST para gerenciamento de produtos usando Spring Boot. A API permite realizar operações CRUD (Create, Read, Update, Delete) em produtos, além de funcionalidades específicas como atualização de estoque.

## Estrutura do Projeto

### Controlador (ProdutoController)
O controlador é responsável por receber as requisições HTTP e direcioná-las para os serviços apropriados.

## Modelo de Dados

### Produto
A entidade `Produto` parece conter, pelo menos, os seguintes campos:
- `id` (Integer): Identificador único do produto
- `estoque` (Integer): Quantidade em estoque do produto
- Outros campos não mostrados no controlador (possivelmente nome, preço, descrição, etc.)

## Serviço (ProdutoService)

O controlador utiliza um serviço (`ProdutoService`) com os seguintes métodos:
- `findAll()`: Retorna todos os produtos
- `findById(Integer id)`: Busca um produto por ID
- `save(Produto produto)`: Salva ou atualiza um produto
- `delete(Integer id)`: Remove um produto
- `validarProduto(Produto produto)`: Valida os dados de um produto
- `atualizarEstoque(Integer id, Integer estoque)`: Atualiza o estoque de um produto

## Observações Importantes
1. A API utiliza o padrão REST para comunicação
2. A injeção de dependências é feita via construtor com auxílio da anotação `@RequiredArgsConstructor`
3. Respostas HTTP são encapsuladas em objetos `ResponseEntity` para controle de status e cabeçalhos
4. Validações são aplicadas para garantir a integridade dos dados
5. Estoque padrão é 0 quando não informado na criação de produtos
