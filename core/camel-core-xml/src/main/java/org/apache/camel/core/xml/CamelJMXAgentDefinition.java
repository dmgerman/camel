begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|IdentifiedType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|Metadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|CollectionStringBuffer
import|;
end_import

begin_comment
comment|/**  * JMX configuration.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"spring,configuration"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"jmxAgent"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|CamelJMXAgentDefinition
specifier|public
class|class
name|CamelJMXAgentDefinition
extends|extends
name|IdentifiedType
block|{
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|disabled
specifier|private
name|String
name|disabled
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|onlyRegisterProcessorWithCustomId
specifier|private
name|String
name|onlyRegisterProcessorWithCustomId
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"1099"
argument_list|)
DECL|field|registryPort
specifier|private
name|String
name|registryPort
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"-1"
argument_list|)
DECL|field|connectorPort
specifier|private
name|String
name|connectorPort
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"org.apache.camel"
argument_list|)
DECL|field|mbeanServerDefaultDomain
specifier|private
name|String
name|mbeanServerDefaultDomain
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"org.apache.camel"
argument_list|)
DECL|field|mbeanObjectDomainName
specifier|private
name|String
name|mbeanObjectDomainName
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"/jmxrmi"
argument_list|)
DECL|field|serviceUrlPath
specifier|private
name|String
name|serviceUrlPath
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|createConnector
specifier|private
name|String
name|createConnector
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|usePlatformMBeanServer
specifier|private
name|String
name|usePlatformMBeanServer
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|registerAlways
specifier|private
name|String
name|registerAlways
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|registerNewRoutes
specifier|private
name|String
name|registerNewRoutes
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"Default"
argument_list|)
DECL|field|statisticsLevel
specifier|private
name|String
name|statisticsLevel
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|loadStatisticsEnabled
specifier|private
name|String
name|loadStatisticsEnabled
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|endpointRuntimeStatisticsEnabled
specifier|private
name|String
name|endpointRuntimeStatisticsEnabled
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|includeHostName
specifier|private
name|String
name|includeHostName
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|useHostIPAddress
specifier|private
name|String
name|useHostIPAddress
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|mask
specifier|private
name|String
name|mask
decl_stmt|;
DECL|method|getDisabled ()
specifier|public
name|String
name|getDisabled
parameter_list|()
block|{
return|return
name|disabled
return|;
block|}
comment|/**      * Disable JMI (default false)      */
DECL|method|setDisabled (String disabled)
specifier|public
name|void
name|setDisabled
parameter_list|(
name|String
name|disabled
parameter_list|)
block|{
name|this
operator|.
name|disabled
operator|=
name|disabled
expr_stmt|;
block|}
DECL|method|getOnlyRegisterProcessorWithCustomId ()
specifier|public
name|String
name|getOnlyRegisterProcessorWithCustomId
parameter_list|()
block|{
return|return
name|onlyRegisterProcessorWithCustomId
return|;
block|}
comment|/**      * Only register processor if a custom id was defined for it.      */
DECL|method|setOnlyRegisterProcessorWithCustomId (String onlyRegisterProcessorWithCustomId)
specifier|public
name|void
name|setOnlyRegisterProcessorWithCustomId
parameter_list|(
name|String
name|onlyRegisterProcessorWithCustomId
parameter_list|)
block|{
name|this
operator|.
name|onlyRegisterProcessorWithCustomId
operator|=
name|onlyRegisterProcessorWithCustomId
expr_stmt|;
block|}
DECL|method|getRegistryPort ()
specifier|public
name|String
name|getRegistryPort
parameter_list|()
block|{
return|return
name|registryPort
return|;
block|}
comment|/**      * RMI connector registry port (default 1099)      */
DECL|method|setRegistryPort (String registryPort)
specifier|public
name|void
name|setRegistryPort
parameter_list|(
name|String
name|registryPort
parameter_list|)
block|{
name|this
operator|.
name|registryPort
operator|=
name|registryPort
expr_stmt|;
block|}
DECL|method|getConnectorPort ()
specifier|public
name|String
name|getConnectorPort
parameter_list|()
block|{
return|return
name|connectorPort
return|;
block|}
comment|/**      * RMI connector server port (default -1 not used)      */
DECL|method|setConnectorPort (String connectorPort)
specifier|public
name|void
name|setConnectorPort
parameter_list|(
name|String
name|connectorPort
parameter_list|)
block|{
name|this
operator|.
name|connectorPort
operator|=
name|connectorPort
expr_stmt|;
block|}
DECL|method|getMbeanServerDefaultDomain ()
specifier|public
name|String
name|getMbeanServerDefaultDomain
parameter_list|()
block|{
return|return
name|mbeanServerDefaultDomain
return|;
block|}
comment|/**      * MBean server default domain name (default org.apache.camel)      */
DECL|method|setMbeanServerDefaultDomain (String mbeanServerDefaultDomain)
specifier|public
name|void
name|setMbeanServerDefaultDomain
parameter_list|(
name|String
name|mbeanServerDefaultDomain
parameter_list|)
block|{
name|this
operator|.
name|mbeanServerDefaultDomain
operator|=
name|mbeanServerDefaultDomain
expr_stmt|;
block|}
DECL|method|getMbeanObjectDomainName ()
specifier|public
name|String
name|getMbeanObjectDomainName
parameter_list|()
block|{
return|return
name|mbeanObjectDomainName
return|;
block|}
comment|/**      * MBean object domain name (default org.apache.camel)      */
DECL|method|setMbeanObjectDomainName (String mbeanObjectDomainName)
specifier|public
name|void
name|setMbeanObjectDomainName
parameter_list|(
name|String
name|mbeanObjectDomainName
parameter_list|)
block|{
name|this
operator|.
name|mbeanObjectDomainName
operator|=
name|mbeanObjectDomainName
expr_stmt|;
block|}
DECL|method|getServiceUrlPath ()
specifier|public
name|String
name|getServiceUrlPath
parameter_list|()
block|{
return|return
name|serviceUrlPath
return|;
block|}
comment|/**      * JMX Service URL path (default /jmxrmi)      */
DECL|method|setServiceUrlPath (String serviceUrlPath)
specifier|public
name|void
name|setServiceUrlPath
parameter_list|(
name|String
name|serviceUrlPath
parameter_list|)
block|{
name|this
operator|.
name|serviceUrlPath
operator|=
name|serviceUrlPath
expr_stmt|;
block|}
DECL|method|getCreateConnector ()
specifier|public
name|String
name|getCreateConnector
parameter_list|()
block|{
return|return
name|createConnector
return|;
block|}
comment|/**      * A flag that indicates whether the agent should be created      */
DECL|method|setCreateConnector (String createConnector)
specifier|public
name|void
name|setCreateConnector
parameter_list|(
name|String
name|createConnector
parameter_list|)
block|{
name|this
operator|.
name|createConnector
operator|=
name|createConnector
expr_stmt|;
block|}
DECL|method|getUsePlatformMBeanServer ()
specifier|public
name|String
name|getUsePlatformMBeanServer
parameter_list|()
block|{
return|return
name|usePlatformMBeanServer
return|;
block|}
comment|/**      * A flag that indicates whether the platform mbean server should be used      */
DECL|method|setUsePlatformMBeanServer (String usePlatformMBeanServer)
specifier|public
name|void
name|setUsePlatformMBeanServer
parameter_list|(
name|String
name|usePlatformMBeanServer
parameter_list|)
block|{
name|this
operator|.
name|usePlatformMBeanServer
operator|=
name|usePlatformMBeanServer
expr_stmt|;
block|}
DECL|method|getStatisticsLevel ()
specifier|public
name|String
name|getStatisticsLevel
parameter_list|()
block|{
return|return
name|statisticsLevel
return|;
block|}
comment|/**      * Level of granularity for performance statistics enabled      */
DECL|method|setStatisticsLevel (String statisticsLevel)
specifier|public
name|void
name|setStatisticsLevel
parameter_list|(
name|String
name|statisticsLevel
parameter_list|)
block|{
name|this
operator|.
name|statisticsLevel
operator|=
name|statisticsLevel
expr_stmt|;
block|}
DECL|method|getRegisterAlways ()
specifier|public
name|String
name|getRegisterAlways
parameter_list|()
block|{
return|return
name|registerAlways
return|;
block|}
comment|/**      * A flag that indicates whether to register mbeans always      */
DECL|method|setRegisterAlways (String registerAlways)
specifier|public
name|void
name|setRegisterAlways
parameter_list|(
name|String
name|registerAlways
parameter_list|)
block|{
name|this
operator|.
name|registerAlways
operator|=
name|registerAlways
expr_stmt|;
block|}
DECL|method|getRegisterNewRoutes ()
specifier|public
name|String
name|getRegisterNewRoutes
parameter_list|()
block|{
return|return
name|registerNewRoutes
return|;
block|}
comment|/**      * A flag that indicates whether to register mbeans when starting new routes      */
DECL|method|setRegisterNewRoutes (String registerNewRoutes)
specifier|public
name|void
name|setRegisterNewRoutes
parameter_list|(
name|String
name|registerNewRoutes
parameter_list|)
block|{
name|this
operator|.
name|registerNewRoutes
operator|=
name|registerNewRoutes
expr_stmt|;
block|}
DECL|method|getLoadStatisticsEnabled ()
specifier|public
name|String
name|getLoadStatisticsEnabled
parameter_list|()
block|{
return|return
name|loadStatisticsEnabled
return|;
block|}
comment|/**      * A flag that indicates whether Load statistics is enabled      */
DECL|method|setLoadStatisticsEnabled (String loadStatisticsEnabled)
specifier|public
name|void
name|setLoadStatisticsEnabled
parameter_list|(
name|String
name|loadStatisticsEnabled
parameter_list|)
block|{
name|this
operator|.
name|loadStatisticsEnabled
operator|=
name|loadStatisticsEnabled
expr_stmt|;
block|}
DECL|method|getEndpointRuntimeStatisticsEnabled ()
specifier|public
name|String
name|getEndpointRuntimeStatisticsEnabled
parameter_list|()
block|{
return|return
name|endpointRuntimeStatisticsEnabled
return|;
block|}
comment|/**      * A flag that indicates whether endpoint runtime statistics is enabled      */
DECL|method|setEndpointRuntimeStatisticsEnabled (String endpointRuntimeStatisticsEnabled)
specifier|public
name|void
name|setEndpointRuntimeStatisticsEnabled
parameter_list|(
name|String
name|endpointRuntimeStatisticsEnabled
parameter_list|)
block|{
name|this
operator|.
name|endpointRuntimeStatisticsEnabled
operator|=
name|endpointRuntimeStatisticsEnabled
expr_stmt|;
block|}
DECL|method|getIncludeHostName ()
specifier|public
name|String
name|getIncludeHostName
parameter_list|()
block|{
return|return
name|includeHostName
return|;
block|}
comment|/**      * A flag that indicates whether to include hostname in JMX MBean names.      */
DECL|method|setIncludeHostName (String includeHostName)
specifier|public
name|void
name|setIncludeHostName
parameter_list|(
name|String
name|includeHostName
parameter_list|)
block|{
name|this
operator|.
name|includeHostName
operator|=
name|includeHostName
expr_stmt|;
block|}
DECL|method|getMask ()
specifier|public
name|String
name|getMask
parameter_list|()
block|{
return|return
name|mask
return|;
block|}
comment|/**      * A flag that indicates whether to remove detected sensitive information (such as passwords) from MBean names and attributes.      */
DECL|method|setMask (String mask)
specifier|public
name|void
name|setMask
parameter_list|(
name|String
name|mask
parameter_list|)
block|{
name|this
operator|.
name|mask
operator|=
name|mask
expr_stmt|;
block|}
DECL|method|getUseHostIPAddress ()
specifier|public
name|String
name|getUseHostIPAddress
parameter_list|()
block|{
return|return
name|useHostIPAddress
return|;
block|}
comment|/**      * A flag that indicates whether to use hostname or IP Address in the service url.      */
DECL|method|setUseHostIPAddress (String useHostIPAddress)
specifier|public
name|void
name|setUseHostIPAddress
parameter_list|(
name|String
name|useHostIPAddress
parameter_list|)
block|{
name|this
operator|.
name|useHostIPAddress
operator|=
name|useHostIPAddress
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"CamelJMXAgent["
argument_list|)
expr_stmt|;
name|CollectionStringBuffer
name|csb
init|=
operator|new
name|CollectionStringBuffer
argument_list|()
decl_stmt|;
if|if
condition|(
name|disabled
operator|!=
literal|null
condition|)
block|{
name|csb
operator|.
name|append
argument_list|(
literal|"disabled="
operator|+
name|disabled
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|usePlatformMBeanServer
operator|!=
literal|null
condition|)
block|{
name|csb
operator|.
name|append
argument_list|(
literal|"usePlatformMBeanServer="
operator|+
name|usePlatformMBeanServer
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|createConnector
operator|!=
literal|null
condition|)
block|{
name|csb
operator|.
name|append
argument_list|(
literal|"createConnector="
operator|+
name|createConnector
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|connectorPort
operator|!=
literal|null
condition|)
block|{
name|csb
operator|.
name|append
argument_list|(
literal|"connectorPort="
operator|+
name|connectorPort
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|registryPort
operator|!=
literal|null
condition|)
block|{
name|csb
operator|.
name|append
argument_list|(
literal|"registryPort="
operator|+
name|registryPort
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|serviceUrlPath
operator|!=
literal|null
condition|)
block|{
name|csb
operator|.
name|append
argument_list|(
literal|"serviceUrlPath="
operator|+
name|serviceUrlPath
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mbeanServerDefaultDomain
operator|!=
literal|null
condition|)
block|{
name|csb
operator|.
name|append
argument_list|(
literal|"mbeanServerDefaultDomain="
operator|+
name|mbeanServerDefaultDomain
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mbeanObjectDomainName
operator|!=
literal|null
condition|)
block|{
name|csb
operator|.
name|append
argument_list|(
literal|"mbeanObjectDomainName="
operator|+
name|mbeanObjectDomainName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|statisticsLevel
operator|!=
literal|null
condition|)
block|{
name|csb
operator|.
name|append
argument_list|(
literal|"statisticsLevel="
operator|+
name|statisticsLevel
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|loadStatisticsEnabled
operator|!=
literal|null
condition|)
block|{
name|csb
operator|.
name|append
argument_list|(
literal|"loadStatisticsEnabled="
operator|+
name|loadStatisticsEnabled
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpointRuntimeStatisticsEnabled
operator|!=
literal|null
condition|)
block|{
name|csb
operator|.
name|append
argument_list|(
literal|"endpointRuntimeStatisticsEnabled="
operator|+
name|endpointRuntimeStatisticsEnabled
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|onlyRegisterProcessorWithCustomId
operator|!=
literal|null
condition|)
block|{
name|csb
operator|.
name|append
argument_list|(
literal|"onlyRegisterProcessorWithCustomId="
operator|+
name|onlyRegisterProcessorWithCustomId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|registerAlways
operator|!=
literal|null
condition|)
block|{
name|csb
operator|.
name|append
argument_list|(
literal|"registerAlways="
operator|+
name|registerAlways
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|registerNewRoutes
operator|!=
literal|null
condition|)
block|{
name|csb
operator|.
name|append
argument_list|(
literal|"registerNewRoutes="
operator|+
name|registerNewRoutes
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|includeHostName
operator|!=
literal|null
condition|)
block|{
name|csb
operator|.
name|append
argument_list|(
literal|"includeHostName="
operator|+
name|includeHostName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|useHostIPAddress
operator|!=
literal|null
condition|)
block|{
name|csb
operator|.
name|append
argument_list|(
literal|"useHostIPAddress="
operator|+
name|useHostIPAddress
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mask
operator|!=
literal|null
condition|)
block|{
name|csb
operator|.
name|append
argument_list|(
literal|"mask="
operator|+
name|mask
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|csb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit
