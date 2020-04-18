package sender.com.vn.controller;

import static javax.servlet.http.HttpServletResponse.SC_ACCEPTED;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sender.com.vn.model.QuartzInformation;
import sender.com.vn.model.QuartzJobDetail;
import sender.com.vn.model.QuartzResponse;
import sender.com.vn.service.QuartzService;

@Api(tags = "Scheduler API")
@Controller
@CrossOrigin
@RequestMapping(value = "/quartz/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class QuartzController {
	private QuartzService schedulerService;
	private static final Logger logger = LogManager.getLogger(QuartzController.class);
	
    public QuartzController(QuartzService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @ApiOperation(value = "Retrieves general information about the Quartz scheduler")
    @ApiResponses(value = { @ApiResponse(code = SC_OK, message = "ok"), @ApiResponse(code = SC_BAD_REQUEST, message = "An unexpected error occurred") })
    @GetMapping(value = "information")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<QuartzInformation> getSchedulerInformation() {
        try {
        	logger.debug("information");
            return new ResponseEntity<>(schedulerService.getSchedulerInformation(), HttpStatus.OK);
        } catch (SchedulerException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @ApiOperation(value = "Retrieves job key information from the Quartz scheduler")
    @ApiResponses(value = { @ApiResponse(code = SC_OK, message = "ok"), @ApiResponse(code = SC_BAD_REQUEST, message = "An unexpected error occurred") })
    @GetMapping(value = "jobKeys")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<JobKey>> getJobKeys() {
        try {
            return new ResponseEntity<>(schedulerService.getJobKeys(), HttpStatus.OK);
        } catch (SchedulerException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "For a given name and group, returns job details from the Quartz scheduler")
    @ApiResponses(value = { @ApiResponse(code = SC_OK, message = "ok"), @ApiResponse(code = SC_BAD_REQUEST, message = "An unexpected error occurred") })
    @GetMapping(value = "jobDetail")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<QuartzJobDetail> getJobDetail(@RequestParam String name, @RequestParam String group) {
        try {
            return new ResponseEntity<>(schedulerService.getJobDetail(name, group), HttpStatus.OK);
        } catch (SchedulerException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "For a given name and group, deletes the job/trigger(s) from the Quartz scheduler")
    @ApiResponses(value = { @ApiResponse(code = SC_ACCEPTED, message = "accepted"), @ApiResponse(code = SC_BAD_REQUEST, message = "An unexpected error occurred") })
    @DeleteMapping(value = "deleteJob")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<QuartzResponse> deleteJob(@RequestParam String name, @RequestParam String group) {
        try {
            return new ResponseEntity<>(schedulerService.deleteJobDetail(name, group), HttpStatus.ACCEPTED);
        } catch (SchedulerException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
