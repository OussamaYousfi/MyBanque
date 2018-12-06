package tn.esprit.services;

import org.springframework.data.domain.Page;

import tn.esprit.entities.Compte;
import tn.esprit.entities.Operation;

public interface IBanqueService {

	public Compte ConsulterCompte(String codeCmpte);
	public void verser(String codeCmpte, double montant);
	public void retirer(String codeCmpte, double montant);
	public void virement(String codeCmpte1,String codeCmpte2,double montant);
	public Page<Operation> listOperation(String codeCmpte,int page,int size);
}
