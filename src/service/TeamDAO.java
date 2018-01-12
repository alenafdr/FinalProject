package service;

import model.Core;
import model.Team;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TeamDAO implements CoreDAO{
    private File file = new File("teams.txt");
    private DeveloperDAO developerDAO;
    public TeamDAO() {
        developerDAO = new DeveloperDAO();
    }

    @Override
    public boolean save(Core core){
        Team team = (Team) core;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))){
            bw.write(team.getId() + ","
                    + team.getName() + ","
                    + team.getDevelopersString() + "\r\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Team getById(long id){
        Path input = file.toPath();

        if (id == 0){
            return new Team(0L, "null");
        }
        try (Stream<String> lines = Files.lines(input))
        {
            String results[] = lines.filter(line -> line.startsWith(id + ",")).toArray((String[]::new));
            if (results.length != 0){
                String params[] = results[0].split(",");
                String developers[] = params[2].split(";");
                Team team = new Team(Long.valueOf(params[0]),
                        params[1]);
                for (String idDev : developers){
                    team.setDeveloper(developerDAO.getById(Long.valueOf(idDev)));
                }
                return team;
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
                String developers[] = params[2].split(";");
                Team team = new Team(Long.valueOf(params[0]),
                        params[1]);
                for (String idDev : developers){
                    team.setDeveloper(developerDAO.getById(Long.valueOf(idDev)));
                }
                result.add(team);
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
        Team team = (Team) core;
        boolean result = remove(getById(id));
        if (result){
            save(team);
        }
        return result;
    }

    @Override
    public boolean remove(Core core){
        Team team = (Team) core;
        if (!contain(team)) {
            return false;
        }
        Path input = file.toPath();
        String newLines[] = null;
        try (Stream<String> lines = Files.lines(input))
        {
            newLines = lines.filter(line -> !line.equals(team.getId() + ","
                    + team.getName() + ","
                    + team.getDevelopersString()))
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
        Team team = (Team) core;
        Path input = file.toPath();
        String newLines[] = null;
        try (Stream<String> lines = Files.lines(input))
        {
            newLines = lines.filter(line -> line.equals(team.getId() + ","
                    + team.getName() + ","
                    + team.getDevelopersString()))
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
