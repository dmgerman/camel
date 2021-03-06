begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.fix
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|fix
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|EndpointInject
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
name|Produce
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
name|ProducerTemplate
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
name|builder
operator|.
name|RouteBuilder
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
name|mock
operator|.
name|MockEndpoint
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
name|dataformat
operator|.
name|bindy
operator|.
name|kvp
operator|.
name|BindyKeyValuePairDataFormat
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
name|dataformat
operator|.
name|bindy
operator|.
name|model
operator|.
name|fix
operator|.
name|withoutsection
operator|.
name|Order
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
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_class
annotation|@
name|ContextConfiguration
DECL|class|BindySimpleKeyValuePairWithoutSectionMarshallDslTest
specifier|public
class|class
name|BindySimpleKeyValuePairWithoutSectionMarshallDslTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
DECL|field|URI_MOCK_RESULT
specifier|private
specifier|static
specifier|final
name|String
name|URI_MOCK_RESULT
init|=
literal|"mock:result"
decl_stmt|;
DECL|field|URI_MOCK_ERROR
specifier|private
specifier|static
specifier|final
name|String
name|URI_MOCK_ERROR
init|=
literal|"mock:error"
decl_stmt|;
DECL|field|URI_DIRECT_START
specifier|private
specifier|static
specifier|final
name|String
name|URI_DIRECT_START
init|=
literal|"direct:start"
decl_stmt|;
DECL|field|models
specifier|private
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|models
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|URI_DIRECT_START
argument_list|)
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|URI_MOCK_RESULT
argument_list|)
DECL|field|result
specifier|private
name|MockEndpoint
name|result
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|URI_MOCK_ERROR
argument_list|)
DECL|field|error
specifier|private
name|MockEndpoint
name|error
decl_stmt|;
annotation|@
name|Test
DECL|method|testMarshallWithoutSection ()
specifier|public
name|void
name|testMarshallWithoutSection
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|generateModel
argument_list|()
argument_list|)
expr_stmt|;
comment|// We don't expect to have a message as an error will be raised
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// Message has been delivered to the mock error
name|error
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|error
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// and check that we have the caused exception stored
name|Exchange
name|exch
init|=
name|error
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Exception
name|cause
init|=
name|exch
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|IllegalArgumentException
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cause
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"@Section and/or @KeyValuePairDataField have not been defined"
argument_list|,
name|cause
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|generateModel ()
specifier|public
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|generateModel
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|modelObjects
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Order
name|order
init|=
operator|new
name|Order
argument_list|()
decl_stmt|;
name|order
operator|.
name|setAccount
argument_list|(
literal|"BE.CHM.001"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setClOrdId
argument_list|(
literal|"CHM0001-01"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setIDSource
argument_list|(
literal|"4"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setSecurityId
argument_list|(
literal|"BE0001245678"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setSide
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setText
argument_list|(
literal|"this is a camel - bindy test"
argument_list|)
expr_stmt|;
name|modelObjects
operator|.
name|put
argument_list|(
name|order
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|order
argument_list|)
expr_stmt|;
name|models
operator|.
name|add
argument_list|(
name|modelObjects
argument_list|)
expr_stmt|;
return|return
name|models
return|;
block|}
DECL|class|ContextConfig
specifier|public
specifier|static
class|class
name|ContextConfig
extends|extends
name|RouteBuilder
block|{
DECL|field|orderBindyDataFormat
name|BindyKeyValuePairDataFormat
name|orderBindyDataFormat
init|=
operator|new
name|BindyKeyValuePairDataFormat
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|model
operator|.
name|fix
operator|.
name|withoutsection
operator|.
name|Order
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// default should errors go to mock:error
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
name|URI_MOCK_ERROR
argument_list|)
argument_list|)
expr_stmt|;
name|onException
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|0
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|URI_DIRECT_START
argument_list|)
operator|.
name|marshal
argument_list|(
name|orderBindyDataFormat
argument_list|)
operator|.
name|to
argument_list|(
name|URI_MOCK_RESULT
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

