package com.game.repository;

import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
        try (final Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            session.persist(player);
            transaction.commit();
            return session.find(Player.class, player.getId());
        }
    }

    @Override
    public Player update(Player player) {
        try (final Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            session.update(player);
            transaction.commit();
            return session.find(Player.class , player.getId());
        }
    }

    @Override
    public Optional<Player> findById(long id) {
        try (final Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(Player.class, id));
        }
    }

    @Override
    public void delete(Player player) {
        try (final Session session = sessionFactory.openSession()) {
            session.remove(player);
            session.flush();
            session.clear();
        }

    }

    @PreDestroy
    public void beforeStop() {
        try (final Session session = sessionFactory.openSession()) {
            session.close();
        }
    }
}