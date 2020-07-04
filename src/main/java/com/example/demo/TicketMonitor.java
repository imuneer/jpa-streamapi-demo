package com.example.demo;

import lombok.Data;

import java.util.*;

@Data
public class TicketMonitor {

    private Calendar cal;
    private List<String> makesList;
    private List<String> statesList;
    private Map<String, String> stateNames;

    public TicketMonitor() {
        cal = Calendar.getInstance();
        makesList = new ArrayList<>();
        statesList = new ArrayList<>();
        String[] _makesList = {"FORT", "TOYOT", "HONDA", "NISSA", "CHEVR", "FRUEH", "ME/BE",
                "BMW", "JEEP", "LEXUS", "HYUND", "INTER", "ACURA", "CHRYS", "VOLKS",
                "MAZDA", "GMC", "YAMAH", "TESLA", "DODGE", "FIAT"};
        makesList = Arrays.asList(_makesList);

        stateNames = new HashMap<String, String>() {{
            put("NY", "New York");
            put("NJ", "New Jersy");
            put("PA", "Pennsylv.");
            put("CT", "Co..ticut");
            put("FL", "Florida");
            put("MA", "Mass.");
            put("IN", "Indiana");
            put("VA", "Virginia");
            put("MD", "Maryland");
            put("NC", "N.Carolina");
            put("IL", "Illinois");
            put("GA", "Georgia");
            put("TX", "Texas");
            put("_OTHER", "Others");
        }};
        statesList =  new ArrayList<>(stateNames.keySet());
    }

    @Data
    public static class StrCount {
        private Map<String, Integer> strCount = new HashMap<>();

        public void addItem(String key) {
            Integer _c = 0;
            if (strCount.containsKey(key)) {
                _c = strCount.get(key);
            }
            strCount.put(key, (_c+1));
        }

        public Map<String, Integer> getAll() {
            return strCount;
        }
    }

    private Map<Integer, StrCount> makeCount = new HashMap<>();
    private Map<Integer, StrCount> stateCount = new HashMap<>();
    private Set<Integer> years = new HashSet<>();
    private Set<String> makes = new HashSet<>();
    private Set<String> states = new HashSet<>();

    public void addItem(ParkingTickets ticket) {
        if (ticket == null || ticket.getIssueDate() == null) return;
        cal.setTime(ticket.getIssueDate());
        int year = cal.get(Calendar.YEAR);
        this.years.add(year);

        if (ticket.getVehicleMake() != null && !ticket.getVehicleMake().isEmpty()) {
            String make = "_OTHER";
            if (this.makesList.contains(ticket.getVehicleMake())) {
                make = ticket.getVehicleMake();
            }
            this.addMake(year, make);
            this.makes.add(make);
        }
        if (ticket.getState() != null && !ticket.getState().isEmpty()) {
            String st = "_OTHER";
            if (this.statesList.contains(ticket.getState())) {
                st = ticket.getState();
            }
            this.addStateCount(year, st);
            this.states.add(st);
        }
    }

    public void addMake(Integer year, String make) {
        StrCount _count = new StrCount();
        if (makeCount.containsKey(year)) {
            _count = makeCount.get(year);
        }
        _count.addItem(make);
        makeCount.put(year, _count);
    }

    public void addStateCount(Integer year, String plateId) {
        StrCount _count = new StrCount();
        if (stateCount.containsKey(year)) {
            _count = stateCount.get(year);
        }
        _count.addItem(plateId);
        stateCount.put(year, _count);
    }

    public Map<Integer, StrCount> getAllMakes() {
        return this.makeCount;
    }

    public Map<Integer, StrCount> getAllStates() {
        return this.stateCount;
    }
}
