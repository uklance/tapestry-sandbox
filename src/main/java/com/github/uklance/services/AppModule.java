package com.github.uklance.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.Environment;

import com.github.uklance.mode.ModeComponentEventLinkEncoder;
import com.github.uklance.services.internal.ItemDaoImpl;
import com.github.uklance.services.internal.ItemServiceImpl;
import com.github.uklance.services.internal.TestItemInserter;

/**
 * This module is automatically included as part of the Tapestry IoC Registry,
 * it's a good place to configure and extend Tapestry, or to place your own
 * service definitions.
 */
public class AppModule {
	public static void bind(ServiceBinder binder) {
		binder.bind(ItemDao.class, ItemDaoImpl.class);
		binder.bind(ItemService.class, ItemServiceImpl.class);
	}

	public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
		configuration.override(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT");
	}

	public static void contributeApplicationDefaults(MappedConfiguration<String, Object> configuration) {
		configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en");
	}

	public ComponentEventLinkEncoder decorateComponentEventLinkEncoder(ComponentEventLinkEncoder delegate, Environment environment) {
		Set<String> specialPrefixes = new HashSet<String>(Arrays.asList("foo", "bar", "facebook"));
		return new ModeComponentEventLinkEncoder(delegate, environment, specialPrefixes);
	}

	public Directory buildDirectory() {
		return new RAMDirectory();
	}
	
	@Startup
	public void addTestItems(ItemService itemService) {
		new TestItemInserter(itemService).addTubeStations();
	}
}
