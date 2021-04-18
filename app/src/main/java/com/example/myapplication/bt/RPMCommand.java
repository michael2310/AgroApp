package com.example.myapplication.bt;
//
//import com.sohrab.obd.reader.enums.AvailableCommandNames;
//import com.sohrab.obd.reader.obdCommand.ObdCommand;
//
//import java.util.Locale;
//
///**
// * Displays the current engine revolutions per minute (RPM).
// *
// * @author pires
// * @version $Id: $Id
// */
//public class RPMCommand extends ObdCommand {
//
//    private int rpm = -1;
//
//    /**
//     * Default ctor.
//     */
//    public RPMCommand() {
//        super("01 0C");
//    }
//
//    /**
//     * Copy ctor.
//     */
//    public RPMCommand(com.sohrab.obd.reader.obdCommand.engine.RPMCommand other) {
//        super(other);
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    protected void performCalculations() {
//        // ignore first two bytes [41 0C] of the response((A*256)+B)/4
//        rpm = (buffer.get(4) * 256 + buffer.get(5)) / 4;
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public String getFormattedResult() {
//        return String.format(Locale.ENGLISH, "%d%s", rpm, getResultUnit());
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public String getCalculatedResult() {
//        return String.valueOf(rpm);
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public String getResultUnit() {
//        return "RPM";
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public String getName() {
//        return AvailableCommandNames.ENGINE_RPM.getValue();
//    }
//
//    /**
//     * <p>getRPM.</p>
//     *
//     * @return a int.
//     */
//    public int getRPM() {
//        return rpm;
//    }
//
//}


import com.github.pires.obd.commands.ObdCommand;
import com.github.pires.obd.enums.AvailableCommandNames;

/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under

import com.github.pires.obd.commands.ObdCommand;
import com.github.pires.obd.enums.AvailableCommandNames;

/**
 * Displays the current engine revolutions per minute (RPM).
 *
 */
public class RPMCommand extends ObdCommand {

    private int rpm = -1;

    /**
     * Default ctor.
     */
    public RPMCommand() {
        super("01 0C");
    }

    /**
     * Copy ctor.
     *
     * @param other a {@link com.github.pires.obd.commands.engine.RPMCommand} object.
     */
    public RPMCommand(RPMCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [41 0C] of the response((A*256)+B)/4
        rpm = (buffer.get(4) * 256 + buffer.get(5)) / 4;
        //rpm = (buffer.get(2) * 256 + buffer.get(3)) / 4;
    }

    /** {@inheritDoc} */
    @Override
    public String getFormattedResult() {
        return String.format("%d%s", rpm, getResultUnit());
    }

    /** {@inheritDoc} */
    @Override
    public String getCalculatedResult() {
        return String.valueOf(rpm);
    }

    /** {@inheritDoc} */
    @Override
    public String getResultUnit() {
        return "RPM";
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.ENGINE_RPM.getValue();
    }

    /**
     * <p>getRPM.</p>
     *
     * @return a int.
     */
    public int getRPM() {
        return rpm;
    }

}
