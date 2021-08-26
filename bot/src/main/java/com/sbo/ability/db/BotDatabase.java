package com.sbo.ability.db;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.db.Var;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author viktar hraskou
 */
// TODO real database.
public class BotDatabase implements DBContext {
    @Override
    public <T> List<T> getList(String name) {
        return null;
    }

    @Override
    public <K, V> Map<K, V> getMap(String name) {
        return null;
    }

    @Override
    public <T> Set<T> getSet(String name) {
        return null;
    }

    @Override
    public <T> Var<T> getVar(String name) {
        return null;
    }

    @Override
    public String summary() {
        return null;
    }

    @Override
    public Object backup() {
        return null;
    }

    @Override
    public boolean recover(Object backup) {
        return false;
    }

    @Override
    public String info(String name) {
        return null;
    }

    @Override
    public void commit() {

    }

    @Override
    public void clear() {

    }

    @Override
    public boolean contains(String name) {
        return false;
    }

    @Override
    public void close() throws IOException {

    }
}
