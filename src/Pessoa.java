import java.time.LocalDate;
import java.time.Period;

public class Pessoa {
  private String nome;
  private LocalDate dataNascimento;

  public Pessoa(String nome, LocalDate dataNascimento) {
    this.nome = nome;
    this.dataNascimento = dataNascimento;
  }

  public String getNome() {
    return this.nome;
  }

  public LocalDate getDataNascimento() {
    return this.dataNascimento;
  }

  public int getIdade() {
    return Period.between(
        this.getDataNascimento(),
        LocalDate.now()).getYears();
  }
}
