import com.nanuvem.lom.model.Entity;
import com.nanuvem.lom.service.EntityService;
import com.nanuvem.lom.service.EntityServiceImpl;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("teste");
		
		Entity e = new Entity();
		e.setName("entity test");
		e.setName("namespace test");
		
		
		EntityService es = new EntityServiceImpl();
		es.saveEntity(e);
		
		
		System.out.println(es.findAllEntitys().get(0).getName());
	}

}
