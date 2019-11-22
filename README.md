# edamam-recipes
> Android app to search for recipes, their ingredients and amount. 
> It is also possible to favourite recipes for later usage. Alongside each recipe is a picture that can be enlarged

## How to use
Go to edamam.com, sign up and find 

---
1. api key
2. app id
---
then change gradle.properties file accordingly
### Moving around in the App:
* Search recipes by typing in recipe name  and wait for results. Loading of the pictures may take a while depending on the speed of your internet connection.
* A list will be displayed. Picture of the recipe is on the left, recipe name, and url where to read more information about the recipe on the right.
* Clicking on a recipe results with a menu that displays ingredients and amount. It's also possible to favourite the recipe in this menu.
* Settings menu: Swipe left anywhere on the screen after seeing search results.
## Settings
There are 4 toggles for search parameters:
* Alcohol-free:
* tree-nut-free
* peanut-free
* number of items to display (default is 10 which is also limitation of the API)
