# Décadi — Horloge décimale révolutionnaire

Petite app Android perso pour afficher l'heure en **temps décimal français**, le système inventé pendant la Révolution française.

## L'heure décimale, c'est quoi ?

En 1793, la Convention nationale décide de décimaliser le temps. L'idée : diviser la journée en **10 heures** de **100 minutes** de **100 secondes**. Minuit = 0:00:00, midi = 5:00:00. Simple, logique, révolutionnaire.

Le système a été officiellement utilisé pendant environ 2 ans (1794-1796) avant d'être abandonné — trop dur de changer les habitudes. Les montres décimales de l'époque sont aujourd'hui des pièces de collection.

Cette app le fait revivre.

## Ce que fait l'app

- Affichage digital ou cadran analogique de l'heure décimale
- Secondes décimales affichables ou masquées
- Personnalisation complète des couleurs (fond, principal, secondaire, accent)
- Taille de police réglable (mode digital)
- **Deux widgets** pour l'écran d'accueil : digital et cadran

## Stack

- Kotlin + Jetpack Compose
- Min SDK 26 (Android 8.0)
- Pas de dépendance externe particulière

## Comment marchent les widgets

Les widgets Android sont notoirement limités pour afficher du temps en direct — le rafraîchissement minimum natif est de 30 minutes, ce qui est inutile pour une horloge.

La solution ici : un **foreground service** (`WidgetUpdateService`) qui tourne en tâche de fond et met à jour les widgets toutes les **864 millisecondes** (= 1 seconde décimale). Le service se lance automatiquement dès qu'un widget est posé et redémarre au boot du téléphone.

Le widget digital utilise des `RemoteViews` classiques (des TextViews). Le widget cadran génère un **bitmap** à chaque tick via un renderer Canvas partagé avec l'app — même code de dessin pour les deux.

## Le cadran

10 graduations majeures (heures), 100 mineures (minutes), 3 aiguilles. Les chiffres vont de 0 à 9. L'aiguille des secondes a un petit contrepoids comme sur les vraies montres.

## Installation

Pas de Play Store. Build l'APK et partage-le :

```
./gradlew assembleRelease
```

L'APK sort dans `app/build/outputs/apk/release/`.

## Auteur

Théotime Dmitrašinović — [github.com/TTBroulo/Decadi](https://github.com/TTBroulo/Decadi)
