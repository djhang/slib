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
 
 
package slib.utils.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author seb
 */
public class BigFileReader
{
	private String _currentLine;
	private BufferedReader _reader;

	/**
     *
     * @param filePath
     * @throws FileNotFoundException
     */
    public BigFileReader(String filePath) throws FileNotFoundException {
		_reader = new BufferedReader(new FileReader(filePath));
	}

	/**
     *
     * @throws IOException
     */
    public void close() throws IOException{
		_reader.close();
	}

	/**
     *
     * @return
     * @throws IOException
     */
    public boolean hasNext() throws IOException{
		_currentLine = _reader.readLine();
		return _currentLine != null;
	}

	/**
     *
     * @return
     */
    public String next(){
		return _currentLine;
	}
	
	/**
     *
     * @return
     */
    public String nextTrimmed(){
		return _currentLine.trim();
	}

}
