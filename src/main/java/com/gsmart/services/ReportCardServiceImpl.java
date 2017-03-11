package com.gsmart.services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gsmart.dao.ProfileDao;
import com.gsmart.dao.ReportCardDao;
import com.gsmart.model.CompoundReportCard;
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
	public String calculatPercentage(String smartId, List<ReportCard> childReportCards)
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
		return ""+percentage;
	}

	@Override
	public Document downloadPdf(Token tokenDetail, String academicYear, String examName)
			throws GSmartServiceException {
		Calendar startMonth = Calendar.getInstance();
		Calendar endMonth = Calendar.getInstance();
		List<ReportCard> list=null;
		document.open();
		try {
			
			while (startMonth.compareTo(endMonth) <= 0) {

				list = reportCardDao.search(tokenDetail, academicYear, examName);
				try {
					PdfWriter.getInstance(document,
							new FileOutputStream(tokenDetail.getSmartId() + " " + examName + " " + academicYear + ".pdf"));
				} catch (FileNotFoundException | DocumentException e) {
					e.printStackTrace();
				}
				
				document=generatePDF(list, examName, academicYear);
				Loggers.loggerValue(academicYear, "pdfgenerated");
				startMonth.add(Calendar.MONTH, 1);
			}
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;

	}

	public Document generatePDF(List<ReportCard> card, String examName, String academicYear) throws GSmartServiceException {

		// Image image = null;

		try {
			document.open();
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

			PdfPTable empDetails = new PdfPTable(3);

			PdfPCell cell1 = new PdfPCell(new Paragraph("Smart Id", labels));
			PdfPCell cell2 = new PdfPCell(new Paragraph(card.get(0).getSmartId(), contents));
			System.out.println("smart id of student "+card.get(0).getSmartId());
			PdfPCell cell3 = new PdfPCell(new Paragraph("Student Name", labels));
			PdfPCell cell4 = new PdfPCell(new Paragraph(card.get(0).getStudentName(), contents));
			PdfPCell cell5 = new PdfPCell(new Paragraph("Exam Name", labels));
			PdfPCell cell6 = new PdfPCell(new Paragraph(card.get(0).getExamName(), contents));
			

			cell1.setPadding(5f);
			cell2.setPadding(5f);
			cell3.setPadding(5f);
			cell4.setPadding(5f);
			cell5.setPadding(5f);
			cell6.setPadding(5f);

			empDetails.addCell(cell1);
			empDetails.addCell(cell2);
			empDetails.addCell(cell3);
			empDetails.addCell(cell4);
			empDetails.addCell(cell5);
			//empDetails.addCell(cell6);

			document.add(empDetails);

			document.add(gap);

			PdfPTable salDetails = new PdfPTable(3);

			// salDetails.setWidthPercentage(10f);

			PdfPCell scell1 = new PdfPCell(new Paragraph("S.No.", labels));
			PdfPCell scell2 = new PdfPCell(new Paragraph("SUBJECTS", labels));
			PdfPCell scell3 = new PdfPCell(new Paragraph(examName+" Grade", labels));

			scell1.setPadding(2f);
			scell2.setPadding(5f);
			scell3.setPadding(3f);
		
			salDetails.addCell(scell1);
			salDetails.addCell(scell2);
			salDetails.addCell(scell3);
			for (ReportCard reportcard : card) {
				int i=1;
				String sno=""+i;
				salDetails.addCell(sno);
				salDetails.addCell(reportcard.getSubject());
				salDetails.addCell(reportcard.getSubjectGrade());
				i++;
			}
			salDetails.addCell("TOTAL PERCENTAGE % IS "+calculatPercentage("", card));
			document.add(salDetails);
			System.out.println("***************** Generate PDF **************** ");
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}
	
}
