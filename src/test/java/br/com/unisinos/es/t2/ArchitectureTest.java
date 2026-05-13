package br.com.unisinos.es.t2;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "br.com.unisinos.es.t2")
public class ArchitectureTest {

	private static final String DOMAIN = "Domain";
	private static final String PORT = "Port";
	private static final String ADAPTER_IN = "Adapter In";
	private static final String ADAPTER_OUT = "Adapter Out";
	private static final String CONFIG = "Config";

	@ArchTest
	static final ArchRule domain_should_not_depend_on_outside = noClasses()
			.that().resideInAPackage("..application.domain..")
			.should().dependOnClassesThat().resideOutsideOfPackages(
					"..application.domain..",
					"..application.port..",
					"java..",
					"javax..",
					"jakarta..",
					// Exceções definidas:
					"lombok..",
					"org.slf4j..",
					"org.springframework.stereotype..",
					"org.mapstruct.."
			)
			.because("O Domínio deve conter apenas regras de negócio puras.");

	@ArchTest
	static final ArchRule application_should_not_depend_on_adapters = noClasses()
			.that().resideInAPackage("..application..")
			.should().dependOnClassesThat().resideInAPackage("..adapter..")
			.because("A aplicação (use cases/portas) deve ser agnóstica em relação a quem a chama ou onde os dados são persistidos.");

	@ArchTest
	static final ArchRule adapters_in_should_not_depend_on_adapters_out = noClasses()
			.that().resideInAPackage("..adapter.in..")
			.should().dependOnClassesThat().resideInAPackage("..adapter.out.persistence..")
			.because("Adaptadores de entrada devem usar as portas de entrada da aplicação, e não chamar repositórios/bancos de dados diretamente.");

	@ArchTest
	static final ArchRule layer_dependencies_are_respected = layeredArchitecture()
			.consideringOnlyDependenciesInLayers()
			.layer(DOMAIN).definedBy("..application.domain..")
			.layer(PORT).definedBy("..application.port..")
			.layer(ADAPTER_IN).definedBy("..adapter.in..")
			.layer(ADAPTER_OUT).definedBy("..adapter.out.persistence..")
			.layer(CONFIG).definedBy("..config..")

			// Regras de quem pode acessar quem
			.whereLayer(DOMAIN).mayOnlyBeAccessedByLayers(PORT, ADAPTER_IN, ADAPTER_OUT, CONFIG)
			.whereLayer(PORT).mayOnlyBeAccessedByLayers(DOMAIN, ADAPTER_IN, ADAPTER_OUT, CONFIG)
			.whereLayer(ADAPTER_IN).mayNotBeAccessedByAnyLayer()
			.whereLayer(ADAPTER_OUT).mayNotBeAccessedByAnyLayer();

}
