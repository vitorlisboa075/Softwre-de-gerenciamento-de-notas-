// Local: src/model/Usuario.java
package model;

import javafx.beans.property.*;

public class Usuario {

    // --- Dados Pessoais ---
    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty nome = new SimpleStringProperty();
    private final StringProperty cpf = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty telefone = new SimpleStringProperty();
    private final StringProperty senha = new SimpleStringProperty();
    private final StringProperty tipoUsuario = new SimpleStringProperty(); // "Aluno", "Professor", "Secretaria"
    
    // --- Campo específico para Aluno ---
    private final StringProperty matricula = new SimpleStringProperty();

    // --- Endereço ---
    private final StringProperty logradouro = new SimpleStringProperty();
    private final StringProperty numero = new SimpleStringProperty();
    private final StringProperty complemento = new SimpleStringProperty();
    private final StringProperty bairro = new SimpleStringProperty();
    private final StringProperty cidade = new SimpleStringProperty();
    private final StringProperty estado = new SimpleStringProperty();
    private final StringProperty cep = new SimpleStringProperty();

    public Usuario() {}

    //Getters e Setters
    public long getId() { return id.get(); }
    public void setId(long value) { id.set(value); }
    public LongProperty idProperty() { return id; }

    public String getNome() { return nome.get(); }
    public void setNome(String value) { nome.set(value); }
    public StringProperty nomeProperty() { return nome; }

    public String getCpf() { return cpf.get(); }
    public void setCpf(String value) { cpf.set(value); }
    public StringProperty cpfProperty() { return cpf; }

    public String getEmail() { return email.get(); }
    public void setEmail(String value) { email.set(value); }
    public StringProperty emailProperty() { return email; }
    
    public String getTelefone() { return telefone.get(); }
    public void setTelefone(String value) { telefone.set(value); }
    public StringProperty telefoneProperty() { return telefone; }

    public String getSenha() { return senha.get(); }
    public void setSenha(String value) { senha.set(value); }
    public StringProperty senhaProperty() { return senha; }

    public String getTipoUsuario() { return tipoUsuario.get(); }
    public void setTipoUsuario(String value) { tipoUsuario.set(value); }
    public StringProperty tipoProperty() { return tipoUsuario; }
    
    public String getMatricula() { return matricula.get(); }
    public void setMatricula(String value) { matricula.set(value); }
    public StringProperty matriculaProperty() { return matricula; }

    //getters e setters para endereço 
    public String getLogradouro() { return logradouro.get(); }
    public void setLogradouro(String value) { logradouro.set(value); }
    public StringProperty logradouroProperty() { return logradouro; }

    public String getNumero() { return numero.get(); }
    public void setNumero(String value) { numero.set(value); }
    public StringProperty numeroProperty() { return numero; }

    public String getComplemento() { return complemento.get(); }
    public void setComplemento(String value) { complemento.set(value); }
    public StringProperty complementoProperty() { return complemento; }

    public String getBairro() { return bairro.get(); }
    public void setBairro(String value) { bairro.set(value); }
    public StringProperty bairroProperty() { return bairro; }

    public String getCidade() { return cidade.get(); }
    public void setCidade(String value) { cidade.set(value); }
    public StringProperty cidadeProperty() { return cidade; }

    public String getEstado() { return estado.get(); }
    public void setEstado(String value) { estado.set(value); }
    public StringProperty estadoProperty() { return estado; }

    public String getCep() { return cep.get(); }
    public void setCep(String value) { cep.set(value); }
    public StringProperty cepProperty() { return cep; }
    
    
    // Métodos auxiliares
    public boolean isProfessor() {
        return "professor".equalsIgnoreCase(getTipoUsuario());
    }

    public boolean isSecretaria() {
        return "secretaria".equalsIgnoreCase(getTipoUsuario());
    }
    
    public boolean isAluno() {
        return "aluno".equalsIgnoreCase(getTipoUsuario());
    }
}

