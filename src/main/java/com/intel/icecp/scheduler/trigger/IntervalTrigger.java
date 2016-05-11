/*
 * ******************************************************************************
 *
 * INTEL CONFIDENTIAL
 *
 * Copyright 2013 - 2016 Intel Corporation All Rights Reserved.
 *
 * The source code contained or described herein and all documents related to
 * the source code ("Material") are owned by Intel Corporation or its suppliers
 * or licensors. Title to the Material remains with Intel Corporation or its
 * suppliers and licensors. The Material contains trade secrets and proprietary
 * and confidential information of Intel or its suppliers and licensors. The
 * Material is protected by worldwide copyright and trade secret laws and treaty
 * provisions. No part of the Material may be used, copied, reproduced,
 * modified, published, uploaded, posted, transmitted, distributed, or disclosed
 * in any way without Intel's prior express written permission.
 *
 * No license under any patent, copyright, trade secret or other intellectual
 * property right is granted to or conferred upon you by disclosure or delivery
 * of the Materials, either expressly, by implication, inducement, estoppel or
 * otherwise. Any license under such intellectual property rights must be
 * express and approved by Intel in writing.
 *
 * Unless otherwise agreed by Intel in writing, you may not remove or alter this
 * notice or any other notice embedded in Materials by Intel or Intel's
 * suppliers or licensors in any way.
 *
 * ******************************************************************************
 */

package com.intel.icecp.scheduler.trigger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Used to schedule a trigger with a {@link com.intel.icecp.scheduler.schedule.Schedule} instance.
 * <p>
 * Simple Triggers will help schedule triggers at the current time instant repeats it periodically as per the
 * specified interval.
 * <p>
 * When a trigger fires, a {@link com.intel.icecp.scheduler.message.TriggerMessage} object is created and published to the
 * publishChannel specified by the trigger.
 *
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class IntervalTrigger extends BaseTrigger {
    private static final Logger LOGGER = LogManager.getLogger();

    private final int interval;
    private final String unit;

    /**
     * Constructor
     *
     * @param id Unique identifier for the trigger. This unique identifier is used for published trigger event messages
     * to determine the trigger that was fired.* @param interval the number of minutes at which the trigger should repeat.
     * @param interval the interval at which the trigger is repeated
     * @param unit the time unit for this triggers - enum value of {@link TimeUnit} MINUTES, HOURS, SECONDS, MILLISECONDS
     * @param publishChannel Channel the trigger event should be published on.
     */
    @JsonCreator
    public IntervalTrigger(
            @JsonProperty(value = "id") String id,
            @JsonProperty(value = "interval") int interval,
            @JsonProperty(value = "unit") String unit,
            @JsonProperty(value = "publishChannel") String publishChannel,
            @JsonProperty(value = "cmd") String cmd,
            @JsonProperty(value = "params") Map<String, String> params) {
        super(id, publishChannel, cmd, params);
        this.interval = interval;
        this.unit = unit;
    }

    /**
     * @return the time unit of the trigger
     */
    public TimeUnit getUnit() {
        if(unit == null){
            LOGGER.error("Value of time unit is null!");
            return null;
        }
        try {
            return TimeUnit.valueOf(unit);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Illegal value of time unit: {}", unit, e);
            return null;
        }
    }

    /**
     * Get the interval at which the trigger should repeat
     *
     * @return integer representing the interval value
     */
    public int getInterval() {
        return interval;
    }

    /**
     *  method to check if a {@link IntervalTrigger} trigger is valid and contains all the required fields
     *
     * @return true if valid, else false
     */
    @Override
    public boolean isValid() {
        return getInterval() > 0 && getUnit() != null && super.isValid();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        IntervalTrigger that = (IntervalTrigger) o;

        if (getInterval() != that.getInterval()) return false;
        return getUnit() != null ? getUnit().equals(that.getUnit()) : that.getUnit() == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getInterval();
        result = 31 * result + (getUnit() != null ? getUnit().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IntervalTrigger{" +
                "interval=" + interval +
                "} " + super.toString();
    }
}
