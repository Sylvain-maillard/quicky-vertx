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

 ## Concepts

- processCommand:
    - Commande -> Event(s)
- applyEvent:
    - Aggregat -> Event -> Aggregat

- stocker tous les events sur un aggregat.
- pour reconstruire l'aggregat dans son état actuel: charger l'ensemble
des events et les jouer les un après les autres.

cf. http://docs.geteventstore.com/introduction/event-sourcing-basics/

### Event store

 Un __event store__ va stocker les evenements les uns après les autres. Pour chaque évènement reçu, une notification
 est transmises via l'_event bus_ vertx.

### Projections

Il s'agit d'éléments logiciel qui sont des vues sur le modèle. Ces vues sont créées en écoutant les évènements
transmis via l'eventbus par l'event store.

* ex: récupérer le hall of fame.

# RAF

- présenter les concepts vertx eventbus et verticles (très important avant de présenter le jeu)
- présenter le jeu
- présenter et coder un webservice REST pour exposer les commandes du jeu
- présenter et coder une vue avec un moteur de template (à voir si on a le tps)
- présenter et coder l'event store dans une base de données.
- présenter et coder un cluster vertx ? (pas nécessaire car on l'utilise pas chez VSC)