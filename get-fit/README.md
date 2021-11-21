# Get Fit

Get Fit is an application for storing and logging activity and exercise. The user will add data like duration, exercise form and a comment when adding a new session. The application will store and organize the data and can do calculations based on the input data of the user. Examples of these calculations can be average pace per kilometer and calories burnt during a workout. These calculations can also be expanded in the future. Because the sessions include the date, the user can choose to view the compressed data for a time period, like how many hours the user has exercised during a year. 


## Design Documentation

User stories, scenarios and design models can be found here:
[Design documentation](/design-documentation)


## How to use the application 

This JavaFX-application are designed for logging and viewing workouts. The first time you open the app you will get an empty list of workouts. 

![Getfit1](/uploads/8af34ce065d46e7cc2cd55cc4df4f6c7/Getfit1.png)

You can create a new workout by pressing the “add workout” button. This button will take you to a new page where you can log your session. Your entry can include these elements: title, date, duration, exercise type, tag, maximum heartrate, feeling and comment. If you choose the exercise type “running”, “cycling” or “swimming” can you also add distance to your entry.  

![Getfit2](/uploads/e4720e506ad52ad146a8d3e34bc01adb/Getfit2.png) 

When you are happy with your description of the workout press the button “Create session” and the app takes you back to the start page. The newly created workout will be added to the workout list. 

![Getfit3](/uploads/66e7e7abf8cefd0c8c296d711e0240ed/Getfit3.png)

Above the list will you find three different dropdown menus. These menus let you choose different ways to sort and filter your workout sessions. The first menu let you sort on title, date and duration and the second menu filters on exercise type. 

![Getfit4](/uploads/78d03c470389ffba32a4f8828dbfc726/Getfit4.png)

After choosing an exercise type can you use the third menu to filter on tags. 

![getfit5](/uploads/94f8953984c6ac1af2318b04c98b4f91/getfit5.png) 

You can also choose to reverse the list of workouts by checking the reverse checkbox.

![getfit6](/uploads/63d648fc74991632e5f6070eedc251d6/getfit6.png) 

If you push the “create workout” by mistake, use the return button to get back to the start page. This application will also give you statistics based on your logged workouts. If you want to view the statistics press the button “Statistics” and you will be sent to the statistics page. 
 
![getfit7](/uploads/b1350e6deb991c30d3e6db0c42220d1a/getfit7.png)

This page shows statistics based on your workouts between to dates. The page includes a graph with numbers of workouts based on exercise type. It also includes a list of statistics including total duration, number of sessions, average feeling, maximum heart rate, average speed and average duration. This list will change based on the exercise type you choose from a dropdown menu.  You choose the two dates at the top of the page and press enter to update graph and statistics list.

![getfit8](/uploads/acf71613086c4e462bd91d6f286a8525/getfit8.png)

Press return button to get back to start page. You can view an entry at the start page by pressing show. A new window will pop up with all information about your workout.
 
![getfit9](/uploads/62e1ffe9eeff1f215e09e43294a95280/getfit9.png)

If you want to close the page press the X-button in the right corner. You can also delete your logged entries by pressing the delete button next to the show button. 

![getfit10](/uploads/e8fd9916e5ea9db01445d36e47b7c71a/getfit10.png)

