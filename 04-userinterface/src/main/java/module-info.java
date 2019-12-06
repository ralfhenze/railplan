module com.ralfhenze.railplan.userinterface {
	requires com.ralfhenze.railplan.domain;
	requires com.ralfhenze.railplan.application;
	requires com.ralfhenze.railplan.infrastructure;

	requires mongo.java.driver;
	requires spring.beans;
	requires spring.data.mongodb;
	requires spring.web;
	requires spring.boot;
	requires spring.boot.autoconfigure;
	requires spring.context;
}
