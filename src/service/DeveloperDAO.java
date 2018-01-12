package service;

import model.Core;
import model.Developer;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DeveloperDAO implements CoreDAO{
    private File file = new File("developers.txt");
    private SkillDAO skillDAO;

    public DeveloperDAO() {
        skillDAO = new SkillDAO();
    }

    @Override
    public boolean save(Core core){
        Developer developer = (Developer) core;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))){
            bw.write(core.getId() + ","
                    + developer.getFirstName() + ","
                    + developer.getLastName() + ","
                    + developer.getSpecialty() + ","
                    + developer.getSalary() + ","
                    + developer.getSkillsString() + "\r\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Developer getById(long id){
        Path input = file.toPath();

        if (id == 0){
            return new Developer(0L, "null", "null", "null", new BigDecimal(0));
        }
        try (Stream<String> lines = Files.lines(input))
        {
            String results[] = lines.filter(line -> line.startsWith(id + ",")).toArray((String[]::new));
            if (results.length != 0){
                String params[] = results[0].split(",");
                String skills[] = params[5].split(";");
                Developer developer = new Developer(Long.valueOf(params[0]),
                        params[1],
                        params[2],
                        params[3],
                        BigDecimal.valueOf(Long.valueOf( params[4])));
                for (String idSkill : skills){
                    developer.setSkill(skillDAO.getById(Long.valueOf(idSkill)));
                }
                return developer;
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
                String skills[] = params[5].split(";");
                Developer developer = new Developer(Long.valueOf(params[0]),
                        params[1],
                        params[2],
                        params[3],
                        BigDecimal.valueOf(Long.valueOf( params[4])));

                for (String id : skills){
                    try {
                        developer.setSkill(skillDAO.getById(Long.valueOf(id)));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                result.add(developer);
            }
        } catch (FileNotFoundException e){
            //e.printStackTrace();

        }catch (IOException e) {
            //e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean update(long id, Core core){
        Developer developer = (Developer) core;
        boolean result = remove(getById(id));
        if (result){
            save(developer);
        }
        return result;
    }

    @Override
    public boolean remove(Core core){
        Developer developer = (Developer) core;
        if (!contain(developer)) {
            return false;
        }
        Path input = file.toPath();
        String newLines[] = null;

        try (Stream<String> lines = Files.lines(input))
        {
            newLines = lines.filter(line -> !line.equals(developer.getId() + ","
                    + developer.getFirstName() + ","
                    + developer.getLastName() + ","
                    + developer.getSpecialty() + ","
                    + developer.getSalary() + ","
                    + developer.getSkillsString()))
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
        Developer developer = (Developer) core;
        Path input = file.toPath();
        String newLines[] = null;
        try (Stream<String> lines = Files.lines(input))
        {
            newLines = lines.filter(line -> line.equals(developer.getId() + ","
                    + developer.getFirstName() + ","
                    + developer.getLastName() + ","
                    + developer.getSpecialty() + ","
                    + developer.getSalary() + ","
                    + developer.getSkillsString()))
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
