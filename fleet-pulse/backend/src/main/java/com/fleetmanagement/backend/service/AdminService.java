package com.fleetmanagement.backend.service;

import com.fleetmanagement.backend.dto.VehicleDTO;
import com.fleetmanagement.backend.dto.DriverDTO;
import com.fleetmanagement.backend.dao.BookingDao;
import com.fleetmanagement.backend.dao.CompanyBankDao;
import com.fleetmanagement.backend.dto.BookingDTO;
import com.fleetmanagement.backend.entity.Vehicle;
import com.fleetmanagement.backend.entity.Driver;
import com.fleetmanagement.backend.entity.DriverStatus;
import com.fleetmanagement.backend.entity.Booking;
import com.fleetmanagement.backend.entity.BookingStatus;
import com.fleetmanagement.backend.entity.CompanyBank;
import com.fleetmanagement.backend.entity.VehicleStatus;
import com.fleetmanagement.backend.repository.VehicleRepository;
import com.fleetmanagement.backend.repository.DriverRepository;
import com.fleetmanagement.backend.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional

public class AdminService {

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final BookingRepository bookingRepository;
    private final BookingDao bookingDao;
    private final CompanyBankDao companyBankDao;   

    
    
    

    // VEHICLE LOGIC

    public List<VehicleDTO> findAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::mapToVehicleDTO)
                .collect(Collectors.toList());
    }

    public VehicleDTO addNewVehicle(VehicleDTO dto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setRegistrationNumber(dto.registrationNumber());
        vehicle.setBrand(dto.brand());
        vehicle.setModel(dto.model());
        vehicle.setYear(dto.year() != null ? dto.year() : 2026); // Default or required
        vehicle.setVehicleType(dto.vehicleType());
        vehicle.setSeatingCapacity(dto.seatingCapacity());
        vehicle.setImageUrl(dto.imageUrl());
        
        //for now we are setting the expiry date and insurance number "hardcoded"
        vehicle.setInsuranceNumber("123456789");
        vehicle.setInsuranceExpiryDate((LocalDateTime.of(2030, 12, 26, 0, 0)));
        
        // Corporate Auto-Configuration
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        vehicle.setIsVerified(true); // Company owned assets are pre-verified
        vehicle.setIsActive(true);
        vehicle.setMileage(0); // New asset initialization

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return mapToVehicleDTO(savedVehicle);
    }

    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new RuntimeException("Vehicle not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
    }

    // DRIVER LOGIC

    public void updateDriverVerification(Long driverId, boolean status, DriverStatus driverstatus) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        driver.setBackgroundVerified(status);
        driver.setDocumentVerified(status);
        if (status) {
            driver.setStatus(DriverStatus.ACTIVE);
            driver.setIsAvailable(true); // Assuming approved drivers become available
        } else {
            driver.setStatus(DriverStatus.SUSPENEDED); // Using the enum value as defined
            driver.setIsAvailable(false);
        }
        driverRepository.save(driver);
    }

    // DASHBOARD & STATS

    public Map<String, Object> getSystemStatistics() {
    	// Get the CompanyBank data
        CompanyBank bank = companyBankDao.getMasterBank();
        
        return Map.of(
                "totalVehicles", vehicleRepository.count(),
                "activeDrivers", driverRepository.countByBackgroundVerifiedTrue(),
                "pendingBookings", bookingRepository.countByStatus(BookingStatus.PENDING),
                "totalRevenue", bank.getLifetimeRevenue() != null ? bank.getLifetimeRevenue() : BigDecimal.ZERO
        );
    }
    
    
    
    // HELPER MAPPERS (Internal)

    private VehicleDTO mapToVehicleDTO(Vehicle vehicle) {
        return new VehicleDTO(
                vehicle.getId(),
                vehicle.getRegistrationNumber(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getVehicleType(),
                vehicle.getYear(),
                vehicle.getSeatingCapacity(),
                vehicle.getStatus(),
                vehicle.getIsVerified(),
                vehicle.getImageUrl());
    }

    // Placeholder methods for Drivers and Bookings

    public List<DriverDTO> findAllDrivers() {
        return driverRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private DriverDTO mapToDTO(Driver driver) {
        return new DriverDTO(
                driver.getId(),
                driver.getUser().getId(),
                driver.getUser().getFirstName() + " " + driver.getUser().getLastName(),
                driver.getUser().getEmail(),
                driver.getLicenseNumber(),
                driver.getLicenseExpiryDate(),
                driver.getExperienceYears(),
                driver.getRating(),
                driver.getStatus(),
                driver.getIsAvailable(),
                driver.getDocumentVerified());
    }

    /*
    public List<BookingDTO> getActiveBookings() {
        return List.of();
    }
    

    public List<BookingDTO> getBookingHistory() {
        return List.of();
    }
    */
    
    //new method for history
    public List<BookingDTO> getBookingHistory() {
        return bookingDao.findBookingHistory()
                .stream()
                .map(this::mapToBookingDTO)
                .toList();
    }
    
    
    //new method
    public List<BookingDTO> getActiveBookings() {
    return bookingDao.findActiveBookings()
            .stream()
            .map(this::mapToBookingDTO)
            .toList();
}

private BookingDTO mapToBookingDTO(Booking booking) {
    return new BookingDTO(
        booking.getId(),
        booking.getBookingNumber(),
        booking.getCustomer().getFirstName() + " " + booking.getCustomer().getLastName(),
        booking.getVehicle() != null ? booking.getVehicle().getRegistrationNumber() : "N/A",
        booking.getDriver() != null ? 
        booking.getDriver().getUser().getFirstName() + " " + booking.getDriver().getUser().getLastName() : "Unassigned",
        booking.getSourceLocation(),
        booking.getDestinationLocation(),
        booking.getTotalAmount(),
        booking.getStatus(),
        booking.getPaymentStatus(),
        booking.getScheduledStartTime()
    );
}
    
    
    
}