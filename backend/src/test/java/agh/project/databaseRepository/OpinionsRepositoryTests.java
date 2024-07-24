package agh.project.databaseRepository;

import agh.project.databaseModel.Opinion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class OpinionsRepositoryTests {

    @Autowired
    private OpinionsRepository opinionsRepository;

    @BeforeEach
    public void setUp() {
        opinionsRepository.deleteAll();

        Opinion opinion1 = new Opinion("1", "client1", "car1", "Great car!");
        Opinion opinion2 = new Opinion("2", "client1", "car2", "Not bad.");
        Opinion opinion3 = new Opinion("3", "client2", "car1", "Excellent service!");

        opinionsRepository.saveAll(List.of(opinion1, opinion2, opinion3));
    }

    @Test
    public void testFindAllByCarID() {
        List<Opinion> opinions = opinionsRepository.findAllByCarID("car1");
        assertThat(opinions).hasSize(2);
        assertThat(opinions.stream().map(Opinion::getClientID).toList()).containsExactlyInAnyOrder("client1", "client2");
    }

    @Test
    public void testFindAllByClientID() {
        List<Opinion> opinions = opinionsRepository.findAllByClientID("client1");
        assertThat(opinions).hasSize(2);
        assertThat(opinions.stream().map(Opinion::getCarID).toList()).containsExactlyInAnyOrder("car1", "car2");
    }

    @Test
    public void testFindById() {
        Optional<Opinion> opinion = opinionsRepository.findBy_id("1");
        assertThat(opinion).isPresent();
        assertThat(opinion.get().getCarID()).isEqualTo("car1");
        assertThat(opinion.get().getDescription()).isEqualTo("Great car!");
    }

    @Test
    public void testDeleteById() {
        opinionsRepository.deleteBy_id("1");
        Optional<Opinion> opinion = opinionsRepository.findBy_id("1");
        assertThat(opinion).isNotPresent();
    }
}
