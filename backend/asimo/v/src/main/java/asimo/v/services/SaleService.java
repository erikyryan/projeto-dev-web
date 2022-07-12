package asimo.v.services;

import asimo.v.entities.*;
import asimo.v.entities.dto.SaleDTO;
import asimo.v.entities.dto.SaleTicketDTO;
import asimo.v.entities.dto.TicketDTO;
import asimo.v.entities.operation.SaleOperation;
import asimo.v.entities.operation.SessionOperation;
import asimo.v.entities.operation.TicketOperation;
import asimo.v.repositories.ConfigurationRepository;
import asimo.v.repositories.SaleRepository;
import asimo.v.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SaleService {

    private static String PROTOCOLO = "PROTOCOLO";

    SaleRepository saleRepository;

    TicketRepository ticketRepository;

    ConfigurationRepository configurationRepository;

    public SaleService(SaleRepository saleRepository, TicketRepository ticketRepository, ConfigurationRepository configurationRepository) {
        this.saleRepository = saleRepository;
        this.ticketRepository = ticketRepository;
        this.configurationRepository = configurationRepository;
    }

    public Sale createProtocol(Sale sale){

        if(!saleRepository.findByProtocol(sale.getProtocol()).isPresent()){
            Optional<Configuration> configuration = configurationRepository.findByName(PROTOCOLO);

            if(!configuration.isPresent()) {
                throw new RuntimeException("Configuração inválida");
            }

            Configuration config = configuration.get();
            sale.setProtocol(sale.generateProtocol(config.getValue()));
            sale.setSaleDate(new Date());
            config.setValue(config.getValue() + 1);
            configurationRepository.save(config);
            return saleRepository.save(sale);
        }

        throw new RuntimeException("O protocolo da venda já existe");

    }

    public SaleDTO makeTheSales( Event event, Session session,SaleOperation saleOperation, List<TicketOperation> ticketOperations){

        Sale sale = createProtocol(new Sale(saleOperation));

        List<SaleTicketDTO> tickets = ticketOperations.stream().map(t -> saleTicket(t,sale))
                .map( t -> t.generateSaleTicketDTO()).collect(Collectors.toList());

        return sale.generateSaleDTO(tickets,event.getName(),session.getSessionStartDate());

    }

    public Ticket saleTicket(TicketOperation ticketOperation, Sale sale){
        Ticket ticket = ticketRepository.findByTicketIdentifier(ticketOperation.getTicketIdentifier());
        ticket.setProtocol(sale.getProtocol());
        ticket.setNameUser(ticketOperation.getNameUser());
        ticket.setSex(ticketOperation.getSex());
        ticket.setDoc(ticketOperation.getDoc());
        ticket.setPrice(ticketOperation.getPrice());
        ticket.setOccupied(true);
        return ticketRepository.save(ticket);
    }

}
