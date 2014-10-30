/**
 * 
 */
package an.dpr.manteniket.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.domain.Component;
import an.dpr.manteniket.domain.ComponentUse;

/**
 * Repository of ComponentUse data
 * 
 * @author rsaez
 * 
 */
public interface ComponentUsesRepository extends
	CrudRepository<ComponentUse, Long> {

    @Query("FROM ComponentUse cu WHERE cu.bike=:bike")
    List<ComponentUse> findByBike(@Param("bike")Bici bike);
    List<ComponentUse> findByBike(Bici bike, Sort sort);
    Page<ComponentUse> findByBike(Bici bike, Pageable pageable);

    @Query("FROM ComponentUse cu WHERE cu.component=:component")
    List<ComponentUse> findByComponent(@Param("component") Component component);
    List<ComponentUse> findByComponent(Component component, Sort sort);
    Page<ComponentUse> findByComponent(Component component, Pageable pageable);
//    
    @Query("FROM ComponentUse cu WHERE cu.init <= :date AND cu.finish >= :date")
    public List<ComponentUse> findByDate(@Param("date")Date date);

    @Query("select count(cu) FROM ComponentUse cu WHERE cu.bike=:bike")
    long countByBike(@Param("bike")Bici bike);
    @Query("select count(cu) FROM ComponentUse cu WHERE cu.component=:component")
    long countByComponent(Component component);
    
    List<ComponentUse> findAll();
    List<ComponentUse> findAll(Sort sort);
    Page<ComponentUse> findAll(Pageable pageable);

}
