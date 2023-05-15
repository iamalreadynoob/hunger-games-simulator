package game;

import fileReading.DataReading;
import fileWriting.DataWriting;
import fileWriting.TextWriting;
import fileWriting.TinfWriting;
import stringHandling.ShortedProcesses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Playground
{

    private ArrayList<String> name, surname, district, gender, age;
    private ArrayList<Integer> weakness, killer, kills, deathsPerDay;

    public Playground()
    {
        fill();
        setDeathsPerDay();
        cases();
    }

    private void fill()
    {
        DataReading reading = new DataReading();
        reading.scan("database/current-pool.csv");

        name = reading.getColumn("name");
        surname = reading.getColumn("surname");
        district = reading.getColumn("district");
        gender = reading.getColumn("gender");
        age = reading.getColumn("age");

        weakness = new ArrayList<>();
        for (String w: reading.getColumn("weakness")) weakness.add(Integer.parseInt(w));

        killer = new ArrayList<>();
        for (String k: reading.getColumn("killer")) killer.add(Integer.parseInt(k));

        kills = new ArrayList<>();
        for (int i = 0; i < 24; i++) kills.add(0);
    }

    private void setDeathsPerDay()
    {
        deathsPerDay = new ArrayList<>();

        int players = 24;
        int day = 1;

        while (players > 1)
        {
            if (day == 1)
            {
                int deaths = new Random().nextInt(6) + 5;
                players -= deaths;
                deathsPerDay.add(deaths);
            }
            else if (day == 2)
            {
                int deaths = new Random().nextInt(5) + 4;
                players -= deaths;
                deathsPerDay.add(deaths);
            }
            else if (day == 3)
            {
                int deaths = new Random().nextInt(4) + 3;
                players -= deaths;
                deathsPerDay.add(deaths);
            }
            else
            {
                int deaths = new Random().nextInt(2) + 1;

                if (players - deaths < 1)
                {
                    players -= 1;
                    deathsPerDay.add(1);
                }
                else
                {
                    players -= deaths;
                    deathsPerDay.add(deaths);
                }
            }
            day++;
        }
    }

    private void cases()
    {
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<String> survivors = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();

        lines.add("day,k-name,k-surname,k-district,k-kills,k-age,k-gender,v-name,v-surname,v-district,v-kills,v-age,v-gender");
        for (int i = 0; i < deathsPerDay.size(); i++)
        {

            for (int j = 0; j < deathsPerDay.get(i); j++)
            {
                String day = Integer.toString(i+1);

                int victimID = detectVictim();
                String victimName = name.get(victimID);
                String victimSurname = surname.get(victimID);
                String victimDistrict = district.get(victimID);
                String victimAge = age.get(victimID);
                String victimGender = gender.get(victimID);
                String victimKills = Integer.toString(kills.get(victimID));
                name.remove(victimID);
                surname.remove(victimID);
                district.remove(victimID);
                age.remove(victimID);
                gender.remove(victimID);
                kills.remove(victimID);
                weakness.remove(victimID);
                killer.remove(victimID);

                int killerID = killerDetect();
                String killerName = name.get(killerID);
                String killerSurname = surname.get(killerID);
                String killerDistrict = district.get(killerID);
                String killerAge = age.get(killerID);
                String killerGender = gender.get(killerID);
                kills.set(killerID, kills.get(killerID) + 1);
                String killerKills = Integer.toString(kills.get(killerID));

                lines.add(day + "," + killerName + "," + killerSurname + "," + killerDistrict + "," + killerKills + "," + killerAge + "," + killerGender + "," + victimName + "," + victimSurname + "," + victimDistrict + "," + victimKills + "," + victimAge + "," + victimGender);
            }

            String dayEnd = "";
            for (int j = 0; j < name.size(); j++)
            {
                dayEnd += "\nthe " + gender.get(j) + " tribute of district " + district.get(j) + " " + name.get(j) + " " + surname.get(j) + " (" + age.get(j) + " years old, has " + kills.get(j) + " kill(s).)";
            }
            survivors.add(dayEnd);

            titles.add("Day " + (i+1));
        }

        TextWriting.write("database/events.csv", lines);
        TinfWriting tinfWriting = new TinfWriting();
        tinfWriting.write("database/survivors.tinf", titles, survivors);
    }


    private Integer detectVictim()
    {
        Integer id = null;
        int total = 0;
        for (Integer val: weakness) total += val;
        int pt = new Random().nextInt(total);

        int ceil = 0;
        int floor = 0;
        for (int i = 0; i < name.size(); i++)
        {
            ceil += weakness.get(i);

            if (pt >= floor && pt < ceil)
            {
                id = i;
                break;
            }

            floor = ceil;
        }

        return id;
    }

    private Integer killerDetect()
    {
        Integer id = null;
        int total = 0;
        for (Integer val: killer) total += val;
        int pt = new Random().nextInt(total);

        int ceil = 0;
        int floor = 0;
        for (int i = 0; i < name.size(); i++)
        {
            ceil += killer.get(i);

            if (pt >= floor && pt < ceil)
            {
                id = i;
                break;
            }
            else {floor = ceil;}
        }

        return id;
    }
}
