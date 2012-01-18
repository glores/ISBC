package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

import es.ucm.fdi.isbc.eventos.MuestraSolEvent;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;

public class VentanaPpal extends JFrame implements Observer {
	private static final long serialVersionUID = 1L;

	/** Atributos **/

	/* Estaticos */

	private static Galeria galeria;
	private static PanelVisitados panelVisitados;
	private static VentanaResult vResult;

	/* No estaticos */

	private JMenuBar barraMenus;
	private JMenu mArchivo;
	private JMenuItem salir;
	private JSplitPane horizontal, vertical;

	private PanelFiltro panelFiltro;
	private PanelDiversidad panelDiversidad;

	private boolean flag = false;

	/** Constructores **/

	public VentanaPpal() {
		super("Recomendador Viviendas");
		setExtendedState(MAXIMIZED_BOTH);

		barraMenus = inicializaMenus();
		this.setJMenuBar(barraMenus);

		galeria = new Galeria();
		panelFiltro = new PanelFiltro(this);
		panelDiversidad = new PanelDiversidad(galeria);
		panelVisitados = new PanelVisitados(galeria, this);

		vertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelDiversidad,
				panelVisitados);
		vertical.setDividerSize(7);
		vertical.setDividerLocation(0.8);
		vertical.setOneTouchExpandable(false);
		vertical.setEnabled(false);

		horizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelFiltro,
				vertical);
		horizontal.setDividerSize(3);
		horizontal.setOneTouchExpandable(false);
		horizontal.setEnabled(false);

		this.setContentPane(horizontal);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setMinimumSize(new Dimension(dim.width - 100, dim.height - 100));
		setSize(dim.width - 100, dim.height - 100);
		setLocation((dim.width - getWidth()) / 2,
				(dim.height - getHeight()) / 2);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	/** Metodos **/

	/* Métodos que implementan la interfaz Observer */

	public void update(Observable o, Object arg) {
		if (arg instanceof MuestraSolEvent) {
			if (((MuestraSolEvent) arg).getType().equals("Similares"))
				lanzarVentanaResult(((MuestraSolEvent) arg).getDescrs());
			else if (((MuestraSolEvent) arg).getType().equals("Diversos"))
				panelDiversidad.setViviendasDiversas(((MuestraSolEvent) arg)
						.getDescrs());
		} else {
			if (flag) {
				panelFiltro.enableButton(false);
				flag = false;
			} else {
				panelFiltro.enableButton(true);
				flag = true;
			}
		}
	}

	/* Otros métodos */

	/**
	 * @return El menú de la ventana
	 */
	private JMenuBar inicializaMenus() {
		JMenuBar barra = new JMenuBar();
		mArchivo = new JMenu("Archivo");
		salir = new JMenuItem("Salir");
		mArchivo.add(salir);
		barra.add(mArchivo);

		salir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		return barra;
	}

	/* AUXILIARES */

	/**
	 * Lanza a ejecución la ventana resultado en un nuevo hilo para que no se
	 * bloqueen las ventanas ni el se tenga que quedar el proceso princial
	 * esperando a que acaben todas las acciones generadas por esta ventana.
	 * 
	 * @param ArrayList
	 *            <DescripcionVivienda> VIVIENDAS
	 */
	public static void lanzarVentanaResult(
			final ArrayList<DescripcionVivienda> VIVIENDAS) {
		Runnable runnable = new Runnable() {
			public void run() {
				vResult = new VentanaResult(galeria);
				vResult.setResultado(VIVIENDAS);
			}
		};
		Thread hilo = new Thread(runnable);
		hilo.start();
	}

	/**
	 * Lanza a ejecucion la ventana descripcion en un nuevo hilo para que no se
	 * bloqueen las ventanas ni el se tenga que quedar el proceso princial
	 * esperando a que acaben todas las acciones generadas por esta ventana.
	 * 
	 * @param DescripcionVivienda
	 *            VIVIENDAS
	 */
	public static void lanzarVentanaDescripcion(
			final DescripcionVivienda VIVIENDA) {
		Runnable runnable = new Runnable() {
			public void run() {
				new VentanaDescripcion(VIVIENDA, VIVIENDA.getId(), galeria);
			}
		};
		Thread hilo = new Thread(runnable);
		hilo.start();
	}

	public static boolean enteroEsCorrecto(String entero) {
		/**
		 * Para que el entero sea correcto tiene que tener formato de Integer y
		 * ser mayor o igual que cero ya que la superficie, el numero de
		 * habitaciones y de baños no puede ser negativo. Si la cadena es vacía
		 * devolvemos true;
		 */

		if (entero.isEmpty())
			return true;
		try {
			Integer ent = Integer.valueOf(entero);
			return ent >= 0;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static String cortarString(String string, final int TOPE,
			final int PRIMERO) {
		String s = string;
		String cortarString = "";

		try {
			int cantidad = TOPE - PRIMERO;
			cortarString += s.substring(0, cantidad);
			s = s.substring(cantidad);
			while (!s.isEmpty() && s.charAt(0) != ' ') {
				cortarString += s.charAt(0);
				s = s.substring(1);
			}
			if (!s.isEmpty()) {
				cortarString += "<br>";
				s = s.substring(1);
			}
			while (!s.isEmpty()) {
				cortarString += s.substring(0, TOPE);
				s = s.substring(TOPE);
				while (!s.isEmpty() && s.charAt(0) != ' ') {
					cortarString += s.charAt(0);
					s = s.substring(1);
				}
				if (!s.isEmpty()) {
					cortarString += "<br>";
					s = s.substring(1);
				}
			}
		} catch (StringIndexOutOfBoundsException ex) {
			cortarString += s;
		}

		return cortarString;
	}

	public static String transformar(String string) {
		String s = string;
		String transformar = "";
		while (!s.isEmpty()) {
			String car = s.substring(0, 1);
			s = s.substring(1);
			if (car.equals("Ã") || car.equals("Â") || car.equals("m")
					|| car.equals(",") || car.equals(".")) {
				if (s.length() > 0) {
					car += s.substring(0, 1);
					s = s.substring(1);
					if (car.charAt(0) == 'm' && car.charAt(1) != '2') {
						transformar += "m";
						car = car.substring(1);
						if (car.equals("Ã") || car.equals("Â")) {
							car += s.substring(0, 1);
							s = s.substring(1);
						}
					} else if (car.charAt(0) == ',' && car.charAt(1) != ' ') {
						transformar += ", ";
						car = car.substring(1);
						if (car.equals("Ã") || car.equals("Â")) {
							car = s.substring(0, 1);
							s = s.substring(1);
						}
					} else if (car.charAt(0) == '.' && car.charAt(1) != ' '
							&& car.charAt(1) != '.'
							&& !VentanaPpal.enteroEsCorrecto(car.substring(1))) {
						transformar += ". ";
						car = car.substring(1);
						if (car.equals("Ã") || car.equals("Â")) {
							car = s.substring(0, 1);
							s = s.substring(1);
						}
					}
				}
			} else if (car.equals("â")) {
				if (s.length() > 1) {
					car += s.substring(0, 1);
					s = s.substring(1);
					if (car.charAt(1) == '‚') {
						car += s.substring(0, 1);
						s = s.substring(1);
						if (car.charAt(2) != '¬') {
							transformar += car.substring(0, 2);
							car = car.substring(2);
							if (car.equals("Ã") || car.equals("Â")) {
								car = s.substring(0, 1);
								s = s.substring(1);
							}
						}
					} else {
						transformar += "â";
						car = car.substring(1);
						if (car.equals("Ã") || car.equals("Â")) {
							car = s.substring(0, 1);
							s = s.substring(1);
						}
					}
				}
			}
			transformar += codificar(car);
		}
		return transformar;
	}

	private static String codificar(String cod) {
		// Debido a la propia codificación del Eclipse y a que no reconoce
		// ciertos caracteres: Á, Í, Ï se escriben aquí
		// de la misma forma lo cuál generará un problema en la conversión, por
		// suerte, las dos últimas no son letras
		// muy utilizadas.

		if (cod.equals("Ã¡"))
			return "á";
		else if (cod.equals("Ã©"))
			return "é";
		else if (cod.equals("Ã­"))
			return "í";
		else if (cod.equals("Ã³"))
			return "ó";
		else if (cod.equals("Ãº"))
			return "ú";
		else if (cod.equals("Ã�"))
			return "Á";
		else if (cod.equals("Ã‰"))
			return "É";
		else if (cod.equals("Ã�"))
			return "Í";
		else if (cod.equals("Ã“"))
			return "Ó";
		else if (cod.equals("Ãš"))
			return "Ú";
		else if (cod.equals("Ã±"))
			return "ñ";
		else if (cod.equals("Ã‘"))
			return "Ñ";
		else if (cod.equals("Ã¤"))
			return "ä";
		else if (cod.equals("Ã«"))
			return "ë";
		else if (cod.equals("Ã¯"))
			return "ï";
		else if (cod.equals("Ã¶"))
			return "ö";
		else if (cod.equals("Ã¼"))
			return "ü";
		else if (cod.equals("Ã„"))
			return "Ä";
		else if (cod.equals("Ã‹"))
			return "Ë";
		else if (cod.equals("Ã�"))
			return "Ï";
		else if (cod.equals("Ã–"))
			return "Ö";
		else if (cod.equals("Ãœ"))
			return "Ü";
		else if (cod.equals("â‚¬"))
			return "€";
		else if (cod.equals("Âº"))
			return "º";
		else if (cod.equals("Âª"))
			return "ª";
		else if (cod.equals("Â¡"))
			return "¡";
		else if (cod.equals("m2"))
			return " metros cuadrados";
		else
			return cod;
	}

	/*--------- SETTERS DE PANELES --------*/

	public static ArrayList<Integer> getIdDescViviendVisitadas() {
		return panelVisitados.getIdDescViviendVisitadas();
	}

	public static void addVivienda(DescripcionVivienda d) {
		panelVisitados.addVivienda(d);
	}

	public static void actualizarPanel() {
		panelVisitados.actualizarPanel();
	}

	public static void disposeResult() {
		vResult.dispose();
	}

}
