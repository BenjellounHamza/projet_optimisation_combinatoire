Projet de Spécialité (ENSIMAG 2A):


Sujet : Expérimentation de méthodes de résolution pour l'optimisation combinatoire


Contexte:
Nous étudions le problème présenté sur l'application web disponible à l'adresse http://librallu.gitlab.io/hypergraph-viz/
Il s'agit de trouver une permutation des lignes et colonnes d'une matrice binaire de telle sorte à maximiser 
le triangle "blanc" (composé de 0) dans le coin supérieur droit de la matrice.
Ce problème est utilisé pour optimiser certains algorithmes présents dans des systèmes de vision embarquée.


Méthodes:
Le problème considéré est intrinsèquement difficile (NP-Complet). Il est donc très difficile (et très long) de trouver 
la meilleure solution possible. Deux stratégies sont généralement considérées.

1 - Les méthodes exactes (Programmation linéaire en nombre entiers, Programmation par contraintes, Branch and Bound 
dédiés, etc.) cherchent à énumérer de manière intelligente l'ensemble des solutions réalisables de telle sorte à trouver 
une des meilleure solutions et prouver qu'elle est la meilleure le plus rapidement possible.

2 - Les méthodes heuristiques (Tabu Search, Algorithmes génétiques, Optimisation par colonies de fourmis, etc.) cherchent
à trouver une très bonne solution très rapidement sans garantir qu'il s'agit de la meilleure.


Objectifs:
L'objet du projet est d'implémenter un certain nombre de méthodes exactes et heuristiques pour le problème considéré.
Une fois les méthodes implémentées, une analyse des résultats sur les instances sera considérée (facilité d'implémentation,
qualité des solutions, temps d'exécution, impact du type d'instance sur la résolution, etc.)
