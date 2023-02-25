package co.com.manageliquidation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.com.manageliquidation.Entity.Liquidation;

public interface ILiquidationRepository extends JpaRepository<Liquidation, Integer>{

	@Query("SELECT l FROM Liquidation l WHERE l.idCard = ?1")
	public Liquidation findByIdCard(Long idCard);
	
}
