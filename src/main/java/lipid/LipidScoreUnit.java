package lipid;

import org.drools.ruleunits.api.DataSource;
import org.drools.ruleunits.api.DataStore;
import org.drools.ruleunits.api.RuleUnitData;

/**
 * Rule unit data class that contains the facts (annotations)
 * to be evaluated by Drools rules in the context of lipid scoring.
 */
public class LipidScoreUnit implements RuleUnitData {

    private final DataStore<Annotation> annotations;

    public LipidScoreUnit() {
        this(DataSource.createStore());
    }

    public LipidScoreUnit(DataStore<Annotation> annotations) {
        this.annotations = annotations;

    }

    public DataStore<Annotation> getAnnotations() {
        return annotations;
    }

}
