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
comment|/**  * Gora Configuration.  *  * @author ipolyzos  */
end_comment

begin_class
DECL|class|GoraConfiguration
specifier|public
class|class
name|GoraConfiguration
block|{
comment|/**      * key type      */
DECL|field|keyClass
specifier|private
name|String
name|keyClass
decl_stmt|;
comment|/**      * configuration      */
DECL|field|hadoopConfiguration
specifier|private
name|Configuration
name|hadoopConfiguration
decl_stmt|;
comment|/**      * value type      */
DECL|field|valueClass
specifier|private
name|String
name|valueClass
decl_stmt|;
comment|/**      *  dataStore type      */
DECL|field|dataStoreClass
specifier|private
name|String
name|dataStoreClass
decl_stmt|;
comment|/** Consumer only properties! */
comment|/**      *  Gora Query Start Time attribute      */
DECL|field|startTime
specifier|private
name|long
name|startTime
decl_stmt|;
comment|/**      * Gora Query End Time attribute      */
DECL|field|endTime
specifier|private
name|long
name|endTime
decl_stmt|;
comment|/**      * Gora Query Time Range From attribute      */
DECL|field|timeRangeFrom
specifier|private
name|long
name|timeRangeFrom
decl_stmt|;
comment|/**      * Gora Query Key Range To attribute      */
DECL|field|timeRangeTo
specifier|private
name|long
name|timeRangeTo
decl_stmt|;
comment|/**      * Gora Query Limit attribute      */
DECL|field|limit
specifier|private
name|long
name|limit
decl_stmt|;
comment|/**      * Gora Query Timestamp attribute      */
DECL|field|timestamp
specifier|private
name|long
name|timestamp
decl_stmt|;
comment|/**      * Gora Query Start Key attribute      */
DECL|field|startKey
specifier|private
name|Object
name|startKey
decl_stmt|;
comment|/**      * Gora Query End Key attribute      */
DECL|field|endKey
specifier|private
name|Object
name|endKey
decl_stmt|;
comment|/**      * Gora Query Key Range From attribute      */
DECL|field|keyRangeFrom
specifier|private
name|Object
name|keyRangeFrom
decl_stmt|;
comment|/**      * Gora Query Key Range To attribute      */
DECL|field|keyRangeTo
specifier|private
name|Object
name|keyRangeTo
decl_stmt|;
comment|/**      * Gora Query Fields attribute      */
DECL|field|fields
specifier|private
name|Strings
name|fields
decl_stmt|;
comment|/**      * Concurrent Consumers      *      *<b>NOTE:<b/> used only by consumer      */
DECL|field|concurrentConsumers
specifier|private
name|int
name|concurrentConsumers
init|=
literal|1
decl_stmt|;
comment|/**      * Flush on every operation      *      *<b>NOTE:<b/> used only by producer      */
DECL|field|flushOnEveryOperation
specifier|private
name|boolean
name|flushOnEveryOperation
init|=
literal|true
decl_stmt|;
comment|/**      * Default Constructor      */
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
comment|/**      * Get type of the key (i.e clients)      *      * @return key class      */
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
comment|/**      * Set type class of the key      *      * @param keyClass      */
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
comment|/**      * Get type of the value      *      * @return      */
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
comment|/**      * Set type of the value      *      * @param valueClass      */
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
comment|/**      * Get type of the dataStore      *      * @return  DataStore class      */
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
comment|/**      * Set type of the dataStore      *      * @param dataStoreClass      */
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
comment|/**      * Get Hadoop Configuration      *      * @return      */
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
comment|/**      * Get Start Time      *      * @return      */
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
comment|/**      * Set Start Time      *      * @return      */
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
comment|/**      * Get End Time      *      * @return      */
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
comment|/**      * Set End Time      *      * @return      */
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
comment|/**      * Get Time Range From      *      * @return      */
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
comment|/**      * Set Time Range From      *      * @return      */
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
comment|/**      * Get Time Range To      *      * @return      */
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
comment|/**      * Set Time Range To      *      * @return      */
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
comment|/**      * Get Limit      *      * @return      */
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
comment|/**      * Set Limit      *      * @param limit      */
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
comment|/**      * Get Timestamp      *      * @return      */
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
comment|/**      * Set Timestamp      *      * @param timestamp      */
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
comment|/**      * Get Start Key      *      * @return      */
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
comment|/**      * Set Start Key      *      * @param startKey      */
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
comment|/**      * Get End Key      *      * @return      */
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
comment|/**      * Set End Key      *      * @param endKey      */
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
comment|/**      * Get Key Range From      * @return      */
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
comment|/**      * Set Key Range From      *      * @param keyRangeFrom      */
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
comment|/**      * Get Key Range To      * @return      */
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
comment|/**      * Set Key Range To      *      * @param keyRangeTo      */
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
comment|/**      * Get Fields      *      * @return      */
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
comment|/**      * Set Fields      *      * @param fields      */
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
comment|/**      * Get Concurrent Consumers      * @return      */
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
comment|/**      * Set Concurrent Consumers      *      * @param concurrentConsumers      */
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
comment|/**      * Get flush on every operation      *      * @return      */
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
comment|/**      * Set flush on every operation      *      * @param flushOnEveryOperation      */
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
comment|/**      * Set Hadoop Configuration      *      * @param hadoopConfiguration      */
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
block|}
end_class

end_unit

