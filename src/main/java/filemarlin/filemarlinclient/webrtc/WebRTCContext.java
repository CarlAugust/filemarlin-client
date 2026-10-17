package filemarlin.filemarlinclient.webrtc;

import dev.onvoid.webrtc.PeerConnectionFactory;
import dev.onvoid.webrtc.RTCAnswerOptions;
import dev.onvoid.webrtc.RTCConfiguration;
import dev.onvoid.webrtc.RTCOfferOptions;

public class WebRTCContext {

    private final RTCConfiguration config = new RTCConfiguration();
    private final PeerConnectionFactory factory = new PeerConnectionFactory();
    private final RTCOfferOptions offerOptions = new RTCOfferOptions();
    private final RTCAnswerOptions answerOptions = new RTCAnswerOptions();

    public RTCConfiguration getConfig() {
        return config;
    }

    public PeerConnectionFactory getFactory() {
        return factory;
    }

    public RTCOfferOptions getOfferOptions() {
        return offerOptions;
    }

    public RTCAnswerOptions getAnswerOptions() {
        return answerOptions;
    }
}
