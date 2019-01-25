## **ARXOrm**

MicroOrm de uso interno en ARX para CINV programado en Java para conexión y mapeo de base de datos Firebird (proximamente MYSQL)

**Modo de uso** 
Se debe tener previamente la base de datos creada y por cada tabla tener una clase POJO con el mismo nombre y los mismo campos que posee, como requisito todas las tablas deben tener el campo ID del tipo Integer.

**Tabla "Almacen"**

Nombre Campo |Tipo dato
-- | --
|   Id               |   Int      |
|   Nombre           |   Varchar  |
|   Domicilio        |   Varchar  |

**POJO "Almacen**"

~~~
 public class Almacen
 {
   private Integer id;
   private String nombre;
   private String domicilio;
   
   public Integer getId()
   {
     return this.id;
   }
   
   public void setId(Integer id) 
   { 
	   this.id = id; 
   }
   
   public String getNombre() {
     return this.nombre;
   }
   
   public void setNombre(String nombre)
   { 
	   this.nombre = nombre; 
   }
   
   public String getDomicilio() {
     return this.domicilio;
   }
   
   public void setDomicilio(String domicilio) { 
	   this.domicilio = domicilio;
	   }
 }
~~~

**Conexión**
~~~
ARXOrm orm = new ARXOrm(ARXOrm.ARXDriversORM.Conectar(ARXOrm.ARXDriversORM.baseDeDatos.FIREBIRD, "nombre del servidor", "ruta base de datos", "nombre usuario", "clave", puerto));
~~~
**Selecciona**
~~~
Object[] resultado=orm.Query(Almacen.class, "select * from ALMACEN");  
~~~

**Inserta**
~~~
Almancen a= new Almacen();
a.setNombre("Almacen 1");
a.setDomicilo("Domicilio Conocido");

orm.insert(a);
~~~

**Actualiza**
~~~
Almancen a= new Almacen();
a.setId(1);
a.setNombre("Almacen 1");
a.setDomicilo("Nuevo Domicilio Conocido");

orm.update(a);
~~~

**Borra**
~~~
Almancen a= new Almacen();
a.setId(1);

orm.delete(a);
~~~
