import java.util.*;
import java.io.*;
import java.beans.*;

public class Facade {

    private Map<Integer, Usuario> usuarios = new HashMap<>();
    private Map<Integer, Empresa> empresas = new HashMap<>();
    private Map<Integer, Produto> produtos = new HashMap<>();

    private int nextUserId = 1;
    private int nextEmpresaId = 1;
    private int nextProdutoId = 1;

    private final String FILE = "dados.xml";

    public Facade() {
        carregar();
    }

    public void zerarSistema() {
        usuarios.clear();
        empresas.clear();
        produtos.clear();
        nextUserId = 1;
        nextEmpresaId = 1;
        nextProdutoId = 1;
        salvar();
    }

    public void encerrarSistema() {
        salvar();
    }

   
    private void salvar() {
        try {
            XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(FILE)));

            encoder.writeObject(usuarios);
            encoder.writeObject(empresas);
            encoder.writeObject(produtos);
            encoder.writeObject(nextUserId);
            encoder.writeObject(nextEmpresaId);
            encoder.writeObject(nextProdutoId);

            encoder.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void carregar() {
        try {
            File f = new File(FILE);
            if (!f.exists()) return;

            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(FILE)));

            usuarios = (Map<Integer, Usuario>) decoder.readObject();
            empresas = (Map<Integer, Empresa>) decoder.readObject();
            produtos = (Map<Integer, Produto>) decoder.readObject();
            nextUserId = (int) decoder.readObject();
            nextEmpresaId = (int) decoder.readObject();
            nextProdutoId = (int) decoder.readObject();

            decoder.close();
        } catch (Exception e) {
            
        }
    }

    

    public void criarUsuario(String nome, String email, String senha, String endereco) {
        validarUsuario(nome, email, senha, endereco);

        for (Usuario u : usuarios.values()) {
            if (u.getEmail().equals(email))
                throw new RuntimeException("Conta com esse email ja existe");
        }

        usuarios.put(nextUserId, new Usuario(nextUserId++, nome, email, senha, endereco, null));
        salvar();
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf) {
        validarUsuario(nome, email, senha, endereco);

        if (cpf == null || cpf.isEmpty() || cpf.length() != 14)
            throw new RuntimeException("CPF invalido");

        for (Usuario u : usuarios.values()) {
            if (u.getEmail().equals(email))
                throw new RuntimeException("Conta com esse email ja existe");
        }

        usuarios.put(nextUserId, new Usuario(nextUserId++, nome, email, senha, endereco, cpf));
        salvar();
    }

    private void validarUsuario(String nome, String email, String senha, String endereco) {
        if (nome == null || nome.isEmpty()) throw new RuntimeException("Nome invalido");
        if (email == null || email.isEmpty() || !email.contains("@"))
            throw new RuntimeException("Email invalido");
        if (senha == null || senha.isEmpty()) throw new RuntimeException("Senha invalido");
        if (endereco == null || endereco.isEmpty()) throw new RuntimeException("Endereco invalido");
    }

    public int login(String email, String senha) {
        if (email == null || senha == null || email.isEmpty() || senha.isEmpty())
            throw new RuntimeException("Login ou senha invalidos");

        for (Usuario u : usuarios.values()) {
            if (u.getEmail().equals(email) && u.getSenha().equals(senha))
                return u.getId();
        }
        throw new RuntimeException("Login ou senha invalidos");
    }

    public String getAtributoUsuario(int id, String atributo) {
        Usuario u = usuarios.get(id);
        if (u == null) throw new RuntimeException("Usuario nao cadastrado.");

        switch (atributo) {
            case "nome": return u.getNome();
            case "email": return u.getEmail();
            case "senha": return u.getSenha();
            case "endereco": return u.getEndereco();
            case "cpf": return u.getCpf() == null ? "" : u.getCpf();
        }
        return "";
    }

    

    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, String tipoCozinha) {

        Usuario u = usuarios.get(dono);
        if (u == null || u.getCpf() == null)
            throw new RuntimeException("Usuario nao pode criar uma empresa");

        for (Empresa e : empresas.values()) {
            if (e.getNome().equals(nome) && e.getDono() != dono)
                throw new RuntimeException("Empresa com esse nome ja existe");

            if (e.getNome().equals(nome) && e.getEndereco().equals(endereco) && e.getDono() == dono)
                throw new RuntimeException("Proibido cadastrar duas empresas com o mesmo nome e local");
        }

        Empresa e = new Empresa(nextEmpresaId++, dono, nome, endereco, tipoCozinha);
        empresas.put(e.getId(), e);
        salvar();
        return e.getId();
    }

    public String getEmpresasDoUsuario(int idDono) {
        Usuario u = usuarios.get(idDono);
        if (u == null || u.getCpf() == null)
            throw new RuntimeException("Usuario nao pode criar uma empresa");

        List<String> lista = new ArrayList<>();

        for (Empresa e : empresas.values()) {
            if (e.getDono() == idDono) {
                lista.add("[" + e.getNome() + ", " + e.getEndereco() + "]");
            }
        }

        return "{[" + String.join(", ", lista) + "]}";
    }

    public String getAtributoEmpresa(int empresaId, String atributo) {
        Empresa e = empresas.get(empresaId);
        if (e == null) throw new RuntimeException("Empresa nao cadastrada");

        switch (atributo) {
            case "nome": return e.getNome();
            case "endereco": return e.getEndereco();
            case "tipoCozinha": return e.getTipoCozinha();
            case "dono": return usuarios.get(e.getDono()).getNome();
        }

        throw new RuntimeException("Atributo invalido");
    }

    public int getIdEmpresa(int idDono, String nome, int indice) {

        if (nome == null || nome.isEmpty())
            throw new RuntimeException("Nome invalido");

        if (indice < 0)
            throw new RuntimeException("Indice invalido");

        List<Empresa> lista = new ArrayList<>();

        for (Empresa e : empresas.values()) {
            if (e.getDono() == idDono && e.getNome().equals(nome)) {
                lista.add(e);
            }
        }

        if (lista.isEmpty())
            throw new RuntimeException("Nao existe empresa com esse nome");

        if (indice >= lista.size())
            throw new RuntimeException("Indice maior que o esperado");

        return lista.get(indice).getId();
    }

   
    public int criarProduto(int empresaId, String nome, float valor, String categoria) {

        if (nome == null || nome.isEmpty())
            throw new RuntimeException("Nome invalido");

        if (valor < 0)
            throw new RuntimeException("Valor invalido");

        if (categoria == null || categoria.isEmpty())
            throw new RuntimeException("Categoria invalido");

        Empresa e = empresas.get(empresaId);
        if (e == null) throw new RuntimeException("Empresa nao encontrada");

        for (Produto p : produtos.values()) {
            if (p.getEmpresaId() == empresaId && p.getNome().equals(nome))
                throw new RuntimeException("Ja existe um produto com esse nome para essa empresa");
        }

        Produto p = new Produto(nextProdutoId++, empresaId, nome, valor, categoria);
        produtos.put(p.getId(), p);
        e.getProdutos().add(p.getId());

        salvar();
        return p.getId();
    }

    public void editarProduto(int produtoId, String nome, float valor, String categoria) {

        Produto p = produtos.get(produtoId);
        if (p == null) throw new RuntimeException("Produto nao cadastrado");

        if (nome == null || nome.isEmpty())
            throw new RuntimeException("Nome invalido");

        if (valor < 0)
            throw new RuntimeException("Valor invalido");

        if (categoria == null || categoria.isEmpty())
            throw new RuntimeException("Categoria invalido");

        p.setNome(nome);
        p.setValor(valor);
        p.setCategoria(categoria);

        salvar();
    }

    public String getProduto(String nome, int empresaId, String atributo) {

        for (Produto p : produtos.values()) {
            if (p.getEmpresaId() == empresaId && p.getNome().equals(nome)) {

                switch (atributo) {
                    case "valor": return String.format("%.2f", p.getValor());
                    case "categoria": return p.getCategoria();
                    case "empresa": return empresas.get(empresaId).getNome();
                }

                throw new RuntimeException("Atributo nao existe");
            }
        }

        throw new RuntimeException("Produto nao encontrado");
    }

    public String listarProdutos(int empresaId) {

        Empresa e = empresas.get(empresaId);
        if (e == null) throw new RuntimeException("Empresa nao encontrada");

        List<String> nomes = new ArrayList<>();

        for (Integer id : e.getProdutos()) {
            nomes.add(produtos.get(id).getNome());
        }

        return "{[" + String.join(", ", nomes) + "]}";
    }
}
