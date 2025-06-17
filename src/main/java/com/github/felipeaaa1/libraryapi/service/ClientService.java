package com.github.felipeaaa1.libraryapi.service;

import com.github.felipeaaa1.libraryapi.model.Client;
import com.github.felipeaaa1.libraryapi.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder encoder;

    public Client salvar (Client client){
        String enconded = encoder.encode(client.getClientSecret());
        client.setClientSecret(enconded);
        return clientRepository.save(client);
    }

    public Client obterPorID(String id){
        return clientRepository.findByClientId(id);
    }

}
