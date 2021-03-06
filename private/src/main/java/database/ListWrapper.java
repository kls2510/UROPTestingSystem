package database;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import publicinterfaces.StaticOptions;

/**
 * Wrapper class for Java.util.List. Needed to store List<XMLTestSettings> in mongoDB, because the JacksonDB wrapper
 * can't cope with generics
 * @author as2388
 */
public class ListWrapper {
    private String tickId;
    private List<StaticOptions> elements;
    private String dynamicContainerId;
    private String dynamicTestId;

    @JsonCreator
    public ListWrapper(@JsonProperty("_id") String tickId, @JsonProperty("elements") List<StaticOptions> elements,
    		@JsonProperty("dynamicContainerId") String containerId,@JsonProperty("dynamicTestId") String testId) {
        this.tickId = tickId;
        this.elements = elements;
        this.setDynamicContainerId(containerId);
        this.setDynamicTestId(testId);
    }

    public ListWrapper() {}

    public List<StaticOptions> getElements() {
        return elements;
    }

    public void setElements(List<StaticOptions> elements) {
        this.elements = elements;
    }

    @JsonProperty("_id")
    public String getTickId() {
        return tickId;
    }

    @JsonProperty("_id")
    public void setTickId(String tickId) {
        this.tickId = tickId;
    }

	public String getDynamicContainerId() {
		return dynamicContainerId;
	}

	public void setDynamicContainerId(String dynamicContainerId) {
		this.dynamicContainerId = dynamicContainerId;
	}

	public String getDynamicTestId() {
		return dynamicTestId;
	}

	public void setDynamicTestId(String dynamicTestId) {
		this.dynamicTestId = dynamicTestId;
	}
}
