package com.example.jeeproject.controllers;
import com.example.jeeproject.dtos.ClientDTO;
import com.example.jeeproject.helpers.ClientHelper;
import com.example.jeeproject.helpers.TeamMateHelper;
import com.example.jeeproject.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private TeamMateHelper teamMateHelper;

    @Autowired
    private ClientHelper clientHelper;

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        String roleMember = teamMateHelper.getTeamMateRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            List<ClientDTO> clients = clientService.getAllClients();
            return new ResponseEntity<>(clients, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        String roleMember = teamMateHelper.getTeamMateRole();
        long authenticatedClient = clientHelper.checkClientExists();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER") || authenticatedClient == id) {
            ClientDTO client = clientService.getClient(id);
            return new ResponseEntity<>(client, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientDto) {
        ClientDTO createdClient = clientService.createClient(clientDto);
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDto) {
        String roleMember = teamMateHelper.getTeamMateRole();
        long authenticatedClient = clientHelper.checkClientExists();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER") || authenticatedClient == id) {
            ClientDTO updatedClient = clientService.updateClient(id, clientDto);
            return new ResponseEntity<>(updatedClient, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        String roleMember = teamMateHelper.getTeamMateRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            clientService.deleteClient(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
