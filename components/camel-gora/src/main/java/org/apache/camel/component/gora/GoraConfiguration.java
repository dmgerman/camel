begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gora
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
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
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
name|spi
operator|.
name|UriParam
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
name|UriParams
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
name|UriPath
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|conf
operator|.
name|Configuration
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
name|Strings
operator|.
name|isNullOrEmpty
import|;
end_import

begin_comment
comment|/**  * Gora Configuration.  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|GoraConfiguration
specifier|public
class|class
name|GoraConfiguration
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|UriParam
DECL|field|keyClass
specifier|private
name|String
name|keyClass
decl_stmt|;
annotation|@
name|UriParam
DECL|field|valueClass
specifier|private
name|String
name|valueClass
decl_stmt|;
annotation|@
name|UriParam
DECL|field|dataStoreClass
specifier|private
name|String
name|dataStoreClass
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|hadoopConfiguration
specifier|private
name|Configuration
name|hadoopConfiguration
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|startTime
specifier|private
name|long
name|startTime
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|endTime
specifier|private
name|long
name|endTime
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|timeRangeFrom
specifier|private
name|long
name|timeRangeFrom
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|timeRangeTo
specifier|private
name|long
name|timeRangeTo
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|limit
specifier|private
name|long
name|limit
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|timestamp
specifier|private
name|long
name|timestamp
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|startKey
specifier|private
name|Object
name|startKey
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|endKey
specifier|private
name|Object
name|endKey
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|keyRangeFrom
specifier|private
name|Object
name|keyRangeFrom
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|keyRangeTo
specifier|private
name|Object
name|keyRangeTo
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|fields
specifier|private
name|Strings
name|fields
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"1"
argument_list|)
DECL|field|concurrentConsumers
specifier|private
name|int
name|concurrentConsumers
init|=
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|flushOnEveryOperation
specifier|private
name|boolean
name|flushOnEveryOperation
init|=
literal|true
decl_stmt|;
DECL|method|GoraConfiguration ()
specifier|public
name|GoraConfiguration
parameter_list|()
block|{
name|this
operator|.
name|hadoopConfiguration
operator|=
operator|new
name|Configuration
argument_list|()
expr_stmt|;
block|}
DECL|method|getKeyClass ()
specifier|public
name|String
name|getKeyClass
parameter_list|()
block|{
return|return
name|keyClass
return|;
block|}
comment|/**      * The type class of the key      */
DECL|method|setKeyClass (final String keyClass)
specifier|public
name|void
name|setKeyClass
parameter_list|(
specifier|final
name|String
name|keyClass
parameter_list|)
block|{
if|if
condition|(
name|isNullOrEmpty
argument_list|(
name|keyClass
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Key class could not be null or empty!"
argument_list|)
throw|;
block|}
name|this
operator|.
name|keyClass
operator|=
name|keyClass
expr_stmt|;
block|}
DECL|method|getValueClass ()
specifier|public
name|String
name|getValueClass
parameter_list|()
block|{
return|return
name|valueClass
return|;
block|}
comment|/**      * The type of the value      */
DECL|method|setValueClass (final String valueClass)
specifier|public
name|void
name|setValueClass
parameter_list|(
specifier|final
name|String
name|valueClass
parameter_list|)
block|{
if|if
condition|(
name|isNullOrEmpty
argument_list|(
name|valueClass
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Value class  could not be null or empty!"
argument_list|)
throw|;
block|}
name|this
operator|.
name|valueClass
operator|=
name|valueClass
expr_stmt|;
block|}
DECL|method|getDataStoreClass ()
specifier|public
name|String
name|getDataStoreClass
parameter_list|()
block|{
return|return
name|dataStoreClass
return|;
block|}
comment|/**      * The type of the dataStore      */
DECL|method|setDataStoreClass (String dataStoreClass)
specifier|public
name|void
name|setDataStoreClass
parameter_list|(
name|String
name|dataStoreClass
parameter_list|)
block|{
if|if
condition|(
name|isNullOrEmpty
argument_list|(
name|dataStoreClass
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"DataStore class could not be null or empty!"
argument_list|)
throw|;
block|}
name|this
operator|.
name|dataStoreClass
operator|=
name|dataStoreClass
expr_stmt|;
block|}
DECL|method|getHadoopConfiguration ()
specifier|public
name|Configuration
name|getHadoopConfiguration
parameter_list|()
block|{
return|return
name|hadoopConfiguration
return|;
block|}
DECL|method|getStartTime ()
specifier|public
name|long
name|getStartTime
parameter_list|()
block|{
return|return
name|startTime
return|;
block|}
comment|/**      * The Start Time      */
DECL|method|setStartTime (long startTime)
specifier|public
name|void
name|setStartTime
parameter_list|(
name|long
name|startTime
parameter_list|)
block|{
name|this
operator|.
name|startTime
operator|=
name|startTime
expr_stmt|;
block|}
DECL|method|getEndTime ()
specifier|public
name|long
name|getEndTime
parameter_list|()
block|{
return|return
name|endTime
return|;
block|}
comment|/**      * The End Time      */
DECL|method|setEndTime (long endTime)
specifier|public
name|void
name|setEndTime
parameter_list|(
name|long
name|endTime
parameter_list|)
block|{
name|this
operator|.
name|endTime
operator|=
name|endTime
expr_stmt|;
block|}
DECL|method|getTimeRangeFrom ()
specifier|public
name|long
name|getTimeRangeFrom
parameter_list|()
block|{
return|return
name|timeRangeFrom
return|;
block|}
comment|/**      * The Time Range From      */
DECL|method|setTimeRangeFrom (long timeRangeFrom)
specifier|public
name|void
name|setTimeRangeFrom
parameter_list|(
name|long
name|timeRangeFrom
parameter_list|)
block|{
name|this
operator|.
name|timeRangeFrom
operator|=
name|timeRangeFrom
expr_stmt|;
block|}
DECL|method|getTimeRangeTo ()
specifier|public
name|long
name|getTimeRangeTo
parameter_list|()
block|{
return|return
name|timeRangeTo
return|;
block|}
comment|/**      * The Time Range To      */
DECL|method|setTimeRangeTo (long timeRangeTo)
specifier|public
name|void
name|setTimeRangeTo
parameter_list|(
name|long
name|timeRangeTo
parameter_list|)
block|{
name|this
operator|.
name|timeRangeTo
operator|=
name|timeRangeTo
expr_stmt|;
block|}
DECL|method|getLimit ()
specifier|public
name|long
name|getLimit
parameter_list|()
block|{
return|return
name|limit
return|;
block|}
comment|/**      * The Limit      */
DECL|method|setLimit (long limit)
specifier|public
name|void
name|setLimit
parameter_list|(
name|long
name|limit
parameter_list|)
block|{
name|this
operator|.
name|limit
operator|=
name|limit
expr_stmt|;
block|}
DECL|method|getTimestamp ()
specifier|public
name|long
name|getTimestamp
parameter_list|()
block|{
return|return
name|timestamp
return|;
block|}
comment|/**      * The Timestamp      */
DECL|method|setTimestamp (long timestamp)
specifier|public
name|void
name|setTimestamp
parameter_list|(
name|long
name|timestamp
parameter_list|)
block|{
name|this
operator|.
name|timestamp
operator|=
name|timestamp
expr_stmt|;
block|}
DECL|method|getStartKey ()
specifier|public
name|Object
name|getStartKey
parameter_list|()
block|{
return|return
name|startKey
return|;
block|}
comment|/**      * The Start Key      */
DECL|method|setStartKey (Object startKey)
specifier|public
name|void
name|setStartKey
parameter_list|(
name|Object
name|startKey
parameter_list|)
block|{
name|this
operator|.
name|startKey
operator|=
name|startKey
expr_stmt|;
block|}
DECL|method|getEndKey ()
specifier|public
name|Object
name|getEndKey
parameter_list|()
block|{
return|return
name|endKey
return|;
block|}
comment|/**      * The End Key      */
DECL|method|setEndKey (Object endKey)
specifier|public
name|void
name|setEndKey
parameter_list|(
name|Object
name|endKey
parameter_list|)
block|{
name|this
operator|.
name|endKey
operator|=
name|endKey
expr_stmt|;
block|}
DECL|method|getKeyRangeFrom ()
specifier|public
name|Object
name|getKeyRangeFrom
parameter_list|()
block|{
return|return
name|keyRangeFrom
return|;
block|}
comment|/**      * The Key Range From      */
DECL|method|setKeyRangeFrom (Object keyRangeFrom)
specifier|public
name|void
name|setKeyRangeFrom
parameter_list|(
name|Object
name|keyRangeFrom
parameter_list|)
block|{
name|this
operator|.
name|keyRangeFrom
operator|=
name|keyRangeFrom
expr_stmt|;
block|}
DECL|method|getKeyRangeTo ()
specifier|public
name|Object
name|getKeyRangeTo
parameter_list|()
block|{
return|return
name|keyRangeTo
return|;
block|}
comment|/**      * The Key Range To      */
DECL|method|setKeyRangeTo (Object keyRangeTo)
specifier|public
name|void
name|setKeyRangeTo
parameter_list|(
name|Object
name|keyRangeTo
parameter_list|)
block|{
name|this
operator|.
name|keyRangeTo
operator|=
name|keyRangeTo
expr_stmt|;
block|}
DECL|method|getFields ()
specifier|public
name|Strings
name|getFields
parameter_list|()
block|{
return|return
name|fields
return|;
block|}
comment|/**      * The Fields      */
DECL|method|setFields (Strings fields)
specifier|public
name|void
name|setFields
parameter_list|(
name|Strings
name|fields
parameter_list|)
block|{
name|this
operator|.
name|fields
operator|=
name|fields
expr_stmt|;
block|}
DECL|method|getConcurrentConsumers ()
specifier|public
name|int
name|getConcurrentConsumers
parameter_list|()
block|{
return|return
name|concurrentConsumers
return|;
block|}
comment|/**      * Number of concurrent consumers      */
DECL|method|setConcurrentConsumers (int concurrentConsumers)
specifier|public
name|void
name|setConcurrentConsumers
parameter_list|(
name|int
name|concurrentConsumers
parameter_list|)
block|{
name|this
operator|.
name|concurrentConsumers
operator|=
name|concurrentConsumers
expr_stmt|;
block|}
DECL|method|isFlushOnEveryOperation ()
specifier|public
name|boolean
name|isFlushOnEveryOperation
parameter_list|()
block|{
return|return
name|flushOnEveryOperation
return|;
block|}
comment|/**      * Flush on every operation      */
DECL|method|setFlushOnEveryOperation (boolean flushOnEveryOperation)
specifier|public
name|void
name|setFlushOnEveryOperation
parameter_list|(
name|boolean
name|flushOnEveryOperation
parameter_list|)
block|{
name|this
operator|.
name|flushOnEveryOperation
operator|=
name|flushOnEveryOperation
expr_stmt|;
block|}
comment|/**      * Hadoop Configuration      */
DECL|method|setHadoopConfiguration (Configuration hadoopConfiguration)
specifier|public
name|void
name|setHadoopConfiguration
parameter_list|(
name|Configuration
name|hadoopConfiguration
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|hadoopConfiguration
argument_list|,
literal|"Hadoop Configuration could not be null!"
argument_list|)
expr_stmt|;
name|this
operator|.
name|hadoopConfiguration
operator|=
name|hadoopConfiguration
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * Instance name      */
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
block|}
end_class

end_unit

