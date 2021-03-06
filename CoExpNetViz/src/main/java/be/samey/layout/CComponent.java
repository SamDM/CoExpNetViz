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

import be.samey.internal.CyModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.cytoscape.model.CyNode;

/**
 *
 * @author sam
 * @param <T> The type of the attribute which is used for grouping and sorting
 * @param <U> The type of the attribute used to group baits
 */
class CComponent<T extends Comparable<T>, U extends Comparable<U>> implements Comparable<CComponent> {

    private final Map<T, List<CyNode>> targetsMap = new TreeMap<T, List<CyNode>>();
    private final Map<U, List<CyNode>> baitsMap = new TreeMap<U, List<CyNode>>();
    private final List<CyNode> invalidNodes = new ArrayList<CyNode>();
    int size = 0;

    Comparator<CyNode> comparator = new Comparator<CyNode>() {
        public int compare(CyNode node1, CyNode node2) {
            Long a = node1.getSUID();
            Long b = node2.getSUID();

            return a.compareTo(b);
        }
    };

    /**
     * Adds a node to this connected component and automatically groups it as
     * target or bait in the correct partition
     *
     * @param node the {@link Cynode} to add to this connected component
     * @param group The attribute to use for grouping
     */
    void addNode(CyNode node, T group, U species) {

        if (group == null) {
            //we don't know what this are,
            // this will eventually be classified as target
            invalidNodes.add(node);

        } else if (group.equals(CyModel.BAIT_GROUP)) {
            //this node is a bait
            List<CyNode> baitgroup = baitsMap.get(species);

            if (baitgroup == null) {
                baitgroup = new ArrayList<CyNode>();
                baitsMap.put(species, baitgroup);
            }
            baitgroup.add(node);

        } else {
            //this node is a target
            List<CyNode> partition = targetsMap.get(group);
            if (partition == null) {
                partition = new ArrayList<CyNode>();
                targetsMap.put(group, partition);
            }
            partition.add(node);
        }

        size++;
    }

    /**
     * Sorts the targets in this connected component on the attribute use for
     * grouping. Every partition has its nodes sorted by SUID. Nodes with
     * <code>null<\code> attributes are grouped in the last partition.
     *
     * @return
     */
    List<List<CyNode>> getSortedTargets() {

        if (targetsMap.isEmpty()) {
            return null;
        }

        List<T> keys = new ArrayList<T>(targetsMap.keySet());
        Collections.sort(keys);

        List<List<CyNode>> sortedlist = new ArrayList<List<CyNode>>(targetsMap.keySet().size());

        for (T key : keys) {
            List<CyNode> partition = targetsMap.get(key);
            Collections.sort(partition, comparator);
            sortedlist.add(partition);
        }

        Collections.sort(invalidNodes, comparator);
        sortedlist.add(invalidNodes);

        return sortedlist;
    }

    /**
     * Returns a list of lists of baits sorted by attribute, then by SUID
     *
     * @return
     */
    List<List<CyNode>> getSortedBaits() {

        if (baitsMap.isEmpty()) {
            return null;
        }

        List<U> keys = new ArrayList<U>(baitsMap.keySet());
        Collections.sort(keys);

        List<List<CyNode>> sortedlist = new ArrayList<List<CyNode>>(baitsMap.keySet().size());

        for (U key : keys) {
            List<CyNode> partition = baitsMap.get(key);
            Collections.sort(partition, comparator);
            sortedlist.add(partition);
        }

        return sortedlist;
    }

    /**
     * sort in reverse order (from big to small)
     *
     * @param other
     * @return
     */
    @Override
    public int compareTo(CComponent other) {
        return Integer.compare(other.size, this.size);
    }

}
