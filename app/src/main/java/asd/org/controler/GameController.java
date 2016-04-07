package asd.org.controler;

/**
 * Created by lucas on 6 abr 2016.
 */
public class GameController {

    public char[] findLetter(char[] originalWorld, char[] world, CharSequence letter) {
        boolean existLetter = false;
        for (int i = 0; i < originalWorld.length; i++) {
            if (letter.toString().toCharArray()[0] == originalWorld[i]) {
                world[i] = originalWorld[i];
                existLetter = true;
            }
        }
        if (!existLetter) {
            //TODO:Restar vida
        }
        return world;
    }

    public String setText() {
        StringBuilder myWorld = new StringBuilder();
        char[] worldChar = new char[getRandomWorld().toCharArray().length];
        for (int i = 0; i < worldChar.length; i++) {
            myWorld.append("_");
        }
        return myWorld.toString();
    }

    private String getRandomWorld() {
        //TODO get world
        return "MUBER";
    }

    public String myNewWorld(char[] myWorld) {
        StringBuilder world = new StringBuilder();
        for (int i = 0; i < myWorld.length; i++) {
            world.append(myWorld[i]);
        }
        return world.toString();
    }
}
