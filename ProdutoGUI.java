import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

public class ProdutoGUI extends Application {

  private ProdutoDAO produtoDAO;
  private ObservableList<Produto> produtos;
  private TableView<Produto> tableView;
  private TextField nomeInput, quantidadeInput, precoInput;
  private ComboBox<String> statusComboBox;
  private Connection conexaoDB;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage palco) {

    // Inicializa conexão com SQLite
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      System.err.println("Driver SQLite não encontrado!");
    }
    conexaoDB = ConexaoDB.conectar();
    produtoDAO = new ProdutoDAO(conexaoDB);
    produtos = FXCollections.observableArrayList(produtoDAO.listarTodos());

    palco.setTitle("Gerenciamento de Estoque de Produtos");

    // ===== Root =====
    VBox root = new VBox(20);
    root.setPadding(new Insets(15));

    // ===== Formulário refinado =====
    VBox formBox = new VBox(15);
    formBox.setPadding(new Insets(15));
    formBox.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 8px; "
        + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8,0,0,2);");

    GridPane formGrid = new GridPane();
    formGrid.setHgap(12);
    formGrid.setVgap(12);
    formGrid.setAlignment(Pos.CENTER);

    ColumnConstraints col1 = new ColumnConstraints();
    col1.setPercentWidth(25); // Label ocupa 25%
    ColumnConstraints col2 = new ColumnConstraints();
    col2.setPercentWidth(75); // Input ocupa 75%
    formGrid.getColumnConstraints().addAll(col1, col2);

    // Produto
    Label nomeLabel = new Label("Produto:");
    nomeInput = new TextField();
    nomeInput.setPromptText("Digite o nome do produto");
    nomeInput.setMaxWidth(Double.MAX_VALUE);
    formGrid.add(nomeLabel, 0, 0);
    formGrid.add(nomeInput, 1, 0);

    // Quantidade
    Label quantidadeLabel = new Label("Quantidade:");
    quantidadeInput = new TextField();
    quantidadeInput.setPromptText("Digite a quantidade");
    quantidadeInput.setMaxWidth(Double.MAX_VALUE);
    formGrid.add(quantidadeLabel, 0, 1);
    formGrid.add(quantidadeInput, 1, 1);

    // Preço
    Label precoLabel = new Label("Preço:");
    precoInput = new TextField();
    precoInput.setPromptText("Digite o preço");
    precoInput.setMaxWidth(Double.MAX_VALUE);
    formGrid.add(precoLabel, 0, 2);
    formGrid.add(precoInput, 1, 2);

    // Status
    Label statusLabel = new Label("Status:");
    statusComboBox = new ComboBox<>();
    statusComboBox.getItems().addAll("Estoque Normal", "Estoque Baixo");
    statusComboBox.setMaxWidth(Double.MAX_VALUE);
    formGrid.add(statusLabel, 0, 3);
    formGrid.add(statusComboBox, 1, 3);

    // Botões
    HBox buttonBox = new HBox(12);
    buttonBox.setAlignment(Pos.CENTER_RIGHT);

    Button addButton = new Button("Adicionar");
    addButton.setOnAction(e -> adicionarProduto());
    Button updateButton = new Button("Atualizar");
    updateButton.setOnAction(e -> atualizarProduto());
    Button deleteButton = new Button("Excluir");
    deleteButton.setOnAction(e -> excluirProduto());
    Button clearButton = new Button("Limpar");
    clearButton.setOnAction(e -> limparCampos());

    buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, clearButton);

    formBox.getChildren().addAll(formGrid, buttonBox);

    // ===== Tabela =====
    tableView = new TableView<>();
    tableView.setItems(produtos);
    tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

    List<TableColumn<Produto, ?>> columns = List.of(
        criarColuna("ID", "id"),
        criarColuna("Produto", "nome"),
        criarColuna("Quantidade", "quantidade"),
        criarColuna("Preço", "preco"),
        criarColuna("Status", "status"));
    tableView.getColumns().addAll(columns);

    tableView.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldSelection, newSelection) -> preencherCampos(newSelection));

    TitledPane formPane = new TitledPane("Cadastro de Produto", formBox);
    formPane.setCollapsible(false);

    TitledPane tablePane = new TitledPane("Produtos Cadastrados", tableView);
    tablePane.setCollapsible(false);

    root.getChildren().addAll(formPane, tablePane);

    Scene scene = new Scene(root, 850, 600);
    scene.getStylesheets().add("styles-produtos.css"); // CSS refinado já criado
    palco.setScene(scene);
    palco.show();
  }

  @Override
  public void stop() {
    try {
      conexaoDB.close();
    } catch (SQLException e) {
      System.err.println("Erro ao fechar conexão" + e.getMessage());
    }
  }

  // ===== Métodos Auxiliares =====
  private void limparCampos() {
    nomeInput.clear();
    quantidadeInput.clear();
    precoInput.clear();
    statusComboBox.setValue(null);
  }

  private void preencherCampos(Produto produto) {
    if (produto != null) {
      nomeInput.setText(produto.getNome());
      quantidadeInput.setText(String.valueOf(produto.getQuantidade()));
      precoInput.setText(String.valueOf(produto.getPreco()));
      statusComboBox.setValue(produto.getStatus());
    }
  }

  private void adicionarProduto() {
    try {
      String preco = precoInput.getText().replace(',', '.');
      Produto produto = new Produto(nomeInput.getText(),
          Integer.parseInt(quantidadeInput.getText()),
          Double.parseDouble(preco),
          statusComboBox.getValue());
      produtoDAO.inserir(produto);
      produtos.setAll(produtoDAO.listarTodos());
      limparCampos();
    } catch (Exception e) {
      showError("Erro ao adicionar produto", e.getMessage());
    }
  }

  private void atualizarProduto() {
    Produto selectedProduto = tableView.getSelectionModel().getSelectedItem();
    if (selectedProduto != null) {
      try {
        selectedProduto.setNome(nomeInput.getText());
        selectedProduto.setQuantidade(Integer.parseInt(quantidadeInput.getText()));
        String preco = precoInput.getText().replace(',', '.');
        selectedProduto.setPreco(Double.parseDouble(preco));
        selectedProduto.setStatus(statusComboBox.getValue());
        produtoDAO.atualizar(selectedProduto);
        produtos.setAll(produtoDAO.listarTodos());
        limparCampos();
      } catch (Exception e) {
        showError("Erro ao atualizar produto", e.getMessage());
      }
    }
  }

  private void excluirProduto() {
    Produto selectedProduto = tableView.getSelectionModel().getSelectedItem();
    if (selectedProduto != null) {
      produtoDAO.excluir(selectedProduto.getId());
      produtos.setAll(produtoDAO.listarTodos());
      limparCampos();
    }
  }

  private TableColumn<Produto, String> criarColuna(String title, String property) {
    TableColumn<Produto, String> col = new TableColumn<>(title);
    col.setCellValueFactory(new PropertyValueFactory<>(property));
    return col;
  }

  private void showError(String titulo, String mensagem) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(mensagem);
    alert.showAndWait();
  }
}
