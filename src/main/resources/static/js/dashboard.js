console.log("Dashboard Loaded");

const date = new Date();

const today = document.getElementById("todayDate");

if(today){

today.innerHTML = date.toDateString();

}