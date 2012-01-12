package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 *  Panel que contiene las viviendas con diversidad de contenido
 *
 */

class PanelDiversidad extends JPanel
{
	
	private static final long serialVersionUID = 1L;
	private Galeria galeria;
	
	public PanelDiversidad(Galeria galeria)
	{
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		this.galeria = galeria;
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dim.setSize(dim.width - 100, dim.height - 100);
		
		setMinimumSize(new Dimension((int) (dim.width * 0.73), (int) (dim.height * 0.78)));
		setSize(new Dimension((int) (dim.width * 0.73), (int) (dim.height * 0.78)));
	}
	
	// Una vez obtenidas las viviendas con diversidad de 3 a 5, meterlas en el panel
	
	public void setViviendasDiversas()
	{
		
	}

}
