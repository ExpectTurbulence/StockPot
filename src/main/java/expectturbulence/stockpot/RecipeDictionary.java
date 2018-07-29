package expectturbulence.stockpot;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class RecipeDictionary {
    private ArrayList<Integer> ingredients; // This is for the core ingredients (wheat, sugar,  melon, etc.) - Store as unlocalized name
    private ArrayList<Recipe> recipeList;

    public RecipeDictionary() {
        ingredients = new ArrayList<Integer>(0);
        recipeList = new ArrayList<Recipe>(0);
    }

    public boolean isValidIngredient(Item item) {
        return ingredients.contains(item);
    }

    // Adds a new entry to recipeList consisting of an output and sorted inputs
    public boolean addRecipe(Item output, Item[] input) {
        // Check that input is of a valid size
        if(input.length <= 0) return false;
        int[] inputID = new int[input.length];
        for(int i = 0; i < input.length; i++){
            // Convert array of items to array of item names to be stored
            inputID[i] = Item.REGISTRY.getIDForObject(input[i]);
        }
        for(int itemID : inputID){
            if(!(ingredients.contains(itemID)))ingredients.add(itemID);
        }
        // Sort inputs alphabetically by unlocalized name
        Arrays.sort(inputID);
        recipeList.add(new Recipe(Item.REGISTRY.getIDForObject(output), inputID));
        return true;
    }

    public Item getResult(Item[] input) {
        // Check that input is of a valid size
        if(input.length <= 0) return null;
        int[] inputID = new int[input.length];
        for(int i = 0; i < input.length; i++){
            // Convert array of items to array of item names to be checked
            inputID[i] = Item.REGISTRY.getIDForObject(input[i]);
        }
        // Sort inputs alphabetically by unlocalized name
        Arrays.sort(inputID);
        for(Recipe recipe : recipeList) {
            if(recipe.getInput().equals(inputID)) return Item.REGISTRY.getObjectById(recipe.getOutput());
        }
        return null;
    }

    public class Recipe {
        // Inputs and outputs store as unlocalized names using which Items can be fetched
        private int output;
        private int[] input;
        public Recipe(int output, int[] input) {
            this.output = output;
            this.input = input;
        }
        int getOutput(){return output;}
        int[] getInput(){return input;}
        public String toString(){
            String outputString = Item.REGISTRY.getObjectById(output).getUnlocalizedName() + " | " + Item.REGISTRY.getObjectById(input[0]).getUnlocalizedName();
            for(int i = 1; i < input.length; i++) outputString += ", " + Item.REGISTRY.getObjectById(input[i]).getUnlocalizedName();
            return outputString;
        }
    }

    public String toString(){
        String output = "";
        output += "\n\n- Recipe Dictionary -\n\n";
        output += "Ingredients:\n";
        for(int i : ingredients) output += "-" + Item.REGISTRY.getObjectById(i).getItemStackDisplayName(new ItemStack(Item.REGISTRY.getObjectById(i))) +"\n";
        output += "\nRecipes:\n";
        for(Recipe r : recipeList) output += "-" + r.toString() +"\n";
        return output;
    }
}