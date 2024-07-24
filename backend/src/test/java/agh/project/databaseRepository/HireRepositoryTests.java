package agh.project.databaseRepository;

import agh.project.databaseModel.Date;
import agh.project.databaseModel.FuelValue;
import agh.project.databaseModel.Hire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class HireRepositoryTests {

    @Autowired
    private HireRepository hireRepository;

    @BeforeEach
    public void setUp() {
        hireRepository.deleteAll();

        Date date1 = new Date(LocalDateTime.of(2024, 1, 1, 10, 0), LocalDateTime.of(2024, 1, 10, 10, 0));
        Date date2 = new Date(LocalDateTime.of(2024, 2, 1, 10, 0), LocalDateTime.of(2024, 2, 5, 10, 0));

        Hire hire1 = new Hire("1", "client1", date1, 100, "car1", new FuelValue(50, 75), "confirmed");
        Hire hire2 = new Hire("2", "client2", date2, 150, "car2", new FuelValue(40, 70), "pending");
        Hire hire3 = new Hire("3", "client1", date1, 200, "car3", new FuelValue(30, 65), "completed");

        hireRepository.saveAll(List.of(hire1, hire2, hire3));
    }

    @Test
    public void testFindById() {
        Optional<Hire> hire = hireRepository.findBy_id("1");
        assertThat(hire).isPresent();
        assertThat(hire.get().getClientID()).isEqualTo("client1");
        assertThat(hire.get().getFuel().getStart()).isEqualTo(50);
    }

    @Test
    public void testFindAllByClientID() {
        List<Hire> hires = hireRepository.findAllByClientID("client1");
        assertThat(hires).hasSize(2);
        assertThat(hires.stream().map(Hire::getCarID).toList()).containsExactlyInAnyOrder("car1", "car3");
    }

    @Test
    public void testFindAllByCarID() {
        List<Hire> hires = hireRepository.findAllByCarID("car2");
        assertThat(hires).hasSize(1);
        assertThat(hires.get(0).getClientID()).isEqualTo("client2");
    }

    @Test
    public void testFindAllByPriceLessThan() {
        List<Hire> hires = hireRepository.findAllByPriceLessThan(150);
        assertThat(hires).hasSize(2);
        assertThat(hires.stream().map(Hire::getPrice).toList()).containsExactlyInAnyOrder(100, 150);
    }

    @Test
    public void testFindAllByPriceBetween() {
        List<Hire> hires = hireRepository.findAllByPriceBetween(100.0, 150.0);
        assertThat(hires).hasSize(2);
        assertThat(hires.stream().map(Hire::getPrice).toList()).containsExactlyInAnyOrder(100, 150);
    }

    @Test
    public void testFindAllByStatus() {
        List<Hire> hires = hireRepository.findAllByStatus("confirmed");
        assertThat(hires).hasSize(1);
        assertThat(hires.get(0).getCarID()).isEqualTo("car1");
    }
}
