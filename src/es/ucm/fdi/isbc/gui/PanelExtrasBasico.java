package es.ucm.fdi.isbc.gui;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.ucm.fdi.isbc.viviendas.representacion.ExtrasBasicos;

@SuppressWarnings("serial")
public class PanelExtrasBasico extends JPanel{
	
	private JLabel[] labels;
	private JComboBox combos[];
	
	public PanelExtrasBasico(){
		super();
		setLayout(new GridLayout(18,2));
		
		labels = new JLabel[18];
		labels[0] = new JLabel("Lavadero ");
		labels[1]  = new JLabel("Internet ");
		labels[2]  = new JLabel("Microondas ");
		labels[3]  = new JLabel("Horno ");
		labels[4]  = new JLabel("Amueblado ");
		labels[5] = new JLabel("Cocina Office ");
		labels[6] = new JLabel("Parquet ");
		labels[7] = new JLabel("Dom�tica ");
		labels[8]  = new JLabel("Armarios ");
		labels[9]  = new JLabel("Televisi�n ");
		labels[10]  = new JLabel("Lavadora ");
		labels[11]  = new JLabel("Electrodom�sticos ");
		labels[12] = new JLabel("Suite con ba�o ");
		labels[13] = new JLabel("Puerta blindada ");
		labels[14] = new JLabel("Gres cer�mica ");
		labels[15]  = new JLabel("Calefacci�n ");
		labels[16]  = new JLabel("Aire acondicionado ");
		labels[17]  = new JLabel("Nevera ");
		
		combos = new JComboBox[18];
		String[] options = {"No", "S�"};
		int i = 0;
		for (JLabel l: labels){
			add(l);
			combos[i] = new JComboBox(options);
			add(combos[i]);
			i++;
		}
	}
	
	public ExtrasBasicos getExtrasBasicos(int id){
		// TODO: id de extras b�sicos y dem�s?
		ExtrasBasicos extrasBasicos = new ExtrasBasicos(id);
		extrasBasicos.setLavadero(combos[0].getSelectedItem().equals("S�"));
		extrasBasicos.setInternet(combos[1].getSelectedItem().equals("S�"));
		extrasBasicos.setMicroondas(combos[2].getSelectedItem().equals("S�"));
		extrasBasicos.setHorno(combos[3].getSelectedItem().equals("S�"));
		extrasBasicos.setAmueblado(combos[4].getSelectedItem().equals("S�"));
		extrasBasicos.setCocinaOffice(combos[5].getSelectedItem().equals("S�"));
		extrasBasicos.setParquet(combos[6].getSelectedItem().equals("S�"));
		extrasBasicos.setDomotica(combos[7].getSelectedItem().equals("S�"));
		extrasBasicos.setArmarios(combos[8].getSelectedItem().equals("S�"));
		extrasBasicos.setTv(combos[9].getSelectedItem().equals("S�"));
		extrasBasicos.setLavadora(combos[10].getSelectedItem().equals("S�"));
		extrasBasicos.setElectrodomesticos(combos[11].getSelectedItem().equals("S�"));
		extrasBasicos.setSuiteConBanio(combos[12].getSelectedItem().equals("S�"));
		extrasBasicos.setPuertaBlindada(combos[13].getSelectedItem().equals("S�"));
		extrasBasicos.setGresCeramica(combos[14].getSelectedItem().equals("S�"));
		extrasBasicos.setCalefaccion(combos[15].getSelectedItem().equals("S�"));
		extrasBasicos.setAireAcondicionado(combos[16].getSelectedItem().equals("S�"));
		extrasBasicos.setNevera(combos[17].getSelectedItem().equals("S�"));
		
		return extrasBasicos;
	}

}
