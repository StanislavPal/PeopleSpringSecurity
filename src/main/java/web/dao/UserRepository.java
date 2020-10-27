package web.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import web.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>{
}
