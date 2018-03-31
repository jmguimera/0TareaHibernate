/*
 * ClientesDAO.java
 *
 */

package hibernate.mapeos;

//import hibernate.mapeos.HibernateUtil;
import clientes.entity.Clientes;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ClientesDAO
{  
    private Session sesion; 
    private Transaction tx;  

    // insertando un registro
    public void guardaCliente(Clientes cliente) throws HibernateException 
    { 

        try 
        { 
            iniciaOperacion(); 
            sesion.save(cliente);

            tx.commit(); 
        } catch (HibernateException he) 
        { 
            manejaExcepcion(he); 
            throw he; 
        } finally 
        { 
            sesion.close(); 
        }  


    }  

    // modificando un registro (actualizarlo)
    public void actualizaCliente(Clientes cliente) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.update(cliente); 
            tx.commit(); 
        } catch (HibernateException he) 
        { 
            manejaExcepcion(he); 
            throw he; 
        } finally 
        { 
            sesion.close(); 
        } 
    }  

    // borrando un registro de la tabla
    public void eliminaCliente(Clientes cliente) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.delete(cliente); 
            tx.commit(); 
        } catch (HibernateException he) 
        { 
            manejaExcepcion(he); 
            throw he; 
        } finally 
        { 
            sesion.close(); 
        } 
    }  

    // buscando un registro
    public Clientes getCliente(String idCliente) throws HibernateException 
    { 
        Clientes cliente = null;  
        try 
        { 
            iniciaOperacion(); 
            cliente = (Clientes) sesion.get(Clientes.class, idCliente); 
        } finally 
        { 
            sesion.close(); 
        }  

        return cliente; 
    }  

    // se obtiene una List coleccion con los objetos clientes
    public List<Clientes> getListaClientes() throws HibernateException 
    { 
        List<Clientes> listaClientes = null;  

        try 
        { 
            iniciaOperacion(); 
            listaClientes = sesion.createQuery("select c.nif,c.nombre from Clientes c").list(); 
        } finally 
        { 
            sesion.close(); 
        }  

        return listaClientes; 
    }  
// aqui llenamos el modelo de la rejilla    
    public DefaultTableModel llenaRejilla(DefaultTableModel dtm){
    
        iniciaOperacion();
        Query q =sesion.createQuery("from Clientes");
        List<Clientes> lista =q.list();
        Iterator<Clientes> iter=lista.iterator();
        sesion.close();

        while(iter.hasNext())
        {
         // aqui sacamos cada elemento de la rejilla com objeto   
          Clientes cliente=iter.next();
          // luego creamos el objeto fila con los datos que mostrara la tabla
          Object[] fila={cliente.getNif(),cliente.getNombre()};
            dtm.addRow(fila);
        }
        //aqui devolvemos el contenido de la rejilla
        return dtm;
    }

    // rutina de inicio de operacion de cada transacción sobre la base de datos
    private void iniciaOperacion() throws HibernateException 
    { 
        sesion = HibernateUtil.getSessionFactory().openSession(); 
        tx = sesion.beginTransaction(); 
    }  
    // rutina de captura de errores de Exception que lanze hibernate
    private void manejaExcepcion(HibernateException he) throws HibernateException 
    { 
        tx.rollback(); 
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos", he); 
    } 
}
