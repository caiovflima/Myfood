import java.io.Serializable;
import java.util.*;

public class Empresa implements Serializable {
    private int id, dono;
    private String nome, endereco, tipoCozinha;
    private List<Integer> produtos = new ArrayList<>();

    public Empresa() {}

    public Empresa(int id, int dono, String nome, String endereco, String tipoCozinha) {
        this.id = id;
        this.dono = dono;
        this.nome = nome;
        this.endereco = endereco;
        this.tipoCozinha = tipoCozinha;
    }

    public int getId() { return id; }
    public int getDono() { return dono; }
    public String getNome() { return nome; }
    public String getEndereco() { return endereco; }
    public String getTipoCozinha() { return tipoCozinha; }
    public List<Integer> getProdutos() { return produtos; }

    public void setId(int id) { this.id = id; }
    public void setDono(int dono) { this.dono = dono; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setTipoCozinha(String tipoCozinha) { this.tipoCozinha = tipoCozinha; }
    public void setProdutos(List<Integer> produtos) { this.produtos = produtos; }
}
