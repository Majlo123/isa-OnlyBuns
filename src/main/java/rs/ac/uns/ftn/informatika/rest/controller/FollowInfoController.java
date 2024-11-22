package rs.ac.uns.ftn.informatika.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.informatika.rest.dto.FollowInfoDTO;
import rs.ac.uns.ftn.informatika.rest.service.FollowInfoService;

@RestController
@RequestMapping("/api/followInfo")
public class FollowInfoController {

    @Autowired
    private FollowInfoService followInfoService;

    @PostMapping
    public ResponseEntity<FollowInfoDTO> create(@RequestBody FollowInfoDTO followInfoDTO) {
        return ResponseEntity.ok(followInfoService.create(followInfoDTO));
    }
}
