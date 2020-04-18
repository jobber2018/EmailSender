package sender.com.vn.jobs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import sender.com.vn.common.Common;
import sender.com.vn.common.Db;
import sender.com.vn.common.Define;
import sender.com.vn.model.Contact;
import sender.com.vn.model.EmailTemplate;
import sender.com.vn.model.ImportContactMap;
import sender.com.vn.model.MailNotify;
import sender.com.vn.model.User;
import sender.com.vn.service.ContactService;
import sender.com.vn.service.EmailTemplateService;
import sender.com.vn.service.SendMailNotify;
import sender.com.vn.service.UserService;

public class ImportContact extends Thread{
	private static final Logger logger = LogManager.getLogger(ImportContact.class);
	
	private Thread t;
	
	private ImportContactMap importContactMap;
    private String threadName ="importContact";
    private Db db = new Db();
    int totalContactInFile=0;
	int totalImportContactFail=0;
	String newFilepath;
	
    public ImportContact (ImportContactMap importContactMap) {
    	this.importContactMap = importContactMap;
    }
    
    public void start() {
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
    
    @Override
    public void run() {
        try {
        	/*
        	logger.info("Email: {} - Firstname: {} - Lastname: {} - Filepath: {} - Contact group: {}"
        			,importContactMap.getEmail()
        			,importContactMap.getFirstname()
        			,importContactMap.getLastname()
        			,importContactMap.getFilepath()
        			,importContactMap.getContact_group_id()
        			);*/
        	String filepath = importContactMap.getFilepath();
        	String filename = filepath.substring(filepath.lastIndexOf("/")+1);
    		String fileExtension = filename.substring(filename.lastIndexOf(".")+1).toLowerCase();
    		newFilepath = filepath.substring(0,filepath.lastIndexOf("/")).substring(0,filepath.substring(0,filepath.lastIndexOf("/")).lastIndexOf("/")+1)+"contact.bak/"+filename;
    		
    		if(fileExtension.equals("xls"))
    			readFileXls();
    		else if(fileExtension.equals("xlsx"))
    			readFileXlsx();
    		else if(fileExtension.equals("csv"))
    			readFileCsv();
    		
    		if(totalContactInFile > 0) {
    			//mail notification active campaign
				MailNotify mailNotify = new MailNotify();
				EmailTemplateService emailTemplateService=new EmailTemplateService(db);
				EmailTemplate emailTemplate = emailTemplateService.getByKey("import-contact-completed");
				
				UserService userService = new UserService(db);
				User user = userService.getById(importContactMap.getUid());
				
				mailNotify.setMailTo(user.getEmail());
				mailNotify.setMailBCC(Define.MAIL_BCC_DEFAULT);
				mailNotify.setMailSubject(emailTemplate.getSubject());

		    	HashMap<String, Object> scopes = new HashMap<String, Object>();
		        scopes.put("firstname", user.getFirstname());
		        scopes.put("total_contact_fail", Common.formatMoney(totalImportContactFail));
		        scopes.put("total_contact_import", Common.formatMoney(totalContactInFile));
		        
		        mailNotify.setScopes(scopes);
		        
		        String mailContent = emailTemplate.getContent();
		        mailNotify.setMailContent(mailContent);
		        
				SendMailNotify sendMail = new SendMailNotify(mailNotify);
				sendMail.start();
    		}
    		
        } catch (Exception e) {
			logger.error("import file error "+e.getMessage());
		}
    }
    private void readFileCsv() {
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(importContactMap.getFilepath()));

            while ((line = br.readLine()) != null) {
            	Contact contact = new Contact();
                String[] data = line.split(cvsSplitBy);
                
                for (int i = 0; i < data.length; i++) {
                	contact = buildContactData(contact,i,data[i]);
                }
                //save data
                if(!saveContact(contact)) {
                	totalImportContactFail++;
                	System.out.println("==Row err===: "+ totalContactInFile + "====================");
                }
                totalContactInFile++;
            }
            System.out.println("Total: "+ totalContactInFile + " fail: "+ totalImportContactFail);
            Files.move(Paths.get(this.importContactMap.getFilepath()), Paths.get(newFilepath));
            
        } catch (FileNotFoundException e) {
        	logger.error(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
        	logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void readFileXls() {
    	// Đọc một file.
        try {
			FileInputStream inputStream = new FileInputStream(new File(importContactMap.getFilepath()));
			
			// Đối tượng workbook cho file XSL.
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            // Lấy ra sheet đầu tiên từ workbook
            HSSFSheet sheet = workbook.getSheetAt(0);
            
            Iterator<Row> rowIterator = sheet.iterator();
        	
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if(isRowEmpty(row)) {
                	continue;
                }
                if(!getExcelRowData(row)) {
                	totalImportContactFail++;
                	System.out.println("==Row err===: "+ row.getRowNum() + "====================");
                }
                totalContactInFile++;
            }
            System.out.println("Total: "+ totalContactInFile + " fail: "+ totalImportContactFail);
            
            Files.move(Paths.get(this.importContactMap.getFilepath()), Paths.get(newFilepath));
                    
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
    }
    
    private void readFileXlsx() {
    	// Đọc một file.
        try {
			FileInputStream inputStream = new FileInputStream(new File(importContactMap.getFilepath()));
			// Finds the workbook instance for XLSX file 
            XSSFWorkbook workbook = new XSSFWorkbook (inputStream); 
            // Return first sheet from the XLSX workbook 
            XSSFSheet sheet = workbook.getSheetAt(0);
            
            Iterator<Row> rowIterator = sheet.iterator();
        	
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if(isRowEmpty(row)) {
                	continue;
                }
                if(!getExcelRowData(row)) {
                	totalImportContactFail++;
                	System.out.println("==Row err===: "+ row.getRowNum() + "=======================================");
                }
                totalContactInFile++;
            }
            System.out.println("Total: "+ totalContactInFile + " fail: "+ totalImportContactFail);
            Files.move(Paths.get(this.importContactMap.getFilepath()), Paths.get(newFilepath));
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
    }
    
    private boolean getExcelRowData(Row row) {
    	try {
    		// Lấy Iterator cho tất cả các cell của dòng hiện tại.
            Iterator<Cell> cellIterator = row.cellIterator();
            Contact contact = new Contact();
            
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                
                Integer columnIndex = cell.getColumnIndex();
                String value = getExcelCellValue(cell);
                
                contact = buildContactData(contact,columnIndex,value);
            }
            return saveContact(contact);
    	}catch (Exception e) {
			return false;
		}
    }
    
    private boolean saveContact(Contact contact) {
    	try {
    		if(Common.isEmail(contact.getEmail())) {
            	ContactService contactService = new ContactService(db);
            	Contact contactDb = contactService.getByEmail(contact.getEmail());
            	
            	if(contactDb.getEmail() != null) {
            		if(contactDb.getAeg()==null || contactDb.getAeg()!=null) 
            			contactDb.setAeg(contact.getAeg());
            		if(contactDb.getAddress()==null && contact.getAddress()!=null) 
            			contactDb.setAddress(contact.getAddress());
            		if(contactDb.getFullname()==null && contact.getFullname()!=null) 
            			contactDb.setFullname(contact.getFullname());
            		if(contactDb.getLastname()==null && contact.getLastname()!=null) 
            			contactDb.setLastname(contact.getLastname());
            		if(contactDb.getFirstname()==null && contact.getFirstname()!=null) 
            			contactDb.setFirstname(contact.getFirstname());
            		if(contactDb.getGender()==null && contact.getGender()!=null) 
            			contactDb.setGender(contact.getGender());
            		if(contactDb.getBirthday()==null && contact.getBirthday()!=null) 
            			contactDb.setBirthday(contact.getBirthday());
            		contactService.update(contactDb);
            	}else {
            		contact.setCreated_by(this.importContactMap.getUid());
            		contactService.insert(contact);
            		contactDb = contactService.getByEmail(contact.getEmail());
            	}
            	
            	contactService.insertContactGroup(contactDb.getId(),importContactMap.getContact_group_id());
            	
            	return true;
            }
    		return false;
    	}catch (Exception e) {
			return false;
		}
    }
    
    private Contact buildContactData(Contact contact, Integer columnIndex, String value) {
        
        if(columnIndex==importContactMap.getEmail()) {
        	contact.setEmail(value);
        }else if(columnIndex==importContactMap.getFirstname()) {
        	contact.setFirstname(value);
        }else if(columnIndex==importContactMap.getLastname()) {
        	contact.setLastname(value);
        }else if(columnIndex==importContactMap.getMobile()) {
        	contact.setMobile(value);
        }else if(columnIndex==importContactMap.getBirthday()) {
        	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date = formatter.parse(value);
                formatter = new SimpleDateFormat("MM-dd-yyyy");
                String strDate = formatter.format(date);
                contact.setBirthday(strDate);
            } catch (ParseException e) {
            	contact.setBirthday(null);
            }
        }else if(columnIndex==importContactMap.getAddress()) {
        	contact.setAddress(value);
        }else if(columnIndex==importContactMap.getAeg()) {
        	contact.setAeg(value);
        }else if(columnIndex==importContactMap.getGender()) {
        	contact.setGender(value);
        }else if(columnIndex==importContactMap.getFullname()) {
        	contact.setFullname(value);
        }
        
        return contact;
    }
    
    private String getExcelCellValue(Cell cell) {
    	// Đổi thành getCellType() nếu sử dụng POI 4.x
        CellType cellType = cell.getCellTypeEnum();
        String value="";
    	switch (cellType) {
	        case _NONE:
	            break;
	        case BOOLEAN:
	        	value = String.valueOf(cell.getBooleanCellValue());
	            break;
	        case BLANK:
//	            System.out.print("");
//	            System.out.print("\t");
	            break;
	        case FORMULA:
	            // Công thức
//	            System.out.print(cell.getCellFormula());
//	            System.out.print("\t");
	             
//	            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	  
	            // In ra giá trị từ công thức
//	            System.out.print(evaluator.evaluate(cell).getNumberValue());
	            break;
	        case NUMERIC:
	        	value = String.valueOf(cell.getNumericCellValue());
//	            System.out.print(cell.getNumericCellValue());
//	            System.out.print("\t");
	            break;
	        case STRING:
	        	value = cell.getStringCellValue();
//	            System.out.print(cell.getStringCellValue());
//	            System.out.print("\t");
	            break;
	        case ERROR:
//	        	value = cell.getStringCellValue();
//	            System.out.print("!");
//	            System.out.print("\t");
	            break;
        }
    	return value.trim();
    }
    
    private boolean isRowEmpty(Row row) {
		boolean isEmpty = true;
		DataFormatter dataFormatter = new DataFormatter();

		if (row != null) {
			for (Cell cell : row) {
				if (dataFormatter.formatCellValue(cell).trim().length() > 0) {
					isEmpty = false;
					break;
				}
			}
		}

		return isEmpty;
	}
}
