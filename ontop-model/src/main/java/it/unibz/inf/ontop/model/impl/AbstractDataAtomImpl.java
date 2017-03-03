package it.unibz.inf.ontop.model.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import it.unibz.inf.ontop.model.*;


import java.util.HashSet;
import java.util.Set;


public abstract class AbstractDataAtomImpl extends ImmutableFunctionalTermImpl implements DataAtom {

    private final AtomPredicate predicate;

    protected AbstractDataAtomImpl(AtomPredicate predicate, ImmutableList<? extends VariableOrGroundTerm> variableOrGroundTerms) {
        super(predicate, variableOrGroundTerms);
        this.predicate = predicate;
    }

    protected AbstractDataAtomImpl(AtomPredicate predicate, VariableOrGroundTerm... variableOrGroundTerms) {
        super(predicate, variableOrGroundTerms);
        this.predicate = predicate;
    }

    @Override
    public AtomPredicate getPredicate() {
        return predicate;
    }

    @Override
    public int getEffectiveArity() {
        return getTerms().size();
    }

    @Override
    public boolean hasSamePredicateAndArity(DataAtom otherAtom) {
        if (!predicate.equals(otherAtom.getPredicate()))
            return false;

        if (getEffectiveArity() != otherAtom.getEffectiveArity())
            return false;

        return true;
    }

    @Override
    public ImmutableList<? extends VariableOrGroundTerm> getArguments() {
        return (ImmutableList<? extends VariableOrGroundTerm>)super.getArguments();
    }

    @Override
    public boolean containsGroundTerms() {
        for (ImmutableTerm term : getArguments()) {
            if (term.isGround()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public VariableOrGroundTerm getTerm(int index) {
        return (VariableOrGroundTerm) super.getTerm(index);
    }

    protected static boolean hasDuplicates(DataAtom atom) {
        ImmutableList<? extends VariableOrGroundTerm> termList = atom.getArguments();
        Set<VariableOrGroundTerm> termSet = new HashSet<>(termList);

        return termSet.size() < termList.size();
    }
}
