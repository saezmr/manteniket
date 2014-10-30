package an.dpr.manteniket.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.ComponentUse;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.repository.ComponentUsesRepository;

public class ComponentUsesDAO implements IComponentUsesDAO {

    private static final Logger log = LoggerFactory.getLogger(ComponentUsesDAO.class);
    @Autowired
    private ComponentUsesRepository repo;
    @Autowired
    private PlatformTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;
    
    private TransactionTemplate getTransactionTemplate(){
	if (transactionTemplate == null){
	    transactionTemplate = new TransactionTemplate(transactionManager);
	}
	return transactionTemplate;
    }
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IComponentUsesDAOo#findOne(java.lang.Long)
     */
    @Override
    public ComponentUse findOne(final Long id){
	return getTransactionTemplate().execute(new TransactionCallback<ComponentUse>(){

	    @Override
	    public ComponentUse doInTransaction(TransactionStatus status) {
		ComponentUse cu = repo.findOne(id);
		Hibernate.initialize(cu.getBike());
		Hibernate.initialize(cu.getComponent());
		return cu;
	    }
	
	});
	//TODO excepciones sql
    }
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IComponentUsesDAOo#save(an.dpr.manteniket.domain.ComponentUse)
     */
    @Override
    public ComponentUse save(ComponentUse use){
	return repo.save(use);
	//TODO excepciones sql
    }
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IComponentUsesDAOo#delete(java.lang.Long)
     */
    @Override
    public void delete(Long id){
	repo.delete(id);
	//TODO excepciones sql
    }
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IComponentUsesDAOo#findByBike(an.dpr.manteniket.domain.Bici)
     */
    @Override
    public List<ComponentUse> findByBike(final Bici bike){
	return getTransactionTemplate().execute(
		new TransactionCallback<List<ComponentUse>>(){
	    
	    @Override
	    public List<ComponentUse> doInTransaction(TransactionStatus status) {
		List<ComponentUse> list = repo.findByBike(bike);
		for (ComponentUse cu : list){
		    Hibernate.initialize(cu.getBike());
		    Hibernate.initialize(cu.getComponent());
		}
		return list;
	    }
	    
	});
	//TODO excepciones sql
    }
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IComponentUsesDAOo#findByComponent(an.dpr.manteniket.domain.Component)
     */
    @Override
    public List<ComponentUse> findByComponent(final Component component){
	return getTransactionTemplate().execute(
		new TransactionCallback<List<ComponentUse>>(){
		    
		    @Override
		    public List<ComponentUse> doInTransaction(TransactionStatus status) {
			List<ComponentUse> list = repo.findByComponent(component);
			for (ComponentUse cu : list){
			    Hibernate.initialize(cu.getBike());
			    Hibernate.initialize(cu.getComponent());
			}
			return list;
		    }
		    
		});
	//TODO excepciones sql
    }
    
    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IComponentUsesDAOo#findByDate(java.util.Date)
     */
    @Override
    public List<ComponentUse> findByDate(final Date date){
	return getTransactionTemplate().execute(
		new TransactionCallback<List<ComponentUse>>(){
		    
		    @Override
		    public List<ComponentUse> doInTransaction(TransactionStatus status) {
			List<ComponentUse> list = repo.findByDate(date);
			for (ComponentUse cu : list){
			    Hibernate.initialize(cu.getBike());
			    Hibernate.initialize(cu.getComponent());
			}
			return list;
		    }
		    
		});
	//TODO excepciones sql
    }

    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IComponentUsesDAOo#getRepo()
     */
    @Override
    public ComponentUsesRepository getRepo() {
        return repo;
    }

    /* (non-Javadoc)
     * @see an.dpr.manteniket.dao.IComponentUsesDAOo#setRepo(an.dpr.manteniket.repository.ComponentUsesRepository)
     */
    @Override
    public void setRepo(ComponentUsesRepository repo) {
        this.repo = repo;
    }

    @Override
    public long count() {
	return repo.count();
    }

    @Override
    public long count(ComponentUse use) {
	long count = 0;
	if (use != null &&use.getBike()!=null){
	    count = repo.countByBike(use.getBike());
	    
	} else if (use != null && use.getComponent()!=null){
	    count = repo.countByComponent(use.getComponent());
	    
	} else {
	    count = count();
	}
	return count;
    }

    @Override
    public List<ComponentUse> find(final ComponentUse filtro, final Sort sort, final int fromPage, final int itemsPage) {
	return getTransactionTemplate().execute(
		new TransactionCallback<List<ComponentUse>>(){
		    
		    @Override
		    public List<ComponentUse> doInTransaction(TransactionStatus status) {
			List<ComponentUse> list = findLazy(filtro, sort, fromPage, itemsPage);
			for (ComponentUse cu : list){
			    Hibernate.initialize(cu.getBike());
			    Hibernate.initialize(cu.getComponent());
			}
			return list;
		    }
		    
		});
    }
    
    public List<ComponentUse> findLazy(ComponentUse filtro, Sort sort, int fromPage, int itemsPage) {
	List<ComponentUse> list;
	Page<ComponentUse> page;
	PageRequest pageRequest = new PageRequest(fromPage, itemsPage, sort);
	
	if (filtro != null && filtro.getBike() != null && filtro.getBike().getId() != null){
	    page = repo.findByBike(filtro.getBike(), pageRequest);
	    
	} else if (filtro != null && filtro.getComponent() != null && filtro.getComponent().getId() != null){
	    page = repo.findByComponent(filtro.getComponent(), pageRequest);
	    
	}  else {
	    page = repo.findAll(pageRequest);
	    list = page.getContent();
	}
	list = page.getContent();
	return list;
    }
}
