package service;

import model.Aluno;

import java.util.List;

public class AlunoService {

    private final AlunoRepository repo = new AlunoRepository();

    public List<Aluno> buscarTodos() {
        return repo.listarTodos();
    }

    public void excluir(Long id) {
        repo.excluir(id);
    }

    public void abrirFormulario(Aluno aluno) {
        // abre nova tela de formulário em modo edição ou novo
        // caso o aluno != null, popular dados
        // no formulário, validar:
        // - CPF: Validador.validarCPF()
        // - Email: Validador.validarEmail()
        // - Matrícula única: repo.verificarMatriculaUnica()
    }
}
