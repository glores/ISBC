package es.ucm.fdi.isbc.gui;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.ucm.fdi.isbc.viviendas.representacion.ExtrasFinca;

@SuppressWarnings("serial")
public class PanelExtrasFinca extends JPanel{
	
	private JLabel[] labels;
	private JComboBox combos[];
	
	public PanelExtrasFinca(){
		super();
		setLayout(new GridLayout(7,2));
		
		labels = new JLabel[7];
		labels[0] = new JLabel("Ascensor ");
		labels[1]  = new JLabel("Trastero ");
		labels[2]  = new JLabel("Energ�a solar ");
		labels[3]  = new JLabel("Porter�a ");
		labels[4]  = new JLabel("Parking comunitario ");
		labels[5] = new JLabel("Garaje privado ");
		labels[6] = new JLabel("Video portero ");
		
		combos = new JComboBox[7];
		String[] options = {"No", "S�"};
		int i = 0;
		for (JLabel l: labels){
			add(l);
			combos[i] = new JComboBox(options);
			add(combos[i]);
			i++;
		}
	}
	
	public ExtrasFinca getExtrasFinca(int id){
		// TODO: id de extras b�sicos y dem�s?
		ExtrasFinca extrasFinca = new ExtrasFinca(id);
		extrasFinca.setAscensor(combos[0].getSelectedItem().equals("S�"));
		extrasFinca.setTrastero(combos[1].getSelectedItem().equals("S�"));
		extrasFinca.setEnergiaSolar(combos[2].getSelectedItem().equals("S�"));
		extrasFinca.setServPorteria(combos[3].getSelectedItem().equals("S�"));
		extrasFinca.setParkingComunitario(combos[4].getSelectedItem().equals("S�"));
		extrasFinca.setGarajePrivado(combos[5].getSelectedItem().equals("S�"));
		extrasFinca.setVideoportero(combos[6].getSelectedItem().equals("S�"));
		
		return extrasFinca;
	}
	
}
