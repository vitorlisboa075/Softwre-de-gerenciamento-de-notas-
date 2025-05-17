/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package softwre.de.gerenciamento.de.notas;

import softwre.de.gerenciamento.de.notas.curso.Curso;

/**
 *
 * @author Tulyo
 */
public class Disciplina {
    

    private int id;
    private String nome;
    private String descricao;
    private int cargaHoraria;
    private String codigo;
    private String ementa;
    private String semestreOferecido;
    private Curso curso;

    public Disciplina(int idDisc, String nomeDisc, String descricaoDisc, int carga,
                      String codDisc, String ementaDisc, String semestre, Curso cursoRelacionado) {
        id = idDisc;
        nome = nomeDisc;
        descricao = descricaoDisc;
        cargaHoraria = carga;
        codigo = codDisc;
        ementa = ementaDisc;
        semestreOferecido = semestre;
        curso = cursoRelacionado;
    }

    public int getId() {
        return id;
    }

    public void setId(int novoId) {
        id = novoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String novoNome) {
        nome = novoNome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String novaDescricao) {
        descricao = novaDescricao;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int novaCarga) {
        cargaHoraria = novaCarga;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String novoCodigo) {
        codigo = novoCodigo;
    }

    public String getEmenta() {
        return ementa;
    }

    public void setEmenta(String novaEmenta) {
        ementa = novaEmenta;
    }

    public String getSemestreOferecido() {
        return semestreOferecido;
    }

    public void setSemestreOferecido(String novoSemestre) {
        semestreOferecido = novoSemestre;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso novoCurso) {
        curso = novoCurso;
    }

    @Override
    public String toString() {
        return "Disciplina{" +
               "id=" + id +
               ", nome='" + nome + '\'' +
               ", descricao='" + descricao + '\'' +
               ", cargaHoraria=" + cargaHoraria +
               ", codigo='" + codigo + '\'' +
               ", ementa='" + ementa + '\'' +
               ", semestreOferecido='" + semestreOferecido + '\'' +
               ", curso=" + curso +
               '}';
    }
}
