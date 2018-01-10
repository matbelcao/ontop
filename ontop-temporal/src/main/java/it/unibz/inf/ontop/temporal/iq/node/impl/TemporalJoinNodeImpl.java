package it.unibz.inf.ontop.temporal.iq.node.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import it.unibz.inf.ontop.datalog.impl.DatalogTools;
import it.unibz.inf.ontop.evaluator.ExpressionEvaluator;
import it.unibz.inf.ontop.evaluator.TermNullabilityEvaluator;
import it.unibz.inf.ontop.injection.IntermediateQueryFactory;
import it.unibz.inf.ontop.iq.IQProperties;
import it.unibz.inf.ontop.iq.IQTree;
import it.unibz.inf.ontop.iq.IntermediateQuery;
import it.unibz.inf.ontop.iq.exception.QueryNodeSubstitutionException;
import it.unibz.inf.ontop.iq.exception.QueryNodeTransformationException;
import it.unibz.inf.ontop.iq.impl.DefaultSubstitutionResults;
import it.unibz.inf.ontop.iq.node.*;
import it.unibz.inf.ontop.iq.node.impl.ConstructionNodeTools;
import it.unibz.inf.ontop.iq.node.impl.JoinLikeNodeImpl;
import it.unibz.inf.ontop.iq.transform.node.HeterogeneousQueryNodeTransformer;
import it.unibz.inf.ontop.iq.transform.node.HomogeneousQueryNodeTransformer;
import it.unibz.inf.ontop.model.term.*;
import it.unibz.inf.ontop.model.term.impl.ImmutabilityTools;
import it.unibz.inf.ontop.model.type.TypeFactory;
import it.unibz.inf.ontop.substitution.ImmutableSubstitution;
import it.unibz.inf.ontop.substitution.SubstitutionFactory;
import it.unibz.inf.ontop.substitution.impl.ImmutableSubstitutionTools;
import it.unibz.inf.ontop.substitution.impl.ImmutableUnificationTools;
import it.unibz.inf.ontop.temporal.iq.node.TemporalJoinNode;
import it.unibz.inf.ontop.temporal.iq.node.TemporalQueryNodeVisitor;
import it.unibz.inf.ontop.utils.ImmutableCollectors;
import it.unibz.inf.ontop.utils.VariableGenerator;

import java.util.Map;
import java.util.Optional;


public class TemporalJoinNodeImpl extends JoinLikeNodeImpl implements TemporalJoinNode {

    private static final String JOIN_NODE_STR = "TEMPORAL JOIN" ;

    private static final int MAX_ITERATIONS = 100000;
    private final IntermediateQueryFactory iqFactory;
    private final SubstitutionFactory substitutionFactory;
    private final ConstructionNodeTools constructionNodeTools;

    @AssistedInject
    protected TemporalJoinNodeImpl(@Assisted Optional<ImmutableExpression> optionalFilterCondition,
                                TermNullabilityEvaluator nullabilityEvaluator,
                                TermFactory termFactory, TypeFactory typeFactory, DatalogTools datalogTools,
                                ExpressionEvaluator defaultExpressionEvaluator, ImmutabilityTools immutabilityTools,
                                IntermediateQueryFactory iqFactory, SubstitutionFactory substitutionFactory,
                                ConstructionNodeTools constructionNodeTools,
                                ImmutableUnificationTools unificationTools, ImmutableSubstitutionTools substitutionTools) {
        super(optionalFilterCondition, nullabilityEvaluator, termFactory, iqFactory, typeFactory, datalogTools,
                defaultExpressionEvaluator, immutabilityTools, substitutionFactory, unificationTools, substitutionTools);
        this.iqFactory = iqFactory;
        this.substitutionFactory = substitutionFactory;
        this.constructionNodeTools = constructionNodeTools;
    }

    @AssistedInject
    private TemporalJoinNodeImpl(@Assisted ImmutableExpression joiningCondition,
                              TermNullabilityEvaluator nullabilityEvaluator,
                              TermFactory termFactory, TypeFactory typeFactory, DatalogTools datalogTools,
                              ExpressionEvaluator defaultExpressionEvaluator, ImmutabilityTools immutabilityTools,
                              IntermediateQueryFactory iqFactory, SubstitutionFactory substitutionFactory,
                              ConstructionNodeTools constructionNodeTools,
                              ImmutableUnificationTools unificationTools, ImmutableSubstitutionTools substitutionTools) {
        super(Optional.of(joiningCondition), nullabilityEvaluator, termFactory, iqFactory, typeFactory, datalogTools,
                defaultExpressionEvaluator, immutabilityTools, substitutionFactory, unificationTools, substitutionTools);
        this.iqFactory = iqFactory;
        this.substitutionFactory = substitutionFactory;
        this.constructionNodeTools = constructionNodeTools;
    }

    @AssistedInject
    private TemporalJoinNodeImpl(TermNullabilityEvaluator nullabilityEvaluator, TermFactory termFactory,
                              TypeFactory typeFactory, DatalogTools datalogTools,
                              ExpressionEvaluator defaultExpressionEvaluator, ImmutabilityTools immutabilityTools,
                              IntermediateQueryFactory iqFactory, SubstitutionFactory substitutionFactory,
                              ConstructionNodeTools constructionNodeTools,
                              ImmutableUnificationTools unificationTools, ImmutableSubstitutionTools substitutionTools) {
        super(Optional.empty(), nullabilityEvaluator, termFactory, iqFactory, typeFactory, datalogTools, defaultExpressionEvaluator,
                immutabilityTools, substitutionFactory, unificationTools, substitutionTools);
        this.iqFactory = iqFactory;
        this.substitutionFactory = substitutionFactory;
        this.constructionNodeTools = constructionNodeTools;
    }

    @Override
    public TemporalJoinNode changeOptionalFilterCondition(Optional<ImmutableExpression> newOptionalFilterCondition) {
        return new TemporalJoinNodeImpl(newOptionalFilterCondition, getNullabilityEvaluator(),
                termFactory, typeFactory, datalogTools, createExpressionEvaluator(), getImmutabilityTools(), iqFactory,
                substitutionFactory, constructionNodeTools, unificationTools, substitutionTools);
    }


    private SubstitutionResults<TemporalJoinNode> applyEvaluation(ExpressionEvaluator.EvaluationResult evaluationResult,
                                                               ImmutableSubstitution<? extends ImmutableTerm> substitution) {
        if (evaluationResult.isEffectiveFalse()) {
            return DefaultSubstitutionResults.declareAsEmpty();
        }
        else {
            TemporalJoinNode newNode = changeOptionalFilterCondition(evaluationResult.getOptionalExpression());
            return DefaultSubstitutionResults.newNode(newNode, substitution);
        }
    }

    @Override
    public void acceptVisitor(QueryNodeVisitor visitor) {
        ((TemporalQueryNodeVisitor)visitor).visit(this);
    }

    @Override
    public TemporalJoinNode acceptNodeTransformer(HomogeneousQueryNodeTransformer transformer) throws QueryNodeTransformationException {
        return this;
    }

    @Override
    public NodeTransformationProposal acceptNodeTransformer(HeterogeneousQueryNodeTransformer transformer) {
        return null;
    }


    @Override
    public boolean isVariableNullable(IntermediateQuery query, Variable variable) {
        return false;
    }

    @Override
    public boolean isSyntacticallyEquivalentTo(QueryNode node) {
        return false;
    }

    @Override
    public boolean isEquivalentTo(QueryNode queryNode) {
        return false;
    }

    @Override
    public String toString() {
        return JOIN_NODE_STR + getOptionalFilterString();
    }


    @Override
    public IQTree liftBinding(ImmutableList<IQTree> children, VariableGenerator variableGenerator, IQProperties currentIQProperties) {
        return null;
    }

    @Override
    public IQTree applyDescendingSubstitution(ImmutableSubstitution<? extends VariableOrGroundTerm> descendingSubstitution, Optional<ImmutableExpression> constraint, ImmutableList<IQTree> children) {
        return null;
    }

    @Override
    public ImmutableSet<Variable> getNullableVariables(ImmutableList<IQTree> children) {
        return null;
    }

    @Override
    public boolean isConstructed(Variable variable, ImmutableList<IQTree> children) {
        return false;
    }

    @Override
    public IQTree liftIncompatibleDefinitions(Variable variable, ImmutableList<IQTree> children) {
        return null;
    }

    @Override
    public IQTree propagateDownConstraint(ImmutableExpression constraint, ImmutableList<IQTree> children) {
        return null;
    }
}
