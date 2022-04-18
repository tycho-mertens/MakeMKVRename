# MakeMKVRename
I have a lot of Blu-Ray drives <sub><sup>(tv shows)</sup></sub>, which I want to watch on my pc, phone, etc...  
But when I copy them to my pc the episodes are not in order and have random names.  
To fix this, I wrote this program. It takes the video files with random names and calculates the audio fingerprint,  
from there it matches the fingerprint to the database and renames them to the correct episode.  
I calculate the  fingerprints with [Chromaprint](https://github.com/acoustid/chromaprint).

Here is a simplified diagram on how the program works.  
<img src="https://user-images.githubusercontent.com/73956677/162639107-d2110ecc-5cc3-43d6-a6f2-0d0da2c84d77.png" width="500em"/>  
When watching for the first time, the episode names will need to be matched manually and added to the database. When the episodes are re-watched later on, you don't have to go through manually matching them again.

## How can the program be used
- With a community:  
The program can be used with a community. If the show is not yet in the database, the user to first watch it would
have to manually match the episodes <!-- name to the fingerprint -->and upload it to the database. If the fingerprint has been manually matched once, it can then be used by anybody else to automatically match the episodes

- Without a community <sup><sub>(This is how I use it)</sup></sub>  :  
The program can also be used without a community. For example: I want to watch the TV Show "Suits", I copy it to my pc. I would need to match it manually for the first time and add it to the database. A year later I want to watch "Suits" again, but I don't have it on my pc anymore. I can simply copy the episodes to my pc and automatically match them with the help of the program.

## TODO
- [x] Automatically install needed tools to run the program
- [ ] Windows support
- [ ] Rework Config
- [ ] Cache management

## How to run this on IntelliJ (Linux)
1. Install needed tools
   - Download JavaFX (17.0.2) from: [openjfx-17.0.2_linux-x64_bin-sdk.zip](https://download2.gluonhq.com/openjfx/17.0.2/openjfx-17.0.2_linux-x64_bin-sdk.zip)
2. Extract the tools to a folder (Documents folder for example)
3. Open IntelliJ and clone the project
4. Add the needed JavaFX Libraries
5. Click on "Add Configuration..."
6. Select the main class
8. Add the VM Options <sub><sup>(--module-path /home/user/Documents/javafx-sdk-17.0.2/lib --add-modules=javafx.controls,javafx.fxml)</sub></sup>
9. Now you can run the program 

<video src="https://user-images.githubusercontent.com/73956677/162759677-9fe50154-3629-4733-91d8-54f2ed5a67d1.mp4"></video>
<!-- <video src="https://user-images.githubusercontent.com/73956677/162590936-cc16f0ac-8d24-4cf6-b5b3-78e79eaea6dc.mp4"></video> -->

## How to use the program (Linux)
1. Click on the database search icon to open the "Find TV Show" window  
   <img src="https://user-images.githubusercontent.com/73956677/162643454-1febf839-2df0-4544-a2f6-5ea8922d49f2.png" width="500em"/>  
2. Enter the name of the TV Show and click on "Search"  
   <img src="https://user-images.githubusercontent.com/73956677/162643555-6d7905a0-787a-43d7-9635-4c5280a5581f.png" width="500em"/>  
3. Look through the results for the correct TV Show and double-click on it to select it  
   <img src="https://user-images.githubusercontent.com/73956677/162643632-adcf38c5-a120-4af3-b1e8-a5611efca4fc.png" width="500em"/>  
4. Click on the combo-box and select the season you're renaming <sup><sub>(In my case it's season 3)</sup></sub>  
   <img src="https://user-images.githubusercontent.com/73956677/162643866-6980bc92-9a67-43f0-9300-2ee90eafade9.png" width="500em"/>  
5. Drag and drop the files you want to match  
   <img src="https://user-images.githubusercontent.com/73956677/162644399-47ef74d9-5413-4a2d-9ada-657e5226d8c9.png" width="500em"/>  
6. Click on "Find Matches", then the program will start matching the fingerprints <sup><sub>(this can take a while)</sup></sub>  
   <img src="https://user-images.githubusercontent.com/73956677/162644454-41a65c23-2be3-49b5-b6dd-4414f333c656.png" width="500em"/>  
7. Check the matched episodes <sup><sub>([Low similarity scores](https://user-images.githubusercontent.com/73956677/162764519-4ed318a1-e909-4a1b-9e4f-7690dc330f0b.png) are colored in 
red)</sup></sub> and click on "Rename"  
   <img src="https://user-images.githubusercontent.com/73956677/162644789-27468d49-5623-43f1-8d1a-6bafb5567854.png" width="500em"/>  
8. Files have been renamed  
   <img src="https://user-images.githubusercontent.com/73956677/162645545-9fa4f220-cc05-4e72-989d-206ac28b40a0.png" width="500em"/>  

## How to populate the database
1. Click on "Upload Fingerprints"  
   <img src="https://user-images.githubusercontent.com/73956677/162702787-90982be5-2757-4712-8034-7166477611e8.png" width="500em"/>   
2. We need to select a TV Show, so click on the database search icon  
   <img src="https://user-images.githubusercontent.com/73956677/162702977-f7df6364-beb7-4d1d-99ed-b86b64383c67.png" width="500em"/>  
3. Type in the name of the show and click on "Search"  
   <img src="https://user-images.githubusercontent.com/73956677/162703107-898b43c3-ec6e-43c1-bc36-9da1391ebab2.png" width="500em"/>  
4. Double-click on the TV Show you want to upload a fingerprint for  
   <img src="https://user-images.githubusercontent.com/73956677/162703243-273ef760-c0fd-4c2d-a76d-6494a6e206d8.png" width="500em"/>  
5. Drag and drop the video files you want to upload to the database  
   <img src="https://user-images.githubusercontent.com/73956677/162703519-c7cb3f41-9774-4869-906e-8e580d754ebf.png" width="500em"/>  
6. Click on "Try and auto match", this way the program can extract the season and episode with regex <sup><sub>(Watch the light blue circles in the pictures for the auto matching with regex)</sup></sub>  
   <img src="https://user-images.githubusercontent.com/73956677/162703725-f9933f4c-4500-4ffd-80ff-508d69da3f93.png" width="500em"/>  
7. Now click on "Upload" <sup><sub>(This will take a while)</sup></sub>  
   <img src="https://user-images.githubusercontent.com/73956677/162709640-2518d2e1-cab5-40e0-8e36-1839276fbcc4.png" width="500em"/>  
8. After you get a popup that says "All Done!" (just click on "OK"), the video files will have been inserted into the database as audio fingerprints and you can close the "Uploader"  
   <img src="https://user-images.githubusercontent.com/73956677/162710554-c4613870-8828-404d-bc82-58f71d88e955.png" width="500em"/>  

## Notes
I mainly use Java because my main OS is Linux and not Windows. Java allows me to create desktop applications with SceneBuilder (JavaFX) for Windows and Linux. I would try .NET Core, but it currently doesn't support GUI on Linux. Simply it's the best language for my situation.

The DB of this program is not a proper database, it's just folders and json files. I plan on changing this (to MongoDB) when I'm not the only one using it (if it ever comes to that).


In case of any questions feel free to contact me.
