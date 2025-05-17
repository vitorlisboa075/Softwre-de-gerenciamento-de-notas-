/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package softwre.de.gerenciamento.de.notas;

public class Usuario {
    private int id;
    private String nome;
    private String cpf;
    private String email;
    private String genero;
    private String dataNascimento;
    private String telefone;
    private String senha;
    private String tipoUsuario; // "professor" ou "secretaria"

    // Endereço
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    // Construtor
    public Usuario(int id, String nome, String cpf, String email, String genero, String dataNascimento,
                   String telefone, String senha, String tipoUsuario, String logradouro, String numero,
                   String complemento, String bairro, String cidade, String estado, String cep) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.genero = genero;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }
    
    // Construtor vazio
    public Usuario() {
    }

    // Getters e Setters (agrupados)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    // Métodos de verificação de tipo
    public boolean isProfessor() {
        return tipoUsuario.equalsIgnoreCase("professor");
    }

    public boolean isSecretaria() {
        return tipoUsuario.equalsIgnoreCase("secretaria");
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

    public void atualizarLogradouro(String novoLogradouro) {
        this.logradouro = novoLogradouro;
    }

    public void atualizarNumero(String novoNumero) {
        this.numero = novoNumero;
    }

    public void atualizarComplemento(String novoComplemento) {
        this.complemento = novoComplemento;
    }
}
