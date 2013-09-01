begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.facebook.data
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|facebook
operator|.
name|data
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
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
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|facebook4j
operator|.
name|Reading
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
name|Exchange
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
name|component
operator|.
name|facebook
operator|.
name|FacebookConstants
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
name|component
operator|.
name|facebook
operator|.
name|config
operator|.
name|FacebookConfiguration
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
name|component
operator|.
name|facebook
operator|.
name|config
operator|.
name|FacebookEndpointConfiguration
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Helper class to work with Facebook component properties.  */
end_comment

begin_class
DECL|class|FacebookPropertiesHelper
specifier|public
specifier|final
class|class
name|FacebookPropertiesHelper
block|{
comment|// set of field names which are specific to Facebook4J api, to be excluded from method argument considerations
DECL|field|COMPONENT_CONFIG_FIELDS
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|COMPONENT_CONFIG_FIELDS
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|FacebookPropertiesHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|ENDPOINT_CONFIG_FIELDS
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|ENDPOINT_CONFIG_FIELDS
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
for|for
control|(
name|Field
name|field
range|:
name|FacebookConfiguration
operator|.
name|class
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
name|COMPONENT_CONFIG_FIELDS
operator|.
name|add
argument_list|(
name|field
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Field
name|field
range|:
name|FacebookEndpointConfiguration
operator|.
name|class
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
name|ENDPOINT_CONFIG_FIELDS
operator|.
name|add
argument_list|(
name|field
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|FacebookPropertiesHelper ()
specifier|private
name|FacebookPropertiesHelper
parameter_list|()
block|{
comment|// utility
block|}
comment|/**      * Apply properties for {@link Reading} type to the supplied {@link FacebookEndpointConfiguration}.      * @param configuration endpoint configuration to update      * @param options properties to apply to the reading field in configuration      */
DECL|method|configureReadingProperties (FacebookEndpointConfiguration configuration, Map<String, Object> options)
specifier|public
specifier|static
name|void
name|configureReadingProperties
parameter_list|(
name|FacebookEndpointConfiguration
name|configuration
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|readingProperties
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|options
argument_list|,
name|FacebookConstants
operator|.
name|READING_PREFIX
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|readingProperties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
try|try
block|{
comment|// add to an existing reading reference?
comment|// NOTE Reading class does not support overwriting properties!!!
name|Reading
name|reading
init|=
name|configuration
operator|.
name|getReading
argument_list|()
decl_stmt|;
if|if
condition|(
name|reading
operator|==
literal|null
condition|)
block|{
name|reading
operator|=
operator|new
name|Reading
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|reading
operator|=
name|ReadingBuilder
operator|.
name|copy
argument_list|(
name|reading
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|// set properties
name|ReadingBuilder
operator|.
name|setProperties
argument_list|(
name|reading
argument_list|,
name|readingProperties
argument_list|)
expr_stmt|;
comment|// update reading in configuration
name|configuration
operator|.
name|setReading
argument_list|(
name|reading
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
name|readingProperties
operator|.
name|toString
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// add any unknown properties back to options to throw an error later
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|readingProperties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|options
operator|.
name|put
argument_list|(
name|FacebookConstants
operator|.
name|READING_PREFIX
operator|+
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Gets exchange header properties that start with {@link FacebookConstants}.FACEBOOK_PROPERTY_PREFIX.      *      * @param exchange Camel exchange      * @param properties map to collect properties with required prefix      */
DECL|method|getExchangeProperties (Exchange exchange, Map<String, Object> properties)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getExchangeProperties
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|int
name|nProperties
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|startsWith
argument_list|(
name|FacebookConstants
operator|.
name|FACEBOOK_PROPERTY_PREFIX
argument_list|)
condition|)
block|{
name|properties
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|substring
argument_list|(
name|FacebookConstants
operator|.
name|FACEBOOK_PROPERTY_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|nProperties
operator|++
expr_stmt|;
block|}
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found {} properties in exchange"
argument_list|,
name|nProperties
argument_list|)
expr_stmt|;
return|return
name|properties
return|;
block|}
DECL|method|getEndpointProperties (FacebookEndpointConfiguration configuration, Map<String, Object> properties)
specifier|public
specifier|static
name|void
name|getEndpointProperties
parameter_list|(
name|FacebookEndpointConfiguration
name|configuration
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
if|if
condition|(
name|IntrospectionSupport
operator|.
name|getProperties
argument_list|(
name|configuration
argument_list|,
name|properties
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
condition|)
block|{
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|names
init|=
name|properties
operator|.
name|keySet
argument_list|()
decl_stmt|;
comment|// remove component config properties so we only have endpoint properties
name|names
operator|.
name|removeAll
argument_list|(
name|COMPONENT_CONFIG_FIELDS
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|names
init|=
name|properties
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found endpoint properties {}"
argument_list|,
name|names
operator|.
name|retainAll
argument_list|(
name|ENDPOINT_CONFIG_FIELDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getEndpointPropertyNames (FacebookEndpointConfiguration configuration)
specifier|public
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|getEndpointPropertyNames
parameter_list|(
name|FacebookEndpointConfiguration
name|configuration
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|getEndpointProperties
argument_list|(
name|configuration
argument_list|,
name|properties
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|properties
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

