package tn.esprit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.entities.Compte;

@Repository
public interface CompteRepository extends JpaRepository<Compte, String>{

}
