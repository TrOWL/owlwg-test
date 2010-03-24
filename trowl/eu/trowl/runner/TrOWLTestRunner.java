/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.runner;

import java.net.URI;
import java.util.Collections;

import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

import com.clarkparsia.owlwg.owlapi2.runner.impl.OwlApi2AbstractRunner;
import com.clarkparsia.owlwg.owlapi2.runner.impl.OwlApi2EntailmentChecker;
import eu.trowl.owl.ReasonerFactory;

/**
 * <p>
 * Title: FaCT++ Test Runner
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright &copy; 2009
 * </p>
 * <p>
 * Company: Clark & Parsia, LLC. <a
 * href="http://clarkparsia.com/"/>http://clarkparsia.com/</a>
 * </p>
 *
 * @author Mike Smith &lt;msmith@clarkparsia.com&gt;
 */
public class TrOWLTestRunner extends OwlApi2AbstractRunner {

	public URI getURI() {
		return uri;
	}

	@Override
	protected boolean isConsistent(OWLOntologyManager manager, OWLOntology o)
			throws OWLReasonerException {

		OWLReasoner reasoner = reasonerFactory.createReasoner( manager );
		reasoner.loadOntologies( Collections.singleton( o ) );

		
			reasoner.classify();
			return reasoner.isConsistent( o );
		
	}

	@Override
	protected boolean isEntailed(OWLOntologyManager manager, OWLOntology premise,
			OWLOntology conclusion) throws OWLReasonerException {

		OWLReasoner reasoner = reasonerFactory.createReasoner( manager );

		reasoner.loadOntologies( Collections.singleton( premise ) );
		reasoner.classify();

		OwlApi2EntailmentChecker checker = new OwlApi2EntailmentChecker( reasoner, manager.getOWLDataFactory() );
		for( OWLAxiom axiom : conclusion.getLogicalAxioms() ) {
			if( !checker.isEntailed( axiom ) )
				return false;
		}

		return true;
	}

	private static final ReasonerFactory reasonerFactory;
	private static final URI uri;

	static {
		reasonerFactory = new ReasonerFactory();
		uri = URI.create( "http://trowl.eu/" );
	}

    public String getName() {
        return "TrOWL";
    }
}
