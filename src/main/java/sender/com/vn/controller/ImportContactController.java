package sender.com.vn.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import sender.com.vn.jobs.ImportContact;
import sender.com.vn.model.ImportContactMap;
import sender.com.vn.model.JobResult;

@Api(tags = "Job API")
@Controller
public class ImportContactController {

//	private JobService jobService;
	private static final Logger logger = LogManager.getLogger(ImportContactController.class);
	
//    public UploadContactController(JobService jobService) {
//        this.jobService = jobService;
//    }
    
    @ApiOperation(value = "Read file contact")
    @PostMapping(path= "/read-contact-data", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JobResult> importContact(@RequestBody ImportContactMap importContactMap) {
        try {
        	JobResult jobResult = new JobResult();
        	/*
        	importContactMap.setLastname(0);
        	importContactMap.setFirstname(1);
        	importContactMap.setEmail(2);
        	importContactMap.setMobile(3);
        	importContactMap.setBirthday(4);
        	importContactMap.setAddress(5);
        	importContactMap.setFullname(6);
        	
        	importContactMap.setContact_group_id(2);
        	importContactMap.setUid(12);
        	*/
//        	importContactMap.setFilepath("/Users/TruongHM/Documents/Website/sender/public_html/files/contact/0415202004493030467.xls");
//        	importContactMap.setFilepath("/Users/TruongHM/Documents/Website/sender/public_html/files/contact/contact-template.xlsx");
//        	importContactMap.setFilepath("/Users/TruongHM/Documents/Website/sender/public_html/files/contact/04152020044240323110.csv");
        	logger.info("Run import contact in file {}",importContactMap.getFilepath());
        	ImportContact importContact =new ImportContact(importContactMap);
        	importContact.start();
        	
        	jobResult.setStatus(1);
            return new ResponseEntity<JobResult>(jobResult, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
