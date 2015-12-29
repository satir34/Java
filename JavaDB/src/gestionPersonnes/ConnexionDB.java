package gestionPersonnes;
import java.sql.*;

import gestionPersonnes.Personne;

public class ConnexionDB {
	
	private Statement st;
	private ResultSet rs,rs2;
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
			rs = st.executeQuery("SELECT * FROM Personnes ORDER BY agePersonne DESC NULLS LAST");
			
			pst = cn.prepareStatement("SELECT * FROM Personnes WHERE agePersonne >= ? ORDER BY agePersonne DESC");
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
	
	public Personne rechercheAgeMin(String ageChoisi)
	{
		int ageRecheche = -1;
		if(isParsable(ageChoisi))
			ageRecheche = Integer.parseInt(ageChoisi);
		if(ageRecheche >= 0)
		{
			try {
				pst.setInt(1, ageRecheche);
				rs = pst.executeQuery();
				rs.next();
				
				num = rs.getString(1);
				nom =  rs.getString(2);
				prenom = rs.getString(3);
				age = rs.getInt(4);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
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
