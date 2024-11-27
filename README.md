#FireFighterStarter
Implémentation d'un proto-simulateur d'automate cellulaire. 
Au début, nous avions une exécution sur une grille où les éléments seront des pompiers et des feux.
(Nous avons ajouté des fonctionnalités au cours du projet).
Le but est de voir en combien de tours les pompiers arriveront à éteindre le feu.

##Structure du projet
Tout le travail effectué se trouve dans le package java qui se trouve lui-même dans main et main dans src.
A partir d'ici, nous partons du principe que tous les packages mentionnés se trouvent dans le package java lui-même dans le package main présent dans src.
Dans l'énumération ViewElement du package view, nous avons seulement ajouté quelques éléments afin de pouvoir voir l'élement comme l'indique le nom de la classe.
Le reste du travail se trouve dans le package model :
Nous avons créé les classes : Cloud (pour l'implémentation du comportement des nuages), Fire (pour l'implémentation du comportement du feu), FireFighter (pour l'implémentation du comportement des pompiers) et MotorizedFireFighter (pour l'implémentation du comportement des pompiers motorisés).
Nous avons également modifié l'énumération ModelElement afin d'ajouter les nouveaux éléments ajoutés.
Le plus gros du travail se trouve dans FireFighterBoard. Elle gère les positions des pompiers, des incendies, des nuages, des montagnes et des routes, ainsi que les interactions entre ces éléments. Elle initialise les éléments, gère leurs déplacements et propage le feu.
