import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import utils.FormataData;
import utils.FormataNumeroBr;

public class Principal {
  public static BigDecimal salarioMinimo;

  public static void main(String[] args) {
    List<Funcionario> funcionarios = new ArrayList<>();
    Map<String, List<Funcionario>> funcionariosAgrupados = new HashMap<>();

    funcionarios.addAll(adicionarFuncionarios());

    System.out.println("3.1 - Tabela inicial com todos os funcionários.");
    exibeTabelaFuncionarios(funcionarios);

    System.out.println("3.2 - Tabela de funcionários sem o funcionário João.");
    removeFuncionarioPorNome("João", funcionarios);
    exibeTabelaFuncionarios(funcionarios);

    System.out.println("3.3 - Funcionários com dados formatados.");
    exibeInformacoesFuncionarios(funcionarios);

    System.out.println("3.4 - Funcionários com 10% de aumento nos salários.");
    aumentoSalarioFuncionario(funcionarios, new BigDecimal(10));
    exibeInformacoesFuncionarios(funcionarios);

    System.out.println("3.5 Funcionários agrupados por função.");
    agrupaFuncionarios(funcionarios, "funcao", funcionariosAgrupados);

    System.out.println("3.6 - Exibição dos funcionários agrupados por função.");
    funcionariosAgrupados.forEach((funcao, f) -> {
      System.out.println(funcao);
      exibeInformacoesFuncionarios(f);
    });

    System.out.println("3.8 - Funcionários que fazem aniversário nos meses 10 e 12.");
    exibeAniversariantesPorMeses(funcionarios, new int[] { 10, 12 });

    System.out.println("3.9 - Funcionário com a maior idade.");
    Funcionario funcionarioComMaiorIdade = buscaFuncionarioMaiorIdade(funcionarios);
    System.out.println(String.format("Funcionário(a) com a maior idade: %s (%d anos).\n",
        funcionarioComMaiorIdade.getNome(), funcionarioComMaiorIdade.getIdade()));

    System.out.println("3.10 - Lista de funcionários em ordem alfabética.");
    funcionariosOrdemPorNome(funcionarios);
    exibeInformacoesFuncionarios(funcionarios);

    System.out.print("3.11 - Total dos salários dos funcionários: ");
    BigDecimal somaTotalSalatios = totalSalarios(funcionarios);
    System.out.println(FormataNumeroBr.format(somaTotalSalatios));

    System.out.println("\n3.12 - Quantos salários mínimos cada funcionário recebe.");
    setSalarioMinimo(new BigDecimal("1212.0"));
    exibeQteSalariosMinimos(funcionarios);
  }

  static void setSalarioMinimo(BigDecimal valor) {
    salarioMinimo = valor;
  }

  static void exibeTabelaFuncionarios(List<Funcionario> funcionarios) {
    System.out.printf("+--------------------+--------------------+----------------+----------------+%n");
    System.out.printf("| Nome               | Data Nascimento    | Salário        | Função         |%n");
    System.out.printf("+--------------------+--------------------+----------------+----------------+%n");

    for (Funcionario f : funcionarios) {
      System.out.printf("| %-18s | %-18s | %-14s | %-14s |%n",
          f.getNome(),
          f.getDataNascimento(),
          f.getSalario(),
          f.getFuncao());
    }

    System.out.printf("+--------------------+--------------------+----------------+----------------+%n\n");
  }

  static List<Funcionario> adicionarFuncionarios() {
    return DadosFuncionarios.obterFuncionarios();
  }

  static void removeFuncionarioPorNome(String nome, List<Funcionario> funcionarios) {
    funcionarios.removeIf(funcionario -> funcionario.getNome().equals(nome));
  }

  static void exibeInformacoesFuncionarios(List<Funcionario> funcionarios) {
    System.out.printf("+--------------------+--------------------+----------------+----------------+%n");
    System.out.printf("| Nome               | Data Nascimento    | Salário        | Função         |%n");
    System.out.printf("+--------------------+--------------------+----------------+----------------+%n");

    funcionarios.forEach(funcionario -> {
      String dataNascimento = FormataData.format(funcionario.getDataNascimento(), "dd/MM/yyyy");
      String salario = FormataNumeroBr.format(funcionario.getSalario());

      System.out.printf("| %-18s | %-18s | %-14s | %-14s |%n",
          funcionario.getNome(),
          dataNascimento,
          salario,
          funcionario.getFuncao());
    });

    System.out.printf("+--------------------+--------------------+----------------+----------------+%n\n");
  }

  static void aumentoSalarioFuncionario(List<Funcionario> funcionarios, BigDecimal porcentagem) {
    if (porcentagem.compareTo(BigDecimal.ZERO) < 0) {
      throw new Error("A porcentagem deve ser um número positivo.");
    }

    BigDecimal fator = porcentagem
        .divide(new BigDecimal("100"))
        .add(BigDecimal.ONE);

    funcionarios.forEach(funcionario -> {
      BigDecimal novoSalario = funcionario.getSalario()
          .multiply(fator)
          .setScale(2, RoundingMode.HALF_UP);

      funcionario.setSalario(novoSalario);
    });
  }

  static void agrupaFuncionarios(List<Funcionario> funcionarios,
      String chaveAgrupamento,
      Map<String, List<Funcionario>> funcionariosAgrupados) {
    Map<String, List<Funcionario>> agrupados = funcionarios.stream()
        .collect(Collectors.groupingBy(Funcionario::getFuncao));

    funcionariosAgrupados.putAll(agrupados);
  }

  static void exibeAniversariantesPorMeses(List<Funcionario> funcionarios, int[] meses) {
    List<Funcionario> funcionariosAniversariantes = new ArrayList<>();

    for (Funcionario funcionario : funcionarios) {
      int mesNascimento = funcionario.getDataNascimento().getMonthValue();

      for (int mes : meses) {
        if (mesNascimento == mes) {
          funcionariosAniversariantes.add(funcionario);
          break;
        }
      }
    }

    if (funcionariosAniversariantes.size() == 0) {
      System.out.println("Não há nenhum funcionário aniversariante nos meses fornecidos.");
      return;
    }

    exibeTabelaFuncionarios(funcionariosAniversariantes);
  }

  static Funcionario buscaFuncionarioMaiorIdade(List<Funcionario> funcionarios) {
    return funcionarios.stream()
        .min(Comparator.comparing(Funcionario::getDataNascimento))
        .orElse(null);
  }

  static void funcionariosOrdemPorNome(List<Funcionario> funcionarios) {
    funcionarios.sort(Comparator.comparing(Funcionario::getNome, String.CASE_INSENSITIVE_ORDER));
  }

  static BigDecimal totalSalarios(List<Funcionario> funcionarios) {
    return funcionarios.stream()
        .map(Funcionario::getSalario)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  static void exibeQteSalariosMinimos(List<Funcionario> funcionarios) {
    System.out.printf("+--------------------+------------------------------------+%n");
    System.out.printf("| Nome               | Qte de salários mínimos que recebe |%n");
    System.out.printf("+--------------------+------------------------------------+%n");

    funcionarios.forEach(funcionario -> {
      BigDecimal qteSalarios = funcionario.getSalario().divide(salarioMinimo, 2, RoundingMode.HALF_UP);

      System.out.printf("| %-18s | %-34s |%n",
          funcionario.getNome(),
          FormataNumeroBr.format(qteSalarios));
    });

    System.out.printf("+--------------------+------------------------------------+%n");
  }
}
