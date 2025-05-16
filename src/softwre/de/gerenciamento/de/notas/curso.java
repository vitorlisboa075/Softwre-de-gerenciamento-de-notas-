
package softwre.de.gerenciamento.de.notas;

class curso {

public class Curso {

    private int id;
    private String nome;
    private String descricao;

    // Construtor vazio
    public Curso() {
    }

    // Construtor completo
    public Curso(int id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return nome;
    }
}

}
