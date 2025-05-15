package lipid;
import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.RuleUnitProvider;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static org.junit.Assert.assertEquals;


public class ElutionOrderTest {
    static final Logger LOG = LoggerFactory.getLogger(ElutionOrderTest.class);

    /**
     * Test to check the elution order of the lipids. The elution order is based on the number of carbons if the lipid type and the number of
     * double bonds is the same. The larger the number of carbons, the longer the RT.
     */
    @Test
    public void score1BasedOnRTCarbonNumbers() {
        LOG.info("Creating RuleUnit");
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        Lipid lipid1 = new Lipid(1, "TG 54:3", "C57H104O6", LipidType.TG, 54, 3); // MZ of [M+H]+ = 885.79057
        Lipid lipid2 = new Lipid(2, "TG 52:3", "C55H100O6", LipidType.TG, 52, 3); // MZ of [M+H]+ = 857.75927
        Lipid lipid3 = new Lipid(3, "TG 56:3", "C59H108O6", LipidType.TG, 56, 3); // MZ of [M+H]+ = 913.82187
        Annotation annotation1 = new Annotation(lipid1, 885.79056, 10E6, 10d, IoniationMode.POSITIVE);
        Annotation annotation2 = new Annotation(lipid2, 857.7593, 10E7, 9d, IoniationMode.POSITIVE);
        Annotation annotation3 = new Annotation(lipid3, 913.822, 10E5, 11d, IoniationMode.POSITIVE);

        LOG.info("Insert data");

        try {
            lipidScoreUnit.getAnnotations().add(annotation1);
            lipidScoreUnit.getAnnotations().add(annotation2);
            lipidScoreUnit.getAnnotations().add(annotation3);

            LOG.info("Run query. Rules are also fired");
            instance.fire();

            assertEquals(1.0, annotation1.getNormalizedScore(), 0.01);
            assertEquals(1.0, annotation2.getNormalizedScore(), 0.01);
            assertEquals(1.0, annotation3.getNormalizedScore(), 0.01);

        }
        finally {
            instance.close();
        }

    }

    /**
     * Test to check the elution order of the lipids. The elution order is based on the number of double bonds if the lipid type and the number of
     * carbons is the same. The higher the number of double bonds, the shorter the RT.
     */
    @Test
    public void score1BasedOnRTDoubleBonds() {
        LOG.info("Creating RuleUnit");
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        Lipid lipid1 = new Lipid(1, "TG 54:3", "C57H104O6", LipidType.TG, 54, 3); // MZ of [M+H]+ = 885.79057
        Lipid lipid2 = new Lipid(2, "TG 54:4", "C57H102O6", LipidType.TG, 54, 4); // MZ of [M+H]+ = 883.77492
        Lipid lipid3 = new Lipid(3, "TG 54:2", "C57H106O6", LipidType.TG, 54, 2); // MZ of [M+H]+ = 887.80622
        Annotation annotation1 = new Annotation(lipid1, 885.79056, 10E6, 10d, IoniationMode.POSITIVE);
        Annotation annotation2 = new Annotation(lipid2, 883.77492, 10E7, 9d, IoniationMode.POSITIVE);
        Annotation annotation3 = new Annotation(lipid3, 887.80622, 10E5, 11d, IoniationMode.POSITIVE);

        LOG.info("Insert data");

        try {
            lipidScoreUnit.getAnnotations().add(annotation1);
            lipidScoreUnit.getAnnotations().add(annotation2);
            lipidScoreUnit.getAnnotations().add(annotation3);

            LOG.info("Run query. Rules are also fired");
            instance.fire();
            int fired = instance.fire();
            System.out.println("Número de reglas disparadas: " + fired);
            LOG.info("Número de reglas disparadas: {}", fired);

            assertEquals(1.0, annotation1.getNormalizedScore(), 0.01);
            assertEquals(1.0, annotation2.getNormalizedScore(), 0.01);
            assertEquals(1.0, annotation3.getNormalizedScore(), 0.01);

        }
        finally {
            instance.close();
        }
    }

    /**
     * Test to check the elution order of the lipids. The elution order is based on the number of double bonds if the lipid type and the number of
     * carbons is the same. The higher the number of double bonds, the shorter the RT.
     * The RT order of lipids with the same number of carbons and double bonds is the same
     * -> PG < PE < PI < PA < PS << PC.
     */
    @Test
    public void score1BasedOnLipidType() {
        LOG.info("Creating RuleUnit");
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        Lipid lipid1 = new Lipid(1, "PI 34:0", "C43H83O13P", LipidType.PI, 54, 0); // MZ of [M+H]+ = 839.56441
        Lipid lipid2 = new Lipid(2, "PG 34:0", "C40H79O10P", LipidType.PG, 54, 0); // MZ of [M+H]+ = 751.54836
        Lipid lipid3 = new Lipid(3, "PC 34:0", "C42H84NO8P", LipidType.PC, 54, 0); // MZ of [M+H]+ = 762.60073
        Annotation annotation1 = new Annotation(lipid1, 839.5644179056, 10E6, 10d, IoniationMode.POSITIVE);
        Annotation annotation2 = new Annotation(lipid2, 751.54836, 10E7, 9d, IoniationMode.POSITIVE);
        Annotation annotation3 = new Annotation(lipid3, 913.822, 10E5, 11d, IoniationMode.POSITIVE);

        LOG.info("Insert data");

        try {
            lipidScoreUnit.getAnnotations().add(annotation1);
            lipidScoreUnit.getAnnotations().add(annotation2);
            lipidScoreUnit.getAnnotations().add(annotation3);

            LOG.info("Run query. Rules are also fired");
            instance.fire();

            assertEquals(1.0, annotation1.getNormalizedScore(), 0.01);
            assertEquals(1.0, annotation2.getNormalizedScore(), 0.01);
            assertEquals(1.0, annotation3.getNormalizedScore(), 0.01);

        }
        finally {
            instance.close();
        }
    }

    /**
     * Test to check the elution order of the lipids. The elution order is based on the number of double bonds if the lipid type and the number of
     * carbons is the same. The higher the number of double bonds, the shorter the RT.
     * The RT order of lipids with the same number of carbons and double bonds is the same
     * -> PG < PE < PI < PA < PS << PC.
     */
    @Test
    public void negativeScoreBasedOnRTNumberOfCarbons() {
        LOG.info("Creating RuleUnit");
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        Lipid lipid1 = new Lipid(1, "TG 54:3", "C57H104O6", LipidType.TG, 54, 3); // MZ of [M+H]+ = 885.79057
        Lipid lipid2 = new Lipid(2, "TG 52:3", "C55H100O6", LipidType.TG, 52, 3); // MZ of [M+H]+ = 857.75927
        Lipid lipid3 = new Lipid(3, "TG 56:3", "C59H108O6", LipidType.TG, 56, 3); // MZ of [M+H]+ = 913.82187
        Annotation annotation1 = new Annotation(lipid1, 885.79056, 10E6, 10d, IoniationMode.POSITIVE);
        Annotation annotation2 = new Annotation(lipid2, 857.7593, 10E7, 9d, IoniationMode.POSITIVE);
        Annotation annotation3 = new Annotation(lipid3, 913.822, 10E5, 8d, IoniationMode.POSITIVE);


        LOG.info("Insert data");

        try {
            lipidScoreUnit.getAnnotations().add(annotation1);
            lipidScoreUnit.getAnnotations().add(annotation2);
            lipidScoreUnit.getAnnotations().add(annotation3);

            LOG.info("Run query. Rules are also fired");
            instance.fire();

            assertEquals(0d, annotation1.getNormalizedScore(), 0.01); // !! !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself
            assertEquals(0d, annotation2.getNormalizedScore(), 0.01); // !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself
            assertEquals(-1.0, annotation3.getNormalizedScore(), 0.01); // !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself

        }
        finally {
            instance.close();
        }
    }

    /**
     * Test to check the elution order of the lipids. The elution order is based on the number of double bonds if the lipid type and the number of
     * carbons is the same. The higher the number of double bonds, the shorter the RT.
     */
    @Test
    public void negativeScoreBasedOnRTDoubleBonds() {
        // Assume lipids already annotated
        LOG.info("Creating RuleUnit");
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        Lipid lipid1 = new Lipid(1, "TG 54:3", "C57H104O6", LipidType.TG, 54, 3); // MZ of [M+H]+ = 885.79057
        Lipid lipid2 = new Lipid(2, "TG 54:4", "C57H102O6", LipidType.TG, 54, 4); // MZ of [M+H]+ = 883.77492
        Lipid lipid3 = new Lipid(3, "TG 54:2", "C57H106O6", LipidType.TG, 54, 2); // MZ of [M+H]+ = 887.80622
        Annotation annotation1 = new Annotation(lipid1, 885.79056, 10E6, 10d, IoniationMode.POSITIVE);
        Annotation annotation2 = new Annotation(lipid2, 883.77492, 10E7, 9d, IoniationMode.POSITIVE);
        Annotation annotation3 = new Annotation(lipid3, 887.80622, 10E5, 8d, IoniationMode.POSITIVE);

        LOG.info("Insert data");

        try {
            lipidScoreUnit.getAnnotations().add(annotation1);
            lipidScoreUnit.getAnnotations().add(annotation2);
            lipidScoreUnit.getAnnotations().add(annotation3);

            LOG.info("Run query. Rules are also fired");
            instance.fire();

            assertEquals(0d, annotation1.getNormalizedScore(), 0.01); // !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself
            assertEquals(0d, annotation2.getNormalizedScore(), 0.01); // !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself
            assertEquals(-1.0, annotation3.getNormalizedScore(), 0.01); // !! !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself

        }
        finally {
            instance.close();
        }
    }

    /**
     * Test to check the elution order of the lipids. The elution order is based on the number of carbons if the lipid type and the number of
     * double bonds is the same. The larger the number of carbons, the longer the RT.
     */
    @Test
    public void negativeScoreBasedOnLipidType() {
        LOG.info("Creating RuleUnit");
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        Lipid lipid1 = new Lipid(1, "PI 34:0", "C43H83O13P", LipidType.PI, 54, 0); // MZ of [M+H]+ = 839.56441
        Lipid lipid2 = new Lipid(2, "PG 34:0", "C40H79O10P", LipidType.PG, 54, 0); // MZ of [M+H]+ = 751.54836
        Lipid lipid3 = new Lipid(3, "PC 34:0", "C42H84NO8P", LipidType.PC, 54, 0); // MZ of [M+H]+ = 762.60073
        Annotation annotation1 = new Annotation(lipid1, 839.5644179056, 10E6, 10d, IoniationMode.POSITIVE);
        Annotation annotation2 = new Annotation(lipid2, 751.54836, 10E7, 9d, IoniationMode.POSITIVE);
        Annotation annotation3 = new Annotation(lipid3, 913.822, 10E5, 8d, IoniationMode.POSITIVE);

        LOG.info("Insert data");

        try {
            lipidScoreUnit.getAnnotations().add(annotation1);
            lipidScoreUnit.getAnnotations().add(annotation2);
            lipidScoreUnit.getAnnotations().add(annotation3);

            LOG.info("Run query. Rules are also fired");
            instance.fire();

            assertEquals(0d, annotation1.getNormalizedScore(), 0.01); // !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself
            assertEquals(0d, annotation2.getNormalizedScore(), 0.01); // !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself
            assertEquals(-1.0, annotation3.getNormalizedScore(), 0.01); // !! !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself

        }
        finally {
            instance.close();
        }
    }
}
