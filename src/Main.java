import game.CreatePool;
import game.Playground;
import game.StoryTeller;

public class Main {
    public static void main(String[] args)
    {
        new CreatePool();
        new Playground();
        new StoryTeller().story();
    }
}