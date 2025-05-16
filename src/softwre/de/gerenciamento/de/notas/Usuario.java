/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package softwre.de.gerenciamento.de.notas;

/**
 *
 * @author vitor
 */
public class Usuario {

    
    // Atributos do usuário
    private int id;
    private String nome;
    private String cpf;
    private String email;
    private String genero;
    private String dataNascimento;
    private String telefone;
    private String senha;

    private String cidade;
    private String estado;
    private String bairro;
    private String rua;
    private String numero;
    private String complemento;

    // construtor principal 
  
    public Usuario(int id, String nome, String cpf, String email, String genero, String dataNascimento,
                   String telefone, String senha, String cidade, String estado, String bairro,
                   String rua, String numero, String complemento) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.genero = genero;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.senha = senha;
        this.cidade = cidade;
        this.estado = estado;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
    }

    // Construtor vazio
    public Usuario() {}

    
    // Getters
    
    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getCpf() {
        return cpf;
    }
    public String getEmail() {
        return email;
    }
    public String getGenero() {
        return genero;
    }
    public String getDataNascimento() {
        return dataNascimento;
    }
    public String getTelefone() {
        return telefone;
    }
    public String getSenha() {
        return senha;
    }
    public String getCidade() {
        return cidade;
    }
    public String getEstado() {
        return estado;
    }
    public String getBairro() {
        return bairro;
    }
    public String getRua() {
        return rua;
    }
    public String getNumero() {
        return numero;
    }
    public String getComplemento() {
        return complemento;
    }

    
    // Setters
    
    public void setId(int id) {
        this.id = id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }
    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }
    public void setRua(String rua) {
        this.rua = rua;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    // Métodos para atualizar informações do usuário

    public void atualizarNome(String novoNome) {
        this.nome = novoNome;
    }

    public void atualizarSenha(String novaSenha) {
        this.senha = novaSenha;
    }

    public void atualizarCidade(String novaCidade) {
        this.cidade = novaCidade;
    }

    public void atualizarEstado(String novoEstado) {
        this.estado = novoEstado;
    }

    public void atualizarBairro(String novoBairro) {
        this.bairro = novoBairro;
    }
    
    public void atualizarRua(String novaRua) {
        this.rua = novaRua;
    }
    
    public void atualizarNumero(String novoNumero) {
        this.numero = novoNumero;
    }
    
    public void atualizarComplemento(String novoComplemento) {
        this.complemento = novoComplemento;
    }
    
}
