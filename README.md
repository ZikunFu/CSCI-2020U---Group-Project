a. Project information: \
CSCI2020U Group Project\
Member: Zikun Fu (100742730)\
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Chen Yang (100742300)\
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Yanguang Yang(100738182)
\
Screenshots of running application:\
![alt text](https://github.com/ZikunFu/CSCI2020U_GroupProject/blob/master/src/sample/resources/App_screenshot_1.png)

b. Improvements: \
&nbsp;&nbsp;&nbsp;&nbsp;1.TextArea and View File function to display local stored files.\
&nbsp;&nbsp;&nbsp;&nbsp;2.TreeView for browsing multiple sub-directories under root folder on Server shared folder.\
&nbsp;&nbsp;&nbsp;&nbsp;3.Button Icons.\
&nbsp;&nbsp;&nbsp;&nbsp;4.Alerts for information and incorrect user input.

c. How to run: \
&nbsp;&nbsp;&nbsp;&nbsp;1.Install java jdk-14 or higher version.\
&nbsp;&nbsp;&nbsp;&nbsp;2.Include javafx-sdk-15.0.1 as library.\
&nbsp;&nbsp;&nbsp;&nbsp;3.Note: Launching from IDE need to add VM arguments:\
&nbsp;&nbsp;&nbsp;&nbsp;"--module-path /path/to/javafx-sdk-11.0.2/lib --add-modules javafx.controls,javafx.fxml"\
&nbsp;&nbsp;&nbsp;&nbsp;4.Set command line arguments for Main.java(default: "127.0.0.1 local_shared/")\
&nbsp;&nbsp;&nbsp;&nbsp;Note: default port is 16789, MaxThread is 2, \
&nbsp;&nbsp;&nbsp;&nbsp; default SERVER_PATH is "server_shared" this is where "userProfile.csv" is saved\
&nbsp;&nbsp;&nbsp;&nbsp;5.IMPORTANT: Only two clients are allowed to connect since this is a two-player-versus game\
&nbsp;&nbsp;&nbsp;&nbsp;6.Launch two instances of Main.java.\
&nbsp;&nbsp;&nbsp;&nbsp;7.Launch Server.java.\
&nbsp;&nbsp;&nbsp;&nbsp;8.Testing username and pass word can be found inside userProfile.csv\

d. How to play: \
&nbsp;&nbsp;&nbsp;&nbsp;1.Register an account\
&nbsp;&nbsp;&nbsp;&nbsp;2.You can view your stats by clicking "profile"\
&nbsp;&nbsp;&nbsp;&nbsp;3.You can view your items by clicking "bag"\
&nbsp;&nbsp;&nbsp;&nbsp;4.The game is an auto battler between two players\
&nbsp;&nbsp;&nbsp;&nbsp;5.Both players should ready by clicking "battle" to initiate the battle\
&nbsp;&nbsp;&nbsp;&nbsp;6.During battle, you will roll a die before attacking which determine if an item is used\
&nbsp;&nbsp;&nbsp;&nbsp;7.When battle is completed you will be awarded with ranks and items if you won\
&nbsp;&nbsp;&nbsp;&nbsp;8.Rank affects attack and defence\

e. Other resources: \
&nbsp;&nbsp;&nbsp;&nbsp;1.ChatServer provided by professor Mariana Shimabukuro in canvas.\
&nbsp;&nbsp;&nbsp;&nbsp;2.Button Icons from free opensource website "flaticon.com"