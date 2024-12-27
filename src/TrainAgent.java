import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class TrainAgent extends Agent {
    private String[] route = {"Station1", "Station2", "Station3", "Station4", "Station5"};
    private int currentStationIndex = 0;
    private boolean forward = true;

    @Override
    protected void setup() {
        System.out.println("Train " + getLocalName() + " is ready.");

        addBehaviour(new TickerBehaviour(this, 6000) {
            @Override
            protected void onTick() {
                String currentStation = route[currentStationIndex];
                System.out.println(getLocalName() + " is at " + currentStation);

                // Determine direction based on current station
                String direction = forward ? "Towards " + route[Math.min(currentStationIndex + 1, route.length - 1)] : "Towards " + route[Math.max(currentStationIndex - 1, 0)];

                // Request platform at the current station
                ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                request.addReceiver(getAID(currentStation));
                request.setContent("REQUEST_PLATFORM");
                send(request);

                ACLMessage response = blockingReceive();
                if (response != null) {
                    if (response.getPerformative() == ACLMessage.CONFIRM) {
                        // If platform is available, occupy it
                        int assignedPlatform = Integer.parseInt(response.getContent().split(":")[1]);
                        System.out.println(getLocalName() + " assigned PLATFORM " + assignedPlatform + " at " + currentStation);

                        // Update the GUI with train's current station and direction
                        StationGUI gui = StationGUI.getInstance();  // Get the existing instance
                        gui.updateTrainLocation(getLocalName(), currentStation, direction);

                        // Simulate the train occupying the platform
                        doWait(6000); // Simulate time train occupies the platform

                        // Release the platform once the train leaves
                        ACLMessage leaveMsg = new ACLMessage(ACLMessage.INFORM);
                        leaveMsg.addReceiver(getAID(currentStation));
                        leaveMsg.setContent("LEAVE_PLATFORM:" + assignedPlatform);
                        send(leaveMsg);

                        // Move the train to the next station
                        updateStationIndex();
                    } else {
                        System.out.println(getLocalName() + " could not get a platform at " + currentStation);
                    }
                }
            }
        });
    }

    private void updateStationIndex() {
        if (forward) {
            currentStationIndex++;
            if (currentStationIndex == route.length) {
                forward = false;
                currentStationIndex--;  // Stay at the last station
            }
        } else {
            currentStationIndex--;
            if (currentStationIndex < 0) {
                forward = true;
                currentStationIndex++;  // Stay at the first station
            }
        }
    }
}
