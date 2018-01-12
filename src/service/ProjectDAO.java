package service;

import model.Core;
import model.Project;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ProjectDAO implements CoreDAO{
    private File file = new File("projects.txt");
    TeamDAO teamDAO;


    public ProjectDAO() {
        teamDAO = new TeamDAO();
    }

    @Override
    public boolean save(Core core){
        Project project = (Project) core;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))){
            bw.write(project.getId() + ","
                    + project.getName() + ","
                    + project.getTeamsString() + "\r\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Project getById(long id){
        Path input = file.toPath();

        if (id == 0){
            return new Project(0L, "null");
        }
        try (Stream<String> lines = Files.lines(input))
        {
            String results[] = lines.filter(line -> line.startsWith(id + ",")).toArray((String[]::new));
            if (results.length != 0){
                String params[] = results[0].split(",");
                String projects[] = params[2].split(";");
                Project project = new Project(Long.valueOf(params[0]),
                        params[1]);
                for (String idTeam : projects){
                    project.setTeam(teamDAO.getById(Long.valueOf(idTeam)));
                }
                return project;
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
                String teams[] = params[2].split(";");
                Project project = new Project(Long.valueOf(params[0]),
                        params[1]);
                for (String idTeam : teams){
                    project.setTeam(teamDAO.getById(Long.valueOf(idTeam)));
                }
                result.add(project);
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
        Project project = (Project) core;
        boolean result = remove(getById(id));
        if (result){
            save(project);
        }
        return result;
    }

    @Override
    public boolean remove(Core core){
        Project project = (Project) core;
        if (!contain(project)) {
            return false;
        }
        Path input = file.toPath();
        String newLines[] = null;
        try (Stream<String> lines = Files.lines(input))
        {
            newLines = lines.filter(line -> !line.equals(project.getId() + ","
                    + project.getName() + ","
                    + project.getTeamsString()))
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
        Project project = (Project) core;
        Path input = file.toPath();
        String newLines[] = null;
        try (Stream<String> lines = Files.lines(input))
        {
            newLines = lines.filter(line -> line.equals(project.getId() + ","
                    + project.getName() + ","
                    + project.getTeamsString()))
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
