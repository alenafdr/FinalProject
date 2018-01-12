package service;

import model.Company;
import model.Core;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CompanyDAO implements CoreDAO{
    private File file = new File("company.txt");
    ProjectDAO projectDAO;

    public CompanyDAO() {
        projectDAO = new ProjectDAO();
    }

    @Override
    public boolean save(Core core) {
        Company company = (Company) core;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))){
            bw.write(company.getId() + ","
                    + company.getName() + ","
                    + company.getProjectsString() +
                    "\r\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Company getById(long id){
        Path input = file.toPath();

        if (id == 0){
            return new Company(0L, "null");
        }
        try (Stream<String> lines = Files.lines(input))
        {
            String results[] = lines.filter(line -> line.startsWith(id + ",")).toArray((String[]::new));
            if (results.length != 0){
                String params[] = results[0].split(",");
                String projects[] = params[2].split(";");
                Company company = new Company(Long.valueOf(params[0]),
                        params[1]);
                for (String idProject : projects){
                    company.setProject(projectDAO.getById(Long.valueOf(idProject)));
                }
                return company;
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
                String projects[] = params[2].split(";");
                Company company = new Company(Long.valueOf(params[0]),
                        params[1]);
                for (String idProject : projects){
                    company.setProject(projectDAO.getById(Long.valueOf(idProject)));
                }
                result.add(company);
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return result;
    }
    @Override
    public boolean update(long id, Core core) {
        Company company = (Company) core;
        boolean result = remove(getById(id));
        if (result){
            save(company);
        }
        return result;
    }

    @Override
    public boolean remove(Core core) {
        Company company = (Company) core;
        if (!contain(company)) {
            return false;
        }
        Path input = file.toPath();
        String newLines[] = null;
        try (Stream<String> lines = Files.lines(input))
        {
            newLines = lines.filter(line -> !line.equals(company.getId() + ","
                    + company.getName() + ","
                    + company.getProjectsString()))
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
    public boolean contain(Core core) {
        Company company = (Company) core;
        Path input = file.toPath();
        String newLines[] = null;
        try (Stream<String> lines = Files.lines(input))
        {
            newLines = lines.filter(line -> line.equals(company.getId() + ","
                    + company.getName() + ","
                    + company.getProjectsString()))
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
