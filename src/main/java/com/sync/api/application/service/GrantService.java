package com.sync.api.application.service;

import com.sync.api.domain.model.ScholarGrant;
import com.sync.api.infra.repository.GrantRepository;
import com.sync.api.web.dto.grant.GrantDto;
import com.sync.api.web.dto.grant.GrantResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GrantService {

    private static final Logger logger = LoggerFactory.getLogger(GrantService.class);

    @Autowired
    private GrantRepository grantRepository;

    public ScholarGrant createGrant(GrantDto grantDto){
        try{
            logger.info(grantDto.toString());
            ScholarGrant grant = new ScholarGrant();
            grant.setType(grantDto.type());
            grant.setActing(grantDto.acting());
            grant.setActive(true);
            grant.setDuration(Period.of(grantDto.years(), grantDto.months(), 0));
            grantRepository.save(grant);
            return grant;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Dados inválidos para criação da bolsa: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar a bolsa: " + e.getMessage(), e);
        }
    }



    public GrantResponseDto getGrant(String id){
        try{
            ScholarGrant grant = grantRepository.findById(id)
                    .orElseThrow(() ->{
                        throw  new RuntimeException("Erro ao encontrar bolsa.");
                    });
            GrantResponseDto response = new GrantResponseDto(
                    grant.getId(),
                    grant.getType(),
                    formatDuration(grant.getDuration()),
                    grant.getActing(),
                    grant.isActive());
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<GrantResponseDto> getAllGrant(){
        try{
            List<ScholarGrant> grantList = grantRepository.findAll();
            return grantList.stream()
                    .map((grant) -> new GrantResponseDto(
                            grant.getId(),
                            grant.getType(),
                            formatDuration(grant.getDuration()),
                            grant.getActing(),
                            grant.isActive()
                    )).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deactiveteGrant(String id){
        try{
            ScholarGrant grant = grantRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Bolsa não encontrada com o id: " + id));

            grant.setActive(false);
            grantRepository.save(grant);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private String formatDuration(Period duration) {
        if (duration == null) {
            return "N/A";
        }

        StringBuilder formattedDuration = new StringBuilder();

        // Verificando anos
        if (duration.getYears() > 0) {
            formattedDuration.append(duration.getYears())
                    .append(" ano")
                    .append(duration.getYears() > 1 ? "s" : "");
        }

        // Verificando meses
        if (duration.getMonths() > 0) {
            if (formattedDuration.length() > 0) {
                formattedDuration.append(" e ");
            }
            formattedDuration.append(duration.getMonths())
                    .append(" mes")
                    .append(duration.getMonths() > 1 ? "es" : "");
        }

        // Se não houver anos ou meses, podemos retornar algo como "N/A" ou "0"
        return formattedDuration.length() > 0 ? formattedDuration.toString() : "Periodo não registrado";
    }

}
