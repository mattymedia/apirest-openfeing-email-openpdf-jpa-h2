package co.com.manageliquidation.service;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import co.com.manageliquidation.Entity.Liquidation;

public class LiquidationPdfService {

	private Liquidation liquidation;
			
	public LiquidationPdfService(Liquidation liquidation) {
		this.liquidation = liquidation;
	}
	
	public void export(HttpServletResponse response, String fullName) throws DocumentException, IOException {
		
		String URI_FILE = "pdf/user_" + liquidation.getIdCard() + ".pdf";
		DecimalFormat decimal = new DecimalFormat("#.00");
		
		Document document = new Document(PageSize.A4);
		document.setMargins(70, 50, 40, 40);//L,R,T,B
		PdfWriter.getInstance(document, response.getOutputStream());
		PdfWriter.getInstance(document, new FileOutputStream(URI_FILE));
					
		document.open();
		
		Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		Font fontDate = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		Font fontContent = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontTitle.setSize(18);
		fontTitle.setColor(Color.black);
		fontDate.setSize(14);
		fontDate.setColor(Color.black);
		fontContent.setSize(14);
		fontContent.setColor(Color.black);

		Paragraph title = new Paragraph("Liquidation of social benefits", fontTitle);
		Paragraph date = new Paragraph("Date: " + liquidation.getCreateAt(), fontDate);
		
		Paragraph content = new Paragraph(	
				"Employee: " + fullName + "\n"
				+ "Identification card: " + liquidation.getIdCard() + "\n"
				+ "Admission date: " + liquidation.getAdmissionDate() + "\n"
				+ "Retirement date: " + liquidation.getRetirementDate() + "\n"
				+ "Minimum monthly salary: " + decimal.format(liquidation.getSalary()),				
		fontContent);
		
		Paragraph benefits = new Paragraph("Employee benefits: ", fontContent);
		
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100);
		Font cellFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		cellFont.setSize(14);
		cellFont.setColor(Color.black);
		
		PdfPCell cell1 = new PdfPCell(new Phrase("Severance pay: " + decimal.format(liquidation.getLayoffs()), cellFont));
		table.addCell(cell1);
		PdfPCell cell2 = new PdfPCell(new Phrase("Unemployment benefits: " + decimal.format(liquidation.getInterestLayoffs()), cellFont));
		table.addCell(cell2);
		PdfPCell cell3 = new PdfPCell(new Phrase("Retention bonuses: " + decimal.format(liquidation.getWagePremium()), cellFont));
		table.addCell(cell3);
		PdfPCell cell4 = new PdfPCell(new Phrase("Working holidays: " + decimal.format(liquidation.getWorkingHolidays()), cellFont));
		table.addCell(cell4);
		PdfPCell cell5 = new PdfPCell(new Phrase("Total liquidation: " + decimal.format(liquidation.getTotalLiquidation()), cellFont));
		table.addCell(cell5);
		
		Paragraph eula = new Paragraph(	
				"NOTE 1: along with the payment of the next settlement"
				+ " of definitive social benefits, the payment form to the"
				+ " social security system for the last month is delivered. \n\n"
				+ "NOTE 2: With this settlement, the worker expresses that"
				+ " he has received the value of the social benefits in a free, "
				+ "voluntary manner and DECLARES to PAZ Y SALVO for any past, "
				+ "present or future concept derived from the employment relationship "
				+ "that existed between him and the company. company.",			
		fontContent);
		
		Image signature = Image.getInstance("pdf/signature.jpg");

								
		title.setAlignment(Paragraph.ALIGN_CENTER);
		date.setAlignment(Paragraph.ALIGN_RIGHT);
		content.setAlignment(Paragraph.ALIGN_JUSTIFIED);
		eula.setAlignment(Paragraph.ALIGN_JUSTIFIED);

		document.add(date);
		document.add(new Paragraph("\n"));
		document.add(title);
		document.add(new Paragraph("\n"));
		document.add(content);
		document.add(new Paragraph("\n"));
		document.add(benefits);
		document.add(new Paragraph("\n"));
		document.add(table);
		document.add(new Paragraph("\n"));
		document.add(eula);
		document.add(new Paragraph("\n"));
		document.add(new Paragraph("\n"));
		document.add(new Paragraph("\n"));
		document.add(signature);
		document.close();
	}
}
