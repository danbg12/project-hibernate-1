package com.game.repository;

import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;

@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {

    private final SessionFactory sessionFactory;

    public PlayerRepositoryDB() {
        sessionFactory = new Configuration()
                .addAnnotatedClass(Player.class)
                .buildSessionFactory();
    }

    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {
        try (final Session session = sessionFactory.openSession()) {
            final NativeQuery<Player> nativeQuery = session
                    .createNativeQuery("SELECT * FROM rpg.player", Player.class)
                    .setFirstResult(pageNumber)
                    .setMaxResults(pageSize);
            return nativeQuery.getResultList();
        }
    }

    @Override
    public int getAllCount() {
        try (final Session session = sessionFactory.openSession()) {
            final Query<Integer> namedQuery = session.createNamedQuery("First.query", Integer.class);
            return namedQuery.getSingleResult();
        }
    }

    @Override
    public Player save(Player player) {
        return null;
    }

    @Override
    public Player update(Player player) {
        return null;
    }

    @Override
    public Optional<Player> findById(long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Player player) {

    }

    @PreDestroy
    public void beforeStop() {
        try (final Session session = sessionFactory.openSession()) {
            session.close();
        }
    }
}