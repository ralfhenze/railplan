module com.ralfhenze.railplan.domain {
	requires org.eclipse.collections.api;
	requires javatuples;

	exports com.ralfhenze.railplan.domain.common;
	exports com.ralfhenze.railplan.domain.common.validation;
	exports com.ralfhenze.railplan.domain.common.validation.constraints;
	exports com.ralfhenze.railplan.domain.railnetwork.elements;
	exports com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft;
	exports com.ralfhenze.railplan.domain.railnetwork.presets;
}
