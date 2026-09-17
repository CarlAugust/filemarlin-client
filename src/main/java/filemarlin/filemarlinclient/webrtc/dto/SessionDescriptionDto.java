package filemarlin.filemarlinclient.webrtc.dto;

import dev.onvoid.webrtc.RTCSdpType;
import dev.onvoid.webrtc.RTCSessionDescription;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public class SessionDescriptionDto {
    public RTCSdpType sdpType;
    public String sdp;

    public static RTCSessionDescription toRTCSesssionDescription(ObjectMapper objectMapper, JsonNode payload) throws JacksonException {
        var descriptionDto = objectMapper.treeToValue(payload, SessionDescriptionDto.class );
        return new RTCSessionDescription(descriptionDto.sdpType, descriptionDto.sdp);
    }
}
