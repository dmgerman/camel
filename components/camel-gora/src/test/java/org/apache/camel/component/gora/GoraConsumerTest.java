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
name|ExchangePattern
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
name|Message
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
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_comment
comment|/**  * GORA Consumer Tests  *  */
end_comment

begin_class
DECL|class|GoraConsumerTest
specifier|public
class|class
name|GoraConsumerTest
extends|extends
name|GoraTestSupport
block|{
comment|/**      * Mock CamelExchange      */
DECL|field|mockCamelExchange
specifier|private
name|Exchange
name|mockCamelExchange
decl_stmt|;
comment|/**      * Mock Gora Endpoint      */
DECL|field|mockGoraEndpoint
specifier|private
name|GoraEndpoint
name|mockGoraEndpoint
decl_stmt|;
comment|/**      * Mock Gora Configuration      */
DECL|field|mockGoraConfiguration
specifier|private
name|GoraConfiguration
name|mockGoraConfiguration
decl_stmt|;
comment|/**      * Mock Camel Message      */
DECL|field|mockCamelMessage
specifier|private
name|Message
name|mockCamelMessage
decl_stmt|;
comment|/**      * Mock Gora DataStore      */
DECL|field|mockDatastore
specifier|private
name|DataStore
name|mockDatastore
decl_stmt|;
comment|/**      * Mock Processor      */
DECL|field|mockGoraProcessor
specifier|private
name|Processor
name|mockGoraProcessor
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
comment|//setup mocks
name|mockCamelExchange
operator|=
name|mock
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
expr_stmt|;
name|mockGoraEndpoint
operator|=
name|mock
argument_list|(
name|GoraEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|mockGoraConfiguration
operator|=
name|mock
argument_list|(
name|GoraConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|mockCamelMessage
operator|=
name|mock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
expr_stmt|;
name|mockDatastore
operator|=
name|mock
argument_list|(
name|DataStore
operator|.
name|class
argument_list|)
expr_stmt|;
comment|//setup default conditions
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getIn
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockCamelMessage
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getPattern
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|consumerInstantiationWithMocksShouldSucceed ()
specifier|public
name|void
name|consumerInstantiationWithMocksShouldSucceed
parameter_list|()
throws|throws
name|ClassNotFoundException
throws|,
name|NoSuchMethodException
throws|,
name|IllegalAccessException
throws|,
name|InvocationTargetException
block|{
specifier|final
name|Query
name|mockQuery
init|=
name|mock
argument_list|(
name|Query
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|mockDatastore
operator|.
name|newQuery
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockQuery
argument_list|)
expr_stmt|;
name|GoraConsumer
name|goraConsumer
init|=
operator|new
name|GoraConsumer
argument_list|(
name|mockGoraEndpoint
argument_list|,
name|mockGoraProcessor
argument_list|,
name|mockGoraConfiguration
argument_list|,
name|mockDatastore
argument_list|)
decl_stmt|;
block|}
block|}
end_class

end_unit

