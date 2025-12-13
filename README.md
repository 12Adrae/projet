# PROJET PC

##  Membres du binôme
- Faiza Siroukane
- Adrae BELKHAYATE 

# Diagramme de classes:
<p align="center">
  <img src="image/diagramme.png" width="1200">
</p>



1. **Code fourni et problèmes initiaux**
Au début du projet, nous avions uniquement une version très simplifiée de la classe FirefighterBoard. Cette classe contenait à la fois le stockage du terrain, la propagation du feu, le déplacement des pompiers, la gestion de la grille, la mise à jour des éléments et toute la logique de la simulation. Autrement dit, tout se trouvait regroupé dans une seule classe, ce qui rendait le code difficile à étendre et impossible à maintenir.
Le projet nous demandait explicitement de déléguer les fonctionnalités des pompiers et du feu vers des classes séparées et de restructurer le modèle selon les principes SOLID.

---

2. **Séparation des responsabilités et extraction des comportements**

La première chose que nous avons faite a été d’extraire le comportement du feu hors de FirefighterBoard. Nous avons créé la classe Fire, qui prend désormais en charge l’ensemble de la propagation, la gestion des positions en feu et l’extinction. Avant cela, tout était directement codé dans FirefighterBoard. Cette extraction respecte le principe de responsabilité unique, car le plateau ne s’occupe plus de la logique interne du feu.
Dans un second temps, nous avons extrait le comportement des pompiers. Nous avons créé la classe Firefighter, qui gère son déplacement vers le feu le plus proche grâce à TargetStrategy, son extinction directe et l’extinction des feux adjacents. Là aussi, toute cette logique était initialement dans FirefighterBoard, et nous l’avons déplacée pour respecter SOLID.
Nous avons ensuite ajouté une interface commune, Element, que les nouveaux éléments implémentent. Nous avons ainsi pu déplacer toute la logique dynamique hors de la classe centrale et rendre le modèle extensible. Fire, Firefighter, MotorizedFirefighter et Cloud dérivent désormais de cette interface.

---

3. **Ajout des nouvelles fonctionnalités exigées par le sujet**
Une fois l’architecture clarifiée, nous avons ajouté les fonctionnalités supplémentaires demandées dans le TP.
Nous avons d’abord intégré les nuages. Nous avons créé une classe Cloud qui se déplace aléatoirement d’une case à chaque tour et qui éteint les feux lorsqu’il arrive dessus. Nous avons aussi ajouté leur création automatique dans initElements().
Nous avons ensuite développé les pompiers motorisés. Pour cela, nous avons créé la classe MotorizedFirefighter qui hérite de Firefighter. Nous avons modifié uniquement la méthode update afin qu’un pompier motorisé effectue deux déplacements par tour tout en gardant toute la logique d’extinction héritée. Cette extension a été faite sans changer la classe de base, ce qui montre que l’architecture respecte bien l’ouverture/fermeture.
Nous avons également ajouté la gestion des différents terrains. Nous avons créé des classes séparées pour Mountain, Road, Rock et NormalCell. Nous avons ensuite modifié FirefighterBoard pour générer aléatoirement ces terrains sur la carte. Nous avons aussi ajouté l’interface Terrain, ce qui permet au feu et aux pompiers d’interagir avec ces terrains sans dépendre d’une classe concrète.
Grâce à cela, la propagation du feu respecte les règles du sujet : impossible sur les montagnes et les routes, retardée sur les rochers, et immédiate sur les cellules normales.

---

4. **Amélioration de la structure générale du code**
Après l’extraction des classes dynamiques et l’ajout des terrains, nous avons restructuré FirefighterBoard pour qu’il ne s’occupe plus que de son rôle logique : stocker les éléments, gérer la grille, appeler les mises à jour, enregistrer les positions modifiées et gérer les terrains. Il n’a plus aucune logique de propagation ou de déplacement des éléments, ce qui correspond exactement aux exigences du TP.
Nous avons aussi ajouté une ElementFactory pour créer les éléments dynamques, ce qui rend l’ajout d’un nouveau type extrêmement simple. Le sujet recommandait une architecture extensible, et cette factory répond à cet objectif.

---

5. **Fonctionnement final de la simulation**
Nous avons mis en place un cycle de mise à jour clair :
le plateau appelle la mise à jour de chaque élément, le feu propage ses nouvelles cases selon son rythme, les pompiers se déplacent et éteignent, les pompiers motorisés effectuent deux déplacements et les nuages éteignent les feux par contact.
Ce fonctionnement résulte directement des classes indépendantes que nous avons mises en place.

---

6. **Conclusion du travail réalisé**

En résumé, nous avons :
– extrait le comportement du feu de FirefighterBoard et créé la classe Fire,
– extrait le comportement des pompiers et créé Firefighter,
– ajouté une interface commune Element pour uniformiser les comportements,
– ajouté les nuages, les pompiers motorisés et tous les terrains demandés,
– créé Terrain et ses différentes implémentations,
– restructuré complètement FirefighterBoard pour qu’il ne contienne plus aucune logique métier,
– ajouté une factory pour rendre le modèle extensible,
– et appliqué les principes SOLID dans la totalité du modèle.

Notre travail répond donc précisément aux exigences du sujet : séparation des responsabilités, clarification de l’architecture, ajout des comportements avancés et mise en place d’un automate cellulaire complet, extensible et cohérent

---

7. **Difficultés rencontrées**
La première difficulté a été de comprendre comment réorganiser correctement le code fourni. Comme tout était regroupé dans FirefighterBoard, il n’était pas évident de savoir quelles parties extraire, comment les séparer et surtout comment éviter de casser le fonctionnement du simulateur. Trouver une architecture stable, avec les bonnes classes et les bonnes dépendances, nous a demandé plusieurs révisions.
L’intégration des différents terrains a aussi posé problème. Le feu devait réagir différemment selon le type de sol, et nos premières tentatives donnaient soit un feu qui se propageait trop vite, soit un feu qui ne bougeait plus du tout. Ce n’est qu’après avoir introduit l’interface Terrain et déplacé toute la logique de propagation dans la classe Fire que nous avons réussi à obtenir un comportement cohérent.
L’ajout des pompiers motorisés a été délicat à cause de l’héritage. Nous voulions éviter de dupliquer le code du pompier classique, tout en ajoutant un deuxième déplacement par tour. Il nous a fallu revoir plusieurs fois la méthode update pour obtenir un comportement stable.
Les nuages ont également posé des difficultés à cause de leur déplacement aléatoire. Ils entraient parfois en conflit avec d’autres éléments ou modifiaient des cases déjà mises à jour, ce qui provoquait des incohérences dans l’affichage. Nous avons dû ajuster le suivi des positions modifiées pour éviter ces problèmes.
Enfin, nous avons rencontré un ensemble de difficultés directement liées aux principes SOLID. À chaque ajout de fonctionnalité, il fallait s’assurer que nous ne recréions pas des dépendances inutiles ou des classes trop chargées. Plusieurs fois, nous avons dû réorganiser ou déplacer des méthodes pour conserver une architecture propre. Maintenir le respect de SOLID tout au long du développement a été l’un des aspects les plus exigeants du projet, mais aussi celui qui nous a le plus permis de stabiliser et de clarifier notre code
