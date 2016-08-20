begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gora.utils
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gora
operator|.
name|utils
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
name|InvocationTargetException
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
name|gora
operator|.
name|GoraAttribute
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
name|gora
operator|.
name|GoraConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|beanutils
operator|.
name|PropertyUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|gora
operator|.
name|persistency
operator|.
name|Persistent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|gora
operator|.
name|query
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|gora
operator|.
name|store
operator|.
name|DataStore
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
import|;
end_import

begin_comment
comment|/**  * GoraUtil class contain utility methods for the  * camel component.  */
end_comment

begin_class
DECL|class|GoraUtils
specifier|public
specifier|final
class|class
name|GoraUtils
block|{
comment|/**      * Private Constructor to prevent      * instantiation of the class.      */
DECL|method|GoraUtils ()
specifier|private
name|GoraUtils
parameter_list|()
block|{
comment|// utility Class
block|}
comment|/**      * Utility method to construct a new query from the exchange      *      *<b>NOTE:</b> values used in order construct the query      * should be stored in the "in" message headers.      */
DECL|method|constractQueryFromConfiguration (final DataStore<Object, Persistent> dataStore, final GoraConfiguration conf)
specifier|public
specifier|static
name|Query
argument_list|<
name|Object
argument_list|,
name|Persistent
argument_list|>
name|constractQueryFromConfiguration
parameter_list|(
specifier|final
name|DataStore
argument_list|<
name|Object
argument_list|,
name|Persistent
argument_list|>
name|dataStore
parameter_list|,
specifier|final
name|GoraConfiguration
name|conf
parameter_list|)
throws|throws
name|ClassNotFoundException
throws|,
name|IllegalAccessException
throws|,
name|NoSuchMethodException
throws|,
name|InvocationTargetException
block|{
specifier|final
name|Query
argument_list|<
name|Object
argument_list|,
name|Persistent
argument_list|>
name|query
init|=
name|dataStore
operator|.
name|newQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|configurationExist
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_START_TIME
argument_list|,
name|conf
argument_list|)
condition|)
block|{
name|query
operator|.
name|setStartTime
argument_list|(
name|getAttributeAsLong
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_START_TIME
argument_list|,
name|conf
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configurationExist
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_END_TIME
argument_list|,
name|conf
argument_list|)
condition|)
block|{
name|query
operator|.
name|setEndTime
argument_list|(
name|getAttributeAsLong
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_END_TIME
argument_list|,
name|conf
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configurationExist
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_LIMIT
argument_list|,
name|conf
argument_list|)
condition|)
block|{
name|query
operator|.
name|setLimit
argument_list|(
name|getAttributeAsLong
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_LIMIT
argument_list|,
name|conf
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configurationExist
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_TIME_RANGE_FROM
argument_list|,
name|conf
argument_list|)
operator|&&
name|configurationExist
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_TIME_RANGE_TO
argument_list|,
name|conf
argument_list|)
condition|)
block|{
name|query
operator|.
name|setTimeRange
argument_list|(
name|getAttributeAsLong
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_TIME_RANGE_FROM
argument_list|,
name|conf
argument_list|)
argument_list|,
name|getAttributeAsLong
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_TIME_RANGE_TO
argument_list|,
name|conf
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configurationExist
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_TIMESTAMP
argument_list|,
name|conf
argument_list|)
condition|)
block|{
name|query
operator|.
name|setTimestamp
argument_list|(
name|getAttributeAsLong
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_TIMESTAMP
argument_list|,
name|conf
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configurationExist
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_START_KEY
argument_list|,
name|conf
argument_list|)
condition|)
block|{
name|query
operator|.
name|setStartKey
argument_list|(
name|getAttribute
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_START_KEY
argument_list|,
name|conf
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configurationExist
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_END_KEY
argument_list|,
name|conf
argument_list|)
condition|)
block|{
name|query
operator|.
name|setEndKey
argument_list|(
name|getAttribute
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_END_KEY
argument_list|,
name|conf
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configurationExist
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_KEY_RANGE_FROM
argument_list|,
name|conf
argument_list|)
operator|&&
name|configurationExist
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_KEY_RANGE_TO
argument_list|,
name|conf
argument_list|)
condition|)
block|{
name|query
operator|.
name|setKeyRange
argument_list|(
name|getAttribute
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_KEY_RANGE_FROM
argument_list|,
name|conf
argument_list|)
argument_list|,
name|getAttribute
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_KEY_RANGE_TO
argument_list|,
name|conf
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|query
return|;
block|}
comment|/**      * Utility method to construct a new query from the exchange      *      *<b>NOTE:</b> values used in order construct the query      * should be stored in the "in" message headers.      */
DECL|method|constractQueryFromPropertiesMap (final Map<String, ?> propertiesMap, final DataStore<Object, Persistent> dataStore, final GoraConfiguration conf)
specifier|public
specifier|static
name|Query
argument_list|<
name|Object
argument_list|,
name|Persistent
argument_list|>
name|constractQueryFromPropertiesMap
parameter_list|(
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|propertiesMap
parameter_list|,
specifier|final
name|DataStore
argument_list|<
name|Object
argument_list|,
name|Persistent
argument_list|>
name|dataStore
parameter_list|,
specifier|final
name|GoraConfiguration
name|conf
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
specifier|final
name|Query
argument_list|<
name|Object
argument_list|,
name|Persistent
argument_list|>
name|query
init|=
name|dataStore
operator|.
name|newQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|propertyExist
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_START_TIME
argument_list|,
name|propertiesMap
argument_list|)
condition|)
block|{
name|query
operator|.
name|setStartTime
argument_list|(
name|getPropertyAsLong
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_START_TIME
argument_list|,
name|propertiesMap
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|propertyExist
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_END_TIME
argument_list|,
name|propertiesMap
argument_list|)
condition|)
block|{
name|query
operator|.
name|setEndTime
argument_list|(
name|getPropertyAsLong
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_END_TIME
argument_list|,
name|propertiesMap
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|propertyExist
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_LIMIT
argument_list|,
name|propertiesMap
argument_list|)
condition|)
block|{
name|query
operator|.
name|setLimit
argument_list|(
name|getPropertyAsLong
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_LIMIT
argument_list|,
name|propertiesMap
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|propertyExist
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_TIME_RANGE_FROM
argument_list|,
name|propertiesMap
argument_list|)
operator|&&
name|propertyExist
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_TIME_RANGE_TO
argument_list|,
name|propertiesMap
argument_list|)
condition|)
block|{
name|query
operator|.
name|setTimeRange
argument_list|(
name|getPropertyAsLong
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_TIME_RANGE_FROM
argument_list|,
name|propertiesMap
argument_list|)
argument_list|,
name|getPropertyAsLong
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_TIME_RANGE_TO
argument_list|,
name|propertiesMap
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|propertyExist
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_TIMESTAMP
argument_list|,
name|propertiesMap
argument_list|)
condition|)
block|{
name|query
operator|.
name|setTimestamp
argument_list|(
name|getPropertyAsLong
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_TIMESTAMP
argument_list|,
name|propertiesMap
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|propertyExist
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_START_KEY
argument_list|,
name|propertiesMap
argument_list|)
condition|)
block|{
name|query
operator|.
name|setStartKey
argument_list|(
name|getProperty
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_START_KEY
argument_list|,
name|propertiesMap
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|propertyExist
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_END_KEY
argument_list|,
name|propertiesMap
argument_list|)
condition|)
block|{
name|query
operator|.
name|setStartKey
argument_list|(
name|getProperty
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_END_KEY
argument_list|,
name|propertiesMap
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|propertyExist
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_KEY_RANGE_FROM
argument_list|,
name|propertiesMap
argument_list|)
operator|&&
name|propertyExist
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_KEY_RANGE_TO
argument_list|,
name|propertiesMap
argument_list|)
condition|)
block|{
name|query
operator|.
name|setKeyRange
argument_list|(
name|getProperty
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_KEY_RANGE_FROM
argument_list|,
name|propertiesMap
argument_list|)
argument_list|,
name|getProperty
argument_list|(
name|GoraAttribute
operator|.
name|GORA_QUERY_KEY_RANGE_TO
argument_list|,
name|propertiesMap
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|query
return|;
block|}
comment|/**      * Utility method to check if a value exist in the configuration class      *      *<b>NOTE:</>      * Checks only if is not null      */
DECL|method|configurationExist (final GoraAttribute attr, final GoraConfiguration conf)
specifier|protected
specifier|static
name|boolean
name|configurationExist
parameter_list|(
specifier|final
name|GoraAttribute
name|attr
parameter_list|,
specifier|final
name|GoraConfiguration
name|conf
parameter_list|)
throws|throws
name|IllegalAccessException
throws|,
name|NoSuchMethodException
throws|,
name|InvocationTargetException
block|{
return|return
name|PropertyUtils
operator|.
name|getSimpleProperty
argument_list|(
name|conf
argument_list|,
name|attr
operator|.
name|value
argument_list|)
operator|!=
literal|null
return|;
block|}
comment|/**      * Utility method to check if a value exist in the properties map      */
DECL|method|propertyExist (final GoraAttribute attr, final Map<String, ?> propertiesMap)
specifier|protected
specifier|static
name|boolean
name|propertyExist
parameter_list|(
specifier|final
name|GoraAttribute
name|attr
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|propertiesMap
parameter_list|)
block|{
return|return
name|propertiesMap
operator|.
name|containsKey
argument_list|(
name|attr
operator|.
name|value
argument_list|)
return|;
block|}
comment|/**      * Utility method to extract value from configuration      */
DECL|method|getAttribute (final GoraAttribute attr, final GoraConfiguration conf)
specifier|protected
specifier|static
name|Object
name|getAttribute
parameter_list|(
specifier|final
name|GoraAttribute
name|attr
parameter_list|,
specifier|final
name|GoraConfiguration
name|conf
parameter_list|)
throws|throws
name|IllegalAccessException
throws|,
name|NoSuchMethodException
throws|,
name|InvocationTargetException
block|{
return|return
name|PropertyUtils
operator|.
name|getSimpleProperty
argument_list|(
name|conf
argument_list|,
name|attr
operator|.
name|value
argument_list|)
return|;
block|}
comment|/**      * Utility method to extract value from configuration as String      */
DECL|method|getAttributeAsString (final GoraAttribute attr, final GoraConfiguration conf)
specifier|protected
specifier|static
name|String
name|getAttributeAsString
parameter_list|(
specifier|final
name|GoraAttribute
name|attr
parameter_list|,
specifier|final
name|GoraConfiguration
name|conf
parameter_list|)
throws|throws
name|IllegalAccessException
throws|,
name|NoSuchMethodException
throws|,
name|InvocationTargetException
block|{
return|return
name|String
operator|.
name|valueOf
argument_list|(
name|getAttribute
argument_list|(
name|attr
argument_list|,
name|conf
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Utility method to extract value from configuration as Long      */
DECL|method|getAttributeAsLong (final GoraAttribute attr, final GoraConfiguration conf)
specifier|protected
specifier|static
name|Long
name|getAttributeAsLong
parameter_list|(
specifier|final
name|GoraAttribute
name|attr
parameter_list|,
specifier|final
name|GoraConfiguration
name|conf
parameter_list|)
throws|throws
name|IllegalAccessException
throws|,
name|NoSuchMethodException
throws|,
name|InvocationTargetException
block|{
return|return
name|Long
operator|.
name|parseLong
argument_list|(
name|getAttributeAsString
argument_list|(
name|attr
argument_list|,
name|conf
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Utility method to extract value of a map      */
DECL|method|getProperty (final GoraAttribute attr, final Map<String, ?> propertiesMap)
specifier|protected
specifier|static
name|Object
name|getProperty
parameter_list|(
specifier|final
name|GoraAttribute
name|attr
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|propertiesMap
parameter_list|)
block|{
return|return
name|propertiesMap
operator|.
name|get
argument_list|(
name|attr
operator|.
name|value
argument_list|)
return|;
block|}
comment|/**      * Utility method to extract value of a map as String      */
DECL|method|getPropertyAsString (final GoraAttribute attr, final Map<String, ?> propertiesMap)
specifier|protected
specifier|static
name|String
name|getPropertyAsString
parameter_list|(
specifier|final
name|GoraAttribute
name|attr
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|propertiesMap
parameter_list|)
block|{
return|return
name|String
operator|.
name|valueOf
argument_list|(
name|getProperty
argument_list|(
name|attr
argument_list|,
name|propertiesMap
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Utility method to extract value of a map as long      */
DECL|method|getPropertyAsLong (final GoraAttribute attr, final Map<String, ?> propertiesMap)
specifier|protected
specifier|static
name|Long
name|getPropertyAsLong
parameter_list|(
specifier|final
name|GoraAttribute
name|attr
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|propertiesMap
parameter_list|)
block|{
return|return
name|Long
operator|.
name|parseLong
argument_list|(
name|getPropertyAsString
argument_list|(
name|attr
argument_list|,
name|propertiesMap
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Utility method to extract GORA key from the exchange      *      *<b>NOTE:</b> key value expected to be stored      * in the "in" message headers.      *      * @param exchange The Camel Exchange      * @return The key      */
DECL|method|getKeyFromExchange (Exchange exchange)
specifier|public
specifier|static
name|Object
name|getKeyFromExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|Object
name|key
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_KEY
operator|.
name|value
argument_list|)
decl_stmt|;
name|checkNotNull
argument_list|(
name|key
argument_list|,
literal|"Key should not be null!"
argument_list|)
expr_stmt|;
return|return
name|key
return|;
block|}
comment|/**      * Utility method to extract the value from the exchange      *      *<b>NOTE:</b> the value expected to be instance      * of persistent type.      *      * @param exchange The Camel Exchange      * @return The value      */
DECL|method|getValueFromExchange (Exchange exchange)
specifier|public
specifier|static
name|Persistent
name|getValueFromExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Persistent
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

