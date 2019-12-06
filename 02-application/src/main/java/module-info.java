module com.ralfhenze.railplan.application {
	requires com.ralfhenze.railplan.domain;

	requires org.eclipse.collections.api;

	exports com.ralfhenze.railplan.application.commands;
	exports com.ralfhenze.railplan.application.queries;
}
