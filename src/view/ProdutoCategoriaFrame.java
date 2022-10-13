package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import controller.CategoriaController;
import controller.ProdutoController;
import modelo.Categoria;
import modelo.Produto;

public class ProdutoCategoriaFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JLabel labelNome, labelDescricao, labelCategoria;
	private JTextField textoNome, textoDescricao;
	private JComboBox<Categoria> comboCategoria;
	private JButton botaoSalvar, botaoAlterar, botaoLimpar, botarApagar;
	private JTable tabela;
	private DefaultTableModel modelo;
	private ProdutoController produtoController;
	private CategoriaController categoriaController;
	
	Font fonteLabel = new Font("SansSerif", Font.BOLD, 18);
	Font fonteTable = new Font("SansSerif", Font.PLAIN  , 16);
	Font fonteButton = new Font("SansSerif", Font.BOLD, 18);
	Font fontText = new Font("SansSerif", Font.PLAIN, 16);
	
	public ProdutoCategoriaFrame() throws SQLException {
		super("Produtos");
		Container container = getContentPane();
		container.setBackground(Color.blue);
		setLayout(null);

		this.categoriaController = new CategoriaController();
		this.produtoController = new ProdutoController();

		labelNome = new JLabel("Nome do Produto");
		labelDescricao = new JLabel("Descrição do Produto");
		labelCategoria = new JLabel("Categoria do Produto");

		labelNome.setBounds(10, 10, 240, 15);
		labelDescricao.setBounds(10, 60, 240, 15);
		labelCategoria.setBounds(10, 110, 240, 15);
		
		labelNome.setFont(fonteLabel);
		labelDescricao.setFont(fonteLabel);
		labelCategoria.setFont(fonteLabel);

		labelNome.setForeground(Color.white);
		labelDescricao.setForeground(Color.white);
		labelCategoria.setForeground(Color.white);

		container.add(labelNome);
		container.add(labelDescricao);
		container.add(labelCategoria);

		textoNome = new JTextField();
		textoDescricao = new JTextField();
		comboCategoria = new JComboBox<Categoria>();

		comboCategoria.addItem(new Categoria(0, "Selecione"));
		List<Categoria> categorias = this.listarCategoria();
		for (Categoria categoria : categorias) {
			comboCategoria.addItem(categoria);
		}

		textoNome.setBounds(10, 27, 265, 20);
		textoDescricao.setBounds(10, 77, 265, 20);
		comboCategoria.setBounds(10, 130, 265, 20);
		
		textoNome.setFont(fontText);
		textoDescricao.setFont(fontText);
		comboCategoria.setFont(fontText);

		container.add(textoNome);
		container.add(textoDescricao);
		container.add(comboCategoria);

		botaoSalvar = new JButton("Salvar");
		botaoLimpar = new JButton("Limpar");

		botaoSalvar.setBounds(10, 160,105, 30);
		botaoLimpar.setBounds(130, 160, 105, 30);
		
		botaoSalvar.setFont(fonteButton);
		botaoLimpar.setFont(fonteButton);

		container.add(botaoSalvar);
		container.add(botaoLimpar);

		tabela = new JTable();
		modelo = (DefaultTableModel) tabela.getModel();

		modelo.addColumn("Identificador do Produto");
		modelo.addColumn("Nome do Produto");
		modelo.addColumn("Descrição do Produto");

		preencherTabela();

		tabela.setBounds(10, 200, 760, 300);
		tabela.setFont(fonteTable);
		container.add(tabela);

		botarApagar = new JButton("Excluir");
		botaoAlterar = new JButton("Alterar");

		botarApagar.setBounds(10, 520, 105, 30);
		botaoAlterar.setBounds(130, 520, 105, 30);
		
		botarApagar.setFont(fonteButton);
		botaoAlterar.setFont(fonteButton);

		container.add(botarApagar);
		container.add(botaoAlterar);

		setSize(800, 600);
		setVisible(true);
		setLocationRelativeTo(null);

		botaoSalvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				salvar();
				limparTabela();
				preencherTabela();
			}
		});

		botaoLimpar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				limpar();
			}
		});

		botarApagar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deletar();
				limparTabela();
				preencherTabela();
			}
		});

		botaoAlterar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				alterar();
				limparTabela();
				preencherTabela();
			}
		});
	}

	private void limparTabela() {
		modelo.getDataVector().clear();
	}

	private void alterar() {
		Object objetoDaLinha = (Object) modelo.getValueAt(tabela.getSelectedRow(), tabela.getSelectedColumn());
		if (objetoDaLinha instanceof Integer) {
			Integer id = (Integer) objetoDaLinha;
			String nome = (String) modelo.getValueAt(tabela.getSelectedRow(), 1);
			String descricao = (String) modelo.getValueAt(tabela.getSelectedRow(), 2);
			this.produtoController.alterar(nome, descricao, id);
		} else {
			JOptionPane.showMessageDialog(this, "Por favor, selecionar o ID");
		}
	}

	private void deletar() {
		Object objetoDaLinha = (Object) modelo.getValueAt(tabela.getSelectedRow(), tabela.getSelectedColumn());
		if (objetoDaLinha instanceof Integer) {
			Integer id = (Integer) objetoDaLinha;
			this.produtoController.deletar(id);
			modelo.removeRow(tabela.getSelectedRow());
			JOptionPane.showMessageDialog(this, "Item excluído com sucesso!");
		} else {
			JOptionPane.showMessageDialog(this, "Por favor, selecionar o ID");
		}
	}

	private void preencherTabela() {
		List<Produto> produtos = listarProduto();
		try {
			for (Produto produto : produtos) {
				modelo.addRow(new Object[] { produto.getId(), produto.getNome(), produto.getDescricao() });
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private List<Categoria> listarCategoria() {
		return this.categoriaController.listar();
	}

	private void salvar() {
		if (!textoNome.getText().equals("") && !textoDescricao.getText().equals("")) {
			Produto produto = new Produto(textoNome.getText(), textoDescricao.getText());
			Categoria categoria = (Categoria) comboCategoria.getSelectedItem();
			produto.setCategoriaId(categoria.getId());
			this.produtoController.salvar(produto);
			JOptionPane.showMessageDialog(this, "Salvo com sucesso!");
			this.limpar();
		} else {
			JOptionPane.showMessageDialog(this, "Nome e Descrição devem ser informados.");
		}
	}

	private List<Produto> listarProduto() {
		return this.produtoController.listar();
	}

	private void limpar() {
		this.textoNome.setText("");
		this.textoDescricao.setText("");
		this.comboCategoria.setSelectedIndex(0);
	}
}
