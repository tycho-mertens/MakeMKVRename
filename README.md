# MakeMKVRename
(Quick Explanation)<br> I have a lot of Blu-Ray drives, which I prefer to watch on my computer, so I rip them to my pc.  
My problem is that when I rip them, they get titles like "t00.mkv, t01.mkv". The episodes/titles are not in order, and it takes a long time to find out which video file is which episode if you do it manually.  
So I made a program that will transcode a video file to a .wav file and calculate the fingerprint of that .wav file using [Chromaprint](https://github.com/acoustid/chromaprint).  This way we can keep a database that we can use to match fingerprints to find out automatically which video file is for which episode and rename them properly.

I made this program to keep me busy and to actually make something that I can use to make my life easier.

## TODO
- [ ] Automatically install needed tools to run this program (Currently working on this)
- [ ] Fix some issues that prevent it from fully working on Windows
- [ ] Improve fingerprint matching status
- [ ] Make it so only the last 5 minutes will be transcoded to .wav file for fingerprinting (last 5 minutes is more accurate than the first 5)
- [ ] Rework Config/Settings
- [ ] Create a CacheManager so scraping for images and info will get cached and go faster
- [ ] Port some Python code to Java
- [ ] Fix small bugs
- [ ] Improve exception handling were needed
- [ ] Make a proper database interface etc...

## Upload a video file to the DB
First we need to have a DB, so we add some video files to it.  
This program is meant to be used with a centralized DB, or a DB which will be imported and exported among other people (aka share the DB with people).  
First select the tv show/serie, then drag and drop the video files, then click on "Try to auto match" (The program will try to extract the season and episode from the filename with regex), finally click on upload.
<video src="https://user-images.githubusercontent.com/73956677/162569865-acfb79c9-033c-4887-aa68-e43b73e68574.mp4"></video>

## Match the fingerprints and rename the files
You first search/select the tv show that you ripped from your Blu-Ray, then add the "t00.mkv, t01.mkv" etc to the left table, then select the season from the combo-box and click on "Find Matches" and wait.  
Matching fingerprints will take some time (I paused the recording here to make the video shorter), even-though it's multithreaded, it has to do a lot of transcoding and calculations with floating points (heavy calculations).  
When the program is finished processing everything, you will see the results on the right side  
<video src="https://user-images.githubusercontent.com/73956677/162572425-c37c05b8-5633-4bf1-9388-e0056f82a384.mp4"></video>

## What happens when there are no real matches?
The program detects low similarity results and flags them, you can delete the flagged results and rename the good results.  
In this case the file "randomVideoFile.mp4" has no results in the current database, the file "probablyEp9?.mkv" is S03E09 from the TV Show Suits  
<video src="https://user-images.githubusercontent.com/73956677/162572637-87cae79d-ce0b-4803-8c79-95274f2322a8.mp4"></video>

## Settings
How the settings/config currently works needs to be reworked. Everything selected in red will be removed, because I made a module that will automatically install the needed components to run this program.  
Every setting has a tooltip that shows more information about the setting.
![settings](https://user-images.githubusercontent.com/73956677/162572766-c7e4ab8c-ea4b-4240-ab91-3329cf1a0965.png)

## How to run this on IntelliJ (Linux)
1. Download JavaFX (17.0.2) from: [openjfx-17.0.2_linux-aarch64_bin.sdk.zip](https://download2.gluonhq.com/openjfx/17.0.2/openjfx-17.0.2_linux-aarch64_bin-sdk.zip)
2. Extract the JavaFX to your Documents folder
3. Open IntelliJ and clone the project
4. Add the needed JavaFX Libraries
5. Run the program, it will give an error about JavaFX modules missing
6. Add the VM Options (--module-path /home/user/Documents/javafx-sdk-17.0.2/lib --add-modules=javafx.controls,javafx.fxml,javafx.media)
7. Now you can run the program

<video src="https://user-images.githubusercontent.com/73956677/162590936-cc16f0ac-8d24-4cf6-b5b3-78e79eaea6dc.mp4"></video>

## Notes
I mainly use Java because my main OS is Linux and not Windows. Java allows me to make desktop applications with SceneBuilder (JavaFX) for Windows and Linux. I would try .NET Core, but it currently doesn't support GUI on Linux. Simply it's the best language for my situation.  
  
The DB of this program is not a proper database, it's just folders and json files. I plan on changing this (to MongoDB probably) when I'm not the only one using it (if it ever comes to that).

Please know that the program is far from perfect and that not everything is programmed how I wanted it to be. I will have to change this later on and fix it.

If you still don't understand something or need more explanation, feel free to contact me.
