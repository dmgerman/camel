begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mongodb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mongodb
package|;
end_package

begin_class
DECL|class|MongoDbTailTrackingConfig
specifier|public
class|class
name|MongoDbTailTrackingConfig
block|{
DECL|field|DEFAULT_COLLECTION
specifier|static
specifier|final
name|String
name|DEFAULT_COLLECTION
init|=
literal|"camelTailTracking"
decl_stmt|;
DECL|field|DEFAULT_FIELD
specifier|static
specifier|final
name|String
name|DEFAULT_FIELD
init|=
literal|"lastTrackingValue"
decl_stmt|;
comment|/**      * See {@link MongoDbEndpoint#setTailTrackDb(String)}      */
DECL|field|db
specifier|public
specifier|final
name|String
name|db
decl_stmt|;
comment|/**      * See {@link MongoDbEndpoint#setTailTrackCollection(String)}      */
DECL|field|collection
specifier|public
specifier|final
name|String
name|collection
decl_stmt|;
comment|/**      * See {@link MongoDbEndpoint#setTailTrackIncreasingField(String)}      */
DECL|field|increasingField
specifier|final
name|String
name|increasingField
decl_stmt|;
comment|/**      * See {@link MongoDbEndpoint#setPersistentTailTracking(boolean)}      */
DECL|field|persistent
specifier|final
name|boolean
name|persistent
decl_stmt|;
comment|/**      * See {@link MongoDbEndpoint#setTailTrackField(String)}      */
DECL|field|field
specifier|final
name|String
name|field
decl_stmt|;
comment|/**      * See {@link MongoDbEndpoint#setPersistentId(String)}      */
DECL|field|persistentId
specifier|final
name|String
name|persistentId
decl_stmt|;
comment|/**      * See {@link MongoDbEndpoint#setTailTrackingStrategy(MongoDBTailTrackingEnum)}      */
DECL|field|mongoDBTailTrackingStrategy
specifier|final
name|MongoDBTailTrackingEnum
name|mongoDBTailTrackingStrategy
decl_stmt|;
DECL|method|MongoDbTailTrackingConfig (boolean persistentTailTracking, String tailTrackIncreasingField, String tailTrackDb, String tailTrackCollection, String tailTrackField, String persistentId, MongoDBTailTrackingEnum mongoDBTailTrackingStrategy)
specifier|public
name|MongoDbTailTrackingConfig
parameter_list|(
name|boolean
name|persistentTailTracking
parameter_list|,
name|String
name|tailTrackIncreasingField
parameter_list|,
name|String
name|tailTrackDb
parameter_list|,
name|String
name|tailTrackCollection
parameter_list|,
name|String
name|tailTrackField
parameter_list|,
name|String
name|persistentId
parameter_list|,
name|MongoDBTailTrackingEnum
name|mongoDBTailTrackingStrategy
parameter_list|)
block|{
name|this
operator|.
name|increasingField
operator|=
name|tailTrackIncreasingField
expr_stmt|;
name|this
operator|.
name|persistent
operator|=
name|persistentTailTracking
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|tailTrackDb
expr_stmt|;
name|this
operator|.
name|persistentId
operator|=
name|persistentId
expr_stmt|;
name|this
operator|.
name|collection
operator|=
name|tailTrackCollection
operator|==
literal|null
condition|?
name|MongoDbTailTrackingConfig
operator|.
name|DEFAULT_COLLECTION
else|:
name|tailTrackCollection
expr_stmt|;
name|this
operator|.
name|field
operator|=
name|tailTrackField
operator|==
literal|null
condition|?
name|MongoDbTailTrackingConfig
operator|.
name|DEFAULT_FIELD
else|:
name|tailTrackField
expr_stmt|;
name|this
operator|.
name|mongoDBTailTrackingStrategy
operator|=
name|mongoDBTailTrackingStrategy
operator|==
literal|null
condition|?
name|MongoDBTailTrackingEnum
operator|.
name|LITERAL
else|:
name|mongoDBTailTrackingStrategy
expr_stmt|;
block|}
block|}
end_class

end_unit

