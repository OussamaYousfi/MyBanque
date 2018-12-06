package tn.esprit.MyBanque;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import tn.esprit.dao.ClientRepository;
import tn.esprit.dao.CompteRepository;
import tn.esprit.dao.OperationRepository;
import tn.esprit.entities.Client;
import tn.esprit.entities.Compte;
import tn.esprit.entities.CompteCourant;
import tn.esprit.entities.CompteEpargne;
import tn.esprit.entities.Retrait;
import tn.esprit.entities.Versement;
import tn.esprit.services.IBanqueService;

@SpringBootApplication
@EnableConfigurationProperties
@EntityScan(basePackages = {"tn.esprit.entities"})
@EnableJpaRepositories("tn.esprit.dao")
@ComponentScan({"tn.esprit.services","tn.esprit.web","tn.esprit.security"})
public class MyBanque3Application implements CommandLineRunner{

	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private CompteRepository compteRepository;
	@Autowired
	private OperationRepository operationRepository;
	@Autowired
	private IBanqueService iBanqueService;
	public static void main(String[] args) {
		SpringApplication.run(MyBanque3Application.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		Client c1 =clientRepository.save(new Client("Oussama","oussama@esprit.tn"));
		Client c2 =clientRepository.save(new Client("Ahmed","ahmed@esprit.tn"));
		
		Compte cp1 = compteRepository.save(new CompteCourant("c1",new Date(),9000,c1,6000));
		Compte cp2 = compteRepository.save(new CompteEpargne("c2",new Date(),6000,c2,5.5));
		
		operationRepository.save(new Versement(new Date(), 9000, cp1));
		operationRepository.save(new Versement(new Date(), 6000, cp1));
		operationRepository.save(new Versement(new Date(), 2300, cp1));
		operationRepository.save(new Retrait(new Date(), 9000, cp1));
		
		operationRepository.save(new Versement(new Date(), 9000, cp2));
		operationRepository.save(new Versement(new Date(), 6000, cp2));
		operationRepository.save(new Versement(new Date(), 2300, cp2));
		operationRepository.save(new Retrait(new Date(), 9000, cp2));
		
		iBanqueService.verser("c1", 1111111);
	}
}
