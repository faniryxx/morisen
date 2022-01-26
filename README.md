# Morisen

## Introduction 

Ce projet a été réalisé dans le cadre de l'évaluation du module Android de CIPA4 option GL.

Il a été réalisé sur Android Studio et écrit en Java en plus ou moins une semaine.

La vidéo démonstrative de l'application peut se trouver [ici](https://drive.google.com/file/d/1DlsYf71l_0JmW2RWfiWiNuT-KyGZ0iUD/view?usp=sharing).

Merci de me faire savoir par mail à nathan.ranaivo-ravoaja@isen-ouest.yncrea.fr quand vous auriez fini de jeter un oeil au repo pour que je puisse le remettre en privé.

## Objectif du projet

Le but du projet est donc de programmer le jeu du morpion en réseau en utilisant la plateforme Firebase comme backend.

Le cahier des charges nous a imposé quelques contraintes, notamment au niveau du schéma de la base Firebase. Nous les avons toutes respectées à un détail près, le nom des variables peuvent être différentes dans notre code, mais elles remplissent la même fonction.

Nous avons eu la possibilité d'être libres de nos actions et d'ajouter des fonctionnalités qui ne sont pas demandées, dans l'objectif d'améliorer le programme et de traiter tous les cas de figure pouvant entraîner un blocage dans le déroulement de l'application.


## Déroulement du jeu 

1. Exécuter l'application.

2. L'utilisateur doit se connecter avec son numéro de téléphone mais aussi de le confirmer à l'aide d'un code qu'il aura reçu par SMS.

3. Il doit renseigner son "pseudo" qui sera visible par l'autre joueur dans la partie. On a rajouté cette fonctionnalité pour que l'interface soit plus conviviale; il est bien plus agréable de voir un pseudo qu'un numéro de téléphone à 10 chiffres.

4. L'utilisateur doit attendre un adversaire ou commencer à jouer en plaçant son premier rond. Par défaut, c'est le joueur 1 qui commence.

5. Une fois la partie "Gagnée", "Perdue" ou en "Égalité", le joueur a le choix de continuer de jouer avec son adversaire ou de quitter l'application. S'il quitte l'application, celle de son adversaire se fermera automatiquement.


## Description du programme 

### MainActivity.java
Il s'agit de l'activité principale, qui sera lancée lors de l'éxecution de l'application. Elle contient l'authentification prédéfinie qu'on peut retrouver sur le site officiel de Firebase.

### RegisterActivity.java
Elle permet à chaque utilisateur de renseigner son pseudo, qui sera affiché par la suite sur l'écran de jeu de son adversaire.

### GameActivity.java 
Activité de jeu principale.

## Crédits
Nathan RANAIVO - nathan.ranaivo-ravoaja@isen-ouest.yncrea.fr

Lucas LANDAIS - lucas.landais@isen-ouest.yncrea.fr
