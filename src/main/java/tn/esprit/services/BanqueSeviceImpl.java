package tn.esprit.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.dao.CompteRepository;
import tn.esprit.dao.OperationRepository;
import tn.esprit.entities.Compte;
import tn.esprit.entities.CompteCourant;
import tn.esprit.entities.Operation;
import tn.esprit.entities.Retrait;
import tn.esprit.entities.Versement;

@Service
@Transactional
public class BanqueSeviceImpl implements IBanqueService{

	@Autowired
	private CompteRepository compteRepository;
	@Autowired
	private OperationRepository operationRepository;
	@Override
	public Compte ConsulterCompte(String codeCmpte) {
		Compte cp = compteRepository.findOne(codeCmpte);
		if(cp==null) {
			throw new RuntimeException("Compte Introuvable");
		}
		return cp;
	}

	@Override
	public void verser(String codeCmpte, double montant) {
		Compte cp = ConsulterCompte(codeCmpte);
		Versement v = new Versement(new Date(), montant, cp);
		operationRepository.save(v);
		cp.setSolde(cp.getSolde()+montant);
		compteRepository.save(cp);
	}

	@Override
	public void retirer(String codeCmpte, double montant) {
		Compte cp = ConsulterCompte(codeCmpte);
		double faciliteCaisse=0;
		if(cp instanceof CompteCourant) {
			faciliteCaisse=((CompteCourant)cp).getDecouvert();
		}
		if(cp.getSolde()+faciliteCaisse<montant) {
			throw new RuntimeException("Solde Insuffisant");
		}
		Retrait r = new Retrait(new Date(), montant, cp);
		operationRepository.save(r);
		cp.setSolde(cp.getSolde()-montant);
		compteRepository.save(cp);
	}

	@Override
	public void virement(String codeCmpte1, String codeCmpte2, double montant) {
		if(codeCmpte1.equals(codeCmpte2)) {
			throw new RuntimeException("Operation impossible... ");
		}
		retirer(codeCmpte1, montant);
		verser(codeCmpte2, montant);
		
	}

	@Override
	public Page<Operation> listOperation(String codeCmpte, int page, int size) {
		// TODO Auto-generated method stub
		return operationRepository.listOperation(codeCmpte, new PageRequest(page, size));
	}

}
