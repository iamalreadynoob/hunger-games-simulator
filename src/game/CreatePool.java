package game;

import fileReading.DataReading;
import fileWriting.TextWriting;

import java.util.ArrayList;
import java.util.Random;

public class CreatePool
{
    private ArrayList<String> nameList, genderList, surnameList, boyList, girlList;
    public CreatePool()
    {
        fill();
        create();
    }

    private void create()
    {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("id,name,surname,district,gender,age,weakness,killer");

        int district = 1;

        for (int i = 0; i < 24; i++)
        {
            String gender, name;
            if (i % 2 == 0)
            {
                gender = "male";
                int nameID = new Random().nextInt(boyList.size());
                name = boyList.get(nameID).toLowerCase();
                boyList.remove(nameID);
            }
            else
            {
                gender = "female";

                int nameID = new Random().nextInt(girlList.size());
                name = girlList.get(nameID).toLowerCase();
                girlList.remove(nameID);
            }

            if (i % 2 == 0 && i != 0) {district++;}

            String id = Integer.toString(i);

            int surnameID = new Random().nextInt(surnameList.size());
            String surname = surnameList.get(surnameID).toLowerCase();
            surnameList.remove(surnameID);

            String dist = Integer.toString(district);
            String age = Integer.toString(13 + new Random().nextInt(5));
            String death = Integer.toString(deathPossibility(district, Integer.parseInt(age)));
            String ability = Integer.toString(killingAbility(district, Integer.parseInt(age)));

            lines.add(id + "," + name + "," + surname + "," + dist + "," + gender + "," + age + "," + death + "," + ability);
        }

        TextWriting.write("database/current-pool.csv", lines);
    }

    private void fill()
    {
        DataReading nameReading = new DataReading();
        nameReading.scan("database/names.csv");

        DataReading surnameReading = new DataReading();
        surnameReading.scan("database/surnames.csv");

        nameList = nameReading.getColumn("name");
        genderList = nameReading.getColumn("gender");
        surnameList = surnameReading.getColumn("name");

        boyList = new ArrayList<>();
        girlList = new ArrayList<>();

        for (int i = 0; i < nameList.size(); i++)
        {
            if (genderList.get(i).equals("boy")) boyList.add(nameList.get(i));
            else girlList.add(nameList.get(i));
        }
    }

    private int deathPossibility(int district, int age)
    {
        int possibility = new Random().nextInt(10);

        if (district == 1 || district == 2) possibility += 10;
        else if (district == 4 || district == 7 || district == 11) possibility += 20;
        else if (district == 8 || district == 9 || district == 10) possibility += 30;
        else possibility += 40;

        possibility += (17 - age) * 5;

        return possibility;
    }

    private int killingAbility(int district, int age)
    {
        int possibility = new Random().nextInt(10);

        if (district == 1 || district == 2) possibility += 30;
        else if (district == 4 || district == 7 || district == 11) possibility += 20;
        else if (district == 8 || district == 9 || district == 10) possibility += 10;
        else possibility += 1;

        possibility -= (17 - age) * 2;

        if (possibility <= 0) possibility = 1;

        return possibility;
    }

}
