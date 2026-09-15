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
  public static void main(String[] args) {
    List<Funcionario> funcionarios = new ArrayList<>();
    Map<String, List<Funcionario>> funcionariosAgrupados = new HashMap<>();

    funcionarios.addAll(adicionarFuncionarios());

    // Tabela inicial com todos os funcionários
    exibeTabelaFuncionarios(funcionarios);

    removeFuncionarioPorNome("João", funcionarios);
    exibeInformacoesFuncionarios(funcionarios);
    aumentoSalarioFuncionario(funcionarios, new BigDecimal(10));
    agrupaFuncionarios(funcionarios, "funcao", funcionariosAgrupados);
    exibeFuncionariosAgrupados(funcionariosAgrupados);
    exibeAniversariantesPorMeses(funcionarios, new int[] { 10, 12 });

    Funcionario funcionarioComMaiorIdade = buscaFuncionarioMaiorIdade(funcionarios);
    System.out.println(String.format("Funcionário(a) com a maior idade: %s (%d anos).",
        funcionarioComMaiorIdade.getNome(), funcionarioComMaiorIdade.getIdade()));

    funcionariosOrdemPorNome(funcionarios);
    System.out.println(funcionarios);

    System.out.println(funcionarios.size());

  }

  public static void exibeTabelaFuncionarios(List<Funcionario> funcionarios) {
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

    System.out.printf("+--------------------+--------------------+----------------+----------------+%n");
  }

  private static List<Funcionario> adicionarFuncionarios() {
    return DadosFuncionarios.obterFuncionarios();
  }

  private static void removeFuncionarioPorNome(String nome, List<Funcionario> funcionarios) {
    funcionarios.removeIf(funcionario -> funcionario.getNome().equals(nome));
  }

  private static void exibeInformacoesFuncionarios(List<Funcionario> funcionarios) {
    funcionarios.forEach(funcionario -> {
      String nome = funcionario.getNome();
      String dataNascimento = FormataData.format(funcionario.getDataNascimento(), "dd/MM/yyyy");
      String salario = FormataNumeroBr.format(funcionario.getSalario());
      String funcao = funcionario.getFuncao();

      System.out.println(String.format("%s e %s | %s", nome, dataNascimento, salario));
    });
  }

  private static void aumentoSalarioFuncionario(List<Funcionario> funcionarios, BigDecimal porcentagem) {
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

  private static void agrupaFuncionarios(List<Funcionario> funcionarios, String chaveAgrupamento,
      Map<String, List<Funcionario>> funcionariosAgrupados) {
    Map<String, List<Funcionario>> agrupados = funcionarios.stream()
        .collect(Collectors.groupingBy(Funcionario::getFuncao));

    funcionariosAgrupados.putAll(agrupados);
  }

  private static void exibeFuncionariosAgrupados(Map<String, List<Funcionario>> funcionariosAgrupados) {
    funcionariosAgrupados.forEach((grupo, funcionarios) -> {
      System.out.println(grupo);

      funcionarios.forEach(funcionario -> {
        System.out.println(String.format("%s", funcionario.getNome()));
      });

      System.out.println();
    });
  }

  private static void exibeAniversariantesPorMeses(List<Funcionario> funcionarios, int[] meses) {
    for (Funcionario funcionario : funcionarios) {
      int mesNascimento = funcionario.getDataNascimento().getMonthValue();

      for (int mes : meses) {
        if (mesNascimento == mes) {
          System.out.println(funcionario.getNome());
          break;
        }
      }
    }
  }

  private static Funcionario buscaFuncionarioMaiorIdade(List<Funcionario> funcionarios) {
    return funcionarios.stream()
        .min(Comparator.comparing(Funcionario::getDataNascimento))
        .orElse(null);
  }

  private static void funcionariosOrdemPorNome(List<Funcionario> funcionarios) {
    funcionarios.sort(Comparator.comparing(Funcionario::getNome, String.CASE_INSENSITIVE_ORDER));
  }
}