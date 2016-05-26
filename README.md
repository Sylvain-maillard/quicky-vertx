# Quicky-vertx

En préparation du quicky sur vert.x

Idée: faire un jeu type "la brute"

 ## Event-sourcing

 Les events:
 * Une brute a rejoint l'arêne
 * Une deuxième brute a rejoint l'arêne
 * Un combat a démarré dans l'arêne
 * Une brute gagnant a gagné 2 pt XP
 * Une brute perdant a gagné 1 pt XP

 Les commandes:
 * RejoindreArene
 * DemarrerCombat
 * Gagner
 * Perdre

 Les aggregats:
 * Brute

 ## Concept

processCommand:
* Commande -> Event(s)
applyEvent:
* Aggregat -> Event -> Aggregat

