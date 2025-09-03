# Gestão de Produtos

Este é um projeto acadêmico desenvolvido em Java, que implementa um sistema de **Gestão de Produtos** com suporte a banco de dados SQLite e interface gráfica. O objetivo é gerenciar produtos, permitindo operações como cadastro, consulta, atualização e exclusão.

---

## Funcionalidades

- **Interface gráfica (JavaFX):**
  - Visualização de produtos cadastrados.
  - Adição, edição e remoção de produtos.
  - Estilização com CSS (`styles-produtos.css`).

- **Banco de dados SQLite:**
  - Persistência dos dados em `meu_banco_de_dados.db`.
  - Criação automática da tabela de produtos, caso não exista.

- **Versão para terminal:**
  - Testes e manipulação de produtos diretamente pelo terminal.

---

## Estrutura do Projeto

- **Arquivos principais:**
  - `Main.java`: Classe principal para testes no terminal.
  - `ProdutoGUI.java`: Interface gráfica para gestão de produtos.
  - `ProdutoDAO.java`: Classe para acesso e manipulação dos dados no banco.
  - `ConexaoDB.java`: Gerencia a conexão com o banco de dados SQLite.
  - `CriadorTabela.java`: Cria a tabela de produtos no banco, se necessário.

- **Banco de dados:**
  - `meu_banco_de_dados.db`: Arquivo SQLite onde os dados são armazenados.

- **Dependências externas:**
  - `sqlite-jdbc-3.46.0.0.jar`: Driver JDBC para conexão com o SQLite.
  - `slf4j-api-2.0.12.jar` e `slf4j-simple-2.0.12.jar`: Biblioteca para logging.

- **Estilo:**
  - `styles-produtos.css`: Arquivo CSS para estilização da interface gráfica.

---

## Como executar

### Pré-requisitos

- Java 8 ou superior instalado.
- JavaFX configurado no ambiente.
- Dependências externas (`.jar`) incluídas no classpath.

### Compilação

No terminal, navegue até a pasta do projeto e execute:

```bash
javac -cp ".;sqlite-jdbc-3.46.0.0.jar;slf4j-api-2.0.12.jar;slf4j-simple-2.0.12.jar" *.java
```

### Execução

Para executar a versão gráfica:

```bash
java -cp ".;sqlite-jdbc-3.46.0.0.jar;slf4j-api-2.0.12.jar;slf4j-simple-2.0.12.jar" ProdutoGUI
```

Para executar a versão terminal:

```bash
java -cp ".;sqlite-jdbc-3.46.0.0.jar;slf4j-api-2.0.12.jar;slf4j-simple-2.0.12.jar" Main
```

---

## Melhorias futuras

- [ ] Adicionar suporte a relatórios em PDF.
- [ ] Implementar autenticação de usuários.
- [ ] Adicionar filtros avançados na interface gráfica.
- [ ] Internacionalização (i18n) para suporte a múltiplos idiomas.

---

## Sobre

Este projeto foi desenvolvido como parte dos estudos em Engenharia de Software, com foco em:

- Programação orientada a objetos.
- Integração com banco de dados.
- Criação de interfaces gráficas com JavaFX.

---

**Autor:** João