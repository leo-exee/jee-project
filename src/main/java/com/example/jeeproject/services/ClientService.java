package com.example.jeeproject.services;
import com.example.jeeproject.dtos.ClientDTO;
import com.example.jeeproject.entities.Client;
import com.example.jeeproject.entities.Quote;
import com.example.jeeproject.repositories.ClientRepository;
import com.example.jeeproject.repositories.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private QuoteRepository quoteRepository;

    public List<ClientDTO> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public ClientDTO getClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        return convertToDto(client);
    }

    public ClientDTO createClient(ClientDTO clientDto) {
        Client client = convertToEntity(clientDto);
        client.setRegistrationDate(LocalDate.now());
        Client savedClient = clientRepository.save(client);
        return convertToDto(savedClient);
    }

    public ClientDTO updateClient(Long id, ClientDTO clientDto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setEmail(clientDto.getEmail());
        client.setRegistrationDate(clientDto.getRegistrationDate());

        if (clientDto.getQuoteIdList() != null && !clientDto.getQuoteIdList().isEmpty()) {
            List<Quote> newQuotes = quoteRepository.findAllById(clientDto.getQuoteIdList());
            client.getQuotes().addAll(newQuotes);
        }

        Client updatedClient = clientRepository.save(client);
        return convertToDto(updatedClient);
    }

    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        List<Quote> quotes = client.getQuotes();
        for (Quote quote : quotes){
            quote.setClient(null);
            quoteRepository.save(quote);
        }
        clientRepository.deleteById(id);
    }

    private ClientDTO convertToDto(Client client) {
        List<Long> quoteIdList = null;

        if (client.getQuotes() != null) {
            quoteIdList = client.getQuotes()
                    .stream()
                    .map(Quote::getId)
                    .collect(Collectors.toList());
        }

        return new ClientDTO(
                client.getId(),
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(),
                client.getRegistrationDate(),
                quoteIdList
        );
    }

    private Client convertToEntity(ClientDTO clientDto) {
        return new Client(
                clientDto.getFirstName(),
                clientDto.getLastName(),
                clientDto.getEmail(),
                clientDto.getRegistrationDate()
        );
    }
}
