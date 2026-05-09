package com.productos.datos;
import java.sql.*;
public class Conexion
{
	private Statement St; 
	private String driver;
	private String user;
	private String pwd;
	private String cadena;
	private Connection con;

	String getDriver()
	{
		return this.driver;
	}
	String getUser()
	{
		return this.user;
	}
	String getPwd()
	{
		return this.pwd;
	}
	String getCadena()
	{
		return this.cadena;
	}
	public Connection getConexion()
	{ 
		return this.con; 
	}
	
	public Conexion() {
		
		this.driver ="org.postgresql.Driver";
		this.user="postgres";
		this.pwd="1234";
		this.cadena="jdbc:postgresql://localhost:5432/bd_productos";
		this.con=this.crearConexion();	
	}
	
	Connection crearConexion()
	{
		try {
			Class.forName("org.postgresql.Driver");
			}
			catch (ClassNotFoundException e) {
			
			}
		
		try
		{
			Class.forName(getDriver()).newInstance();
			Connection con=DriverManager.getConnection(getCadena(),getUser(),getPwd());
			
			return con;
			
		}
	catch(Exception ee)
	{
		System.out.println("Error: " + ee.getMessage());
		return null;
	}
	}


	public String Ejecutar(String sql) {
		String result="";
		try {
			St = getConexion().createStatement();
			// executeUpdate es el comando correcto para INSERT, UPDATE y DELETE
			int filasAfectadas = St.executeUpdate(sql);

			if (filasAfectadas > 0) {
				result="Operacion realizada con exito";
			} else {
				result="Se ha producido un error";
			}
		} catch(Exception ex) {
			result += ex.getMessage();
		}
		return(result);
	}



	public ResultSet Consulta(String sql)
	{
	String error="";
	ResultSet reg=null;
	
	try
	{
	St=getConexion().createStatement();
	reg=St.executeQuery(sql);
	

	}
	catch(Exception ee)
	{
	error = ee.getMessage();
	}
	return(reg);
	}
}