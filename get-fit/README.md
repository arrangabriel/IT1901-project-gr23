# Get Fit

Get Fit is an application for storing and logging activity and exercise. The user will add data like duration, exercise form and a comment when adding a new session. The application will store and organize the data and can do calculations based on the input data of the user. Examples of these calculations can be average pace per kilometer and calories burnt during a workout. These calculations can also be expanded in the future. Because the sessions include the date, the user can choose to view the compressed data for a time period, like how many hours the user has exercised during a year. 


## Design Documentation

User stories, scenarios and design models can be found here:
[Design documentation](/design-documentation)


## How to use the application 

This JavaFX-application are designed for logging and viewing workouts. The first time you open the app you will get an empty list of workouts. 


<img width="350" src = "/uploads/562c5470c0077cbb2052431383e2fe20/Getfit1.png">

You can create a new workout by pressing the “add workout” button. This button will take you to a new page where you can log your session. Your entry can include these elements: title, date, duration, exercise type, tag, maximum heartrate, feeling and comment. If you choose the exercise type “running”, “cycling” or “swimming” can you also add distance to your entry.  

<img width="350" src = "/uploads/ccd0101d85e31fdd19f3cafa17c52001/Getfit2.png">

When you are happy with your description of the workout press the button “Create session” and the app takes you back to the start page. The newly created workout will be added to the workout list. 

<img width="350" src = "/uploads/f8ff43d0ad1482bd9d092ed69e71f5b6/Getfit3.png">

Above the list will you find three different dropdown menus. These menus let you choose different ways to sort and filter your workout sessions. The first menu let you sort on title, date and duration and the second menu filters on exercise type. 

<img width="350" src = "/uploads/feb7d44e7570840529bbc4d339a9a1b6/Getfit4.png">

After choosing an exercise type can you use the third menu to filter on tags. 

<img width="350" src = "/uploads/933f621fda5bf5f80e4471b2b9246bf1/getfit5.png"> 

You can also choose to reverse the list of workouts by checking the reverse checkbox.

<img width="350" src = "/uploads/22b6594604611974a5d01d2a878dfc01/getfit6.png"> 

If you push the “create workout” by mistake, use the return button to get back to the start page. This application will also give you statistics based on your logged workouts. If you want to view the statistics press the button “Statistics” and you will be sent to the statistics page. 
 
<img width="350" src = "/uploads/2786d0d29d18f3d0b5fd52a156fd0c91/getfit7.png">

This page shows statistics based on your workouts between to dates. The page includes a graph with numbers of workouts based on exercise type. It also includes a list of statistics including total duration, number of sessions, average feeling, maximum heart rate, average speed and average duration. This list will change based on the exercise type you choose from a dropdown menu.  You choose the two dates at the top of the page and press enter to update graph and statistics list.

<img width="350" src = "/uploads/7479c188845706a591a4064b6f81781e/getfit8.png">

Press return button to get back to start page. You can view an entry at the start page by pressing show. A new window will pop up with all information about your workout.
 
<img width="350" src = "/uploads/e64413633d9fba9b26953153ba8d5990/getfit9.png">

If you want to close the page press the X-button in the right corner. You can also delete your logged entries by pressing the delete button next to the show button. 

<img width="350" src = "/uploads/666a6aad5287c274fe341b254adf41c9/getfit10.png">

