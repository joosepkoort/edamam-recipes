# edamam-recipes
> Android app to search for recipes, their ingredients and amount. 
> It is also possible to favourite recipes for later usage. Alongside each recipe is a picture, which can be enlarged.

## How to use
Go to edamam.com, sign up and find 

---
1. api key
2. app id
---
then change gradle.properties file accordingly
### Moving around in the App:
* Enter a recipe name, and wait for results. Depending on the speed of the internet loading pictures may take awhile.
* A list will be displayed. Picture of the recipe on the left, recipe name, and url where to read more information about the recipe.
* When clicked on the recipe, a menu will be displayed with ingredients and amount. It is also possible to favourite the recipe in this menu.
* to access settings menu: Swipe left anywhere on the screen after seeing search results.
## Settings
There are 4 toggles for search parameters:
* Alcohol-free:
* tree-nut-free
* peanut-free
* number of items to display (default is 10 which is also limitation of the API)
