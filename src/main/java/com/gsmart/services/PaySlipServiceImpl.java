package com.gsmart.services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.dao.PaySlipDAO;
import com.gsmart.model.GenerateSalaryStatement;
import com.gsmart.model.PaySlip;
import com.gsmart.model.Profile;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
@Transactional
public class PaySlipServiceImpl implements PaySlipService {

	@Autowired
	private PaySlipDAO paySlipDAO;

	Logger logger = Logger.getLogger(PaySlipServiceImpl.class);
	Document document = new Document(PageSize.A4, 50, 50, 50, 50);

	@Override
	public void download(PaySlip paySlip) {

		Calendar startMonth = Calendar.getInstance();
		Calendar endMonth = Calendar.getInstance();

		/***********************************************************/
		logger.info("Generating PDF File");
		try {
			document.open();

			startMonth.setTime(
					new SimpleDateFormat("MMMM/yyyy").parse(paySlip.getFromMonth() + "/" + paySlip.getFromYear()));
			endMonth.setTime(new SimpleDateFormat("MMMM/yyyy").parse(paySlip.getToMonth() + "/" + paySlip.getToYear()));
			logger.info("tada");
			while (startMonth.compareTo(endMonth) <= 0) {
				String month = new SimpleDateFormat("MMMM").format(startMonth.getTime());
				String year = new SimpleDateFormat("yyyy").format(startMonth.getTime());

				paySlip.setFromMonth(month);
				paySlip.setFromYear(year);

				GenerateSalaryStatement salStmt = paySlipDAO.download(paySlip);
				logger.info(paySlip);
				try {
					PdfWriter.getInstance(document,
							new FileOutputStream(salStmt.getSmartId() + " " + month + " " + year + ".pdf"));
					logger.info("before catch");
				} catch (FileNotFoundException | DocumentException e) {
					e.printStackTrace();
				}
				logger.info(salStmt);
				logger.info(month);
				logger.info(year);
				generatePDF(salStmt, month, year);
				logger.info("pdfgenerated");
				startMonth.add(Calendar.MONTH, 1);
				logger.info(startMonth);
			}
			document.close();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public void generatePDF(GenerateSalaryStatement salStmt, String month, String year) {

		// Image image = null;

		try {

			/*
			 * try { image =
			 * Image.getInstance("/home/gtpl/Desktop/gowdanar.png");
			 * image.scaleAbsolute(70f, 70f); } catch (BadElementException |
			 * IOException e1) { e1.printStackTrace(); }
			 */

			Rectangle rect = new Rectangle(575, 825, 20, 20);

			rect.setBorder(Rectangle.BOX);
			rect.setBorderWidth(0.5f);
			logger.info(rect);
			try {
				document.add(rect);
			} catch (DocumentException e2) {
				e2.printStackTrace();
			}

			Font heading = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
			Font subHeading = new Font(Font.FontFamily.TIMES_ROMAN, 8);
			Font labels = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
			// Font labelsWithUDL = new Font(Font.FontFamily.TIMES_ROMAN, 14,
			// Font.BOLD | Font.UNDERLINE);
			Font contents = new Font(Font.FontFamily.TIMES_ROMAN, 11);

			Chunk company = new Chunk("Gowdanar Technologies", heading);
			Chunk address1 = new Chunk(" No. 2750 3RD FLOOR E BLOCK", subHeading);
			Chunk address2 = new Chunk("AIRPORT ROAD SAHAKAR NAGAR", subHeading);
			Chunk address3 = new Chunk("BANGALORE-560092", subHeading);

			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);
			table.setWidths(new int[] { 10, 50 });
			logger.info("table");
			/*
			 * PdfPCell imgCell = new PdfPCell(image, false);
			 * imgCell.setPadding(5f); imgCell.setBorder(Rectangle.LEFT |
			 * Rectangle.TOP | Rectangle.BOTTOM); table.addCell(imgCell);
			 */

			PdfPCell textCell = new PdfPCell();
			textCell.addElement(company);
			textCell.addElement(address1);
			textCell.addElement(address2);
			textCell.addElement(address3);
			textCell.setBorder(Rectangle.RIGHT | Rectangle.TOP | Rectangle.BOTTOM);
			table.addCell(textCell);
			document.add(table);
			logger.info(table);
			Paragraph subject = new Paragraph("Payslip for the month of " + month + " " + year, labels);
			subject.setSpacingBefore(20f);
			subject.setAlignment(Element.ALIGN_CENTER);
			document.add(subject);
			logger.info(subject);
			Paragraph gap = new Paragraph();
			gap.setSpacingAfter(25f);
			document.add(gap);

			PdfPTable empDetails = new PdfPTable(4);

			PdfPCell cell1 = new PdfPCell(new Paragraph("Employee Code", labels));
			PdfPCell cell2 = new PdfPCell(new Paragraph(salStmt.getSmartId().toString(), contents));
			PdfPCell cell3 = new PdfPCell(new Paragraph("Employee Name", labels));
			PdfPCell cell4 = new PdfPCell(new Paragraph(salStmt.getEmpName(), contents));
			PdfPCell cell5 = new PdfPCell(new Paragraph("Designation", labels));
			PdfPCell cell6 = new PdfPCell(new Paragraph(salStmt.getEmpDesignation(), contents));
			PdfPCell cell7 = new PdfPCell(new Paragraph("Role", labels));
			PdfPCell cell8 = new PdfPCell(new Paragraph(salStmt.getEmpRole(), contents));
			PdfPCell cell9 = new PdfPCell(new Paragraph("Location", labels));
			PdfPCell cell10 = new PdfPCell(new Paragraph("Bangalore", contents));
			PdfPCell cell11 = new PdfPCell(new Paragraph("Bank Name", labels));
			PdfPCell cell12 = new PdfPCell(new Paragraph("SBI", contents));
			PdfPCell cell13 = new PdfPCell(new Paragraph("Date Of Birth", labels));
			PdfPCell cell14 = new PdfPCell(new Paragraph("12/07/1990", contents));
			PdfPCell cell15 = new PdfPCell(new Paragraph("Account Number", labels));
			PdfPCell cell16 = new PdfPCell(new Paragraph("1234567890", contents));
			PdfPCell cell17 = new PdfPCell(new Paragraph("Date Of Joining", labels));
			PdfPCell cell18 = new PdfPCell(new Paragraph("12/03/2016", contents));
			PdfPCell cell19 = new PdfPCell(new Paragraph("PAN Number", labels));
			PdfPCell cell20 = new PdfPCell(new Paragraph("GOPX2119D", contents));

			cell1.setPadding(5f);
			cell2.setPadding(5f);
			cell3.setPadding(5f);
			cell4.setPadding(5f);
			cell5.setPadding(5f);
			cell6.setPadding(5f);
			cell7.setPadding(5f);
			cell8.setPadding(5f);
			cell9.setPadding(5f);
			cell10.setPadding(5f);
			cell11.setPadding(5f);
			cell12.setPadding(5f);
			cell13.setPadding(5f);
			cell14.setPadding(5f);
			cell15.setPadding(5f);
			cell16.setPadding(5f);
			cell17.setPadding(5f);
			cell18.setPadding(5f);
			cell19.setPadding(5f);
			cell20.setPadding(5f);

			empDetails.addCell(cell1);
			empDetails.addCell(cell2);
			empDetails.addCell(cell3);
			empDetails.addCell(cell4);
			empDetails.addCell(cell5);
			empDetails.addCell(cell6);
			empDetails.addCell(cell7);
			empDetails.addCell(cell8);
			empDetails.addCell(cell9);
			empDetails.addCell(cell10);
			empDetails.addCell(cell11);
			empDetails.addCell(cell12);
			empDetails.addCell(cell13);
			empDetails.addCell(cell14);
			empDetails.addCell(cell15);
			empDetails.addCell(cell16);
			empDetails.addCell(cell17);
			empDetails.addCell(cell18);
			empDetails.addCell(cell19);
			empDetails.addCell(cell20);

			document.add(empDetails);

			document.add(gap);

			PdfPTable salDetails = new PdfPTable(4);

			// salDetails.setWidthPercentage(10f);

			PdfPCell scell1 = new PdfPCell(new Paragraph("Earnings", labels));
			PdfPCell scell2 = new PdfPCell(new Paragraph("Amount", labels));
			PdfPCell scell3 = new PdfPCell(new Paragraph("Deductions", labels));
			PdfPCell scell4 = new PdfPCell(new Paragraph("Amount", labels));
			PdfPCell scell5 = new PdfPCell(new Paragraph("Basic", labels));
			PdfPCell scell6 = new PdfPCell(new Paragraph(salStmt.getBasicSalary().toString(), contents));
			PdfPCell scell7 = new PdfPCell(new Paragraph("Provident Fund", labels));
			PdfPCell scell8 = new PdfPCell(new Paragraph(salStmt.getPf().toString(), contents));
			PdfPCell scell9 = new PdfPCell(new Paragraph("HRA", labels));
			PdfPCell scell10 = new PdfPCell(new Paragraph(salStmt.getHra().toString(), contents));
			PdfPCell scell11 = new PdfPCell(new Paragraph("Professional Tax", labels));
			PdfPCell scell12 = new PdfPCell(new Paragraph(salStmt.getPt().toString(), contents));
			PdfPCell scell13 = new PdfPCell(new Paragraph("Conveyance", labels));
			PdfPCell scell14 = new PdfPCell(new Paragraph(salStmt.getConveyance().toString(), contents));
			PdfPCell scell15 = new PdfPCell(new Paragraph("Income Tax", labels));
			PdfPCell scell16 = new PdfPCell(new Paragraph(salStmt.getIt().toString(), contents));
			PdfPCell scell17 = new PdfPCell(new Paragraph("Special Allowance", labels));
			PdfPCell scell18 = new PdfPCell(new Paragraph(salStmt.getSpecialAllowance().toString(), contents));
			PdfPCell scell19 = new PdfPCell(new Paragraph(""));
			PdfPCell scell20 = new PdfPCell(new Paragraph(""));
			PdfPCell scell21 = new PdfPCell(new Paragraph("Total", labels));
			PdfPCell scell22 = new PdfPCell(new Paragraph(salStmt.getActualSalary().toString(), contents));
			PdfPCell scell23 = new PdfPCell(new Paragraph("Total Deduction", labels));
			PdfPCell scell24 = new PdfPCell(new Paragraph(salStmt.getDeductedSalary().toString(), contents));
			PdfPCell scell25 = new PdfPCell(new Paragraph("Net Pay", labels));
			PdfPCell scell26 = new PdfPCell(new Paragraph(salStmt.getPayable().toString()));
			PdfPCell scell27 = new PdfPCell(new Paragraph("In words: xxx only"));

			scell26.setColspan(3);
			scell27.setColspan(4);

			scell1.setPadding(5f);
			scell2.setPadding(5f);
			scell3.setPadding(5f);
			scell4.setPadding(5f);
			scell5.setPadding(5f);
			scell6.setPadding(5f);
			scell7.setPadding(5f);
			scell8.setPadding(5f);
			scell9.setPadding(5f);
			scell10.setPadding(5f);
			scell11.setPadding(5f);
			scell12.setPadding(5f);
			scell13.setPadding(5f);
			scell14.setPadding(5f);
			scell15.setPadding(5f);
			scell16.setPadding(5f);
			scell17.setPadding(5f);
			scell18.setPadding(5f);
			scell19.setPadding(5f);
			scell20.setPadding(5f);
			scell21.setPadding(5f);
			scell22.setPadding(5f);
			scell23.setPadding(5f);
			scell24.setPadding(5f);
			scell25.setPadding(5f);
			scell26.setPadding(5f);
			scell27.setPadding(5f);

			salDetails.addCell(scell1);
			salDetails.addCell(scell2);
			salDetails.addCell(scell3);
			salDetails.addCell(scell4);
			salDetails.addCell(scell5);
			salDetails.addCell(scell6);
			salDetails.addCell(scell7);
			salDetails.addCell(scell8);
			salDetails.addCell(scell9);
			salDetails.addCell(scell10);
			salDetails.addCell(scell11);
			salDetails.addCell(scell12);
			salDetails.addCell(scell13);
			salDetails.addCell(scell14);
			salDetails.addCell(scell15);
			salDetails.addCell(scell16);
			salDetails.addCell(scell17);
			salDetails.addCell(scell18);
			salDetails.addCell(scell19);
			salDetails.addCell(scell20);
			salDetails.addCell(scell21);
			salDetails.addCell(scell22);
			salDetails.addCell(scell23);
			salDetails.addCell(scell24);
			salDetails.addCell(scell25);
			salDetails.addCell(scell26);
			salDetails.addCell(scell27);

			document.add(salDetails);

		} catch (DocumentException e) {
			e.printStackTrace();
		}
		/***********************************************************/
		logger.info("Generated PDF File");
	}

	@Override
	public void adminDownload(PaySlip paySlip) {

		List<GenerateSalaryStatement> stmt = paySlipDAO.adminDownload(paySlip);

		try {
			document.open();
			PdfWriter.getInstance(document,
					new FileOutputStream(paySlip.getFromMonth() + " " + paySlip.getFromYear() + ".pdf"));
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}

		try {

			for (GenerateSalaryStatement generateSalaryStatement : stmt) {

				generatePDF(generateSalaryStatement, paySlip.getFromMonth(), paySlip.getFromYear());
				document.newPage();
				document.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* sendEmail By Soumen */
	@Override
	public void sendEmail(PaySlip paySlip) {

		List<Profile> list = paySlipDAO.emailAddress(paySlip);
		String id = list.get(0).getEmailId();
		logger.info(id);
		final String username = "soumendey1991@gmail.com";
		final String password = "=======================";

		Properties props = new Properties();
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("soumendey1991@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(id));
			message.setSubject("Testing Subject");
			message.setText("PFA");

			MimeBodyPart messageBodyPart = new MimeBodyPart();

			Multipart multipart = new MimeMultipart();

			messageBodyPart = new MimeBodyPart();
			String file = "/home/gtpl103/" + paySlip.getSmartId() + " " + paySlip.getFromMonth() + " "
					+ paySlip.getFromYear() + ".pdf";
			String fileName = paySlip.getSmartId() + " " + paySlip.getFromMonth() + " " + paySlip.getFromYear()
					+ ".pdf";
			DataSource source = new FileDataSource(file);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fileName);
			multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);

			logger.info("Sending");

			Transport.send(message);

			logger.info("Done");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
