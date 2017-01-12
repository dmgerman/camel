begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.couchbase
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchbase
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

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
name|com
operator|.
name|couchbase
operator|.
name|client
operator|.
name|CouchbaseClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|couchbase
operator|.
name|client
operator|.
name|vbucket
operator|.
name|ConfigurationException
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
name|Processor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|CouchbaseConsumerTest
specifier|public
class|class
name|CouchbaseConsumerTest
block|{
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ConfigurationException
operator|.
name|class
argument_list|)
DECL|method|testNewCouchbaseConsumer ()
specifier|public
name|void
name|testNewCouchbaseConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|CouchbaseConsumer
name|couchbaseConsumer
init|=
operator|new
name|CouchbaseConsumer
argument_list|(
operator|new
name|CouchbaseEndpoint
argument_list|()
argument_list|,
operator|new
name|CouchbaseClient
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|URI
argument_list|>
argument_list|()
argument_list|,
literal|"bucketName"
argument_list|,
literal|"pwd"
argument_list|)
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Nothing to do
block|}
block|}
argument_list|)
decl_stmt|;
block|}
block|}
end_class

end_unit

