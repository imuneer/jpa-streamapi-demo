package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

@Service
public class TicketService {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TicketRepository ticketRepo;

    private Date started;
    private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");

    @Transactional(readOnly = true)
    public void readAndCollect() throws Exception {

        this.started = new Date();
        System.out.print("\033[H\033[2J");
        Map<String, Integer> size = new HashMap<String, Integer>() {{
            put("size", 0);
        }};
        System.out.println("Processing stream");
        TicketMonitor ticketMonitor = new TicketMonitor();
        try (Stream<ParkingTickets> tickets = ticketRepo.collectTickets()) {
            tickets.forEach(item -> {
                ticketMonitor.addItem(item);
                entityManager.detach(item);
                int s = size.get("size") + 1;
                if (s%10000 == 1) {
                    this.printItems(ticketMonitor, s);

                }
                size.put("size", s);
            });
        }
    }

    public void printItems(TicketMonitor ticketMonitor, int total) {
        System.out.print("\033[H\033[2J");
        System.out.println("Fines by Vehicle Make");
        System.out.println("#####################");
        Map<Integer, TicketMonitor.StrCount> makeCounter = ticketMonitor.getMakeCount();
        List<String> makes = new ArrayList<>(ticketMonitor.getMakes());
        Collections.sort(makes);
        System.out.print("Year  ");
        for (String item: makes) {
            System.out.print(formatSize(item));
        }
        System.out.println("");
        List<Integer> years = new ArrayList<>(makeCounter.keySet());
        Collections.sort(years);
        for (Integer year: years) {
            System.out.print(year + "  ");
            Map<String, Integer> makesCount = makeCounter.get(year).getAll();
            for (String make: makes) {
                int c = makesCount.get(make) == null ? 0 : makesCount.get(make);
                System.out.print(formatSize(c + ""));
            }
            System.out.println("");
        }

        System.out.println("");
        System.out.println("");
        System.out.println("Fines by Vehicle Registration State");
        System.out.println("###################################");
        Map<Integer, TicketMonitor.StrCount> statesCounts = ticketMonitor.getStateCount();
        List<String> states = new ArrayList<>(ticketMonitor.getStates());
        Collections.sort(states);
        System.out.print("Year  ");
        Map<String, String> stateNames = ticketMonitor.getStateNames();
        for (String item: states) {
            System.out.print(formatSize(stateNames.get(item), 11));
        }
        System.out.println("");
        for (Integer year: years) {
            System.out.print(year + "  ");
            Map<String, Integer> statesCounter = statesCounts.get(year).getAll();
            for (String state: states) {
                int c = statesCounter.get(state) == null ? 0 : statesCounter.get(state);
                System.out.print(formatSize(c + "", 11));
            }
            System.out.println("");
        }
        System.out.println("");
        DecimalFormat df = new DecimalFormat("#");
        df.setGroupingUsed(true);
        df.setGroupingSize(3);
        System.out.println("Total Processed: " + df.format(total));
        System.out.println("Start Time     : " + sdf.format(this.started));
        System.out.println("Now Time       : " + sdf.format(new Date()));
    }

    private String formatSize(String s, int len) {
        return String.format("%1$"+len+ "s", s);
    }

    private String formatSize(String s) {
        return formatSize(s, 8);
    }
}
