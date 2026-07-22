package com.linklite.controller;


import com.linklite.service.ActivityService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/activity")
@CrossOrigin
public class ActivityController {


private final ActivityService service;



public ActivityController(ActivityService service){

this.service=service;

}




@GetMapping
public Object getActivities(){

return service.getRecentActivities();

}


}