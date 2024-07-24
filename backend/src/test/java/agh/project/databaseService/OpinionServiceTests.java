package agh.project.databaseService;

import agh.project.databaseModel.Hire;
import agh.project.databaseModel.Opinion;
import agh.project.databaseRepository.HireRepository;
import agh.project.databaseRepository.OpinionsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OpinionServiceTests {

    @Mock
    private OpinionsRepository opinionsRepository;

    @Mock
    private HireRepository hireRepository;

    @InjectMocks
    private OpinionService opinionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        Opinion opinion = new Opinion("1", "client1", "car1", "Great car!");
        when(opinionsRepository.findAll()).thenReturn(List.of(opinion));

        List<Opinion> opinions = opinionService.findAll();

        assertThat(opinions).containsExactly(opinion);
    }

    @Test
    public void testFindAllByCarID() {
        Opinion opinion = new Opinion("1", "client1", "car1", "Great car!");
        when(opinionsRepository.findAllByCarID("car1")).thenReturn(List.of(opinion));

        List<Opinion> opinions = opinionService.findAllByCarID("car1");

        assertThat(opinions).containsExactly(opinion);
    }

    @Test
    public void testFindAllByClientID() {
        Opinion opinion = new Opinion("1", "client1", "car1", "Great car!");
        when(opinionsRepository.findAllByClientID("client1")).thenReturn(List.of(opinion));

        List<Opinion> opinions = opinionService.findAllByClientID("client1");

        assertThat(opinions).containsExactly(opinion);
    }

    @Test
    public void testAddOpinion_Success() {
        Opinion opinion = new Opinion("1", "client1", "car1", "Great car!");
        Hire hire = new Hire("1", "client1", null, 100, "car1", null, "A");
        when(hireRepository.findAllByClientID("client1")).thenReturn(List.of(hire));
        when(opinionsRepository.save(opinion)).thenReturn(opinion);

        Opinion addedOpinion = opinionService.addOpinion(opinion);

        assertThat(addedOpinion).isEqualTo(opinion);
        verify(opinionsRepository).save(opinion);
    }

    @Test
    public void testAddOpinion_UserHasNotHiredCar() {
        Opinion opinion = new Opinion("1", "client1", "car1", "Great car!");
        when(hireRepository.findAllByClientID("client1")).thenReturn(List.of());

        assertThatThrownBy(() -> opinionService.addOpinion(opinion))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("The user hasn't hired the car yet!");
    }

    @Test
    public void testUpdateOpinionDescription_Success() {
        Opinion opinion = new Opinion("1", "client1", "car1", "Great car!");
        when(opinionsRepository.findBy_id("1")).thenReturn(Optional.of(opinion));
        when(opinionsRepository.save(opinion)).thenReturn(opinion);

        Opinion updatedOpinion = opinionService.updateOpinionDescription("1", "Updated description");

        assertThat(updatedOpinion.getDescription()).isEqualTo("Updated description");
        verify(opinionsRepository).save(opinion);
    }

    @Test
    public void testUpdateOpinionDescription_OpinionNotFound() {
        when(opinionsRepository.findBy_id("1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> opinionService.updateOpinionDescription("1", "Updated description"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No opinion found");
    }

    @Test
    public void testDeleteOpinion() {
        Opinion opinion = new Opinion("1", "client1", "car1", "Great car!");
        when(opinionsRepository.findBy_id("1")).thenReturn(Optional.of(opinion));

        opinionService.deleteOpinion("1");

        verify(opinionsRepository).deleteBy_id("1");
    }
}
