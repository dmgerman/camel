begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.solr
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|solr
package|;
end_package

begin_class
DECL|class|SolrConstants
specifier|public
specifier|final
class|class
name|SolrConstants
block|{
DECL|field|FIELD
specifier|public
specifier|static
specifier|final
name|String
name|FIELD
init|=
literal|"SolrField."
decl_stmt|;
DECL|field|OPERATION
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION
init|=
literal|"SolrOperation"
decl_stmt|;
DECL|field|PARAM
specifier|public
specifier|static
specifier|final
name|String
name|PARAM
init|=
literal|"SolrParam."
decl_stmt|;
DECL|field|OPERATION_COMMIT
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_COMMIT
init|=
literal|"COMMIT"
decl_stmt|;
DECL|field|OPERATION_ROLLBACK
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_ROLLBACK
init|=
literal|"ROLLBACK"
decl_stmt|;
DECL|field|OPERATION_OPTIMIZE
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_OPTIMIZE
init|=
literal|"OPTIMIZE"
decl_stmt|;
DECL|field|OPERATION_INSERT
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_INSERT
init|=
literal|"INSERT"
decl_stmt|;
DECL|field|OPERATION_INSERT_STREAMING
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_INSERT_STREAMING
init|=
literal|"INSERT_STREAMING"
decl_stmt|;
DECL|field|OPERATION_ADD_BEAN
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_ADD_BEAN
init|=
literal|"ADD_BEAN"
decl_stmt|;
DECL|field|OPERATION_ADD_BEANS
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_ADD_BEANS
init|=
literal|"ADD_BEANS"
decl_stmt|;
DECL|field|OPERATION_DELETE_BY_ID
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_DELETE_BY_ID
init|=
literal|"DELETE_BY_ID"
decl_stmt|;
DECL|field|OPERATION_DELETE_BY_QUERY
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION_DELETE_BY_QUERY
init|=
literal|"DELETE_BY_QUERY"
decl_stmt|;
DECL|field|PARAM_STREAMING_QUEUE_SIZE
specifier|public
specifier|static
specifier|final
name|String
name|PARAM_STREAMING_QUEUE_SIZE
init|=
literal|"streamingQueueSize"
decl_stmt|;
DECL|field|PARAM_STREAMING_THREAD_COUNT
specifier|public
specifier|static
specifier|final
name|String
name|PARAM_STREAMING_THREAD_COUNT
init|=
literal|"streamingThreadCount"
decl_stmt|;
DECL|field|DEFUALT_STREAMING_QUEUE_SIZE
specifier|public
specifier|static
specifier|final
name|int
name|DEFUALT_STREAMING_QUEUE_SIZE
init|=
literal|10
decl_stmt|;
DECL|field|DEFAULT_SHOULD_HTTPS
specifier|public
specifier|static
specifier|final
name|boolean
name|DEFAULT_SHOULD_HTTPS
init|=
literal|false
decl_stmt|;
DECL|field|DEFAULT_STREAMING_THREAD_COUNT
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_STREAMING_THREAD_COUNT
init|=
literal|2
decl_stmt|;
DECL|method|SolrConstants ()
specifier|private
name|SolrConstants
parameter_list|()
block|{
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

