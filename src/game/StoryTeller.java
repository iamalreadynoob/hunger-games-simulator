package game;

import fileReading.DataReading;
import fileReading.SavfReading;
import fileReading.TinfReading;
import fileWriting.TextWriting;

public class StoryTeller
{
    private String storyline;
    public StoryTeller()
    {
        SavfReading reading = new SavfReading();
        reading.scan("database/req-info.savf");
        storyline = "Welcome to the " + reading.getValue("game-number") + " Hunger Games! My name is " + reading.getValue("presentor") + ", I will narrate the story of this extremely amazing action!\n\n";
    }

    public void story()
    {
        TinfReading reading = new TinfReading();
        DataReading namesReading = new DataReading();
        DataReading historyReading = new DataReading();

        reading.scan("database/survivors.tinf");
        namesReading.scan("database/current-pool.csv");
        historyReading.scan("database/events.csv");

        storyline += "Here are the all tributes from each district:\n";

        for (int i = 0; i < 24; i++)
        {
            storyline += "\nthe " + namesReading.getColumn("gender").get(i) + " tribute of district " + namesReading.getColumn("district").get(i) + " " + namesReading.getColumn("name").get(i) + " " + namesReading.getColumn("surname").get(i) + " (" + namesReading.getColumn("age").get(i) + " years old)";
            if (i % 2 == 1) storyline += "\n";
        }
        storyline += "\n";
        int dayAmount = Integer.parseInt(historyReading.getColumn("day").get(historyReading.getColumn("day").size() - 1));

        int loc = 0;
        for (int i = 0; i < dayAmount; i++)
        {
            while (loc < historyReading.getColumn("day").size() && historyReading.getColumn("day").get(loc).equals(Integer.toString(i + 1)))
            {
                storyline += "\n" + historyReading.getColumn("v-name").get(loc) + " " + historyReading.getColumn("v-surname").get(loc) + " has been killed by " + historyReading.getColumn("k-name").get(loc) + " " + historyReading.getColumn("k-surname").get(loc) + ". With that " + historyReading.getColumn("k-name").get(loc) + " " + historyReading.getColumn("k-surname").get(loc) + " has been reached to " + historyReading.getColumn("k-kills").get(loc) + " as amount of kills.";
                loc++;
            }

            if (i != dayAmount - 1) storyline += "\n\nThe day - " + (i+1) + " has been finished. Here are the survivors:\n" + reading.getTexts().get(i) + "\n";
            else storyline += "\n\nThe day - " + (i+1) + " has been finished and we have a champion! Here is the winner:\n" + reading.getTexts().get(i);
        }

        TextWriting.write("database/result.txt", storyline);
    }

}
