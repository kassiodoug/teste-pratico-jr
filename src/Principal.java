import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Principal {
  public static void main(String[] args) {
    List<Funcionario> funcionarios = new ArrayList<>();

    funcionarios.addAll(adicionarFuncionarios());
    removeFuncionarioPorNome("João", funcionarios);
    exibeInformacoesFuncionarios(funcionarios);
    aumentoSalarioFuncionario(funcionarios, new BigDecimal(10));
    agrupaFuncionarios(funcionarios, "funcao");

    System.out.println(funcionarios.size());
  }

  private static List<Funcionario> adicionarFuncionarios() {
    return DadosFuncionarios.obterFuncionarios();
  }

  private static void removeFuncionarioPorNome(String nome, List<Funcionario> funcionarios) {
    funcionarios.removeIf(funcionario -> funcionario.getNome().equals(nome));
  }

  private static void exibeInformacoesFuncionarios(List<Funcionario> funcionarios) {
    funcionarios.forEach(funcionario -> {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

      DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
      symbols.setDecimalSeparator(',');
      symbols.setGroupingSeparator('.');
      DecimalFormat df = new DecimalFormat("###,###,##0.00", symbols);

      String nome = funcionario.getNome();
      String dataNascimento = funcionario.getDataNascimento().format(formatter);
      String salario = df.format(funcionario.getSalario());
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

  private static void agrupaFuncionarios(List<Funcionario> funcionarios, String chaveAgrupamento) {
    Map<String, List<Funcionario>> funcionariosAgrupados = funcionarios.stream()
        .collect(Collectors.groupingBy(Funcionario::getFuncao));

    System.out.println(funcionariosAgrupados);
  }
}