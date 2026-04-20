import java.io.Serializable;

public class Usuario implements Serializable {
    private int id;
    private String nome, email, senha, endereco, cpf;

    public Usuario() {}

    public Usuario(int id, String nome, String email, String senha, String endereco, String cpf) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.endereco = endereco;
        this.cpf = cpf;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public String getEndereco() { return endereco; }
    public String getCpf() { return cpf; }

    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEmail(String email) { this.email = email; }
    public void setSenha(String senha) { this.senha = senha; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setCpf(String cpf) { this.cpf = cpf; }
}
