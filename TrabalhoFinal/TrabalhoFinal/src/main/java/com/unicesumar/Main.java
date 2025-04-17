package com.unicesumar;

import com.unicesumar.entities.Product;
import com.unicesumar.entities.User;
import com.unicesumar.paymentMethods.BoletoPayment;
import com.unicesumar.paymentMethods.CreditCardPayment;
import com.unicesumar.paymentMethods.PixPayment;
import com.unicesumar.repository.ProductRepository;
import com.unicesumar.repository.UserRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        ProductRepository listaDeProdutos = null;
        UserRepository listaDeUsuarios = null;
        Connection conn = null;
        UUID uuid = UUID.randomUUID();
        String nome;
        String email;
        String email2;
        String senha;
        int opcao;
        int Id;
        double valor = 0;
        int metodoPagamento;
        PixPayment pixPayment = new PixPayment();
        CreditCardPayment creditCardPayment = new CreditCardPayment();
        BoletoPayment boletoPayment = new BoletoPayment();
        List<String> carrinho = new ArrayList<String>();
        String nominho;



        String url = "jdbc:sqlite:database.sqlite";


        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                listaDeUsuarios = new UserRepository(conn);
                listaDeProdutos = new ProductRepository(conn);
            } else {
                System.out.println("Falha na conexão.");
                System.exit(1);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            System.exit(1);
        }


        Scanner scanner = new Scanner(System.in);
        int option;

        List<Product> products;
        listaDeProdutos.resetarTabelaProdutos();
        listaDeUsuarios.resetarTabelaUsers();


        listaDeProdutos.save(new Product(UUID.randomUUID(), "Panela", 100.00, 1));
        listaDeProdutos.save(new Product(UUID.randomUUID() ,"Batedeira", 130.00, 2));
        listaDeProdutos.save(new Product(UUID.randomUUID() ,"Panela de presão", 120.00, 3));
        listaDeProdutos.save(new Product(UUID.randomUUID() ,"Pote", 80.00, 4));
        listaDeProdutos.save(new Product(UUID.randomUUID() ,"Prato", 20.00, 5));
        listaDeProdutos.save(new Product(UUID.randomUUID() ,"Copo", 15.00, 6));
        listaDeProdutos.save(new Product(UUID.randomUUID() ,"Talher", 10.00, 7));

        listaDeUsuarios.save(new User(UUID.randomUUID(), "Pâmela","pamela", "1234"));
        listaDeUsuarios.save(new User(UUID.randomUUID(), "Teste","email", "1234"));
        listaDeUsuarios.save(new User(UUID.randomUUID(), "Rafael","prof", "Da100PraNóis!"));

        /// Estamos resetando a tabela em toda execução para garantir que não haja nenhum produto cadastrado 2x para não dar erro no SQLite.
        /// E também pra que não seja necessário cadastrar os produtos manualmente para testar as funções!!



        do {
            System.out.println("\n\n---MENU---");
            System.out.println("1 - Cadastrar Produto");
            System.out.println("2 - Listar Produtos");
            System.out.println("3 - Cadastrar Usuário");
            System.out.println("4 - Listar Usuários");
            System.out.println("5 - Comprar");
            System.out.println("6 - Sair");
            System.out.print("Escolha uma opção: ");
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.print("Produtos já cadastrados!");
                    break;
                case 2:
                    System.out.println("Listar Produtos");
                    products = listaDeProdutos.findAll();
                    products.forEach(System.out::println);
                    break;

                case 3:
                    System.out.print("Nome do usuário: ");
                    nome = scanner.next();
                    System.out.print("E-mail: ");
                    email = scanner.next();
                    System.out.print("Senha: ");
                    senha = scanner.next();
                    listaDeUsuarios.save(new User(UUID.randomUUID(), nome, email, senha));
                    break;

                case 4:
                    System.out.println("Listar Usuários");
                    List<User> users = listaDeUsuarios.findAll();
                    users.forEach(System.out::println);
                    break;

                case 5:
                    System.out.print("Informe seu e-mail: ");
                    email2 = scanner.next();


                    Optional<User> userOptional = listaDeUsuarios.findByEmail(email2);

                    if (userOptional.isPresent()) {
                        nominho = userOptional.get().getName();
                        System.out.println("\nUsuário encontrado: " + nominho);
                        // E-mail existe, continuar o fluxo
                        products = listaDeProdutos.findAll();
                        products.forEach(System.out::println);

                        do {
                            System.out.println("\nDeseja adicionar algum produto ao carrinho? [1] SIM | [2] NÃO");
                            opcao = scanner.nextInt();
                            if (opcao == 1) {
                                System.out.print("\nInforme o ID do produto: ");
                                Id = scanner.nextInt();
                                Optional<Product> ProductOptional = listaDeProdutos.findById2(Id);
                                if (ProductOptional.isPresent()) {
                                    System.out.println(ProductOptional.get().getName() + " adicionado(a) ao carrinho!" + "\nPreço: R$" + ProductOptional.get().getPrice());
                                    carrinho.add(ProductOptional.get().getName());
                                    Double PriceOptional = listaDeProdutos.findPriceById(Id);
                                    valor += PriceOptional;
                                    System.out.println("\nValor Total: R$" + valor);
                                }else {
                                    System.out.println("\nProduto não existente!");
                                }

                            }
                        }while (opcao != 2);
                        if (valor > 0){
                            do {
                                System.out.println("\nEscolha sua forma de pagamento. [1] PIX | [2] BOLETO | [3] CARTÃO | [4] CANCELAR COMPRA");
                                metodoPagamento = scanner.nextInt();
                                if (metodoPagamento == 1) {
                                    pixPayment.pay(valor);
                                } else if (metodoPagamento == 2) {
                                    boletoPayment.pay(valor);
                                } else if (metodoPagamento == 3) {
                                    creditCardPayment.pay(valor);
                                } else if (metodoPagamento == 4) {
                                    System.out.println("Compra cancelada.");
                                } else {
                                    System.out.println("Escolha inválida.");
                                }
                            }while (metodoPagamento > 4 || metodoPagamento < 1);
                            if (metodoPagamento < 4){
                                Iterator<String> iterator = carrinho.iterator();
                                System.out.println("\n\n-----Nota fiscal-----");
                                System.out.println("Nome: " + nominho + "\nProdutos: ");
                                while (iterator.hasNext()) {
                                        System.out.println(iterator.next());
                                }
                                System.out.print("\nValor Total: R$" + valor + "\nMétodo de pagamento: ");
                                if (metodoPagamento == 1){
                                    System.out.print("Pix \n");
                                    option = 6;

                                } else if (metodoPagamento == 2) {
                                    System.out.print("Boleto \n");
                                    option = 6;
                                } else {
                                    System.out.print("Cartão de crédito \n");
                                    option = 6;
                                }
                                }

                        }
                        carrinho.clear();
                        valor = 0;
                    } else {
                        System.out.println("E-mail inexistente!");
                        break;
                    }
                    break;

                case 6:
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Opção inválida.");
                    break;
            }

        } while (option != 6);

        scanner.close();
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
