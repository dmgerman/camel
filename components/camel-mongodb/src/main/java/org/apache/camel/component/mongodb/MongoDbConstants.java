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
DECL|class|MongoDbConstants
specifier|public
specifier|final
class|class
name|MongoDbConstants
block|{
DECL|field|OPERATION_HEADER
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_HEADER
init|=
literal|"CamelMongoDbOperation"
decl_stmt|;
DECL|field|RESULT_TOTAL_SIZE
specifier|public
specifier|static
specifier|final
name|String
name|RESULT_TOTAL_SIZE
init|=
literal|"CamelMongoDbResultTotalSize"
decl_stmt|;
DECL|field|RESULT_PAGE_SIZE
specifier|public
specifier|static
specifier|final
name|String
name|RESULT_PAGE_SIZE
init|=
literal|"CamelMongoDbResultPageSize"
decl_stmt|;
DECL|field|FIELDS_FILTER
specifier|public
specifier|static
specifier|final
name|String
name|FIELDS_FILTER
init|=
literal|"CamelMongoDbFieldsFilter"
decl_stmt|;
DECL|field|BATCH_SIZE
specifier|public
specifier|static
specifier|final
name|String
name|BATCH_SIZE
init|=
literal|"CamelMongoDbBatchSize"
decl_stmt|;
DECL|field|NUM_TO_SKIP
specifier|public
specifier|static
specifier|final
name|String
name|NUM_TO_SKIP
init|=
literal|"CamelMongoDbNumToSkip"
decl_stmt|;
DECL|field|INSERT_RECORDS_AFFECTED
specifier|public
specifier|static
specifier|final
name|String
name|INSERT_RECORDS_AFFECTED
init|=
literal|"CamelMongoDbInsertRecordsAffected"
decl_stmt|;
DECL|field|LAST_ERROR
specifier|public
specifier|static
specifier|final
name|String
name|LAST_ERROR
init|=
literal|"CamelMongoDbLastError"
decl_stmt|;
DECL|field|MULTIUPDATE
specifier|public
specifier|static
specifier|final
name|String
name|MULTIUPDATE
init|=
literal|"CamelMongoDbMultiUpdate"
decl_stmt|;
DECL|field|UPSERT
specifier|public
specifier|static
specifier|final
name|String
name|UPSERT
init|=
literal|"CamelMongoDbUpsert"
decl_stmt|;
DECL|field|RECORDS_AFFECTED
specifier|public
specifier|static
specifier|final
name|String
name|RECORDS_AFFECTED
init|=
literal|"CamelMongoDbRecordsAffected"
decl_stmt|;
DECL|field|SORT_BY
specifier|public
specifier|static
specifier|final
name|String
name|SORT_BY
init|=
literal|"CamelMongoDbSortBy"
decl_stmt|;
DECL|field|DATABASE
specifier|public
specifier|static
specifier|final
name|String
name|DATABASE
init|=
literal|"CamelMongoDbDatabase"
decl_stmt|;
DECL|field|COLLECTION
specifier|public
specifier|static
specifier|final
name|String
name|COLLECTION
init|=
literal|"CamelMongoDbCollection"
decl_stmt|;
DECL|field|COLLECTION_INDEX
specifier|public
specifier|static
specifier|final
name|String
name|COLLECTION_INDEX
init|=
literal|"CamelMongoDbCollectionIndex"
decl_stmt|;
DECL|field|WRITECONCERN
specifier|public
specifier|static
specifier|final
name|String
name|WRITECONCERN
init|=
literal|"CamelMongoDbWriteConcern"
decl_stmt|;
DECL|field|LIMIT
specifier|public
specifier|static
specifier|final
name|String
name|LIMIT
init|=
literal|"CamelMongoDbLimit"
decl_stmt|;
DECL|field|FROM_TAILABLE
specifier|public
specifier|static
specifier|final
name|String
name|FROM_TAILABLE
init|=
literal|"CamelMongoDbTailable"
decl_stmt|;
DECL|field|WRITERESULT
specifier|public
specifier|static
specifier|final
name|String
name|WRITERESULT
init|=
literal|"CamelMongoWriteResult"
decl_stmt|;
DECL|field|OID
specifier|public
specifier|static
specifier|final
name|String
name|OID
init|=
literal|"CamelMongoOid"
decl_stmt|;
DECL|method|MongoDbConstants ()
specifier|private
name|MongoDbConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

