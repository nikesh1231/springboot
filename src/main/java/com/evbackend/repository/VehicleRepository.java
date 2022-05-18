package com.evbackend.repository;

import com.evbackend.model.vehicle.Vehicle;
import com.evbackend.model.vehicle.VehicleManufacturer;
import com.evbackend.model.vehicle.VehicleModel;
import com.evbackend.model.vehicle.VehicleVersion;
import io.smallrye.mutiny.Uni;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

@Tag(name="Vehicle")
@Component
@RequiredArgsConstructor
public class VehicleRepository {

    private final Mutiny.SessionFactory sessionFactory;

    public Uni<List<Vehicle>> getVehiclesForUser(UUID userId) {
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Vehicle> query = cb.createQuery(Vehicle.class);
        Root<Vehicle> root = query.from(Vehicle.class);
        query.where(cb.equal(root.get("userId"), userId.toString()));
        return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
    }

    public Uni<List<Vehicle>> getActiveVehiclesForUser(UUID userId) {
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Vehicle> query = cb.createQuery(Vehicle.class);
        Root<Vehicle> root = query.from(Vehicle.class);
        query.where(cb.equal(root.get("userId"), userId.toString()), cb.equal(root.get("activeVehicle"), true));
        return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
    }

    public Uni<List<VehicleManufacturer>> getVehicleManufacturers() {
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<VehicleManufacturer> query = cb.createQuery(VehicleManufacturer.class);
        Root<VehicleManufacturer> root = query.from(VehicleManufacturer.class);
        return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
    }

    public Uni<List<VehicleVersion>> getVehicleVersionsForModelAndYear(String year,
                                                                       UUID modelID) {
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<VehicleVersion> query = cb.createQuery(VehicleVersion.class);
        Root<VehicleVersion> root = query.from(VehicleVersion.class);
        query.where(cb.equal(root.get("vehicleModelId"), modelID.toString()), cb.equal(root.get("year"), year));
        return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
    }

    public Uni<List<String>> getYearsForModel(UUID modelID) {
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);

        Root<VehicleVersion> root = query.from(VehicleVersion.class);
        query.select(root.get("year")).distinct(true);
        query.where(cb.equal(root.get("vehicleModelId"), modelID.toString()));

        return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
    }

    public Uni<VehicleManufacturer> getVehicleManufacturer(UUID uuid) {
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<VehicleManufacturer> query = cb.createQuery(VehicleManufacturer.class);
        Root<VehicleManufacturer> root = query.from(VehicleManufacturer.class);
        query.where(cb.equal(root.get("vehicleManufacturerId"), uuid.toString()));
        return this.sessionFactory.withSession(session -> session.createQuery(query).getSingleResultOrNull())
                .onItem()
                .ifNull()
                .switchTo(
                        Uni.createFrom().item(VehicleManufacturer.builder().build())
                );
    }

    public Uni<List<VehicleModel>> getModelsForManufacturer(UUID uuid) {
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<VehicleModel> query = cb.createQuery(VehicleModel.class);
        Root<VehicleModel> root = query.from(VehicleModel.class);
        query.where(cb.equal(root.get("vehicleManufacturerId"), uuid.toString()));
        return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
    }

    public Uni<VehicleModel> getVehicleModel(UUID uuid) {
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<VehicleModel> query = cb.createQuery(VehicleModel.class);
        Root<VehicleModel> root = query.from(VehicleModel.class);
        query.where(cb.equal(root.get("vehicleModelId"), uuid.toString()));
        return this.sessionFactory.withSession(session -> session.createQuery(query).getSingleResultOrNull())
                .onItem()
                .ifNull()
                .switchTo(
                        Uni.createFrom().item(VehicleModel.builder().build())
                );
    }

    public Uni<Vehicle> registerVehicle(Vehicle vehicle) {
        return this.sessionFactory.withSession(
                session -> session.persist(vehicle).chain(session::flush).replaceWith(vehicle));
    }

    public Uni<Vehicle> deregisterVehicle(Vehicle vehicle) {
        return this.sessionFactory.withSession(session -> session.merge(vehicle).onItem().call(session::flush));
    }

    public Uni<VehicleVersion> getVehicleVersion(UUID uuid) {
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<VehicleVersion> query = cb.createQuery(VehicleVersion.class);
        Root<VehicleVersion> root = query.from(VehicleVersion.class);
        query.where(cb.equal(root.get("vehicleVersionId"), uuid.toString()));
        return this.sessionFactory.withSession(session -> session.createQuery(query).getSingleResultOrNull())
                .onItem()
                .ifNull()
                .switchTo(
                        Uni.createFrom().item(VehicleVersion.builder().build())
                );
    }

    public Uni<Vehicle> getVehicle(UUID uuid) {
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Vehicle> query = cb.createQuery(Vehicle.class);
        Root<Vehicle> root = query.from(Vehicle.class);
        query.where(cb.equal(root.get("vehicleId"), uuid.toString()));
        return this.sessionFactory.withSession(session -> session.createQuery(query).getSingleResultOrNull())
                .onItem()
                .ifNull()
                .switchTo(
                        Uni.createFrom().item(Vehicle.builder().build())
                );
    }
    
}
