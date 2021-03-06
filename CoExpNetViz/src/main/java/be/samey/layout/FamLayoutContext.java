package be.samey.layout;

/*
 * #%L
 * CoExpNetViz
 * %%
 * Copyright (C) 2015 PSB/UGent
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.cytoscape.work.Tunable;

public class FamLayoutContext {
    /*
    TODO: update documentation and field descriptions
     Layout parameters:
    //TODO: improve descriptions
     */

    @Tunable(description = "Which column to use to group baits")
    public String speciesAttribute = "Species";
    @Tunable(description = "Horizontal spacing between two unconnected nodes")
    public double nspacingx = 80.0;
    @Tunable(description = "Vertical spacing between two unconnected nodes")
    public double nspacingy = 40.0;
    @Tunable(description = "Maximum width for group of unconnected nodes")
    public double maxwidths = 800.0;
    @Tunable(description = "Horizontal spacing between two partitions in a row")
    public double spacingx = 400.0;
    @Tunable(description = "Vertical spacing between the largest partitions of two rows")
    public double spacingy = 400.0;
    @Tunable(description = "Vertical spacing between two connected components")
    public double ccspacingy = 800.0;
    @Tunable(description = "Maximum width of a row")
    public double maxwidth = 10000.0;
    @Tunable(description = "Minimum width of a partition")
    public double minrad = 100.0;
    @Tunable(description = "Scale of the radius of the partition")
    public double radmult = 50.0;
    @Tunable(description = "How far the baits are from the connected component center")
    public double enlargef = 2.0;
    @Tunable(description = "How large the radius is for baits in the same group")
    public double benlargef = 5.0;
}
