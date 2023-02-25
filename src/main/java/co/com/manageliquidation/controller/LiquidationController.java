package co.com.manageliquidation.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.DocumentException;

import co.com.manageliquidation.Entity.Liquidation;
import co.com.manageliquidation.Entity.User;
import co.com.manageliquidation.client.UserOpenFeignClient;
import co.com.manageliquidation.repository.ILiquidationRepository;
import co.com.manageliquidation.service.LiquidacionEmailService;
import co.com.manageliquidation.service.LiquidationPdfService;


@RestController
@RequestMapping("/liquidation")
public class LiquidationController {
	
	@Autowired
	private UserOpenFeignClient usersClient;
	
	@Autowired
	private ILiquidationRepository liquidationRepository;
		
	@GetMapping("/show-employees")
	public List<User> showAllUsers(){
		return usersClient.getAllUsers();
	}
		
	@PostMapping("/create-liquidation/{idCard}")
	public ResponseEntity<Object> generateLiquidation(@PathVariable Long idCard, @RequestBody Liquidation liquidation) {
		User userByIdCard = usersClient.getAllUsers()
				.stream()
				.filter(dataStream -> dataStream.getIdCard().equals(idCard))
				.findFirst()
				.orElse(null);	
		
		liquidation.setIdCard(userByIdCard.getIdCard());
		
        Map<String, Object> map = new HashMap<String, Object>();
        
        map.put("Employee", userByIdCard.toString());
        map.put("Total_days", liquidation.calculatePeriods());
		map.put("Total_liquidation", liquidation.calculateLiquidation());
		
		liquidationRepository.save(liquidation);
		
		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}
	
	@GetMapping("/show-all-liquidations")
	public List<Liquidation> findAll(){
		return liquidationRepository.findAll();
	}
	
	@GetMapping("/generate-pdf/{idCard}")
	public String generatePdf(@PathVariable Long idCard, HttpServletResponse response,
			LiquidacionEmailService emailService) throws DocumentException, IOException {
		User userByIdCard = usersClient.getAllUsers()
				.stream()
				.filter(dataStream -> dataStream.getIdCard().equals(idCard))
				.findFirst()
				.orElse(null);
		
		String fullName = userByIdCard.getFirstName() + " " + userByIdCard.getLastName();
		
		response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=user_" + idCard + ".pdf";
               
        response.setHeader(headerKey, headerValue);
        
        Liquidation  liquidation = liquidationRepository.findByIdCard(idCard);
        
        LiquidationPdfService createPdf = new LiquidationPdfService(liquidation);
        createPdf.export(response, fullName);
        emailService.sendEmail(idCard);
        
        return "pdf generated correctly, a copy has been sent to the corporate mail.";
	}
	
}
