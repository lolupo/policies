package com.tinubu.policies.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.tinubu.policies")
public class CleanArchitectureImplementationTest {
    @ArchTest
    static final ArchRule domainShouldBeIndependent =
            ArchRuleDefinition.classes()
                    .that().resideInAPackage("..domain..")
                    .should().onlyDependOnClassesThat()
                    .resideInAnyPackage("..domain..", "java..", "javax..", "lombok..", "org.jmolecules..", "org.junit..");

    @ArchTest
    static final ArchRule applicationShouldNotDependOnInfrastructureOrApi =
            ArchRuleDefinition.classes()
                    .that().resideInAPackage("..application..")
                    .should().onlyHaveDependentClassesThat().resideInAnyPackage("..domain..", "..application..");

    @ArchTest
    static final ArchRule infrastructureShouldNotDependOnApi =
            ArchRuleDefinition.classes()
                    .that().resideInAPackage("..infrastructure..")
                    .should().onlyHaveDependentClassesThat().resideInAnyPackage("..domain..", "..application..", "..infrastructure..");

}
