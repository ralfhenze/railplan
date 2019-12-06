module com.ralfhenze.railplan.infrastructure {
	requires com.ralfhenze.railplan.domain;
	requires com.ralfhenze.railplan.application;

	requires mongo.java.driver;
	requires spring.data.mongodb;
	requires spring.beans;
	requires spring.context;
	requires spring.data.commons;
	requires org.eclipse.collections.api;

	exports com.ralfhenze.railplan.infrastructure.persistence;
	exports com.ralfhenze.railplan.infrastructure.persistence.dto;
}
