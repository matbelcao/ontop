package it.unibz.inf.ontop.executor.unsatisfiable;

import it.unibz.inf.ontop.executor.NodeCentricExecutor;
import it.unibz.inf.ontop.iq.node.EmptyNode;
import it.unibz.inf.ontop.iq.proposal.NodeTrackingResults;
import it.unibz.inf.ontop.iq.proposal.RemoveEmptyNodeProposal;

public interface RemoveEmptyNodesExecutor extends NodeCentricExecutor<
        EmptyNode,
        NodeTrackingResults<EmptyNode>,
        RemoveEmptyNodeProposal> {
}
