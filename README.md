# PantryPal

PantryPal is a smart kitchen assistant designed to help users manage their pantry inventory and find recipes based on available ingredients. It provides an interactive interface, integrates audio input capabilities, and offers a seamless user experience for home cooks and kitchen enthusiasts.

## 🌟 Features

- 📦 Track ingredients in your pantry  
- 🎙️ Voice input support for ingredient entry  
- 📄 View and filter recipes from a built-in CSV database  
- 🔄 Remember past inputs for convenience  
- 🧠 Intelligent ingredient matching to suggest relevant recipes  

## 🛠️ Tech Stack

- **Java** (Backend logic)  
- **Gradle** (Build tool)  
- **VS Code** (Recommended IDE)  
- **CSV Files** (Data source for ingredients and recipes)  
- **Audio Processing** (Voice input via WAV files)  

## 📁 Project Structure

PantryPal/ ├── src/ # Source code ├── Recipes.csv # List of recipes ├── RememberMe.csv # Tracks previous user inputs ├── tempAudio.wav # Temporary audio input ├── build.gradle # Gradle build configuration ├── Dockerfile # Docker container setup └── README.md # Project documentation


## 🚀 Getting Started

### Prerequisites

- Java 11 or above  
- Gradle  
- VS Code or compatible IDE  
- Docker (optional)  

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/PantryPal.git
   cd PantryPal
   
2. Build the project:

bash
Copy
Edit
./gradlew build

3. Run the app:

bash
Copy
Edit
./gradlew run

4. For audio input, place your WAV file in the root directory and rename it to tempAudio.wav.

🧪 Testing
Run tests using:

bash
Copy
Edit
./gradlew test
🧑‍💻 Contributing
Contributions are welcome! Please fork the repository and submit a pull request.

📄 License
This project is licensed under the MIT License.

vbnet
Copy
Edit

Let me know if you want a downloadable `.md` file or if you'd like to tweak any section!
