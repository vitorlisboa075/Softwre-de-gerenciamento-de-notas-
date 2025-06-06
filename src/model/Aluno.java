package model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Aluno {

    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty nome = new SimpleStringProperty();
    private final StringProperty matricula = new SimpleStringProperty();
    private final StringProperty cpf = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty telefone = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> dataNascimento = new SimpleObjectProperty<>();
    private final StringProperty cidade = new SimpleStringProperty();
    private final StringProperty estado = new SimpleStringProperty();
    private final StringProperty bairro = new SimpleStringProperty();
    private final StringProperty rua = new SimpleStringProperty();
    private final StringProperty numero = new SimpleStringProperty();
    private final StringProperty complemento = new SimpleStringProperty();
    private final StringProperty curso = new SimpleStringProperty();
    private final StringProperty turma = new SimpleStringProperty();

    public Aluno(int novoId, String nome, String cpf, String email, String telefone, String logradouro, String numero, String complemento, String bairro, String cidade, String estado, String cep, Curso cursoSelecionado, Turma turmaSelecionada) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // ID
    public long getId() {
        return id.get();
    }

    public void setId(long value) {
        id.set(value);
    }

    public LongProperty idProperty() {
        return id;
    }

    // Nome
    public String getNome() {
        return nome.get();
    }

    public void setNome(String value) {
        nome.set(value);
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    // Matrícula
    public String getMatricula() {
        return matricula.get();
    }

    public void setMatricula(String value) {
        matricula.set(value);
    }

    public StringProperty matriculaProperty() {
        return matricula;
    }

    // CPF
    public String getCpf() {
        return cpf.get();
    }

    public void setCpf(String value) {
        cpf.set(value);
    }

    public StringProperty cpfProperty() {
        return cpf;
    }

    // Email
    public String getEmail() {
        return email.get();
    }

    public void setEmail(String value) {
        email.set(value);
    }

    public StringProperty emailProperty() {
        return email;
    }

    // Telefone
    public String getTelefone() {
        return telefone.get();
    }

    public void setTelefone(String value) {
        telefone.set(value);
    }

    public StringProperty telefoneProperty() {
        return telefone;
    }

    // Data de nascimento
    public LocalDate getDataNascimento() {
        return dataNascimento.get();
    }

    public void setDataNascimento(LocalDate value) {
        dataNascimento.set(value);
    }

    public ObjectProperty<LocalDate> dataNascimentoProperty() {
        return dataNascimento;
    }

    // Cidade
    public String getCidade() {
        return cidade.get();
    }

    public void setCidade(String value) {
        cidade.set(value);
    }

    public StringProperty cidadeProperty() {
        return cidade;
    }

    // Estado
    public String getEstado() {
        return estado.get();
    }

    public void setEstado(String value) {
        estado.set(value);
    }

    public StringProperty estadoProperty() {
        return estado;
    }

    // Bairro
    public String getBairro() {
        return bairro.get();
    }

    public void setBairro(String value) {
        bairro.set(value);
    }

    public StringProperty bairroProperty() {
        return bairro;
    }

    // Rua
    public String getRua() {
        return rua.get();
    }

    public void setRua(String value) {
        rua.set(value);
    }

    public StringProperty ruaProperty() {
        return rua;
    }

    // Número
    public String getNumero() {
        return numero.get();
    }

    public void setNumero(String value) {
        numero.set(value);
    }

    public StringProperty numeroProperty() {
        return numero;
    }

    // Complemento
    public String getComplemento() {
        return complemento.get();
    }

    public void setComplemento(String value) {
        complemento.set(value);
    }

    public StringProperty complementoProperty() {
        return complemento;
    }

    // Curso
    public String getCurso() {
        return curso.get();
    }

    public void setCurso(String value) {
        curso.set(value);
    }

    public StringProperty cursoProperty() {
        return curso;
    }

    // Turma
    public String getTurma() {
        return turma.get();
    }

    public void setTurma(String value) {
        turma.set(value);
    }

    public StringProperty turmaProperty() {
        return turma;
    }
}
