package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import es.ucm.fdi.gaia.ontobridge.OntoBridge;
import es.ucm.fdi.gaia.ontobridge.OntologyDocument;
import es.ucm.fdi.gaia.ontobridge.test.gui.PnlConceptsAndInstancesTree;

public class Interfaz extends JFrame implements ActionListener, ItemListener {

	/** Atributos **/

	private static final long serialVersionUID = 1L;
	private final String actorEnPersonaje = "actorInPersonaje";
	private final String aparece = "apareceEnImagen";
//	private final String path = "path";
	private final String[] entidadesConsultas = { 
			"actuaEnAlMenosUnaPeliculas",
			"EscenasConObjetosViolentos", 
			"actuaEnSerieOPeliculaEspa�ola" };

	private JPanel panelResultado;
	private JLabel labelFotoMarcar, labelFotoBuscar;
	private ArrayList<String> images;
	private int index;
	private String[] consulta = {
			"Actores que hayan participado en al menos una pel�cula",
			"Escenas con objetos violentos",
			"Actores que aparecen en alguna serie o pel�cula espa�ola" };

	private OntoBridge ob;
	private OntologyDocument mainOnto;
	private JComboBox comboIndividuos, comboRelaciones;
	private JComboBox comboIndivBusc, comboRelBusc, comboIniBusc, comboIndivBuscAvanzada;
	private JCheckBox checkBuscAvanzada;
	private String imagenCargada;
	private JButton aniadirObjeto, aniadirActividad;
	private JButton botonBuscar, botonAnt, botonSig;

	/** Constructores **/

	public Interfaz() {
		super("Ontolog�a");
		setJMenuBar(getMiJMenuBar());
		abrir();

		getContentPane().add(getPanelPrincipal());
		setPreferredSize(new Dimension(900, 630));
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(
				(int) (Toolkit.getDefaultToolkit().getScreenSize().width / 2 - getPreferredSize().width / 2),
				(int) (Toolkit.getDefaultToolkit().getScreenSize().height / 2 - getPreferredSize().height / 2));
		setVisible(true);
		pack();
	}

	/** Metodos **/

	/* Metodos que crean los distintos paneles de la interfaz */

	private JMenuBar getMiJMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu archivo = new JMenu("Archivo");
		JMenuItem abrir = new JMenuItem("Abrir");
		JMenuItem salir = new JMenuItem("Salir");

		menuBar.add(archivo);
		archivo.add(abrir);
		archivo.addSeparator();
		archivo.add(salir);

		abrir.addActionListener(this);
		salir.addActionListener(this);

		return menuBar;
	}

	private JSplitPane getPanelPrincipal() {
		JSplitPane panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		panel.setDividerSize(0);
		panel.setLeftComponent(getPanelOntologia());
		panel.setRightComponent(getPanelResultado());

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Marcar", getPanelMarcar());
		tabbedPane.addTab("Buscar", getPanelBuscar());
		panelResultado.add(tabbedPane);
		return panel;
	}

	private JSplitPane getPanelBuscar() {
		JSplitPane panelPpalBuscar = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		JSplitPane panelFotosBoton = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		panelFotosBoton.setDividerSize(0);
		panelPpalBuscar.setDividerSize(0);

		botonBuscar = new JButton("Buscar");
		botonAnt = new JButton("<<");
		botonSig = new JButton(">>");
		botonAnt.setEnabled(false);
		botonSig.setEnabled(false);

		botonBuscar.addActionListener(this);
		botonAnt.addActionListener(this);
		botonSig.addActionListener(this);

		JPanel panelConsultas = new JPanel(new GridLayout(2, 1));

		// set border and layout
		Border emptyBorder = BorderFactory.createEmptyBorder(0, 5, 0, 5);
		Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		Border titleBorder = BorderFactory.createTitledBorder(lineBorder,
				"Consultas especiales");
		Border compoundBorder = BorderFactory.createCompoundBorder(titleBorder,
				emptyBorder);
		panelConsultas.setBorder(compoundBorder);

		JPanel panelBotonesBuscAvanzada = new JPanel(new GridLayout(1, 1));
		JPanel panelBotonesBuscAvanzOk = new JPanel(new GridLayout(1, 1));
		comboIndivBuscAvanzada = new JComboBox();
		comboIndivBuscAvanzada.addItem(consulta[0]);
		comboIndivBuscAvanzada.addItem(consulta[1]);
		comboIndivBuscAvanzada.addItem(consulta[2]);
		comboIndivBuscAvanzada.setEnabled(false);
		panelBotonesBuscAvanzada.add(comboIndivBuscAvanzada);
		checkBuscAvanzada = new JCheckBox("Consultas especiales activadas", false);
		checkBuscAvanzada.addItemListener(this);
		panelBotonesBuscAvanzOk.add(checkBuscAvanzada);
		panelConsultas.add(panelBotonesBuscAvanzada);
		panelConsultas.add(panelBotonesBuscAvanzOk);

		panelPpalBuscar.setBottomComponent(panelFotosBoton);
		panelPpalBuscar.setTopComponent(panelConsultas);

		labelFotoBuscar = new JLabel();
		JPanel panelFotosBuscar = new JPanel();
		panelFotosBuscar.setPreferredSize(new Dimension(500, 350));
		panelFotosBuscar.add(labelFotoBuscar);
		
		comboIndivBusc = new JComboBox();
		comboRelBusc = new JComboBox();
		comboRelBusc.addActionListener(this);
		comboIniBusc = new JComboBox();
		comboIniBusc.addActionListener(this);
		comboIniBusc.addItem("Pelicula");
		comboIniBusc.addItem("Imagen");
		comboIniBusc.addItem("Persona");
		comboRelBusc.removeAllItems();
		comboIndivBusc.removeAllItems();


		panelFotosBoton.setTopComponent(panelFotosBuscar);
		JPanel panelBoton = new JPanel(new GridLayout(2, 1));
		JPanel panelBotonesImg = new JPanel(new GridLayout(1, 3));
		JPanel panelBotonesBusc = new JPanel(new GridLayout(1, 3));
		panelBotonesImg.add(botonAnt);
		panelBotonesImg.add(botonBuscar);
		panelBotonesImg.add(botonSig);
		panelBotonesBusc.add(comboIniBusc);
		panelBotonesBusc.add(comboRelBusc);
		panelBotonesBusc.add(comboIndivBusc);
		panelBoton.add(panelBotonesImg);
		panelBoton.add(panelBotonesBusc);
		panelFotosBoton.setBottomComponent(panelBoton);

		panelPpalBuscar.setBottomComponent(panelFotosBoton);

		return panelPpalBuscar;
	}

	private JSplitPane getPanelMarcar() {
		JSplitPane panelPpalMarcado = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		JSplitPane panelBotonFotos = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		panelPpalMarcado.setDividerSize(0);
		panelBotonFotos.setDividerSize(0);

		JButton botonCargarFoto = new JButton("Cargar foto");
		JButton botonMarcarFoto = new JButton("Marcar foto");

		botonCargarFoto.addActionListener(this);
		botonMarcarFoto.addActionListener(this);

		JPanel panelBoton = new JPanel();
		JPanel panelFotos = new JPanel();

		labelFotoMarcar = new JLabel();
		panelBoton.add(botonCargarFoto);
		panelBotonFotos.setTopComponent(panelBoton);
		panelBotonFotos.setBottomComponent(panelFotos);
		panelPpalMarcado.setTopComponent(panelBotonFotos);
		panelFotos.setPreferredSize(new Dimension(500, 350));
		panelFotos.add(labelFotoMarcar);

		JLabel labelOpcionesMarcado = new JLabel("Marcadores: ");
		comboIndividuos = new JComboBox();
		comboRelaciones = new JComboBox();
		comboRelaciones.addActionListener(this);
		JPanel panelOpcionesMarcado = new JPanel(new GridLayout(2, 3));
		panelOpcionesMarcado.setPreferredSize(new Dimension(500, 50));

		panelOpcionesMarcado.add(labelOpcionesMarcado);
		panelOpcionesMarcado.add(comboRelaciones);
		panelOpcionesMarcado.add(comboIndividuos);
		panelOpcionesMarcado.add(botonMarcarFoto);

		aniadirObjeto = new JButton("A\u00F1adir objeto");
		aniadirActividad = new JButton("A\u00F1adir actividad");
		aniadirActividad.addActionListener(this);
		aniadirObjeto.addActionListener(this);

		panelOpcionesMarcado.add(aniadirActividad);
		panelOpcionesMarcado.add(aniadirObjeto);

		JPanel panel = new JPanel();
		panel.add(panelOpcionesMarcado);

		panelPpalMarcado.setBottomComponent(panel);

		return panelPpalMarcado;
	}

	private JPanel getPanelOntologia() {
		JPanel panel = new JPanel();

		PnlConceptsAndInstancesTree tree = new PnlConceptsAndInstancesTree(ob,true);
		tree.setPreferredSize(new Dimension(300, 500));
		panel.add(tree);

		return panel;
	}

	private JPanel getPanelResultado() {
		panelResultado = new JPanel();
		panelResultado.setPreferredSize(new Dimension(300, 600));

		return panelResultado;
	}

	/* Metodos que implementan ActionListener */

	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals("Salir")) {
			System.exit(0);
		} 
		
		else if (e.getActionCommand().equals("Abrir")) {
			abrir();
		} 
		
		else if (e.getActionCommand().equals("Buscar")) {
			if (checkBuscAvanzada.isSelected() && comboIndivBuscAvanzada.getSelectedIndex() == 0) {
				mostrarActoresEnMasDeUnaPelicula();
			} else if (checkBuscAvanzada.isSelected() && comboIndivBuscAvanzada.getSelectedIndex() == 1) {
				mostrarEscenasConObjetosViolentos();
			} else if (checkBuscAvanzada.isSelected() && comboIndivBuscAvanzada.getSelectedIndex() == 2) {
				mostrarPersonajesEnPeliEspaniola();
			} else if (comboIniBusc.getSelectedItem() != null && comboIndivBusc.getSelectedItem() != null && comboRelBusc.getSelectedItem() != null) {
				if (comboIniBusc.getSelectedIndex() == 0){
					if(comboRelBusc.getSelectedIndex() == 0){
						// Peliculas hechas en..
						mostrarPeliculasHechasEn((String) comboIndivBusc.getSelectedItem());
					} else {
						// Peliculas con genero...
						mostrarPeliculasGenero((String) comboIndivBusc.getSelectedItem());
					}
				} else if (comboIniBusc.getSelectedIndex() == 1){
					if(comboRelBusc.getSelectedIndex() == 0){
						// Imagenes aparece objeto...
						mostrarImagenesConObjeto((String) comboIndivBusc.getSelectedItem());
					}
					else if(comboRelBusc.getSelectedIndex() == 1){
						// Imagenes aparece actividad...
						mostrarImagenesConActividad((String) comboIndivBusc.getSelectedItem());
					}
				} else if (comboIniBusc.getSelectedIndex() == 2){
						// Actores que aparecen...
						mostrarImagenesConActor((String) comboIndivBusc.getSelectedItem());
				}
			}
			
			if (images != null && images.size() > 1)
				botonSig.setEnabled(true);
			else
				botonSig.setEnabled(false);
			botonAnt.setEnabled(false);
		} 
		
		else if (e.getActionCommand().equals("<<")) {
			if (!anterior())
				botonAnt.setEnabled(false);
			else
				botonAnt.setEnabled(true);
			if(images != null && images.size() > 1)
				botonSig.setEnabled(true);
		} 
		
		else if (e.getActionCommand().equals(">>")) {
			if (!siguiente())
				botonSig.setEnabled(false);
			else
				botonSig.setEnabled(true);
			if(images != null && images.size() > 1)
				botonAnt.setEnabled(true);
		} 
		
		else if (e.getActionCommand().equals("Cargar foto")) {
			cargarFoto();
		} 
		
		else if (e.getActionCommand().equals("Marcar foto")) {
			// Creamos la nueva relaci�n
			String rel = comboRelaciones.getSelectedItem().toString();
			String ind = comboIndividuos.getSelectedItem().toString();
			if (!rel.equals("-") && !ind.equals("-")) {
				if(!existeIndividuo(imagenCargada))
					crearIndivImagen(imagenCargada);
				ob.createOntProperty(imagenCargada, rel, ind);
			}
		} 
		
		else if (e.getSource().equals(aniadirObjeto)) {
			String s = JOptionPane.showInputDialog("Introduzca el nombre del individuo: ");
			if (s != null && !s.isEmpty()) ob.createInstance("Objeto", s);
		} 
		
		else if (e.getSource().equals(aniadirActividad)) {
			String s = JOptionPane.showInputDialog("Introduzca el nombre del individuo: ");
			if (s != null && !s.isEmpty()) ob.createInstance("Actividad", s);

		} 
		
		else if (e.getSource() == comboRelaciones && comboRelaciones.getItemCount() > 0) {
			obtenerIndividuos((String) comboRelaciones.getSelectedItem(), comboIndividuos);
		}
		
		else if (e.getSource() == comboIniBusc){
			comboRelBusc.removeAllItems();
			if(comboIniBusc.getSelectedIndex() == 0){
				comboRelBusc.addItem("hecha en");
				comboRelBusc.addItem("de g�nero");
			}
			else if(comboIniBusc.getSelectedIndex() == 1){
				comboRelBusc.addItem("aparece objeto");
				comboRelBusc.addItem("aparece actividad");
			}
			else if(comboIniBusc.getSelectedIndex() == 2){
				comboRelBusc.addItem("aparece actor");
			}
		}
		
		else if (e.getSource() == comboRelBusc){
			if(comboRelBusc.getSelectedItem() == "hecha en"){
				obtenerIndividuos( "peliculaHechaIn", comboIndivBusc);
			}
			else if(comboRelBusc.getSelectedItem() == "de g�nero"){
				obtenerIndividuos( "hasGenero", comboIndivBusc);
			}
			else if(comboRelBusc.getSelectedItem() == "aparece objeto"){
				obtenerIndividuos( "imagenTieneObj", comboIndivBusc);
			}
			else if(comboRelBusc.getSelectedItem() == "aparece actividad"){
				obtenerIndividuos( "imagenTieneActividad", comboIndivBusc);
			}
			else if(comboRelBusc.getSelectedItem() == "aparece actor"){
				obtenerIndividuos( "peliculaTieneActor", comboIndivBusc);
			}
		}
	}

	/*-------------- Metodos para las consultas -------------------*/
	
	private boolean existeIndividuo(String individuo){
		return ob.existsInstance(individuo);
	}
	
	private void crearIndivImagen(String individuo){
		ob.createInstance("Imagen", individuo);
		ob.createDataTypeProperty(imagenCargada, "path", imagenCargada+".jpg");
	}

	private void mostrarActoresEnMasDeUnaPelicula() {
		Iterator<String> it = ob.listInstances(entidadesConsultas[0]);
		ArrayList<String> actores = new ArrayList<String>();
		ArrayList<String> personajes = new ArrayList<String>();
		ArrayList<String> escenas = new ArrayList<String>();
		ArrayList<String> paths;
		if(images == null)
			images = new ArrayList<String>();
		else
			images.clear();

		String instance = "";
		while (it.hasNext()) {
			instance = it.next();
			actores.add(instance);
			// Listar personajes de cada actor
			personajes = consultarRel(instance, actorEnPersonaje);

			// Listar escenas donde aparecen esos personajes
			for (String personaje : personajes) {
				escenas = consultarRel(personaje, aparece);
				// Obtener path de las escenas
				for (String escena : escenas) {
					paths = consultarAtrib(escena, "path");
					if (!images.contains(paths.get(0)))
						images.add(paths.get(0));
				}
			}
		}
		Logger.getLogger("CP").info(images.toString());
		cambiarImagen(index = 0);
	}

	private void mostrarPersonajesEnPeliEspaniola() {
		Iterator<String> it = ob.listInstances(entidadesConsultas[2]);
		ArrayList<String> actores = new ArrayList<String>();
		ArrayList<String> personajes = new ArrayList<String>();
		ArrayList<String> escenas = new ArrayList<String>();
		ArrayList<String> paths;
		if(images == null)
			images = new ArrayList<String>();
		else
			images.clear();

		String instance = "";
		while (it.hasNext()) {
			instance = it.next();
			actores.add(instance);
			// Listar personajes de cada actor
			personajes = consultarRel(instance, actorEnPersonaje);

			// Listar escenas donde aparecen esos personajes
			for (String personaje : personajes) {
				escenas = consultarRel(personaje, aparece);
				// Obtener path de las escenas
				for (String escena : escenas) {
					paths = consultarAtrib(escena, "path");
					if (!images.contains(paths.get(0)))
						images.add(paths.get(0));
				}
			}
		}
		Logger.getLogger("CP").info(images.toString());
		cambiarImagen(index = 0);
	}

	private void mostrarEscenasConObjetosViolentos() {
		Iterator<String> it = ob.listInstances(entidadesConsultas[1]);
		ArrayList<String> escenas = new ArrayList<String>();
		ArrayList<String> paths;
		if(images == null)
			images = new ArrayList<String>();
		else
			images.clear();

		while (it.hasNext()) {
			escenas.add(it.next());
		}

		// Obtener path de las escenas
		for (String escena : escenas) {
			paths = consultarAtrib(escena, "path");
			if (!images.contains(paths.get(0)))
				images.add(paths.get(0));
		}
			
		Logger.getLogger("CP").info(images.toString());
		cambiarImagen(index = 0);
	}
	
	/**
	 * Recuperamos las escenas de las peliculas que han sido hechas en el pa�s dado
	 * @param pais
	 */
	private void mostrarPeliculasHechasEn(String pais){
		Iterator<String> it, it2;
		ArrayList<String> escenas = new ArrayList<String>();
		ArrayList<String> peliculas, paths;
		if(images == null)
			images = new ArrayList<String>();
		else
			images.clear();
		
		if (pais.equalsIgnoreCase("EEUU_Mundo") ){
			it = ob.listInstances("EscenasDePeliculasAmericanas");
			while (it.hasNext()) {
				escenas.add(it.next());
			}
		}
		else if (pais.equalsIgnoreCase("Espa�a")){
			it = ob.listInstances("EscenasDePeliculasEspa�olas");
			while (it.hasNext()) {
				escenas.add(it.next());
			}
		}
		else{ //Peliculas de paises que nos hayan dicho.
			it = ob.listInstances("EscenasDePeliculas");
			it2 = ob.listInstances("Pelicula");
			peliculas = new ArrayList<String>();
			String aux;
			ArrayList<String> auxArray;
			//Primero hallamos la peliculas hechas en el pais
			while(it2.hasNext()){
				aux = it2.next();
				auxArray = consultarRel(aux, "peliculaHechaIn");
				if(auxArray.size() > 0 && auxArray.get(0).equalsIgnoreCase(pais))
					peliculas.add(aux.split("#")[1]);
			}
			//Sacamos las escenas de esas pel�culas
			while(it.hasNext()){
				aux = it2.next();
				auxArray = consultarRel(aux, "ApareceEnPeliculaEscena");
				if(auxArray.size() > 0 && peliculas.contains(auxArray.get(0)))
					escenas.add(aux.split("#")[1]);
			}
		}
		
		//Ya tenemos las escenas, ahora a a�adir los paths
		for (String escena : escenas) {
			paths = consultarAtrib(escena, "path");
			if (!images.contains(paths.get(0)))
				images.add(paths.get(0));
		}
			
		Logger.getLogger("CP").info(images.toString());
		cambiarImagen(index = 0);
		
	}
	
	/**
	 * Peliculas respecto al g�nero pedido
	 * @param genero
	 */
	private void mostrarPeliculasGenero(String genero){
		Iterator<String> it, it2;
		ArrayList<String> escenas = new ArrayList<String>();
		ArrayList<String> peliculas, paths;
		if(images == null)
			images = new ArrayList<String>();
		else
			images.clear();
		
		if( genero.equalsIgnoreCase("Accion") ){
			it = ob.listInstances("EscenasDePeliculasAccion");
			while (it.hasNext()) {
				escenas.add(it.next());
			}
		}
		else if (genero.equalsIgnoreCase("Animacion")){
			it = ob.listInstances("EscenasDePeliculasAnimacion");
			while (it.hasNext()) {
				escenas.add(it.next());
			}
		}
		else if (genero.equalsIgnoreCase("Comedia")){
			it = ob.listInstances("EscenasDePeliculasComedia");
			while (it.hasNext()) {
				escenas.add(it.next());
			}
		}
		else if (genero.equalsIgnoreCase("Drama")){
			it = ob.listInstances("EscenasDePeliculasDrama");
			while (it.hasNext()) {
				escenas.add(it.next());
			}
		}
		else if (genero.equalsIgnoreCase("Romantica")){
			it = ob.listInstances("EscenasDePeliculasRomanticas");
			while (it.hasNext()) {
				escenas.add(it.next());
			}
		}
		else{ //Peliculas de paises que nos hayan dicho.
			it = ob.listInstances("Escena");
			it2 = ob.listInstances("Pelicula");
			peliculas = new ArrayList<String>();
			String aux;
			ArrayList<String> auxArray;
			boolean added = false;
			int i = 0;
			//Primero hallamos la peliculas con el g�nero buscado
			while(it2.hasNext()){
				aux = it2.next();
				auxArray = consultarRel(aux, "hasGenero");
				while (!added && i < auxArray.size())
					if(auxArray.get(i).equalsIgnoreCase(genero)){
						peliculas.add(aux.split("#")[1]);
						added = true;
					}
					else
						i++;
				
				i = 0;
				added = false;
				
			}
			//Sacamos las escenas de esas pel�culas			
			while(it.hasNext()){
				aux = it.next();
				auxArray = consultarRel(aux, "apareceEnPeliculaEscena");
				if(auxArray.size() > 0 && peliculas.contains(auxArray.get(0)))
					escenas.add(aux.split("#")[1]);
			}
		}
		
		//Ya tenemos las escenas, ahora a a�adir los paths
		for (String escena : escenas) {
			paths = consultarAtrib(escena, "path");
			if (!images.contains(paths.get(0)))
				images.add(paths.get(0));
		}
			
		Logger.getLogger("CP").info(images.toString());
		cambiarImagen(index = 0);
		
	}
	
	/**
	 * Imagenes que contienen el objeto dado
	 * @param objeto
	 */
	private void mostrarImagenesConObjeto(String objeto){
		Iterator<String> it;
		ArrayList<String> imgs = new ArrayList<String>();
		ArrayList<String> paths;
		if(images == null)
			images = new ArrayList<String>();
		else
			images.clear();
		
		it = ob.listInstances("ImagenConObjetos");
		String aux;
		ArrayList<String> auxArray;
		boolean encontrado = false;
		int i = 0;
		//Primero hallamos las imagenes con el objeto buscado
		while(it.hasNext()){
			aux = it.next();
			auxArray = consultarRel(aux, "imagenTieneObj");
			//No me fio del contains del ArrayList
			while (!encontrado && i < auxArray.size()){
				encontrado = auxArray.get(i).equalsIgnoreCase(objeto);
				if ( encontrado )
					imgs.add(aux.split("#")[1]);
				else
					i++;
			}
			encontrado = false;
			i = 0;
		}
		
		//Ya tenemos las imagenes, ahora a a�adir los paths
		for (String img : imgs) {
			paths = consultarAtrib(img, "path");
			if (!images.contains(paths.get(0)))
				images.add(paths.get(0));
		}
			
		Logger.getLogger("CP").info(images.toString());
		cambiarImagen(index = 0);
		
	}
	
	/**
	 * Escogemos las imagenes en las que se lleve a cabo la actividad dada
	 * @param actividad
	 */
	private void mostrarImagenesConActividad(String actividad){
		Iterator<String> it;
		ArrayList<String> imgs = new ArrayList<String>();
		ArrayList<String> paths;
		if(images == null)
			images = new ArrayList<String>();
		else
			images.clear();
		
		it = ob.listInstances("ImagenConActividad");
		String aux;
		ArrayList<String> auxArray;
		boolean encontrado = false;
		int i = 0;
		//Primero hallamos las imagenes con la actividad buscado
		while(it.hasNext()){
			aux = it.next();
			auxArray = consultarRel(aux, "imagenTieneActividad");
			while (!encontrado && i < auxArray.size()){
				encontrado = auxArray.get(i).equalsIgnoreCase(actividad);
				if ( encontrado )
					imgs.add(aux.split("#")[1]);
				else
					i++;
			}
			encontrado = false;
			i = 0;
		}
		
		//Ya tenemos las imagenes, ahora a a�adir los paths
		for (String img : imgs) {
			paths = consultarAtrib(img, "path");
			if (!images.contains(paths.get(0)))
				images.add(paths.get(0));
		}
			
		Logger.getLogger("CP").info(images.toString());
		cambiarImagen(index = 0);
		
	}
	
	/**
	 * Se desea mostrar las imagenes donde aparezca el actor dado
	 * @param actor
	 */
	private void mostrarImagenesConActor(String actor){
		Iterator<String> it, it2;
		ArrayList<String> imgs = new ArrayList<String>();
		ArrayList<String> personajes = new ArrayList<String>();
		ArrayList<String> paths;
		String aux;
		ArrayList<String> auxArray;
		if(images == null)
			images = new ArrayList<String>();
		else
			images.clear();
		
		if( actor.equalsIgnoreCase("Angelina_Jolie") ){
			it = ob.listInstances("PersonajesDeAngelinaJolie");
			while (it.hasNext()) {
				personajes.add(it.next().split("#")[1]);
			}
		}
		else if (actor.equalsIgnoreCase("Javier_Bardem")){
			it = ob.listInstances("PersonajesDeBardem");
			while (it.hasNext()) {
				personajes.add(it.next().split("#")[1]);
			}
		}
		else if (actor.equalsIgnoreCase("Brad_Pitt")){
			it = ob.listInstances("PersonajesDeBradPitt");
			while (it.hasNext()) {
				personajes.add(it.next().split("#")[1]);
			}
		}
		else if (actor.equalsIgnoreCase("Penelope_Cruz")){
			it = ob.listInstances("PersonajesDePenelopeCruz");
			while (it.hasNext()) {
				personajes.add(it.next().split("#")[1]);
			}
		}
		else if (actor.equalsIgnoreCase("Tom_Cruise")){
			it = ob.listInstances("PersonajesDeTomCruise");
			while (it.hasNext()) {
				personajes.add(it.next().split("#")[1]);
			}
		}
		else{ //Peliculas de paises que nos hayan dicho.
			it = ob.listInstances("Personaje");
			//Primero hallamos los personajes que usan los actores
			while(it.hasNext()){
				aux = it.next();
				auxArray = consultarRel(aux, "personajeHasActor");
				if(auxArray.size() > 0 && auxArray.get(0).equalsIgnoreCase(actor))
					personajes.add(aux.split("#")[1]);
			}
		}
		
		//Tras tener los personajes, sacamos las escenas donde aparecen
		it2 = ob.listInstances("ImagenConActores");
		boolean encontrado = false;
		int i = 0;
		while(it2.hasNext()){
			aux = it2.next();
			auxArray = consultarRel(aux, "imagenTiene");
			while (!encontrado && i < auxArray.size()){
				encontrado = personajes.contains(auxArray.get(i)) || actor.equalsIgnoreCase(auxArray.get(i));
				if ( encontrado )
					imgs.add(aux);
				else
					i++;
			}
			encontrado = false;
			i = 0;
		}
		
		//Ya tenemos las escenas, ahora a a�adir los paths
		for (String img : imgs) {
			paths = consultarAtrib(img, "path");
			if (!images.contains(paths.get(0)))
				images.add(paths.get(0));
		}
			
		Logger.getLogger("CP").info(images.toString());
		cambiarImagen(index = 0);
		
	}

	/**
	 * Realiza una consulta
	 * 
	 * @param individuo
	 *            Individuo sobre el que se realiza la consulta
	 * @param relacion
	 *            Relaci�n a buscar
	 * @return Array con el resultado de la consulta
	 */
	private ArrayList<String> consultarRel(String individuo, String relacion) {
//		ArrayList<String> prop = new ArrayList<String>();
//		ArrayList<String> val = new ArrayList<String>();
		Iterator<String> val;
		ArrayList<String> res = new ArrayList<String>();
		
		val = ob.listPropertyValue(individuo, relacion);
		
		while(val.hasNext())
			res.add(val.next().split("#")[1]);

//		ob.listInstancePropertiesValues(individuo, prop, val);
//
//		for (int i = 0; i < prop.size(); i++)
//			if (relacion.equals(prop.get(i).split("#")[1]))
//				if (!res.contains(val.get(i)))
//					res.add(val.get(i));

		return res;
	}
	
	private ArrayList<String> consultarAtrib(String individuo, String atributo) {
//		ArrayList<String> prop = new ArrayList<String>();
//		ArrayList<String> val = new ArrayList<String>();
		Iterator<String> val;
		ArrayList<String> res = new ArrayList<String>();
		
		val = ob.listPropertyValue(individuo, atributo);
		
		while(val.hasNext())
			res.add(val.next().split("^^")[0]);

//		ob.listInstancePropertiesValues(individuo, prop, val);
//
//		for (int i = 0; i < prop.size(); i++)
//			if (relacion.equals(prop.get(i).split("#")[1]))
//				if (!res.contains(val.get(i)))
//					res.add(val.get(i));

		return res;
	}

	/* Otros metodos */

	private void abrir() {
		// Creamos el objeto de OntoBridge
		ob = new OntoBridge();

		// Inicializamos el objeto de OntoBridge con un razonador Pellet
		ob.initWithPelletReasoner();

		JOptionPane.showMessageDialog(Interfaz.this,
				"Selecciona el archivo para la ontolog�a principal",
				"Ontolog�a principal", JOptionPane.INFORMATION_MESSAGE);

		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Archivos .owl", "owl");
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(filter);
		boolean seleccionCorrecta = fileChooser.showOpenDialog(Interfaz.this) == JFileChooser.APPROVE_OPTION;
		boolean extensionCorrecta = false;
		File file = null;

		if (seleccionCorrecta) {
			file = fileChooser.getSelectedFile();
			extensionCorrecta = file.getName().endsWith(".owl");
		}

		if (seleccionCorrecta && extensionCorrecta) {
			// Se mete la ontologia principal.
			String rutaOnto = "file:" + file.getPath();
			mainOnto = new OntologyDocument(rutaOnto, rutaOnto);

			// Cargar la ontologia
			ob.loadOntology(mainOnto, new ArrayList<OntologyDocument>(), false);

		} else {
			JOptionPane.showMessageDialog(Interfaz.this,
					"La ontolog�a introducida no es v�lida", "ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cargarFoto() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Archivos jpg y png", "jpg", "png");

		try{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(filter);
		File file = null;

		if (fileChooser.showOpenDialog(Interfaz.this) == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
			String path = file.getPath();
			if (path.endsWith(".jpg") || path.endsWith(".png")) {
				ponerImagen(path);
				// Actualizamos el combo box con las propedades de la imagen
				comboIndividuos.removeAllItems();
				comboRelaciones.removeAllItems();
				imagenCargada = path.substring(path.lastIndexOf("\\") + 1, path.length() - 4);
				obtenerPropiedades();
				//obtenerIndividuos((String) comboRelaciones.getSelectedItem(), comboIndividuos);
			}
		}
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "Error al abrir la imagen");
		}
	}

	private void obtenerPropiedades() {
		Iterator<String> iter = ob.listSpecificProperties("Imagen");
		ArrayList<String> rel = new ArrayList<String>();
		String aux = "";
		while (iter.hasNext()) {
			aux = iter.next().split("#")[1];
			if (ob.isOntoProperty(aux) && !rel.contains(aux)) {
				rel.add(aux);
				comboRelaciones.addItem(aux);
			}
		}
	}

	private void obtenerIndividuos(String prop, JComboBox comboBox) {
		comboBox.removeAllItems();
		try{
			Iterator<String> it = ob.listPropertyRange(prop);
			Iterator<String> iter;
			if (!it.hasNext()){
				// Entonces es la relaci�n es compatible con todo
				it = ob.listAllClasses();
			}
			ArrayList<String> ind = new ArrayList<String>();
			String clase = ""; String individuo = "";
			while (it.hasNext()){
				// Para cada clase obtenida listamos todos los individuos
				clase = it.next().split("#")[1];
				iter = ob.listInstances(clase);
				while (iter.hasNext()){
					individuo = iter.next().split("#")[1];
					if (!ind.contains(individuo)){
						ind.add(individuo);
						comboBox.addItem(individuo);
					}
				}

			}
		} catch(NullPointerException e){}
	}

	private boolean anterior() {
		if (index > 0)
			cambiarImagen(--index);
		return index > 0;
	}

	private boolean siguiente() {
		if (images != null && index < images.size() - 1){
			cambiarImagen(++index);
			return index < images.size() - 1;
		}
		else
			return false;
	}

	private void cambiarImagen(int index) {
		if(images.size() > 0){
			String ruta = "img/" + images.get(index);
			ImageIcon imageIcon = new ImageIcon(ruta.substring(0,
					ruta.indexOf("^^")));
			labelFotoBuscar.setIcon(imageIcon);
			if (imageIcon.getIconHeight() > 325 || imageIcon.getIconWidth() > 475)
				labelFotoBuscar.setIcon(new ImageIcon(imageIcon.getImage()
						.getScaledInstance(-1, 325, Image.SCALE_AREA_AVERAGING)));
		}
		repaint();
	}

	private void ponerImagen(String ruta) {
		ImageIcon imageIcon = new ImageIcon(ruta);
		labelFotoMarcar.setIcon(imageIcon);
		if (imageIcon.getIconHeight() > 325 || imageIcon.getIconWidth() > 475)
			labelFotoMarcar.setIcon(new ImageIcon(imageIcon.getImage()
					.getScaledInstance(-1, 325, Image.SCALE_AREA_AVERAGING)));
		repaint();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
	    if (e.getItemSelectable() == checkBuscAvanzada) {
	    	if (checkBuscAvanzada.isSelected()){
	    		comboIndivBuscAvanzada.setEnabled(true);
	    		comboRelBusc.setEnabled(false);
	    		comboIndivBusc.setEnabled(false);
	    		comboIniBusc.setEnabled(false);
	    	}
	    	else{
	    		comboIndivBuscAvanzada.setEnabled(false);
	    		comboRelBusc.setEnabled(true);
	    		comboIndivBusc.setEnabled(true);
	    		comboIniBusc.setEnabled(true);
	    	}
	    } 

	}

}
