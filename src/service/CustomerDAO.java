package service;

import model.Core;
import model.Customer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CustomerDAO implements CoreDAO{
    private File file = new File("customers.txt");
    ProjectDAO projectDAO;

    public CustomerDAO() {
        projectDAO = new ProjectDAO();
    }

    @Override
    public boolean save(Core core) {
        Customer customer = (Customer) core;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))){
            bw.write(customer.getId() + ","
                    + customer.getFirstName() + ","
                    + customer.getLastName() + ","
                    + customer.getAddress() + ","
                    + customer.getProjectsString() +
                    "\r\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Customer getById(long id) {
        Path input = file.toPath();

        if (id == 0){
            return new Customer(0L, "null", "null", "null");
        }
        try (Stream<String> lines = Files.lines(input))
        {
            String results[] = lines.filter(line -> line.startsWith(id + ",")).toArray((String[]::new));
            if (results.length != 0){
                String params[] = results[0].split(",");
                String projects[] = params[4].split(";");
                Customer customer = new Customer(Long.valueOf(params[0]),
                        params[1],
                        params[2],
                        params[3]
                );
                for (String idProject : projects){
                    customer.setProject(projectDAO.getById(Long.valueOf(idProject)));
                }
                return customer;
            }
        } catch (NoSuchFileException e){
            return null;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Core> getAll() {
        List<Core> result = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String params[] = line.split(",");
                String projects[] = params[4].split(";");
                Customer customer = new Customer(Long.valueOf(params[0]),
                        params[1],
                        params[2],
                        params[3]
                );
                for (String idProject : projects) {
                    customer.setProject(projectDAO.getById(Long.valueOf(idProject)));
                }
                result.add(customer);
            }
        } catch (FileNotFoundException e) {
            //e.printStackTrace();

        } catch (IOException e) {
            //e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean update(long id, Core core) {
        Customer customer = (Customer) core;
        boolean result = remove(getById(id));
        if (result){
            save(customer);
        }
        return result;
    }

    @Override
    public boolean remove(Core core) {
        Customer customer = (Customer) core;
        if (!contain(customer)) {
            return false;
        }
        Path input = file.toPath();
        String newLines[] = null;
        try (Stream<String> lines = Files.lines(input))
        {
            newLines = lines.filter(line -> !line.equals(customer.getId() + ","
                    + customer.getFirstName() + ","
                    + customer.getLastName() + ","
                    + customer.getAddress() + ","
                    + customer.getProjectsString()))
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

        Customer customer = (Customer) core;
        Path input = file.toPath();
        String newLines[] = null;
        try (Stream<String> lines = Files.lines(input))
        {
            newLines = lines.filter(line -> line.equals(customer.getId() + ","
                    + customer.getFirstName() + ","
                    + customer.getLastName() + ","
                    + customer.getAddress() + ","
                    + customer.getProjectsString()))
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
