package sender.com.vn.controller;

import static javax.servlet.http.HttpServletResponse.SC_ACCEPTED;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sender.com.vn.model.JobResult;
import sender.com.vn.service.JobService;

@Api(tags = "Job API")
@Controller
public class JobController {

	private JobService jobService;
	private static final Logger logger = LogManager.getLogger(JobController.class);
	
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }
    
    @ApiOperation(value = "Create job send all mail")
    @ApiResponses(value = { @ApiResponse(code = SC_ACCEPTED, message = "accepted"), @ApiResponse(code = SC_BAD_REQUEST, message = "An unexpected error occurred") })
    @GetMapping(value = "/run-campaign")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<JobResult> createJobRunCampaign(@RequestParam Integer id) {
        try {
        	logger.info("Run campaign id {}",id);
            return new ResponseEntity<JobResult>(jobService.runCampaign(id), HttpStatus.ACCEPTED);
        } catch (SchedulerException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @ApiOperation(value = "Create job re-run node")
    @ApiResponses(value = { @ApiResponse(code = SC_ACCEPTED, message = "accepted"), @ApiResponse(code = SC_BAD_REQUEST, message = "An unexpected error occurred") })
    @GetMapping(value = "/re-run-node")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<JobResult> createJobReRunNode() {
        logger.info("Re-Run node id");
		return new ResponseEntity<JobResult>(jobService.reRunNode(), HttpStatus.ACCEPTED);
    }
    
    @ApiOperation(value = "User email read")
    @ApiResponses(value = { @ApiResponse(code = SC_ACCEPTED, message = "accepted"), @ApiResponse(code = SC_BAD_REQUEST, message = "An unexpected error occurred") })
    @GetMapping(value = "/email-read")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<JobResult> createJobEmailRead(@RequestParam Integer id) {
        try {
        	logger.info("Run open email id {}",id);
            return new ResponseEntity<JobResult>(jobService.emailRead(id), HttpStatus.ACCEPTED);
        } catch (SchedulerException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @ApiOperation(value = "User email link lick")
    @ApiResponses(value = { @ApiResponse(code = SC_ACCEPTED, message = "accepted"), @ApiResponse(code = SC_BAD_REQUEST, message = "An unexpected error occurred") })
    @GetMapping(value = "/email-link-click")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<JobResult> createJobEmailLinkClick(@RequestParam Integer id, @RequestParam String uri) {
        try {
        	logger.info("Run email link lick id {} uri {}",id,uri);
            return new ResponseEntity<JobResult>(jobService.emailLinkClick(id,uri), HttpStatus.ACCEPTED);
        } catch (SchedulerException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
