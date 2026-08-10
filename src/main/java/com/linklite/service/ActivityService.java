package com.linklite.service;


import com.linklite.entity.Activity;
import com.linklite.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ActivityService {


private final ActivityRepository repository;


public ActivityService(ActivityRepository repository){

    this.repository = repository;

}



public void saveActivity(
String action,
String description
){


Activity activity = new Activity();


activity.setAction(action);

activity.setDescription(description);

activity.setCreatedAt(LocalDateTime.now());


repository.save(activity);


}



public List<Activity> getRecentActivities(){

return repository.findTop5ByOrderByCreatedAtDesc();

}



}