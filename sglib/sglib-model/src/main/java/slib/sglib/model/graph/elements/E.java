/*

 Copyright or © or Copr. Ecole des Mines d'Alès (2012) 

 This software is a computer program whose purpose is to 
 process semantic graphs.

 This software is governed by the CeCILL  license under French law and
 abiding by the rules of distribution of free software.  You can  use, 
 modify and/ or redistribute the software under the terms of the CeCILL
 license as circulated by CEA, CNRS and INRIA at the following URL
 "http://www.cecill.info". 

 As a counterpart to the access to the source code and  rights to copy,
 modify and redistribute granted by the license, users are provided only
 with a limited warranty  and the software's author,  the holder of the
 economic rights,  and the successive licensors  have only  limited
 liability. 

 In this respect, the user's attention is drawn to the risks associated
 with loading,  using,  modifying and/or developing or reproducing the
 software by the user in light of its specific status of free software,
 that may mean  that it is complicated to manipulate,  and  that  also
 therefore means  that it is reserved for developers  and  experienced
 professionals having in-depth computer knowledge. Users are therefore
 encouraged to load and test the software's suitability as regards their
 requirements in conditions enabling the security of their systems and/or 
 data to be ensured and,  more generally, to use and operate it in the 
 same conditions as regards security. 

 The fact that you are presently reading this means that you have had
 knowledge of the CeCILL license and that you accept its terms.

 */
package slib.sglib.model.graph.elements;

import org.openrdf.model.URI;

/**
 * Interface of an edge defined by a source, a target and an type.
 * Otherwise stated, an edge can be viewed as a statement subject, predicate,
 * object considering that all elements are URIs.
 *
 * Note that edges are not expected to be modifiable i.e. there source, target
 * and type must be final. Therefore no setters must be specified for the
 * source, the target and the predicate of the edge. Moreover the source, the
 * target and the predicate must not be null.
 *
 * @author Sebastien Harispe
 */
public interface E {

    /**
     * @return the vertex corresponding to the source of the edge.
     */
    public URI getSource();

    /**
     * @return the vertex corresponding to the target of the edge.
     */
    public URI getTarget();

    /**
     * @return the predicate URI of the edge.
     */
    public URI getURI();
}
