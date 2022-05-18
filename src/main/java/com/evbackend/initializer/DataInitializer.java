package com.evbackend.initializer;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.evbackend.enumerate.ConnectorStatus;
import com.evbackend.enumerate.VehicleColor;
import com.evbackend.model.chargestation.*;
import com.evbackend.model.faults.Faults;
import com.evbackend.model.users.Account;
import com.evbackend.model.users.User;
import com.evbackend.model.Transaction;
import com.evbackend.model.address.Address;
import com.evbackend.model.chargestation.ChargeSite;
import com.evbackend.model.chargestation.ChargeStation;
import com.evbackend.model.chargestation.ChargeStationModel;
import com.evbackend.model.chargestation.Connector;
import com.evbackend.model.chargestation.ConnectorType;
import com.evbackend.model.chargestation.Review;
import com.evbackend.model.users.Account;
import com.evbackend.model.users.User;
import com.evbackend.model.users.UserRole;
import com.evbackend.model.vehicle.Vehicle;
import com.evbackend.model.vehicle.VehicleManufacturer;
import com.evbackend.model.vehicle.VehicleModel;
import com.evbackend.model.vehicle.VehicleVersion;
import com.evbackend.security.PBKDF2Encoder;

import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final static Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());

    private final Mutiny.SessionFactory sessionFactory;

    @Autowired
    PBKDF2Encoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOGGER.info("Data initialization is starting...");


        String encodedPassword = this.passwordEncoder.encode("password01!");
        Address adminAddress1 = Address.builder()
                .latitude(100.0)
                .longitude(100.0)
                .streetAddress("21 Fake St")
                .administrativeArea("NSW")
                .locality("Sydney")
                .country("AU")
                .postCode("0209").build();

        User adminUser1 = User.builder()
                .activeUser(true)
                .userName("sally_admin")
                .address(adminAddress1)
                .firstName("Sally")
                .lastName("Smith")
                .email("sally@example.com")
                .phoneNumber("042319822")
                .password(encodedPassword).build();
        
        Address adminAddress2 = Address.builder()
                .latitude(100.0)
                .longitude(100.0)
                .streetAddress("3418 Small Street")
                .administrativeArea("New York")
                .locality("New York")
                .country("AU")
                .postCode("10019").build(); 
          
        User adminUser2 = User.builder()
                .activeUser(true)
                .userName("emanuel_admin")
                .address(adminAddress2)
                .firstName("Emanuel")
                .lastName("tm")
                .email("Emanuel@example.com")
                .phoneNumber("212-830-3024")
                .password(encodedPassword).build();        

        Address paygAddress1 = Address.builder()
                .latitude(100.0)
                .longitude(100.0)
                .streetAddress("23 Fake St")
                .administrativeArea("Los Angeles")
                .locality("California")
                .country("US")
                .postCode("102-021").build();

        User paygUser1 = User.builder()
                .activeUser(true)
                .userName("peter_payg")
                .address(paygAddress1)
                .firstName("Peter")
                .lastName("North")
                .email("pete@example.com")
                .phoneNumber("555-333-222")
                .password(encodedPassword).build();       
        
        Address paygAddress2 = Address.builder()
                .latitude(120.0)
                .longitude(120.0)
                .streetAddress("51 st Main")
                .administrativeArea("New York.")
                .locality("California").country("US")
                .postCode("678-354")
                .build();

        User paygUser2 = User.builder()
                .activeUser(true)
                .userName("jos_payg")
                .address(paygAddress2)
                .firstName("andrew")
                .lastName("bt")
                .email("jos@example.com")
                .phoneNumber("666-333-222")
                .password(encodedPassword).build();

        Address paygAddress3 = Address.builder()
                .latitude(110.0)
                .longitude(110.0)
                .streetAddress("26 th Cross")
                .administrativeArea("Phoenix.")
                .locality("California")
                .country("US")
                .postCode("689-354").build();
        
        User paygUser3 = User.builder()
                .activeUser(true)
                .userName("samson_payg")
                .address(paygAddress3)
                .firstName("sam")
                .lastName("sn")
                .email("samson@example.com")
                .phoneNumber("456-123-222")
                .password(encodedPassword).build();

        Address paygAddress4 = Address.builder()
                .latitude(115.0)
                .longitude(115.0)
                .streetAddress("2823 Saints Alley")
                .administrativeArea("Florida")
                .locality("California")
                .country("US")
                .postCode("33602").build();

        User paygUser4 = User.builder()
                .activeUser(true)
                .userName("riyan_payg")
                .address(paygAddress4)
                .firstName("riya")
                .lastName("rn")
                .email("riyan@example.com")
                .phoneNumber("813-491-1149")
                .password(encodedPassword).build();

        Address paygAddress5 = Address.builder()
                .latitude(115.0)
                .longitude(115.0)
                .streetAddress("1383 Victoria Street")
                .administrativeArea("Louisiana")
                .locality("Baton Rouge")
                .country("US")
                .postCode("70801").build();

        User paygUser5 = User.builder()
                .activeUser(true)
                .userName("jason_payg")
                .address(paygAddress5)
                .firstName("jas")
                .lastName("hr")
                .email("jason@example.com")
                .phoneNumber("225-207-7666")
                .password(encodedPassword).build();

        Address managerAddress1 = Address.builder()
                .latitude(115.0)
                .longitude(115.0)
                .streetAddress("1128 Water Street")
                .administrativeArea("New Orleans")
                .locality("Baton Rouge")
                .country("US")
                .postCode("70801").build();

        User managerUser1 = User.builder()
                .activeUser(true)
                .userName("zayne_manager")
                .address(managerAddress1)
                .firstName("manager")
                .lastName("mv")
                .email("manager@example.com")
                .phoneNumber("925-134-2813")
                .password(encodedPassword).build();

        Address managerAddress2 = Address.builder()
                .latitude(125.0)
                .longitude(125.0)
                .streetAddress("2607 Circle Drivet")
                .administrativeArea("Houston")
                .locality("Texas")
                .country("US")
                .postCode("77032").build();

        User managerUser2 = User.builder()
                .activeUser(true)
                .userName("matias_manager")
                .address(managerAddress2)
                .firstName("Matias")
                .lastName("wood")
                .email("wood@example.com")
                .phoneNumber("832-561-5210")
                .password(encodedPassword).build();

        Address deviceTechnicianAddress1 = Address.builder()
                .latitude(135.0)
                .longitude(135.0)
                .streetAddress("3567 Dale Avenue")
                .administrativeArea("Seattle")
                .locality("Washington")
                .country("US")
                .postCode("98119").build();

        User deviceTechnicianUser1 = User.builder()
                .activeUser(true)
                .userName("brody_devtec")
                .address(deviceTechnicianAddress1)
                .firstName("Brody")
                .lastName("Horton")
                .email("Horton@example.com")
                .phoneNumber("253-750-4014")
                .password(encodedPassword).build();
               
        Address deviceTechnicianAddress2 = Address.builder()
                .latitude(135.0)
                .longitude(135.0)
                .streetAddress("306 Graystone Lakes")
                .administrativeArea("Macon")
                .locality("Georgia")
                .country("US")
                .postCode("31210").build();

        User deviceTechnicianUser2 = User.builder()
                .activeUser(true)
                .userName("cristian_devtec")
                .address(deviceTechnicianAddress2)
                .firstName("Cristian")
                .lastName("Watkins")
                .email("Watkins@example.com")
                .phoneNumber("478-477-7519")
                .password(encodedPassword).build();
                
        Address csoAddress1 = Address.builder()
                .latitude(145.0)
                .longitude(145.0)
                .streetAddress("4857 Ryan Road")
                .administrativeArea("Hartford")
                .locality(" South Dakota")
                .country("US")
                .postCode("57033").build();

        User csoUser1 = User.builder()
                .activeUser(true)
                .userName("august_cso")
                .address(csoAddress1)
                .firstName("August")
                .lastName("long")
                .email("long@example.com")
                .phoneNumber("605-528-1040")
                .password(encodedPassword).build();   
                
        Address csoAddress2 = Address.builder()
                .latitude(145.0)
                .longitude(145.0)
                .streetAddress("2695 Margaret Street")
                .administrativeArea("Houston")
                .locality(" South Dakota")
                .country("US")
                .postCode("77032").build();

        User csoUser2 = User.builder()
                .activeUser(true)
                .userName("luke_cso")
                .address(csoAddress2)
                .firstName("Luke")
                .lastName("Lambert")
                .email("Lambert@example.com")
                .phoneNumber("713-875-5433")
                .password(encodedPassword).build();
                
        Address businessManagerAddress1 = Address.builder()
                .latitude(145.0)
                .longitude(145.0)
                .streetAddress("422 Roosevelt Road")
                .administrativeArea("Kansas")
                .locality("South Dakota")
                .country("US")
                .postCode("67035").build();

        User businessManagerUser1 = User.builder()
                .activeUser(true)
                .userName("riley_bm")
                .address(businessManagerAddress1)
                .firstName("Riley")
                .lastName("Young")
                .email("Young@example.com")
                .phoneNumber("620-298-4087")
                .password(encodedPassword).build();
                
        Address businessManagerAddress2 = Address.builder()
                .latitude(155.0)
                .longitude(155.0)
                .streetAddress("1476 Deer Ridge Drive")
                .administrativeArea("Mississippi")
                .locality("Mississippi")
                .country("US")
                .postCode("08901").build();

        User businessManagerUser2 = User.builder()
                .activeUser(true)
                .userName("ashton_bm")
                .address(businessManagerAddress2)
                .firstName("Ashton")
                .lastName("Young")
                .email("Young@example.com")
                .phoneNumber("973-630-0847")
                .password(encodedPassword).build();
                
        Address csoManagerAddress1 = Address.builder()
                .latitude(155.0)
                .longitude(155.0)
                .streetAddress("2068 Locust Street")
                .administrativeArea("Cheshire")
                .locality("New Jersey")
                .country("US")
                .postCode("01225").build();

        User csoManagerUser1 = User.builder()
                .activeUser(true)
                .userName("grant_csomgr")
                .address(csoManagerAddress1)
                .firstName("Grant")
                .lastName("Brown")
                .email("Brown@example.com")
                .phoneNumber("229-762-8941")
                .password(encodedPassword).build();
                
        Address csoManagerAddress2 = Address.builder()
                .latitude(155.0)
                .longitude(155.0)
                .streetAddress("461 Courtright Street")
                .administrativeArea("Thompson")
                .locality("Thompson")
                .country("US")
                .postCode("58278").build();

        User csoManagerUser2 = User.builder()
                .activeUser(true)
                .userName("brayden_csomgr")
                .address(csoManagerAddress2)
                .firstName("Brayden")
                .lastName("adom")
                .email("Brayden@example.com")
                .phoneNumber("701-599-8729")
                .password(encodedPassword).build();        

        sessionFactory
                        .withTransaction(
                                        (conn, tx) -> conn.createQuery("DELETE FROM User").executeUpdate()
                                                        .flatMap(r -> conn.persistAll(adminUser1, adminUser2, paygUser1,
                                                                        paygUser2, paygUser3, paygUser4, paygUser5,
                                                                        managerUser1, managerUser2,
                                                                        deviceTechnicianUser1, deviceTechnicianUser2,
                                                                        csoUser1, csoUser2, businessManagerUser1,
                                                                        businessManagerUser2, csoManagerUser1,
                                                                        csoManagerUser2))
                                                        .chain(conn::flush)
                                                        .flatMap(r -> conn
                                                                        .createQuery("SELECT p from User p", User.class)
                                                                        .getResultList()))
                        .subscribe()
                        .with(
                                        data -> LOGGER.log(Level.INFO, "saved data:{0}", data),
                                        throwable -> LOGGER.warning(
                                                        "Data initialization is failed:" + throwable.getMessage()));


        UserRole businessManager = UserRole.of(null, "Business Manager");
        UserRole fleetManager = UserRole.of(null, "Fleet Manager");
        UserRole csoManager = UserRole.of(null, "CSO Manager");
        UserRole administrator = UserRole.of(null, "Administrator");
        UserRole payAsYouGoUser = UserRole.of(null, "Pay as you go user");
        UserRole cso = UserRole.of(null, "CSO");
        UserRole deviceTechnician = UserRole.of(null, "Device Technician");


        sessionFactory
                .withTransaction(
                        (conn, tx) -> conn.createQuery("DELETE FROM UserRole").executeUpdate()
                                .flatMap(r -> conn.persistAll(businessManager, fleetManager, csoManager, administrator, payAsYouGoUser, cso, deviceTechnician))
                                .chain(conn::flush)
                                .flatMap(r -> conn.createQuery("SELECT p from UserRole p", UserRole.class).getResultList())
                )
                .subscribe()
                .with(
                        data -> LOGGER.log(Level.INFO, "saved data:{0}", data),
                        throwable -> LOGGER.warning("Data initialization is failed:" + throwable.getMessage())
                );


        VehicleManufacturer mv = VehicleManufacturer.builder()
                .name("Audi")
                .manufacturerImageUrl("http://qi-ocpp-test-server.us-east-1.elasticbeanstalk.com/manufacturer/audi.png")
                .build();



        sessionFactory.withSession(session -> session.createQuery("DELETE FROM VehicleManufacturer").executeUpdate()).await().indefinitely();
        sessionFactory.withSession(session -> session.createQuery("DELETE FROM VehicleModel").executeUpdate()).await().indefinitely();
        sessionFactory.withSession(session -> session.createQuery("DELETE FROM VehicleVersion").executeUpdate()).await().indefinitely();
        sessionFactory.withSession(session -> session.createQuery("DELETE FROM Vehicle").executeUpdate()).await().indefinitely();
        sessionFactory.withSession(session -> session.persist(mv).chain(session::flush)).await().indefinitely();

        VehicleManufacturer mv1= sessionFactory.withSession(session -> session.createQuery("SELECT p from VehicleManufacturer p where p.name = 'Audi'", VehicleManufacturer.class).getSingleResult()
        ).await().indefinitely();

        VehicleModel vm = VehicleModel.builder()
                .vehicleManufacturerId(mv1)
                .name("A3 Sportsback e-tron")
                .build();
        sessionFactory.withSession(
                session -> session.persist(vm).chain(session::flush)).await().indefinitely();

        VehicleModel vm1= sessionFactory.withSession(session -> session.createQuery("SELECT p from VehicleModel p where p.name = 'A3 Sportsback e-tron'", VehicleModel.class).getSingleResult()
        ).await().indefinitely();


        VehicleVersion vv = VehicleVersion.builder()
                .vehicleModelId(vm1)
                .name("1.4 TSFI Progressiv")
                .year("2019")
                .build();
        sessionFactory.withSession(
                session -> session.persist(vv).chain(session::flush)).await().indefinitely();

        Vehicle vehicle1 = Vehicle.builder()
                        .activeVehicle(true)
                        .vehicleRegistration("192-XX1")
                        .vehicleVersionId(vv)
                        .vin("220213sadad923qsadas")
                        .userId(paygUser1)
                                .color(VehicleColor.BLACK).build();
        sessionFactory.withSession(
                session -> session.persist(vehicle1).chain(session::flush)).await().indefinitely();

        sessionFactory.withSession(session -> session.createQuery("DELETE FROM ChargeSite").executeUpdate()).await().indefinitely();
        sessionFactory.withSession(session -> session.createQuery("DELETE FROM ChargeStation").executeUpdate()).await().indefinitely();
        sessionFactory.withSession(session -> session.createQuery("DELETE FROM Connector").executeUpdate()).await().indefinitely();
        sessionFactory.withSession(session -> session.createQuery("DELETE FROM ConnectorType").executeUpdate()).await().indefinitely();

        Account account1 = Account.builder()
                .accountName("Example Account 1").build();
        Account account2 = Account.builder()
                .accountName("Example Account 2").build();
        sessionFactory.withSession(session -> session.persist(account1).chain(session::flush)).await().indefinitely();
        sessionFactory.withSession(session -> session.persist(account2).chain(session::flush)).await().indefinitely();

        ConnectorType connectorType1 = ConnectorType.builder()
                .powerRatingKW(100.0)
                .name("Type 1")
                .build();
        ConnectorType connectorType2 = ConnectorType.builder()
                .powerRatingKW(50.0)
                .name("Type 2")
                .build();

        sessionFactory.withSession(session -> session.persist(connectorType1).chain(session::flush)).await().indefinitely();
        sessionFactory.withSession(session -> session.persist(connectorType2).chain(session::flush)).await().indefinitely();

        Address site1Address = Address.builder()
                .latitude(12.9181679)
                .longitude(77.5967575)
                .streetAddress("53 st Main")
                .administrativeArea("New York.")
                .locality("California").country("US")
                .postCode("678-354")
                .build();
        Address site1Address2 = Address.builder()
                .latitude(12.905196)
                .longitude(77.5628652)
                .streetAddress("53 st Main")
                .administrativeArea("New York.")
                .locality("California").country("US")
                .postCode("678-354")
                .build();        

        ChargeStationModel chargeStationModel = ChargeStationModel.builder()
                .modelName("Charge Model A")
                .build();
        sessionFactory.withSession(session -> session.persist(chargeStationModel).chain(session::flush)).await().indefinitely();

        ChargeSite chargeSite1 = ChargeSite.builder()
                .activeSite(true)
                .siteName("Charge Site 1")
                .address(site1Address)
                .createdBy(csoUser1)
                .accountId(account1).build();
        sessionFactory.withSession(session -> session.persist(chargeSite1).chain(session::flush)).await().indefinitely();

        ChargeStation chargeStation1 = ChargeStation.builder()
                .chargeStationModel(chargeStationModel)
                .ip("192.0.0.0.1")
                .chargeSite(chargeSite1)
                .password("example1")
                .createdBy(csoUser1)
                .name("Charge Station 1").build();
        sessionFactory.withSession(session -> session.persist(chargeStation1).chain(session::flush)).await().indefinitely();

        Connector connector1 = Connector.builder()
                .connectorStatus(ConnectorStatus.AVAILABLE)
                .connectorType(connectorType1)
                .chargeStation(chargeStation1)
                .build();
        sessionFactory.withSession(session -> session.persist(connector1).chain(session::flush)).await().indefinitely();

        ChargeSite chargeSite2 = ChargeSite.builder()
        .activeSite(true)
        .siteName("Charge Site 1")
        .address(site1Address2)
        .createdBy(csoUser2)
        .accountId(account1).build();
    sessionFactory.withSession(session -> session.persist(chargeSite2).chain(session::flush)).await().indefinitely();

    ChargeStation chargeStation2 = ChargeStation.builder()
        .chargeStationModel(chargeStationModel)
        .ip("192.0.0.0.1")
        .chargeSite(chargeSite2)
        .password("example1")
        .createdBy(csoUser2)
        .name("Charge Station 1").build();
    sessionFactory.withSession(session -> session.persist(chargeStation2).chain(session::flush)).await().indefinitely();

    Connector connector2 = Connector.builder()
        .connectorStatus(ConnectorStatus.AVAILABLE)
        .connectorType(connectorType2)
        .chargeStation(chargeStation2)
        .build();
        sessionFactory.withSession(session -> session.persist(connector2).chain(session::flush)).await().indefinitely();


        Review review1 = Review.builder()
                .createdBy(paygUser1)
                .feedback("Fantastic Experience")
                .rating(4)
                .chargeStationId(chargeStation1)
                .build();
        Review review2 = Review.builder()
                .createdBy(paygUser2)
                .feedback("Poor Experience")
                .rating(2)
                .chargeStationId(chargeStation1)
                .build();
        sessionFactory.withSession(session -> session.persist(review1).chain(session::flush)).await().indefinitely();
        sessionFactory.withSession(session -> session.persist(review2).chain(session::flush)).await().indefinitely();

        Transaction t1 = Transaction.builder()
                .transactionIdentifier(1)
                .userId(paygUser1)
                .cost(123.0)
                .startPlugTime(LocalDateTime.now())
                .chargeStationId(chargeStation1)
                .connectorId(connector1)
                .vehicleId(vehicle1)
                .endPlugTime(LocalDateTime.now())
                .startTimeOfCharge(LocalDateTime.of(2022, 05, 11, 12, 1))
                .endTimeOfCharge(LocalDateTime.now())
                .energy(167.00)
                .paid(true).build();

        Transaction t2 = Transaction.builder()
                .transactionIdentifier(2)
                .userId(paygUser2)
                .cost(223.0)
                .chargeStationId(chargeStation1)
                .connectorId(connector1)
                .vehicleId(vehicle1)
                .startPlugTime(LocalDateTime.now())
                .endPlugTime(LocalDateTime.now())
                .startTimeOfCharge(LocalDateTime.of(2022, 05, 10, 12, 1))
                .endTimeOfCharge(LocalDateTime.now())
                .energy(267.00)
                .paid(true).build();

        Transaction t3 = Transaction.builder()
                .transactionIdentifier(3)
                .userId(paygUser3)
                .cost(293.0)
                .chargeStationId(chargeStation1)
                .connectorId(connector1)
                .vehicleId(vehicle1)
                .startPlugTime(LocalDateTime.now())
                .endPlugTime(LocalDateTime.now())
                .startTimeOfCharge(LocalDateTime.of(2022, 05, 11, 12, 1))
                .endTimeOfCharge(LocalDateTime.now())
                .energy(287.00)
                .paid(true).build();
        sessionFactory
                        .withTransaction(
                                        (conn, tx) -> conn.createQuery("DELETE FROM Transaction").executeUpdate()
                                                        .flatMap(r -> conn.persistAll(t1, t2, t3))
                                                        .chain(conn::flush)
                                                        .flatMap(r -> conn
                                                                        .createQuery("SELECT t from Transaction t",
                                                                                        Transaction.class)
                                                                        .getResultList()))
                        .subscribe()
                        .with(
                                        data -> LOGGER.log(Level.INFO, "saved data:{0}", data),
                                        throwable -> LOGGER.warning(
                                                        "Data initialization is failed:" + throwable.getMessage()));

        
		Faults fault = Faults.builder().faultTime(LocalDateTime.now()).faultMessage("Test Fault Message")
				.connector(connector1).build();
		Faults fault1 = Faults.builder().faultTime(LocalDateTime.now()).faultMessage("Test Fault Message 1")
				.connector(connector2).build();
		Faults fault2 = Faults.builder().faultTime(LocalDateTime.now()).faultMessage("Test Fault Message 1")
				.connector(connector2).build();

		sessionFactory
				.withTransaction((conn, tx) -> conn.createQuery("DELETE FROM Faults").executeUpdate()
						.flatMap(r -> conn.persistAll(fault, fault1, fault2))
						.chain(conn::flush)
						.flatMap(r -> conn.createQuery("SELECT p from Faults p", Faults.class).getResultList()))
				.subscribe().with(data -> LOGGER.log(Level.INFO, "saved data:{0}", data),
						throwable -> LOGGER.warning("Data initialization is failed:" + throwable.getMessage()));

    }




}
