package gestionPersonnes;
import java.sql.*;

import gestionPersonnes.Personne;
import jdk.nashorn.internal.ir.GetSplitState;

public class ConnexionDB {
	
	private Statement st;
	private ResultSet rs;
	private Connection cn;
	private PreparedStatement pst;
	
	private String num = null, nom = null, prenom = null;
	private int age = -1;
	
	public ConnexionDB()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@162.38.222.149:1521:iut";
			String login = "ronsinl";
			String mdp = "123";
			cn = DriverManager.getConnection(url, login, mdp);
			st = cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = st.executeQuery("SELECT p.* FROM Personnes p ORDER BY p.agePersonne DESC NULLS LAST");
			pst = cn.prepareStatement("SELECT p.* FROM Personnes WHERE p.agePersonne >= ? ORDER BY p.agePersonne DESC", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		}
		catch (SQLException e) 
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) 
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static boolean isParsable(String input){
	    boolean parsable = true;
	    try{
	        Integer.parseInt(input);
	    }catch(NumberFormatException e){
	        parsable = false;
	    }
	    return parsable;
	}
	
	public void delete()
	{
		try {
			rs.deleteRow();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public Personne rechercheAgeMin(String ageChoisi)
	{
		int ageRecherche = -1;
		if(isParsable(ageChoisi))
			ageRecherche = Integer.parseInt(ageChoisi);
		if(ageRecherche >= 0)
		{
			try {
				pst.setInt(1, ageRecherche);
				rs = pst.executeQuery();
				rs.next();
				
				num = rs.getString(1);
				nom =  rs.getString(2);
				prenom = rs.getString(3);
				age = rs.getInt(4);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		Personne result = new Personne(num, nom, prenom, age);
		
		return result;
	}
	
	public Personne tous()
	{
		try {
			
			rs = st.executeQuery("SELECT p.* FROM Personnes p ORDER BY p.agePersonne DESC NULLS LAST");
			rs.first();
			
			num = rs.getString(1);
			nom =  rs.getString(2);
			prenom = rs.getString(3);
			age = rs.getInt(4);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		Personne result = new Personne(num, nom, prenom, age);
		
		return result;
	}
	
	public Personne actionBouton(String performedButton)
	{
		try 
		{
			switch(performedButton)
			{
			case "p" :
				rs.first();
				break;
			case "d" :
				rs.last();
				break;
			case "s" :
				if(!rs.isLast())
				{
					rs.next();
				}								
				break;
			case "b" :
				if(!rs.isFirst())
				{
					rs.previous();
				}
				break;
			default:
				rs.first();
				break;
			}
			num = rs.getString(1);
			nom =  rs.getString(2);
			prenom = rs.getString(3);
			age = rs.getInt(4);
			if(rs.wasNull())
				age = -1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Personne result = new Personne(num, nom, prenom, age);
		
		return result;
	}
	
	public void deconnexion()
	{
		try {
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			cn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
