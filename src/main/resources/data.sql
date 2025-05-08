INSERT INTO clientes (nome, email, telefone, senha) VALUES
('João', 'joao@hotmail.com', '12345678', 'SenhaFacil123'),
('Lunna', 'lunna@hotmail.com', '87654321', 'SenhaFacil123'),
('Tabata', 'tabata@hotmail.com', '23456789', 'SenhaFacil123'),
('Gabriel', 'gabriel@hotmail.com', '34567890', 'SenhaFacil123'),
('Breno', 'breno@hotmail.com', '45678901', 'SenhaFacil123');

INSERT INTO produtos (nome, descricao, preco, estoque) VALUES
('Trilogia, O Senhor dos Aneis', 'Uma obra prima escrita por J.R.R Tolkien', 159.69, 50),
('Harry Potter e a Pedra Filosofal', 'Primeiro livro da série Harry Potter, escrito por J.K. Rowling', 49.90, 50),
('Duna', 'Um clássico da ficção científica, escrito por Frank Herbert', 89.90, 50),
('O Hobbit', 'Aventuras de Bilbo Bolseiro, escrito por J.R.R. Tolkien', 59.90, 50),
('As Crônicas de Nárnia', 'Uma série de fantasia escrita por C.S. Lewis', 99.90, 50),
('O Nome do Vento', 'Uma obra de fantasia e aventura escrita por Patrick Rothfuss', 59.90, 50),
('Jogos Vorazes', 'Uma série de ficção científica e aventura escrita por Suzanne Collins', 49.90, 50);

INSERT INTO pedidos (cliente_id, status) VALUES
(2, 'AGUARDANDO_PAGAMENTO'),
(1, 'PAGAMENTO_CONFIRMADO'),
(2, 'EM_TRASPORTE'),
(2, 'ENTREGUE'),
(3, 'AGUARDANDO_PAGAMENTO'),
(4, 'PAGAMENTO_CONFIRMADO'),
(5, 'EM_TRASPORTE'),
(3, 'ENTREGUE');

INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario) VALUES
(1, 1, 1, 159.69),
(2, 3, 2, 89.90),
(3, 2, 5, 49.90),
(4, 4, 3, 59.90),
(5, 5, 2, 99.90),
(6, 6, 1, 59.90),
(7, 7, 3, 49.90),
(8, 1, 1, 159.69);