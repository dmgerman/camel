begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
package|;
end_package

begin_comment
comment|/**  * This module contains jmx related system property key constants.  */
end_comment

begin_class
DECL|class|JmxSystemPropertyKeys
specifier|public
specifier|final
class|class
name|JmxSystemPropertyKeys
block|{
comment|// disable jmx
DECL|field|DISABLED
specifier|public
specifier|static
specifier|final
name|String
name|DISABLED
init|=
literal|"org.apache.camel.jmx.disabled"
decl_stmt|;
comment|// jmx (rmi registry) port
DECL|field|REGISTRY_PORT
specifier|public
specifier|static
specifier|final
name|String
name|REGISTRY_PORT
init|=
literal|"org.apache.camel.jmx.rmiConnector.registryPort"
decl_stmt|;
comment|// jmx (rmi server connection) port
DECL|field|CONNECTOR_PORT
specifier|public
specifier|static
specifier|final
name|String
name|CONNECTOR_PORT
init|=
literal|"org.apache.camel.jmx.rmiConnector.connectorPort"
decl_stmt|;
comment|// jmx domain name
DECL|field|DOMAIN
specifier|public
specifier|static
specifier|final
name|String
name|DOMAIN
init|=
literal|"org.apache.camel.jmx.mbeanServerDefaultDomain"
decl_stmt|;
comment|// the domain name for the camel mbeans
DECL|field|MBEAN_DOMAIN
specifier|public
specifier|static
specifier|final
name|String
name|MBEAN_DOMAIN
init|=
literal|"org.apache.camel.jmx.mbeanObjectDomainName"
decl_stmt|;
comment|// JMX service URL path
DECL|field|SERVICE_URL_PATH
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_URL_PATH
init|=
literal|"org.apache.camel.jmx.serviceUrlPath"
decl_stmt|;
comment|// A flag that indicates whether the agent should be created
DECL|field|CREATE_CONNECTOR
specifier|public
specifier|static
specifier|final
name|String
name|CREATE_CONNECTOR
init|=
literal|"org.apache.camel.jmx.createRmiConnector"
decl_stmt|;
comment|// use jvm platform mbean server flag
DECL|field|USE_PLATFORM_MBS
specifier|public
specifier|static
specifier|final
name|String
name|USE_PLATFORM_MBS
init|=
literal|"org.apache.camel.jmx.usePlatformMBeanServer"
decl_stmt|;
comment|// whether all processors or only processors with a custom id given should be registered
DECL|field|ONLY_REGISTER_PROCESSOR_WITH_CUSTOM_ID
specifier|public
specifier|static
specifier|final
name|String
name|ONLY_REGISTER_PROCESSOR_WITH_CUSTOM_ID
init|=
literal|"org.apache.camel.jmx.onlyRegisterProcessorWithCustomId"
decl_stmt|;
comment|// whether to enable gathering load statistics in the background
DECL|field|LOAD_STATISTICS_ENABLED
specifier|public
specifier|static
specifier|final
name|String
name|LOAD_STATISTICS_ENABLED
init|=
literal|"org.apache.camel.jmx.loadStatisticsEnabled"
decl_stmt|;
comment|// whether to enable gathering endpoint runtime statistics
DECL|field|ENDPOINT_RUNTIME_STATISTICS_ENABLED
specifier|public
specifier|static
specifier|final
name|String
name|ENDPOINT_RUNTIME_STATISTICS_ENABLED
init|=
literal|"org.apache.camel.jmx.endpointRuntimeStatisticsEnabled"
decl_stmt|;
comment|// the level of statistics enabled
DECL|field|STATISTICS_LEVEL
specifier|public
specifier|static
specifier|final
name|String
name|STATISTICS_LEVEL
init|=
literal|"org.apache.camel.jmx.statisticsLevel"
decl_stmt|;
comment|// whether to register always
DECL|field|REGISTER_ALWAYS
specifier|public
specifier|static
specifier|final
name|String
name|REGISTER_ALWAYS
init|=
literal|"org.apache.camel.jmx.registerAlways"
decl_stmt|;
comment|// whether to register when starting new routes
DECL|field|REGISTER_NEW_ROUTES
specifier|public
specifier|static
specifier|final
name|String
name|REGISTER_NEW_ROUTES
init|=
literal|"org.apache.camel.jmx.registerNewRoutes"
decl_stmt|;
comment|// Whether to remove detected sensitive information (such as passwords) from MBean names and attributes.
DECL|field|MASK
specifier|public
specifier|static
specifier|final
name|String
name|MASK
init|=
literal|"org.apache.camel.jmx.mask"
decl_stmt|;
comment|// Whether to include host name in MBean names
DECL|field|INCLUDE_HOST_NAME
specifier|public
specifier|static
specifier|final
name|String
name|INCLUDE_HOST_NAME
init|=
literal|"org.apache.camel.jmx.includeHostName"
decl_stmt|;
comment|// To configure the default management name pattern using a JVM system property
DECL|field|MANAGEMENT_NAME_PATTERN
specifier|public
specifier|static
specifier|final
name|String
name|MANAGEMENT_NAME_PATTERN
init|=
literal|"org.apache.camel.jmx.managementNamePattern"
decl_stmt|;
comment|// flag to enable host ip address instead of host name
DECL|field|USE_HOST_IP_ADDRESS
specifier|public
specifier|static
specifier|final
name|String
name|USE_HOST_IP_ADDRESS
init|=
literal|"org.apache.camel.jmx.useHostIPAddress"
decl_stmt|;
DECL|method|JmxSystemPropertyKeys ()
specifier|private
name|JmxSystemPropertyKeys
parameter_list|()
block|{
comment|// not instantiated
block|}
block|}
end_class

end_unit

