package tn.esprit.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import tn.esprit.entities.Compte;
import tn.esprit.entities.Operation;
import tn.esprit.services.IBanqueService;

@Controller
public class BanqueController {

	@Autowired
	IBanqueService iBanqueService;
	@RequestMapping("/operations")
	public String index() {
		return "comptes";
	}
	
	@RequestMapping("/consultercompte")
	public String consulter(Model model,String codeCompte,@RequestParam(name="page",defaultValue="0") int page, 
			@RequestParam(name="size",defaultValue="2") int size) {
		model.addAttribute("codeCompte",codeCompte);
		try {
			Compte cp = iBanqueService.ConsulterCompte(codeCompte);
			model.addAttribute("compte",cp);
			Page<Operation> pageOperations = iBanqueService.listOperation(codeCompte, page, size);
			model.addAttribute("pageOperations",pageOperations.getContent());
			int[] pages = new int[pageOperations.getTotalPages()];
			model.addAttribute("pages",pages);
		} catch (Exception e) {
			model.addAttribute("exception",e);
		}
		
		return "comptes";
	}
	@RequestMapping(value="/saveOperation",method=RequestMethod.POST)
	public String saveOperation(Model model,String typeOperation,String codeCompte,double montant,String codeCompte2) {
		try {
			if(typeOperation.equals("VERS")) {
				iBanqueService.verser(codeCompte, montant);
			}  if (typeOperation.equals("RET")) {
				iBanqueService.retirer(codeCompte, montant);
			} if (typeOperation.equals("VIR")){
				iBanqueService.virement(codeCompte, codeCompte2, montant);
			}
		} catch (Exception e) {
			model.addAttribute("error",e);
			return "redirect:/consultercompte?codeCompte="+codeCompte+"&error="+e.getMessage();
		}
		
		return "redirect:/consultercompte?codeCompte="+codeCompte;
	}
}
