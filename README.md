# Fussball App

## Zusammenfassung
Die Fussball App bietet Fussballfans eine umfassende Plattform, um verschiendene Fussballligen wie z.B. die Europameisterschaft 2024 zu verfolgen. Die App liefert aktuelle Informationen zu Spielen und deren Teams. Außerdem ermöglicht sie Benutzern, ihre Lieblingsteams zu markieren und bietet zusätzlich Spielvorhersagen für jedes Spiel.

## Features

### Other Leagues Screen
- **Ligen-Auswahl**: Auswahl verschiedener Ligen und deren Spiele und Teams.
- **Searchbox**: Mithilfe einem Searchbox kann die Auswahl weiter eingegrenzt werden
- **Sortierfunktionen**: Die Ligen können sowohl nach ihrem Namen als auch nach ihrer Saison sortiert werden
- **Markierte Ligen**: Bestimmte Ligen sind mit einem Stern gekennzeichnet. Dieser Stern bedeutet, dass die Daten der Liga richtig und vollständig dokumentiert sind.

### League Screen
- **Next & Last Match**: Anzeigen des letzten und nächsten Spiels dieser Liga
- **Alle Teams in der Liga**: Alle Teams werden in einer Liste bereitgestellt
- **Favorisierte Teams**: Möglichkeit, bis zu 3 Teams zu folgen. Beinflusst die Next & Last Match-Anzeige

### Team Detail Screen
- **Basisinformationen**: Informationen zum derzeitigen Punktestand sowie Siege/Unentschieden/Niederlagen und Torpunkte.
- **Next & Last Match**: Anzeige des nächsten und letzten Spiels des ausgewählten Teams

### Match Detail Screen
- **Spielstände**: Anzeige der aktuellen und vergangenen Spielstände.
- **Spielinformationen**: Details zu Datum, Uhrzeit und Austragungsort (falls vorhanden).
- **Spielverlauf**: Chronologischer Überblick über Tore.
- **Game Prediction by AI**: Schätzung der AI, wie das Spiel ausgehen wird.


## Covered topics

### Sensors
- **Lichtsensor**: Nutzung des integrierten Lichtsensors des Geräts, um den Licht- und Dunkelmodus automatisch anzupassen.
- **Automatische Anpassung**: Dynamische Umschaltung zwischen Licht- und Dunkelmodus basierend auf den Sensordaten.

### Data Centricity
- **Fussball Daten**: Die Daten der Ligen, Matches, etc. werden von der OpenLigaDB gratis und frei zugänglich bereitgestellt. (https://www.openligadb.de/)
- **KI-Modelle**: Nutzung von OpenAI-Modellen zur Vorhersage von Spielergebnissen.

### Advanced Layout
- **Jetpack Compose**: Verwendung von Jetpack Compose für die Erstellung der Benutzeroberfläche.


## Installation

### Schritte zur Installation

1. **Repository klonen**:
   ```bash
   git clone https://github.com/AppDev2024-footballApp/AppDev_Project_EM-App.git
   ```

2. **Projekt in Android Studio öffnen**

3. **Abhängigkeiten synchronisieren**:
   - Warten Sie, bis Android Studio das Projekt geladen und die Abhängigkeiten synchronisiert hat.

4. **(optional) Enable Game Predictions**
   - Falls die Game Predictions generiert werden sollen muss ein OpenAI Key mit Credits angegeben werden. Dieser muss in der local.properties Datei hinzugefügt werden
   ```properties
   openai.api.key=your-open-ai-key
   ```

5. **App auf Gerät/Emulator hochladen und ausführen**

6. **Let's go**:
   - Die App sollte nun auf Ihrem Gerät/Emulator ausgeführt werden. Erkunden Sie die verschiedenen Funktionen und geniessen Sie die beste Fussball App der Uni!

---

