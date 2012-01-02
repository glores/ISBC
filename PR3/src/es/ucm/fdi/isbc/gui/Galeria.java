package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Galeria
{

	/** Atributos **/
	
		private ArrayList<ImageIcon> fotos;
		private static ImageIcon noFoto;

    /** Constructores **/
	
		public Galeria()
		{
			try {
				fotos = new ArrayList<ImageIcon>();
				noFoto = new ImageIcon(new URL("http://farm8.staticflickr.com/7008/6591881587_2c24e9ab39_z.jpg"), 
						"No hay foto");
			}
			catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		
		public Galeria(ArrayList<ImageIcon> fotos)
		{
			try {
				this.fotos = fotos;
				noFoto = new ImageIcon(new URL("http://farm8.staticflickr.com/7008/6591881587_2c24e9ab39_z.jpg"), 
						"No hay foto");
			}
			catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

	/** M�todos **/
		
		/* Setters */
		
			public void setFotos(ArrayList<ImageIcon> fotos)
			{
				this.fotos = fotos;
			}
	
		/* Otros m�todos */
			
			/**
			 * @param num
			 * @return una imagen de tama�o 100 x 100 VISTA PREVIA
			 */
			public Icon getVistaPrevia(int num)
			{
				if (num >= 0 && num < fotos.size()) {
					Image mini = fotos.get(num).getImage().getScaledInstance(100, 100, Image.SCALE_AREA_AVERAGING);
					return new ImageIcon(mini);
				}
				else {
					Image mini = noFoto.getImage().getScaledInstance(100, 100, Image.SCALE_AREA_AVERAGING);
					return new ImageIcon(mini);
				}
			}

			/**
			 * @param num
			 * @param dim
			 * @return la foto original, pero si el tama�o es mayor al contenedor, lo redimensiona.
			 */
			public Icon getFoto(int num, Dimension dim)
			{
				if (num >= 0 && num < fotos.size()) {
					// Si la foto es m�s grande que el contendor entonces redimensionamos.
					if (fotos.get(num).getIconWidth() > dim.getWidth()) {
						float v = getValor(fotos.get(num).getIconWidth(), dim.width);
						return Disminuir(fotos.get(num), v);
					}
					else if (fotos.get(num).getIconHeight() > dim.getHeight()) {
						float v = getValor(fotos.get(num).getIconHeight(), dim.height);               
						return Disminuir(fotos.get(num), v);
					}
					else return fotos.get(num);
				}
				else {
					Image mini = noFoto.getImage().getScaledInstance(400, 400, Image.SCALE_AREA_AVERAGING);
					return new ImageIcon(mini);
				}
			}
			
			/**
			 * @param imageIcon
			 * @param value
			 * @return redimensiona la imagen para que ingrese al contendor pero manteniendo sus proporciones.
			 */
			private ImageIcon Disminuir(ImageIcon imageIcon, float value)
			{
				int valEscalaX =  (int) (imageIcon.getIconWidth() * value );
				int valEscalaY =  (int) (imageIcon.getIconHeight() * value );
				Image mini = imageIcon.getImage().getScaledInstance(valEscalaX, valEscalaY, Image.SCALE_AREA_AVERAGING);
				return new ImageIcon(mini);
			}

			/**
			 * @param a
			 * @param b
			 * @return el valor de escala para redimensionar la imagen
			 */
			private float getValor(int a, int b)
			{
				return Math.abs((a / new Float(b)) - 2f);
			}
}
