/*
 * 
 * Copyright or © or Copr. Ecole des Mines d'Alès (2012) 
 * LGI2P research center
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 * 
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 * 
 */
package slib.sglib.algo.graph.accessor;

import java.util.HashSet;
import java.util.Set;
import org.openrdf.model.URI;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slib.sglib.model.graph.G;
import slib.sglib.model.graph.elements.E;
import slib.sglib.model.graph.utils.Direction;
import slib.utils.impl.SetUtils;

/**
 *
 * @author Harispe Sébastien <harispe.sebastien@gmail.com>
 */
public class GraphAccessor {

    static Logger logger = LoggerFactory.getLogger(GraphAccessor.class);

    /**
     * Return a set of URI corresponding to the classes of the graph. A vertex v
     * of the graph is considered as a class if the graph contains a statement
     * of the form :
     * <ul>
     * <li> v RFDS.SUBCLASSOF ? </li>
     * <li> ? RDFS.SUBCLASSOF v </li>
     * <li> ? RDF.TYPE v </li>
     * </ul>
     *
     * @parm g the graph
     * @return a set of URI corresponding to the classes of the graph
     */
    public static Set<URI> getClasses(G graph) {

        logger.debug("retrieving Classes");

        Set<URI> classes = new HashSet<URI>();
        for (E e : graph.getE(RDFS.SUBCLASSOF)) {
            classes.add(e.getSource());
            classes.add(e.getTarget());
        }
        for (E e : graph.getE(RDF.TYPE)) {
            classes.add(e.getTarget());
        }

        logger.debug("Classes detected " + classes.size());
        return classes;
    }

    /**
     * Return a set of URI corresponding to the instances of the graph, note
     * that instance definition is here different from RDF the definition of an
     * instance. A vertex v of the graph is considered as an instance if the
     * graph contains a statement of the form :
     * <ul>
     * <li> v RFD.TYPE ? with ? not equals to
     * RDFS.RESOURCE/CLASS/LITERAL/DATATYPE/PROPERTY/XMLLITERAL
     * </li>
     * Those restrictions do not cover all cases e.g. RDF instance of
     * RDFS.CONTAINER will be considered as instance...
     * </ul>
     *
     * @parm g the graph
     * @return a set of URI corresponding to the classes of the graph
     */
    public static Set<URI> getInstances(G graph) {
        Set<URI> instances = new HashSet<URI>();
        URI o;
        for (E e : graph.getE(RDF.TYPE)) {

            o = e.getURI();
            if (!o.equals(RDFS.RESOURCE)
                    && !o.equals(RDFS.CLASS)
                    && !o.equals(RDFS.LITERAL)
                    && !o.equals(RDFS.DATATYPE)
                    && !o.equals(RDF.PROPERTY)
                    && !o.equals(RDF.XMLLITERAL)) {
                instances.add(e.getSource());
            }
        }


        return instances;
    }

    public static Set<URI> getV_NoEdgeType(G g, URI edgeType, Direction dir) {
        return getV_NoEdgeType(g, SetUtils.buildSet(edgeType), dir);
    }

    public static Set<URI> getV_NoEdgeType(G g, Set<URI> edgeTypes, Direction dir) {


        Set<URI> valid = new HashSet<URI>();

        Set<URI> vSel = g.getV();

        if (dir == Direction.OUT || dir == Direction.BOTH) {

            for (URI v : vSel) {

                Set<E> edgesSel = g.getE(v, Direction.OUT);

                boolean isValid = true;

                for (E e : edgesSel) {

                    if (edgeTypes == null || edgeTypes.contains(e.getURI())) {
                        isValid = false;
                        break;
                    }
                }
                if (isValid) {
                    valid.add(v);
                }
            }
        }
        if (dir == Direction.IN || dir == Direction.BOTH) {

            for (URI v : vSel) {
                Set<E> edges = g.getE(v, Direction.IN);

                boolean isValid = true;

                for (E e : edges) {
                    if (edgeTypes == null || edgeTypes.contains(e.getURI())) {

                        isValid = false;
                        break;
                    }
                }
                if (isValid) {
                    valid.add(v);
                }
            }
        }
        return valid;
    }
}
