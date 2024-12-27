import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class StationAgent extends Agent {
    private String stationName;
    private int totalPlatforms;
    private boolean[] platformStatus; // Array to track the availability of platforms

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length == 2) {
            stationName = (String) args[0];
            totalPlatforms = Integer.parseInt(args[1].toString());
            platformStatus = new boolean[totalPlatforms]; // Initialize platform status (false = free, true = occupied)
        } else {
            System.err.println("StationAgent requires station name and totalPlatforms as arguments.");
            doDelete();
            return;
        }

        // Add cyclic behaviour to handle incoming messages
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String content = msg.getContent();
                    ACLMessage reply = msg.createReply();

                    if (content.startsWith("REQUEST_PLATFORM")) {
                        handlePlatformRequest(msg, reply);
                    } else if (content.startsWith("LEAVE_PLATFORM")) {
                        handlePlatformRelease(msg);
                    }
                } else {
                    block();
                }
            }
        });
    }

    private void handlePlatformRequest(ACLMessage msg, ACLMessage reply) {
        // Check for a free platform
        int freePlatform = getFreePlatform();

        if (freePlatform != -1) {
            platformStatus[freePlatform] = true; // Mark platform as occupied
            reply.setContent("PLATFORM_ASSIGNED:" + freePlatform);
            reply.setPerformative(ACLMessage.CONFIRM);
            send(reply);

            // Update the GUI with platform status
            StationGUI gui = StationGUI.getInstance();  // Get the existing instance
            gui.updatePlatformStatus(stationName, freePlatform, true, msg.getSender().getLocalName());

            System.out.println("Platform " + freePlatform + " assigned to " + msg.getSender().getLocalName() + " at " + stationName);
        } else {
            reply.setContent("NO_PLATFORM_AVAILABLE");
            reply.setPerformative(ACLMessage.REFUSE);
            send(reply);

            System.out.println("No platform available for " + msg.getSender().getLocalName() + " at " + stationName);
        }
    }

    private void handlePlatformRelease(ACLMessage msg) {
        String[] parts = msg.getContent().split(":");
        if (parts.length == 2) {
            int platform = Integer.parseInt(parts[1]);
            platformStatus[platform] = false; // Mark platform as free
            StationGUI gui = StationGUI.getInstance();  // Get the existing instance
            gui.updatePlatformStatus(stationName, platform, false, "");
            System.out.println("Platform " + platform + " released at " + stationName);
        }
    }

    private int getFreePlatform() {
        for (int i = 0; i < platformStatus.length; i++) {
            if (!platformStatus[i]) {
                return i; // Return the index of the first free platform
            }
        }
        return -1; // No free platform available
    }
}
