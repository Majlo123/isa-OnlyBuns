package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.rest.domain.FollowInfo;
import rs.ac.uns.ftn.informatika.rest.dto.FollowInfoDTO;
import rs.ac.uns.ftn.informatika.rest.repository.FollowInfoRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class FollowInfoService {

    @Autowired
    private FollowInfoRepository followInfoRepository;

    public List<FollowInfoDTO> getAll(){
        List<FollowInfoDTO> followInfoDTOList = new ArrayList<FollowInfoDTO>();

        for(FollowInfo followInfo : followInfoRepository.findAll()){
            FollowInfoDTO followInfoDTO = new FollowInfoDTO(followInfo.getUserAccountId(),followInfo.getFollowedById(),followInfo.getFollowDate());
            followInfoDTOList.add(followInfoDTO);
        }

        return followInfoDTOList;
    }

    public FollowInfoDTO create(FollowInfoDTO followInfoDTO){
        followInfoRepository.save(new FollowInfo(followInfoDTO.getUserAccountId(),followInfoDTO.getFollowedById(),followInfoDTO.getFollowedDate()));

        return followInfoDTO;
    }

    public List<FollowInfoDTO> getAllForSevenDaysForUser(Long userAccountId){
        List<FollowInfo> followInfos = followInfoRepository.findAll();

        List<FollowInfoDTO> found = new ArrayList<>();

        for(FollowInfo followInfo : followInfos){
            if(followInfo.getUserAccountId().equals(userAccountId) && ChronoUnit.DAYS.between(
                    followInfo.getFollowDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    LocalDate.now()) <= 7 ){
                found.add(new FollowInfoDTO(followInfo.getUserAccountId(),followInfo.getFollowedById(),followInfo.getFollowDate()));
            }
        }

        return found;
    }
}
