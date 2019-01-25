 package Utils;
 
 import java.lang.reflect.InvocationTargetException;
 import java.lang.reflect.Method;
 import java.sql.Connection;
 import java.sql.DriverManager;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.text.SimpleDateFormat;
 import java.util.ArrayList;
 import java.util.Date;
 
 
 public class ARXOrm
 {

   private Connection conexion = null;
   private PreparedStatement comando = null;
   private ResultSet resultado = null;
  
   public ARXOrm(Connection pArxConexion)
   {
     this.conexion = pArxConexion;
   }
    
   public void cerrarConexion()
   {
     try
     {
       this.conexion.close();
     } catch (Exception e) {
       System.out.println(e.toString());
     }
   }
    
   public void ejecutaSoloSQL(String pSQL)
   {
     try
     {
       System.out.println(pSQL);
       this.comando = this.conexion.prepareStatement(pSQL);
       this.comando.execute();
       this.comando.close();
     } catch (Exception e) {
       System.out.println(e.toString());
     }
   }
   
   public ResultSet ejecutaSQL(String pSQL)
   {
     try
     {
       System.out.println(pSQL);
       this.comando = this.conexion.prepareStatement(pSQL);
       this.resultado = this.comando.executeQuery();
 
     }
     catch (Exception e)
     {
       System.out.println(e.toString());
     }
     
     return this.resultado;
   }
    
   public Object simpleQuery(Class claseModelo, String pSQL)
   {
     try
     {
       Object tmp = claseModelo.newInstance();
       Method[] metodos = claseModelo.getDeclaredMethods();
       
       ejecutaSQL(pSQL);
       int j= metodos.length;
       int i=0; 
       
       this.resultado.next();
       
       for (i =0 ; i <j ; i++) {
    	 
    	 Method method= metodos[i];  
    	   
         if (!method.getName().contains("get")) {
          
           String tipoRetorno = claseModelo.getMethod(method.getName().replace("set", "get")).getReturnType().getName();
           String str1;
           
           switch (tipoRetorno) {
           case "java.lang.Integer":  
        	   
        	   Integer rEntero = Integer.valueOf(this.resultado.getInt(method.getName().replace("set", "").toUpperCase()));
               method.invoke(tmp, new Object[] { rEntero }); 
        	   
               break; 
           
           case "java.util.Date":  
        	   
        	   Date rFecha = this.resultado.getDate(method.getName().replace("set", "").toUpperCase());
               method.invoke(tmp, new Object[] { rFecha });
               
        	   break;
        	   
           case "java.lang.Double":  
        	   
        	   Double rDecimal = Double.valueOf(this.resultado.getDouble(method.getName().replace("set", "").toUpperCase()));
               method.invoke(tmp, new Object[] { rDecimal });
               
               break;
               
           case "java.lang.String":  
        	   
        	   String rCadena = this.resultado.getString(method.getName().replace("set", "").toUpperCase());
               method.invoke(tmp, new Object[] { rCadena });
               
               break;
           }
         }
         
       }
        
       return tmp;
     }
     catch (Exception e)
     {
       System.out.println(e.toString());
     }
     
     return null;
   }
   
 
   public Object[] Query(Class claseModelo, String pSQL)
   {
     ArrayList<Object> registros = new ArrayList();
     
     try
     {
    	 
         Method[] metodos = claseModelo.getDeclaredMethods();
         
         ejecutaSQL(pSQL);
         int j= metodos.length;
         int i=0;	 
           
       while (this.resultado.next())
       {	
    	   Object tmp = claseModelo.newInstance();
    	   
    	   for (i =0 ; i <j ; i++) {
    	    	 
    	    	 Method method= metodos[i];  
    	    	   
    	         if (!method.getName().contains("get")) {
    	          
    	           Method metTemp = claseModelo.getMethod("get" + method.getName().replace("set", ""));
    	           String tipoRetorno = metTemp.getReturnType().getName();
    	           String str1;
    	           
    	           switch (tipoRetorno) {
    	           case "java.lang.Integer":  
    	        	   
    	        	   Integer rEntero = Integer.valueOf(this.resultado.getInt(method.getName().replace("set", "").toUpperCase()));
    	               method.invoke(tmp, new Object[] { rEntero }); 
    	        	   
    	               break; 
    	           
    	           case "java.util.Date":  
    	        	   
    	        	   Date rFecha = this.resultado.getDate(method.getName().replace("set", "").toUpperCase());
    	               method.invoke(tmp, new Object[] { rFecha });
    	               
    	        	   break;
    	        	   
    	           case "java.lang.Double":  
    	        	   
    	        	   Double rDecimal = Double.valueOf(this.resultado.getDouble(method.getName().replace("set", "").toUpperCase()));
    	               method.invoke(tmp, new Object[] { rDecimal });
    	               
    	               break;
    	               
    	           case "java.lang.String":  
    	        	   
    	        	   String rCadena = this.resultado.getString(method.getName().replace("set", "").toUpperCase());
    	               method.invoke(tmp, new Object[] { rCadena });
    	               
    	               break;
    	           }
    	         }
    	         
    	       }
         registros.add(tmp);
       }
       
       return registros.toArray();
     }
     catch (Exception e)
     {
       System.out.println(e.toString()); }
     return null;
   }
    
 
   public Integer insert(Object pModelo)
   {
     Class clase = pModelo.getClass();
     Method[] metodos = clase.getDeclaredMethods();
     
     StringBuilder campos = new StringBuilder();
     StringBuilder datos = new StringBuilder();
     Method[] arrayOfMethod1;
     int j = (arrayOfMethod1 = metodos).length; 
     
     for (int i = 0; i < j; i++) { 
       
       Method metodo = arrayOfMethod1[i];
       
       if ((!metodo.getName().contains("set")) && (!metodo.getName().equals("getId")))
       {
         campos.append(metodo.getName().replace("get", "").toUpperCase());
         campos.append(",");
         
         String tipoRetorno = metodo.getReturnType().getName();
         
         switch (tipoRetorno) {
         case "java.lang.Integer":  
      	   
        	 try {
				datos.append(metodo.invoke(pModelo, new Object[0]));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
        	 datos.append(",");
      	   
             break; 
         
         case "java.util.Date":  
      	   
        	 datos.append("'");
        	 try {
				datos.append(new SimpleDateFormat("yyyy-MM-dd").format((Date)metodo.invoke(pModelo, new Object[0])));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
        	 datos.append("'");
             datos.append(",");
        	 
      	   	 break;
      	   
         case "java.lang.Double":  
      	   
        	 try {
				datos.append(metodo.invoke(pModelo, new Object[0]));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	 datos.append(",");
             
             break;
             
         case "java.lang.String":  
      	   
        	 datos.append("'");
        	 try {
				datos.append(metodo.invoke(pModelo, new Object[0]));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	 datos.append("'");
             datos.append(",");
             
             break;
         }
           

         } 
       }
     
     StringBuilder sql = new StringBuilder();
     sql.append("INSERT INTO ");
     sql.append(clase.getSimpleName());
     sql.append(" (");
     sql.append(campos.toString().substring(0, campos.length() - 1));
     sql.append(")");
     sql.append(" VALUES(");
     sql.append(datos.toString().substring(0, datos.length() - 1));
     sql.append(")");
     
 
     ejecutaSoloSQL(sql.toString());
     
     StringBuilder sqlIdentificador = new StringBuilder();
     sqlIdentificador.append("select gen_id(");
     sqlIdentificador.append(clase.getSimpleName().toUpperCase());
     sqlIdentificador.append("_ID_GEN, 0) as id from rdb$database");
     
     ejecutaSQL(sqlIdentificador.toString());
     
     try {
		if(this.resultado.next()) {
			 
			return this.resultado.getInt("ID");
		 }else {
			 return 0;
		 }
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return 0;
	}
     
   }
   
 
   public void update(Object pModelo)
   {
     Class clase = pModelo.getClass();
     Method[] metodos = clase.getDeclaredMethods();
     
     StringBuilder campos = new StringBuilder();
     StringBuilder datos = new StringBuilder();
     Method[] arrayOfMethod1;
     int j = (arrayOfMethod1 = metodos).length; 
     
     for (int i = 0; i < j; i++) { 
       
       Method metodo = arrayOfMethod1[i];
       
       if ((!metodo.getName().contains("set")) && (!metodo.getName().equals("getId"))){
       
         campos.append(metodo.getName().replace("get", "").toUpperCase());
         campos.append("=");
         
         String tipoRetorno = metodo.getReturnType().getName();
         
         switch (tipoRetorno) {
         case "java.lang.Integer":  
      	   
        	 try {
				campos.append(metodo.invoke(pModelo, new Object[0]));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	 campos.append(",");
      	   
             break; 
         
         case "java.util.Date":  
      	   
        	 campos.append("'");
        	 try {
				campos.append(new SimpleDateFormat("yyyy-MM-dd").format((Date)metodo.invoke(pModelo, new Object[0])));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	 campos.append("'");
             campos.append(",");
        	 
      	   	 break;
      	   
         case "java.lang.Double":  
      	   
        	 try {
				campos.append(metodo.invoke(pModelo, new Object[0]));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	 campos.append(",");
             
             break;
             
         case "java.lang.String":  
      	   
        	 campos.append("'");
        	 try {
				campos.append(metodo.invoke(pModelo, new Object[0]));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	 campos.append("'");
             campos.append(",");
             
             break;
         }
     
       }
     }
     
     Integer id = Integer.valueOf(0);
     
     try
     {
       Method metodo = clase.getMethod("getId", new Class[0]);
       id = (Integer)metodo.invoke(pModelo, new Object[0]);
     }
     catch (Exception e)
     {
       e.printStackTrace();
     }
     
     StringBuilder sql = new StringBuilder();
     sql.append("UPDATE ");
     sql.append(clase.getSimpleName());
     sql.append(" SET ");
     sql.append(campos.toString().substring(0, campos.length() - 1));
     sql.append("");
     sql.append(" WHERE ID=");
     sql.append(id.toString());
     sql.append("");
     
 
     ejecutaSoloSQL(sql.toString());
   }
   
 
   public void delete(Object pModelo)
   {
     Class clase = pModelo.getClass();
     StringBuilder sql = new StringBuilder();
     sql.append("delete from ");
     sql.append(clase.getSimpleName().toUpperCase());
     sql.append(" where id=");
     try
     {
       Method metodo = clase.getMethod("getId", new Class[0]);
       Integer id = (Integer)metodo.invoke(pModelo, new Object[0]);
       sql.append(id);
     }
     catch (Exception e)
     {
       e.printStackTrace();
     }
     
     ejecutaSoloSQL(sql.toString());
   }
   
   public void close()
   {
     try {
       this.conexion.close();
     }
     catch (SQLException e) {
       System.out.println(e.toString());
     }
   }
   
   public Connection getConnection() {
     return this.conexion;
   }
   
   public static class ARXDriversORM
   {
     private static final String Firebird = "org.firebirdsql.jdbc.FBDriver";
     private static final String MySQL = "com.mysql.jdbc.Driver";
     
     public static enum baseDeDatos {
       FIREBIRD,  MYSQL;
     }
     
     public static Connection Conectar(baseDeDatos tipoBase, String servidor, String base, String usuario, String contraseña, String puerto)
     {
       switch (tipoBase) {
       case FIREBIRD: 
         return ConectaFirebrid(servidor, base, usuario, contraseña, puerto);
       case MYSQL: 
         return null;
       }
       return null;
     }
     
 
     private static Connection ConectaFirebrid(String servidor, String base, String usuario, String contraseña, String puerto)
     {
       Connection conexion = null;
       try {
         Class.forName("org.firebirdsql.jdbc.FBDriver");
         
         conexion = DriverManager.getConnection("jdbc:firebirdsql:" + servidor + "/" + puerto + ":" + base, usuario, contraseña);
       }
       catch (Exception e) {
         System.out.println(e.toString());
       }
       return conexion;
     }
   }
 }