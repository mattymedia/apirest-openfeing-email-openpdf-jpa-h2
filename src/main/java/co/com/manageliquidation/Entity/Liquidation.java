package co.com.manageliquidation.Entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "liquidations")
public class Liquidation implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "id_card")
	private Long idCard;
	
	@Column(name = "admission_date")
	private LocalDate admissionDate;

	@Column(name = "retirement_date")
	private LocalDate retirementDate;

	@Column(name = "create_at")
	private LocalDate createAt;

	private Double salary = 1160000D;

	private Double layoffs;

	@Column(name = "interest_layoffs")
	private Double interestLayoffs;

	@Column(name = "wage_premium")
	private Double wagePremium;

	@Column(name = "working_holidays")
	private Double workingHolidays;

	@Column(name = "total_liquidation")
	private Double totalLiquidation;
	
	@PrePersist
	public void actualDate() {
		this.createAt = LocalDate.now();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getIdCard() {
		return idCard;
	}

	public void setIdCard(Long idCard) {
		this.idCard = idCard;
	}

	public LocalDate getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(LocalDate admissionDate) {
		this.admissionDate = admissionDate;
	}

	public LocalDate getRetirementDate() {
		return retirementDate;
	}

	public void setRetirementDate(LocalDate retirementDate) {
		this.retirementDate = retirementDate;
	}

	public LocalDate getCreateAt() {
		return createAt;
	}

	public void setCreateAt(LocalDate createAt) {
		this.createAt = createAt;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public Double getLayoffs() {
		return layoffs;
	}

	public void setLayoffs(Double layoffs) {
		this.layoffs = layoffs;
	}

	public Double getInterestLayoffs() {
		return interestLayoffs;
	}

	public void setInterestLayoffs(Double interestLayoffs) {
		this.interestLayoffs = interestLayoffs;
	}

	public Double getWagePremium() {
		return wagePremium;
	}

	public void setWagePremium(Double wagePremium) {
		this.wagePremium = wagePremium;
	}

	public Double getWorkingHolidays() {
		return workingHolidays;
	}

	public void setWorkingHolidays(Double workingHolidays) {
		this.workingHolidays = workingHolidays;
	}

	public Double getTotalLiquidation() {
		return totalLiquidation;
	}

	public void setTotalLiquidation(Double totalLiquidation) {
		this.totalLiquidation = totalLiquidation;
	}
	
	public Integer calculatePeriods() {
		Period calculatePeriod = Period.between(this.admissionDate, this.retirementDate);
		int months = calculatePeriod.getMonths();
		int days = calculatePeriod.getDays();
		int years = calculatePeriod.getYears();
		int totalDays = days + ((months * 30) + years * 365);
		
		return totalDays;
	}
	
	public Double calculateLiquidation() {
		int days = calculatePeriods();
		Double calculate = ((days * this.salary) / 360D);
		this.layoffs = calculate;
		this.wagePremium = calculate;
		this.interestLayoffs = ((layoffs * 0.12) * ( days / 360D));
		this.workingHolidays = (salary * days) / 720D;
		this.totalLiquidation = ((this.layoffs + this.wagePremium) + (this.interestLayoffs + this.workingHolidays));
		return this.totalLiquidation;
	}
	
	
	@Override
	public String toString() {
		return "Liquidation [idCard=" + idCard + ", admissionDate=" + admissionDate + ", retirementDate="
				+ retirementDate + ", salary=" + salary + ", layoffs=" + layoffs + ", interestLayoffs="
				+ interestLayoffs + ", wagePremium=" + wagePremium + ", workingHolidays=" + workingHolidays
				+ ", totalLiquidation=" + totalLiquidation + "]";
	}


	private static final long serialVersionUID = 1L;
		
}
