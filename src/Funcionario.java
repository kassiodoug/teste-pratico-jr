import java.math.BigDecimal;
import java.time.LocalDate;

public class Funcionario extends Pessoa {
  private BigDecimal salario;
  private String funcao;

  public Funcionario(String nome, LocalDate dataNascimento, BigDecimal salario, String funcao) {
    super(nome, dataNascimento);
    this.salario = salario;
    this.funcao = funcao;
  }

  public BigDecimal getSalario() {
    return this.salario;
  }

  public String getFuncao() {
    return this.funcao;
  }

  public void setSalario(BigDecimal salario) {
    this.salario = salario;
  }
}
