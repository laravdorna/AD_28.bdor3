/*
O tipo java.sql.Struct permite analizar o contido dun campo tipo obxecto (creado por o suario)
 
para ler dende java  un campo tipo obxecto que se atopa entre outros campos dun resulset (rs) debemos facer :

rs.getObject(n) , sendo n a posicion que ocupa o campo no resulset contando dende 1. 

o resultado de esta lectura temos que convertilo a un tipo Struct deste xeito:

 
 java.sql.Struct x = rs.getObject(n)
   
despois desto debemos crear un array cos campos do obxecto lido

Object[] campos = x.getAttributes();

e xa podemos pasar  os atributos do obxecto a os tipos oracle 
por exemplo se o primeiro campo e un String e o segundo e un enteiro faremos

      String z = (String) empValues[0];
      java.math.BigDecimal y = (java.math.BigDecimal) empValues[1];

Exercicio 28 : bdor3
-dispomos dun script chamado employee.sql que crea duas taboas . lanzao como o usario hr de oracle 
- desenvolve un proxecto java chamado bdor3 que  amose todos os datos de todos os empregados da taboa empregados
 */
/* COMANDOS
lanzar servidor oracle para trabajar desde java con el listener

. oraenv
 orcl
rlwrap sqlplus sys/oracle as sysdba 
startup
conn hr/hr
exit
lsnrctl start
lsnrctl status

*/
/* añadir ojdbc7 a libraries*/
 
package pkg28.bdor3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;

/**
 *
 * @author oracle
 */
public class Bdor3 {
public static Connection conexion = null;

    public static Connection getConexion() throws SQLException {
        String usuario = "hr";
        String password = "hr";
        String host = "localhost";
        String puerto = "1521";
        String sid = "orcl";
        String ulrjdbc = "jdbc:oracle:thin:" + usuario + "/" + password + "@" + host + ":" + puerto + ":" + sid;

        conexion = DriverManager.getConnection(ulrjdbc);
        return conexion;
    }

    public static void closeConexion() throws SQLException {
        conexion.close();
    }
    public static void main(String[] args) throws SQLException {
    
       
 // amose todos os datos de todos os empregados da taboa empregados

        
        //abrir conexion
        getConexion();
        int nFilas=3;
        String consulta= "select id,emp, salario from empregadosbdor ";
        Statement stm = conexion.createStatement();
        ResultSet rs= stm.executeQuery(consulta);

        
       for (int i = 0; i < nFilas; i++) {
            rs.next();
            int id = rs.getInt("id");
            int salario = rs.getInt("salario");
            rs.getObject(2);//posicion de la columna empieza a contar desde uno
            
            Struct empleado = (java.sql.Struct) rs.getObject(2); //tipo extructurado porque es un objeto empleado
            
            //Se guarda en un array
            Object[] campos = empleado.getAttributes(); 
            String nome = (String) campos[0];  //definimos como string el primer parametro
            java.math.BigDecimal edad = (java.math.BigDecimal) campos[1]; //campos de enteros hay que castearlos
            System.out.println("Id: " + id + "Nome: "+nome+"Edade: "+edad + "salario: "+salario+"€"  );

        
       } 
        
        
        
        
        //cerrar conexion
        closeConexion();
        
        
        
        
    }
    
}
