package service;

import model.Core;
import model.Skill;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SkillDAO implements CoreDAO {
    private File file = new File("skills.txt");

    @Override
    public boolean save(Core core){
        Skill skill = (Skill) core;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))){
            bw.write(skill.getId() + ","
                    + skill.getName() + "\r\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Skill getById(long id){
        Path input = file.toPath();

        if (id == 0){
            return new Skill(0L, "no skill");
        }
        try (Stream<String> lines = Files.lines(input))
        {
            String results[] = lines.filter(line -> line.startsWith(id + ",")).toArray((String[]::new));
            if (results.length != 0){
                String params[] = results[0].split(",");
                return new Skill(Long.valueOf(params[0]),
                        params[1]);
            }
        } catch (NoSuchFileException e){
            return null;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Core> getAll(){
        List<Core> result = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr))
        {
            String line;
            while((line = br.readLine()) != null){
                String params[] = line.split(",");
                Skill skill = new Skill(Long.valueOf(params[0]),
                        params[1]);
                result.add(skill);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean update(long id, Core core){
        Skill skill = (Skill) core;
        boolean result = remove(getById(id));
        if (result){
            save(skill);
        }
        return result;
    }

    @Override
    public boolean remove(Core core){
        Skill skill = (Skill) core;
        if (!contain(skill)) {
            return false;
        }
        Path input = file.toPath();
        String newLines[] = null;
        try (Stream<String> lines = Files.lines(input))
        {
            newLines = lines.filter(line -> !line.equals(skill.getId() + ","
                    + skill.getName()))
                    .toArray(String[]::new);
        } catch (IOException e){
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false)))
        {
            for (String line : newLines){
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean contain(Core core){
        Skill skill = (Skill) core;
        Path input = file.toPath();
        String newLines[] = null;
        try (Stream<String> lines = Files.lines(input))
        {
            newLines = lines.filter(line -> line.equals(skill.getId() + ","
                    + skill.getName()))
                    .toArray(String[]::new);

            return newLines.length != 0;
        } catch (IOException e){
            e.printStackTrace();
            return false;
        } catch (NullPointerException e){
            return false;
        }
    }
}
