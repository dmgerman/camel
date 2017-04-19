begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|cloud
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|XmlElement
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|CamelContext
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
name|NoFactoryAvailableException
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
name|cloud
operator|.
name|ServiceChooser
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
name|cloud
operator|.
name|ServiceChooserFactory
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
name|model
operator|.
name|ProcessorDefinition
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
name|PropertyDefinition
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
name|CamelContextHelper
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
name|IntrospectionSupport
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
name|ObjectHelper
import|;
end_import

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"routing,cloud,service-discovery"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"serviceChooserConfiguration"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ServiceCallServiceChooserConfiguration
specifier|public
class|class
name|ServiceCallServiceChooserConfiguration
extends|extends
name|IdentifiedType
implements|implements
name|ServiceChooserFactory
block|{
DECL|field|RESOURCE_PATH
specifier|private
specifier|static
specifier|final
name|String
name|RESOURCE_PATH
init|=
literal|"META-INF/services/org/apache/camel/cloud/"
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|parent
specifier|private
specifier|final
name|ServiceCallDefinition
name|parent
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|factoryKey
specifier|private
specifier|final
name|String
name|factoryKey
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"properties"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|properties
specifier|private
name|List
argument_list|<
name|PropertyDefinition
argument_list|>
name|properties
decl_stmt|;
DECL|method|ServiceCallServiceChooserConfiguration ()
specifier|public
name|ServiceCallServiceChooserConfiguration
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|ServiceCallServiceChooserConfiguration (ServiceCallDefinition parent, String factoryKey)
specifier|public
name|ServiceCallServiceChooserConfiguration
parameter_list|(
name|ServiceCallDefinition
name|parent
parameter_list|,
name|String
name|factoryKey
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
name|this
operator|.
name|factoryKey
operator|=
name|factoryKey
expr_stmt|;
block|}
DECL|method|end ()
specifier|public
name|ServiceCallDefinition
name|end
parameter_list|()
block|{
return|return
name|this
operator|.
name|parent
return|;
block|}
DECL|method|endParent ()
specifier|public
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|endParent
parameter_list|()
block|{
return|return
name|this
operator|.
name|parent
operator|.
name|end
argument_list|()
return|;
block|}
comment|// *************************************************************************
comment|//
comment|// *************************************************************************
DECL|method|getProperties ()
specifier|public
name|List
argument_list|<
name|PropertyDefinition
argument_list|>
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
comment|/**      * Set client properties to use.      *<p/>      * These properties are specific to what service call implementation are in      * use. For example if using ribbon, then the client properties are define      * in com.netflix.client.config.CommonClientConfigKey.      */
DECL|method|setProperties (List<PropertyDefinition> properties)
specifier|public
name|void
name|setProperties
parameter_list|(
name|List
argument_list|<
name|PropertyDefinition
argument_list|>
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
comment|/**      * Adds a custom property to use.      *<p/>      * These properties are specific to what service call implementation are in      * use. For example if using ribbon, then the client properties are define      * in com.netflix.client.config.CommonClientConfigKey.      */
DECL|method|property (String key, String value)
specifier|public
name|ServiceCallServiceChooserConfiguration
name|property
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|properties
operator|==
literal|null
condition|)
block|{
name|properties
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|PropertyDefinition
name|prop
init|=
operator|new
name|PropertyDefinition
argument_list|()
decl_stmt|;
name|prop
operator|.
name|setKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|prop
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|properties
operator|.
name|add
argument_list|(
name|prop
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getPropertiesAsMap (CamelContext camelContext)
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getPropertiesAsMap
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|answer
decl_stmt|;
if|if
condition|(
name|properties
operator|==
literal|null
operator|||
name|properties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|answer
operator|=
name|Collections
operator|.
name|emptyMap
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|PropertyDefinition
name|prop
range|:
name|properties
control|)
block|{
comment|// support property placeholders
name|String
name|key
init|=
name|CamelContextHelper
operator|.
name|parseText
argument_list|(
name|camelContext
argument_list|,
name|prop
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|CamelContextHelper
operator|.
name|parseText
argument_list|(
name|camelContext
argument_list|,
name|prop
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
comment|// *************************************************************************
comment|// Factory
comment|// *************************************************************************
annotation|@
name|Override
DECL|method|newInstance (CamelContext camelContext)
specifier|public
name|ServiceChooser
name|newInstance
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|factoryKey
argument_list|,
literal|"ServiceChooser factoryKey"
argument_list|)
expr_stmt|;
name|ServiceChooser
name|answer
decl_stmt|;
comment|// First try to find the factory from the registry.
name|ServiceChooserFactory
name|factory
init|=
name|CamelContextHelper
operator|.
name|lookup
argument_list|(
name|camelContext
argument_list|,
name|factoryKey
argument_list|,
name|ServiceChooserFactory
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|factory
operator|!=
literal|null
condition|)
block|{
comment|// If a factory is found in the registry do not re-configure it as
comment|// it should be pre-configured.
name|answer
operator|=
name|factory
operator|.
name|newInstance
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
try|try
block|{
comment|// Then use Service factory.
name|type
operator|=
name|camelContext
operator|.
name|getFactoryFinder
argument_list|(
name|RESOURCE_PATH
argument_list|)
operator|.
name|findClass
argument_list|(
name|factoryKey
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|NoFactoryAvailableException
argument_list|(
name|RESOURCE_PATH
operator|+
name|factoryKey
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|ServiceChooserFactory
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|factory
operator|=
operator|(
name|ServiceChooserFactory
operator|)
name|camelContext
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|NoFactoryAvailableException
argument_list|(
literal|"Resolving ServiceChooser: "
operator|+
name|factoryKey
operator|+
literal|" detected type conflict: Not a ServiceChooserFactory implementation. Found: "
operator|+
name|type
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
try|try
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|getProperties
argument_list|(
name|this
argument_list|,
name|parameters
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Convert properties to Map<String, String>
name|parameters
operator|.
name|put
argument_list|(
literal|"properties"
argument_list|,
name|getPropertiesAsMap
argument_list|(
name|camelContext
argument_list|)
argument_list|)
expr_stmt|;
name|postProcessFactoryParameters
argument_list|(
name|camelContext
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|factory
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|answer
operator|=
name|factory
operator|.
name|newInstance
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|answer
return|;
block|}
comment|// *************************************************************************
comment|// Utilities
comment|// *************************************************************************
DECL|method|postProcessFactoryParameters (CamelContext camelContext, Map<String, Object> parameters)
specifier|protected
name|void
name|postProcessFactoryParameters
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{     }
block|}
end_class

end_unit

