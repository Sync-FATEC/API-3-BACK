package com.sync.api.application.service;

import com.sync.api.domain.model.ScholarShipHolder;
import com.sync.api.infra.repository.ScholarShipHolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScholarShipHolderService {

    @Autowired
    private ScholarShipHolderRepository scholarShipHolderRepository;

    public void createScholarShip(){
        try{

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ScholarShipHolder getScholarShipHolder(String id){
        try{
            ScholarShipHolder scholarShipHolder = scholarShipHolderRepository.findById(id)
                    .orElseThrow(()->{
                        throw new RuntimeException();
                    });
            return scholarShipHolder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<ScholarShipHolder> getAllGrant(){
        try{
            List<ScholarShipHolder> grantList = scholarShipHolderRepository.findAll();
            return grantList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void removeScholarShip(String id){
        try{
            ScholarShipHolder scholarShipHolder = scholarShipHolderRepository.findById(id)
                    .orElseThrow(()->{
                        throw new RuntimeException();
                    });
            scholarShipHolder.setRemoved(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
