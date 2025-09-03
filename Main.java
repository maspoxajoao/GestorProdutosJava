// Este arquivo Main.java é usado apenas para testes no terminal.
// Não é necessário para o funcionamento da interface gráfica (ProdutoGUI).

import java.sql.Connection;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    try (Connection conexao = ConexaoDB.conectar()) {
      ProdutoDAO produtoDAO = new ProdutoDAO(conexao);
      // Lista todos os produtos
      mostrarProdutos(produtoDAO);

      // inserção de produtos
      Produto novoProduto1 = new Produto("Notebook", 10, 1999.99, "Em estoque");
      Produto novoProduto2 = new Produto("Smartphone", 20, 1499.99, "Estoque baixo");
      Produto novoProduto3 = new Produto("Tablet", 15, 799.99, "Estoque baixo");

      produtoDAO.inserir(novoProduto1);
      produtoDAO.inserir(novoProduto2);
      produtoDAO.inserir(novoProduto3);

      // lISTA PRODUTSO APOS INSERIR
      mostrarProdutos(produtoDAO);

      // consult apor id
      Produto produtoConsultado = produtoDAO.consultarPorId(1);
      if (produtoConsultado != null) {
        produtoConsultado.setNome("LapTop");
        System.out.println("novo nome do produto: " + produtoConsultado.getNome());
      } else {
        System.out.println(
            "Produto nao encotnrado");
      }
    } catch (Exception e) {
      System.err.println("Erro geral: " + e.getMessage());
    }
  }

  // metodo para listar os produtdos

  private static void mostrarProdutos(ProdutoDAO produtoDAO) {
    List<Produto> todosProdutos = produtoDAO.listarTodos();
    if (todosProdutos.isEmpty()) {
      System.out.println("Nenhum produto encontrado");
    } else {
      System.out.println("Lista de produtos");
      for (Produto p : todosProdutos) {
        System.out.println(p.getId() + ": " + p.getNome() + " - " + p.getPreco());
      }
    }

  }
}
