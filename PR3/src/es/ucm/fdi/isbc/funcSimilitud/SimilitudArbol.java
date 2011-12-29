package es.ucm.fdi.isbc.funcSimilitud;

import es.ucm.fdi.isbc.viviendas.representacion.Arbol;
import jcolibri.exception.NoApplicableSimilarityFunctionException;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

/**
 * No hace falta cargar la base de casos en el preciclo, a cada localizaci�n, que es un String (una ruta, o un �rbol 
 * de un s�lo camino). Puede mirarse la similitud con otro Strig/ruta/�rbol para ver la profundidad de las hojas y del
 * padre com�n.
 * 
 */

public class SimilitudArbol implements LocalSimilarityFunction
{
	/** Atributos **/
	
		Arbol arbol;
	
	/** Constructoras **/
		
		public SimilitudArbol(Arbol arbol)
		{
			this.arbol = arbol;
		}
	
	/** M�todos **/
	
		/* M�todos que implementan a LocalSimilarityFunction */
	
			public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException
			{
				
				if ((caseObject == null) || (queryObject == null)) return 0;

				if (!(caseObject instanceof java.lang.String))
					throw new NoApplicableSimilarityFunctionException(this.getClass(), queryObject.getClass());

				if (!(queryObject instanceof java.lang.String))
					throw new NoApplicableSimilarityFunctionException(this.getClass(), queryObject.getClass());

				Arbol a1 = new Arbol((String)caseObject);
				Arbol a2 = new Arbol((String)queryObject);

				// Si la queryObject no existe en el �rbol de datos saldr� s�lo la ra�z madrid. En cuyo caso el 
				// nivel del padre com�n ser� 0 y la medida de similitud ser� 0. En otro caso,

				// Hacemos el cast a double a uno de los n�meros para que la divisi�n la haga real y no entera ya que
					// los niveles del �rbol son n�meros naturales.

				return ((double)(a1.getNivelPadreComun(a2))) / Math.max(a1.getProfundidad(), a2.getProfundidad());

			}			

			// isApplicable a String
			public boolean isApplicable(Object caseObject, Object queryObject)
			{
				if ((caseObject == null) && (queryObject == null))
					return	true;
				else if (caseObject == null)
					return	queryObject instanceof String;
				else if (queryObject == null)
					return	caseObject instanceof String;
				else
					return	(caseObject instanceof String) &&
							(queryObject instanceof String);
			}
}
