import java.io.Serializable;

public class Produto implements Serializable {
    private int id, empresaId;
    private String nome, categoria;
    private float valor;

    public Produto() {}

    public Produto(int id, int empresaId, String nome, float valor, String categoria) {
        this.id = id;
        this.empresaId = empresaId;
        this.nome = nome;
        this.valor = valor;
        this.categoria = categoria;
    }

    public int getId() { return id; }
    public int getEmpresaId() { return empresaId; }
    public String getNome() { return nome; }
    public float getValor() { return valor; }
    public String getCategoria() { return categoria; }

    public void setId(int id) { this.id = id; }
    public void setEmpresaId(int empresaId) { this.empresaId = empresaId; }
    public void setNome(String nome) { this.nome = nome; }
    public void setValor(float valor) { this.valor = valor; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}
