package service;

import model.Core;

import java.util.List;

public interface CoreDAO {
    public boolean save(Core core);
    public Core getById(long id);
    public List<Core> getAll();
    public boolean update(long id, Core core);
    public boolean remove(Core core);
    public boolean contain(Core core);
}
