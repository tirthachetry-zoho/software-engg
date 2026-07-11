import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * System Design — Parking Lot (Low-Level Design)
 * Implements a multi-level parking lot system
 * Features: Vehicle types, spot management, pricing, ticket generation
 */
public class DesignParkingLot {

    // Vehicle types
    enum VehicleType { MOTORCYCLE, CAR, BUS, TRUCK }
    enum SpotType { MOTORCYCLE, COMPACT, LARGE }
    enum ParkingStatus { AVAILABLE, OCCUPIED, RESERVED }

    // Vehicle class
    static class Vehicle {
        private final String licensePlate; private final VehicleType type; private final String color;
        public Vehicle(String licensePlate, VehicleType type, String color) {
            this.licensePlate = licensePlate; this.type = type; this.color = color;
        }
        public String getLicensePlate() { return licensePlate; }
        public VehicleType getType() { return type; }
        public String getColor() { return color; }
    }

    // Parking spot
    static class ParkingSpot {
        private final String spotId; private final SpotType type; private final int level;
        private volatile ParkingStatus status; private Vehicle currentVehicle;
        private final Object lock = new Object();
        
        public ParkingSpot(String spotId, SpotType type, int level) {
            this.spotId = spotId; this.type = type; this.level = level; this.status = ParkingStatus.AVAILABLE;
        }
        
        public String getSpotId() { return spotId; }
        public SpotType getType() { return type; }
        public int getLevel() { return level; }
        public ParkingStatus getStatus() { return status; }
        public Vehicle getCurrentVehicle() { return currentVehicle; }
        
        public boolean park(Vehicle vehicle) {
            synchronized (lock) {
                if (status != ParkingStatus.AVAILABLE) return false;
                status = ParkingStatus.OCCUPIED; currentVehicle = vehicle;
                return true;
            }
        }
        
        public boolean unpark() {
            synchronized (lock) {
                if (status != ParkingStatus.OCCUPIED) return false;
                status = ParkingStatus.AVAILABLE; currentVehicle = null;
                return true;
            }
        }
        
        public boolean canFit(VehicleType vehicleType) {
            switch (vehicleType) {
                case MOTORCYCLE: return type == SpotType.MOTORCYCLE || type == SpotType.COMPACT || type == SpotType.LARGE;
                case CAR: return type == SpotType.COMPACT || type == SpotType.LARGE;
                case BUS: case TRUCK: return type == SpotType.LARGE;
                default: return false;
            }
        }
    }

    // Parking level
    static class ParkingLevel {
        private final int levelNumber;
        private final Map<SpotType, List<ParkingSpot>> spots;
        private final AtomicInteger availableSpots;
        
        public ParkingLevel(int levelNumber, int motorcycleSpots, int compactSpots, int largeSpots) {
            this.levelNumber = levelNumber;
            this.spots = new ConcurrentHashMap<>();
            this.availableSpots = new AtomicInteger(motorcycleSpots + compactSpots + largeSpots);
            
            initializeSpots(SpotType.MOTORCYCLE, motorcycleSpots);
            initializeSpots(SpotType.COMPACT, compactSpots);
            initializeSpots(SpotType.LARGE, largeSpots);
        }
        
        private void initializeSpots(SpotType type, int count) {
            List<ParkingSpot> spotList = new CopyOnWriteArrayList<>();
            for (int i = 0; i < count; i++) {
                String spotId = "L" + levelNumber + "_" + type + "_" + i;
                spotList.add(new ParkingSpot(spotId, type, levelNumber));
            }
            spots.put(type, spotList);
        }
        
        public ParkingSpot findAvailableSpot(VehicleType vehicleType) {
            List<SpotType> preferredTypes = getPreferredSpotTypes(vehicleType);
            for (SpotType type : preferredTypes) {
                for (ParkingSpot spot : spots.getOrDefault(type, Collections.emptyList())) {
                    if (spot.getStatus() == ParkingStatus.AVAILABLE && spot.canFit(vehicleType)) {
                        return spot;
                    }
                }
            }
            return null;
        }
        
        private List<SpotType> getPreferredSpotTypes(VehicleType vehicleType) {
            switch (vehicleType) {
                case MOTORCYCLE: return Arrays.asList(SpotType.MOTORCYCLE, SpotType.COMPACT, SpotType.LARGE);
                case CAR: return Arrays.asList(SpotType.COMPACT, SpotType.LARGE);
                case BUS: case TRUCK: return Arrays.asList(SpotType.LARGE);
                default: return Collections.emptyList();
            }
        }
        
        public int getAvailableSpots() { return availableSpots.get(); }
        public int getLevelNumber() { return levelNumber; }
    }

    // Parking ticket
    static class ParkingTicket {
        private final String ticketId; private final String spotId; private final String licensePlate;
        private final long entryTime; private volatile long exitTime;
        
        public ParkingTicket(String ticketId, String spotId, String licensePlate) {
            this.ticketId = ticketId; this.spotId = spotId; this.licensePlate = licensePlate;
            this.entryTime = System.currentTimeMillis();
        }
        
        public String getTicketId() { return ticketId; }
        public String getSpotId() { return spotId; }
        public String getLicensePlate() { return licensePlate; }
        public long getEntryTime() { return entryTime; }
        public long getExitTime() { return exitTime; }
        public void setExitTime(long exitTime) { this.exitTime = exitTime; }
        public long getDuration() { return exitTime > 0 ? exitTime - entryTime : System.currentTimeMillis() - entryTime; }
    }

    // Pricing strategy
    interface PricingStrategy {
        double calculatePrice(long durationMillis, VehicleType vehicleType);
    }
    
    static class HourlyPricing implements PricingStrategy {
        private final Map<VehicleType, Double> hourlyRates;
        
        public HourlyPricing() {
            this.hourlyRates = new HashMap<>();
            hourlyRates.put(VehicleType.MOTORCYCLE, 5.0);
            hourlyRates.put(VehicleType.CAR, 10.0);
            hourlyRates.put(VehicleType.BUS, 20.0);
            hourlyRates.put(VehicleType.TRUCK, 25.0);
        }
        
        @Override
        public double calculatePrice(long durationMillis, VehicleType vehicleType) {
            long hours = (durationMillis + 3599999) / 3600000; // Round up to nearest hour
            double hourlyRate = hourlyRates.getOrDefault(vehicleType, 10.0);
            return hours * hourlyRate;
        }
    }

    // Parking lot
    static class ParkingLot {
        private final String parkingLotId;
        private final List<ParkingLevel> levels;
        private final Map<String, ParkingTicket> activeTickets;
        private final Map<String, ParkingTicket> ticketHistory;
        private final PricingStrategy pricingStrategy;
        private final AtomicLong ticketCounter;
        
        public ParkingLot(String parkingLotId, int levels, int motorcycleSpots, int compactSpots, int largeSpots) {
            this.parkingLotId = parkingLotId;
            this.levels = new CopyOnWriteArrayList<>();
            this.activeTickets = new ConcurrentHashMap<>();
            this.ticketHistory = new ConcurrentHashMap<>();
            this.pricingStrategy = new HourlyPricing();
            this.ticketCounter = new AtomicLong(0);
            
            for (int i = 0; i < levels; i++) {
                this.levels.add(new ParkingLevel(i + 1, motorcycleSpots, compactSpots, largeSpots));
            }
        }
        
        public ParkingTicket parkVehicle(Vehicle vehicle) {
            for (ParkingLevel level : levels) {
                ParkingSpot spot = level.findAvailableSpot(vehicle.getType());
                if (spot != null && spot.park(vehicle)) {
                    String ticketId = "T" + ticketCounter.incrementAndGet();
                    ParkingTicket ticket = new ParkingTicket(ticketId, spot.getSpotId(), vehicle.getLicensePlate());
                    activeTickets.put(ticketId, ticket);
                    System.out.println("Vehicle " + vehicle.getLicensePlate() + " parked at " + spot.getSpotId());
                    return ticket;
                }
            }
            throw new RuntimeException("No available spots for vehicle type: " + vehicle.getType());
        }
        
        public double exitVehicle(String ticketId) {
            ParkingTicket ticket = activeTickets.remove(ticketId);
            if (ticket == null) throw new RuntimeException("Invalid ticket ID");
            
            ticket.setExitTime(System.currentTimeMillis());
            double price = pricingStrategy.calculatePrice(ticket.getDuration(), getVehicleType(ticket.getLicensePlate()));
            
            // Free up the parking spot
            for (ParkingLevel level : levels) {
                for (List<ParkingSpot> spots : level.spots.values()) {
                    for (ParkingSpot spot : spots) {
                        if (spot.getSpotId().equals(ticket.getSpotId())) {
                            spot.unpark();
                            break;
                        }
                    }
                }
            }
            
            ticketHistory.put(ticketId, ticket);
            System.out.println("Vehicle " + ticket.getLicensePlate() + " exited. Price: $" + price);
            return price;
        }
        
        private VehicleType getVehicleType(String licensePlate) {
            // In real system, this would look up the vehicle type from a database
            return VehicleType.CAR; // Default for demo
        }
        
        public int getAvailableSpots() {
            return levels.stream().mapToInt(ParkingLevel::getAvailableSpots).sum();
        }
        
        public List<ParkingTicket> getActiveTickets() { return new ArrayList<>(activeTickets.values()); }
    }

    // Entrance gate
    static class EntranceGate {
        private final ParkingLot parkingLot;
        
        public EntranceGate(ParkingLot parkingLot) { this.parkingLot = parkingLot; }
        
        public ParkingTicket processEntry(Vehicle vehicle) {
            System.out.println("Processing entry for vehicle: " + vehicle.getLicensePlate());
            if (parkingLot.getAvailableSpots() == 0) {
                throw new RuntimeException("Parking lot is full");
            }
            return parkingLot.parkVehicle(vehicle);
        }
    }

    // Exit gate
    static class ExitGate {
        private final ParkingLot parkingLot;
        
        public ExitGate(ParkingLot parkingLot) { this.parkingLot = parkingLot; }
        
        public PaymentReceipt processExit(String ticketId) {
            System.out.println("Processing exit for ticket: " + ticketId);
            double amount = parkingLot.exitVehicle(ticketId);
            return new PaymentReceipt(ticketId, amount, System.currentTimeMillis());
        }
    }

    // Payment receipt
    static class PaymentReceipt {
        private final String ticketId; private final double amount; private final long timestamp;
        
        public PaymentReceipt(String ticketId, double amount, long timestamp) {
            this.ticketId = ticketId; this.amount = amount; this.timestamp = timestamp;
        }
        
        public String getTicketId() { return ticketId; }
        public double getAmount() { return amount; }
        public long getTimestamp() { return timestamp; }
    }

    public static void main(String[] args) throws InterruptedException {
        // Create parking lot with 2 levels
        ParkingLot parkingLot = new ParkingLot("LOT1", 2, 5, 10, 5);
        
        // Create gates
        EntranceGate entrance = new EntranceGate(parkingLot);
        ExitGate exit = new ExitGate(parkingLot);
        
        // Park vehicles
        Vehicle car1 = new Vehicle("ABC123", VehicleType.CAR, "Red");
        Vehicle motorcycle1 = new Vehicle("MOTO456", VehicleType.MOTORCYCLE, "Black");
        Vehicle bus1 = new Vehicle("BUS789", VehicleType.BUS, "White");
        
        ParkingTicket ticket1 = entrance.processEntry(car1);
        ParkingTicket ticket2 = entrance.processEntry(motorcycle1);
        ParkingTicket ticket3 = entrance.processEntry(bus1);
        
        System.out.println("Available spots: " + parkingLot.getAvailableSpots());
        
        // Exit vehicles
        Thread.sleep(2000); // Simulate parking time
        PaymentReceipt receipt1 = exit.processExit(ticket1.getTicketId());
        System.out.println("Payment: $" + receipt1.getAmount());
        
        Thread.sleep(1000);
        PaymentReceipt receipt2 = exit.processExit(ticket2.getTicketId());
        System.out.println("Payment: $" + receipt2.getAmount());
        
        System.out.println("Available spots: " + parkingLot.getAvailableSpots());
    }
}
