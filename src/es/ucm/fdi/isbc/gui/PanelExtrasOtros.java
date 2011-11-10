package es.ucm.fdi.isbc.gui;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PanelExtrasOtros extends JPanel{

	private JLabel[] labels;
	private JComboBox combos[];
	
	public PanelExtrasOtros(){
		super();
		setLayout(new GridLayout(9,2));

		labels = new JLabel[9];
		labels[0] = new JLabel("Patio ");
		labels[1]  = new JLabel("Balc�n ");
		labels[2]  = new JLabel("Zona deportiva ");
		labels[3]  = new JLabel("Zona comunitaria ");
		labels[4]  = new JLabel("Terraza ");
		labels[5] = new JLabel("Piscina comunitaria ");
		labels[6] = new JLabel("Jard�n privado ");
		labels[7] = new JLabel("Zona infantil ");
		labels[8]  = new JLabel("Piscina ");
		
		combos = new JComboBox[9];
		String[] options = {"No", "S�"};
		int i = 0;
		for (JLabel l: labels){
			add(l);
			combos[i] = new JComboBox(options);
			add(combos[i]);
			i++;
		}
	}
}
