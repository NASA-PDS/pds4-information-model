// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// * Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// * Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// * Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package gov.nasa.pds.model.plugin;

import java.util.ArrayList;

public class DMDebugUtils {

  public DMDebugUtils() {

  }

  static public void dumpTermEntryMap(String id, DOMAttr domAttr) {
	    
	    // Terminological Entries
	    if (domAttr.termEntryMap != null && domAttr.termEntryMap.size() > 0) {
	      System.out.println("\ndebug  DMDebugUtils  termEntryMap");
	      ArrayList <TermEntryDefn> termEntryDefnArr = new ArrayList <TermEntryDefn> (domAttr.termEntryMap.values());
	      
	      for (TermEntryDefn termEntryDefn : termEntryDefnArr) {
	    	  dumpTermEntry(id, termEntryDefn);
	      }
	    }
	  return;
  }

  static public void dumpTermEntry(String id, TermEntryDefn termEntryDefn) {
      System.out.println("\ndebug  DMDebugUtils -" + id + "- termEntryDefn.name:" + termEntryDefn.name);
      System.out.println("debug  DMDebugUtils -" + id + "- termEntryDefn.lddName:" + termEntryDefn.lddName);
      System.out.println("debug  DMDebugUtils -" + id + "- termEntryDefn.lddVersion:" + termEntryDefn.lddVersion);
      System.out.println("debug  DMDebugUtils -" + id + "- termEntryDefn.definition:" + termEntryDefn.definition);
      System.out.println("debug  DMDebugUtils -" + id + "- termEntryDefn.language:" + termEntryDefn.language);
      System.out.println("debug  DMDebugUtils -" + id + "- termEntryDefn.semanticRelation:" + termEntryDefn.semanticRelation);
      System.out.println("debug  DMDebugUtils -" + id + "- termEntryDefn.mappingProperty:" + termEntryDefn.mappingProperty);
      System.out.println("debug  DMDebugUtils -" + id + "- termEntryDefn.sourceNamespaceId:" + termEntryDefn.sourceNamespaceId);
      System.out.println("debug  DMDebugUtils -" + id + "- termEntryDefn.fromInstanceId:" + termEntryDefn.fromInstanceId);
      System.out.println("debug  DMDebugUtils -" + id + "- termEntryDefn.toInstanceId:" + termEntryDefn.toInstanceId);
      System.out.println("debug  DMDebugUtils -" + id + "- termEntryDefn.isPreferred:" + termEntryDefn.isPreferred);
      System.out.println("");
	return;
  }
 
}
