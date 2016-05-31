package sopi.module.visit.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sopi.module.visit.repository.VisitRepository;

@Component
public class VisitModel {

	@Autowired VisitRepository visitRepo;
	
	public List<Visit> list(){
		return visitRepo.findAll();
	}
}
