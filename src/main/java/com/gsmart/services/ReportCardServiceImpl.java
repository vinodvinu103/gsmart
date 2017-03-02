package com.gsmart.services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.event.ChangeListener;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gsmart.dao.ProfileDao;
import com.gsmart.dao.ReportCardDao;
import com.gsmart.model.CompoundReportCard;
import com.gsmart.model.Fee;
import com.gsmart.model.FeeMaster;
import com.gsmart.model.GenerateSalaryStatement;
import com.gsmart.model.Profile;
import com.gsmart.model.ReportCard;
import com.gsmart.model.Token;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;
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
public class ReportCardServiceImpl implements ReportCardService {

	@Autowired
	ReportCardDao reportCardDao;

	@Autowired
	ProfileDao profiledao;

	@Autowired
	FeeServices feeServices;

	@Autowired
	FeeMasterServices feeMasterServices;

	@Autowired
	SearchService searchService;
	
	Document document = new Document(PageSize.A4, 50, 50, 50, 50);
	
	@Override
	public List<ReportCard> reportCardList() throws GSmartServiceException {
		Loggers.loggerStart();
	List<ReportCard> list = null;
		try {
			// list = reportCardDao.reportCardList();

		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		return list;
	}

	@Override
	public CompoundReportCard addReportCard(ReportCard card) throws GSmartServiceException {
		Loggers.loggerStart(card);
		CompoundReportCard card2 = null;
		try {
			card2 = reportCardDao.addReportCard(card);

		} catch (ConstraintViolationException e) {
			throw new GSmartServiceException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());

		}
		Loggers.loggerEnd(card2);
		return card2;
	}

	@Override
	public ReportCard editReportCard(ReportCard card) throws GSmartServiceException {
		Loggers.loggerStart(card);
		ReportCard card2 = null;
		try {
			card2 = reportCardDao.editReportCard(card);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd(card2);
		return card2;
	}

	@Override
	public void deleteReportCard(ReportCard card) throws GSmartServiceException {
		Loggers.loggerStart(card);
		try {
			reportCardDao.deleteReportCard(card);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
	}

	@Override
	public void excelToDB(String smartId, MultipartFile fileUpload) throws Exception {
		reportCardDao.excelToDB(smartId, fileUpload);
	}

	@Override
	public List<ReportCard> search(Token tokenDetail, String academicYear, String examName)
			throws GSmartServiceException {
		Loggers.loggerStart();
		List<ReportCard> card = null;
		try {
			card = reportCardDao.search(tokenDetail, academicYear, examName);
			Loggers.loggerEnd();
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());

		}
		return card;

	}

	@Override
	public void calculatPercentage(String smartId, ArrayList<ReportCard> childReportCards)
			throws GSmartServiceException {
		Double totalMarks = 0.0;
		Double obtainedMarks = 0.0;
		Double percentage = 0.0;
		for (ReportCard reportCard : childReportCards) {
			/*
			 * System.out.println("Child reportcard in service met"+reportCard);
			 */
			totalMarks += reportCard.getMaxMarks();
			obtainedMarks += reportCard.getMarksObtained();
		}
		/*
		 * System.out.println("Total marks"+totalMarks);
		 * System.out.println("Total obtained marks"+obtainedMarks);
		 */
		try {
			percentage = ((obtainedMarks / totalMarks) * 100);
			System.out.println("percentage..........." + percentage);
		} catch (Exception e) {
			percentage = 0.0;
			System.out.println(" in side catch blk percentage..........." + percentage);
		}

	}

	/*@Override
	public List<ReportCard> downloadPdf(Token tokenDetail, String examName, String academicYear)
			throws GSmartServiceException {
		Calendar startMonth = Calendar.getInstance();
		Calendar endMonth = Calendar.getInstance();
		List<ReportCard> list=null;
		*//***********************************************************//*
		try {
			document.open();
			while (startMonth.compareTo(endMonth) <= 0) {
				String month = new SimpleDateFormat("MMMM").format(startMonth.getTime());
				String year = new SimpleDateFormat("yyyy").format(startMonth.getTime());

				list = reportCardDao.search(tokenDetail, academicYear, examName);
				for (ReportCard reportCard : list) {
					String subject=reportCard.getSubject();
					String subjectGrade=reportCard.getSubjectGrade();
					list.add(reportCard);
				}
				try {
					PdfWriter.getInstance(document,
							new FileOutputStream(tokenDetail.getSmartId() + " " + examName + " " + academicYear + ".pdf"));
				} catch (FileNotFoundException | DocumentException e) {
					e.printStackTrace();
				}
				
				//generatePDF(list, examName, academicYear);
				Loggers.loggerValue(academicYear, "pdfgenerated");
				startMonth.add(Calendar.MONTH, 1);
			}
			document.close();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
*/
	@Override
	public List<ReportCard> downloadPdf(Token tokenDetail, String academicYear, String examName)
			throws GSmartServiceException {
		// TODO Auto-generated method stub
		return null;
	}
	public void generatePDF(GenerateSalaryStatement salStmt, String examName, String academicYear) {

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
			Loggers.loggerStart(rect);
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

			Chunk company = new Chunk("Delhi Public School", heading);
			Chunk address1 = new Chunk(" No. 2750 3RD FLOOR E BLOCK", subHeading);
			Chunk address2 = new Chunk("AIRPORT ROAD SAHAKAR NAGAR", subHeading);
			Chunk address3 = new Chunk("BANGALORE-560092", subHeading);

			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);
			table.setWidths(new int[] { 10, 50 });
			System.out.println("table");
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
			Loggers.loggerStart(table);
			Paragraph subject = new Paragraph(" " + examName + " " + academicYear, labels);
			subject.setSpacingBefore(20f);
			subject.setAlignment(Element.ALIGN_CENTER);
			document.add(subject);
			Loggers.loggerStart(subject);
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
		System.out.println("Generated PDF File");
	}

	
}
