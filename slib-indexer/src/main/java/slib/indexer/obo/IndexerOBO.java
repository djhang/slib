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
 
 
package slib.indexer.obo;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import org.openrdf.model.URI;
import slib.indexer.IndexElementBasic;
import slib.indexer.IndexHash;
import slib.sglib.model.repo.URIFactory;
import slib.utils.ex.SLIB_Ex_Critic;
import slib.utils.ex.SLIB_Exception;
import slib.utils.impl.OBOconstants;

/**
 *
 * @author seb
 */
public class IndexerOBO {

    URIFactory factory;
    
    boolean onTermSpec = false;
    String currentURI = null;
    String currentName = null;
    Pattern colon = Pattern.compile(":");
    Pattern exclamation = Pattern.compile("!");
    Pattern spaces = Pattern.compile("\\s+");
    IndexHash index;
    String defaultNamespace;

    /**
     *
     * @param factory
     * @param filepath
     * @param defaultNamespace
     * @return
     * @throws SLIB_Exception
     */
    public IndexHash buildIndex(URIFactory factory, String filepath, String defaultNamespace) throws SLIB_Exception {
        
        this.factory = factory;
        index = new IndexHash();

        this.defaultNamespace = defaultNamespace;
        try {

            FileInputStream fstream = new FileInputStream(filepath);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line;

            boolean metadataLoaded = false;

            String flag, value;

            while ((line = br.readLine()) != null) {

                flag = null;
                value = null;

                line = line.trim();

                if (!metadataLoaded) { // loading OBO meta data

                    if (line.equals(OBOconstants.TERM_FLAG) || line.equals(OBOconstants.TYPEDEF_FLAG)) {

                        metadataLoaded = true;

                        // check format-version 
//						if(!format_version.equals(format_parser))
//							throw new SGTK_Exception_Warning("Parser of format-version "+format_parser+" used to load OBO version "+format_version);

                        if (line.equals(OBOconstants.TERM_FLAG)) {
                            onTermSpec = true;
                        }
                    }
                } else {
                    if (onTermSpec) { // loading [Term]

                        checkLine(line);

                        if (onTermSpec) {

                            String[] data = getData(line, ":");

                            if (data.length < 2) {
                                continue;
                            }

                            flag = data[0];
                            value = buildValue(data, 1, ":");
                            value = removeComment(value);

                            if (flag.equals(OBOconstants.TERM_ID_FLAG)) { // id
                                currentURI = buildURI(value);
                            } else if (flag.equals(OBOconstants.NAME_FLAG)) { // is_a
                                currentName = value;
                            }
                        }
                    }

                }
            }
            if(onTermSpec){ handleTerm(); }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new SLIB_Ex_Critic(e.getMessage());
        }
        return index;
    }

    private String[] getDataColonSplit(String line) {

        if (line.isEmpty()) {
            return null;
        }

        String data[] = colon.split(exclamation.split(line, 2)[0], 2);
        data[0] = data[0].trim();

        if (data.length > 1) {
            data[1] = data[1].trim();
        }

        return data;
    }

    private String buildURI(String value) throws SLIB_Ex_Critic {

        String info[] = getDataColonSplit(value);


        if (info != null && info.length == 2) {

            String ns = factory.getNamespace(info[0]);
            if (ns == null) {
                throw new SLIB_Ex_Critic("No namespace associated to prefix " + info[0] + ". Cannot load " + value + ", please load required namespace prefix");
            }

            return ns + info[1];
        } else {
            if(defaultNamespace == null){
                throw new SLIB_Ex_Critic("No default-namespace. Cannot load " + value + ", please load required namespace prefix");
            }
            return defaultNamespace + value;
        }
    }

    private String[] getData(String line, String regex) {

        String data_prec[] = line.split("!"); // remove comment
        String data[] = data_prec[0].split(regex);

        for (int i = 0; i < data.length; i++) {
            data[i] = data[i].trim();
        }
        return data;
    }

    private String removeComment(String value) {
        return value.split("!")[0].trim();
    }

    private String buildValue(String[] data, int from, String glue) {
        String value = "";
        for (int i = from; i < data.length; i++) {

            if (i != from) {
                value += glue;
            }

            value += data[i];
        }
        return value;
    }

    private void checkLine(String line) throws SLIB_Ex_Critic {

        if (line.equals(OBOconstants.TERM_FLAG)) {
            handleTerm();
            onTermSpec = true;
        }
    }

    private void handleTerm() throws SLIB_Ex_Critic {

        if (onTermSpec) {

            URI uri = factory.createURI(currentURI);
            
            IndexElementBasic i = new IndexElementBasic(uri,currentName);
            index.addValue(uri, i);

            currentURI  = null;
            currentName = null;
        }
    }
}
