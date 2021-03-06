begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
package|;
end_package

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
name|impl
operator|.
name|DefaultCamelContext
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
name|support
operator|.
name|DefaultExchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
DECL|class|ExchangeBuilderTest
specifier|public
class|class
name|ExchangeBuilderTest
extends|extends
name|Assert
block|{
DECL|field|CONTEXT
specifier|private
specifier|static
specifier|final
name|DefaultCamelContext
name|CONTEXT
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
DECL|field|BODY
specifier|private
specifier|static
specifier|final
name|String
name|BODY
init|=
literal|"Message Body"
decl_stmt|;
DECL|field|KEY
specifier|private
specifier|static
specifier|final
name|String
name|KEY
init|=
literal|"Header key"
decl_stmt|;
DECL|field|VALUE
specifier|private
specifier|static
specifier|final
name|String
name|VALUE
init|=
literal|"Header value"
decl_stmt|;
DECL|field|PROPERTY_KEY
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_KEY
init|=
literal|"Property key"
decl_stmt|;
DECL|field|PROPERTY_VALUE
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_VALUE
init|=
literal|"Property value"
decl_stmt|;
annotation|@
name|Test
DECL|method|testBuildAnExchangeWithDefaultPattern ()
specifier|public
name|void
name|testBuildAnExchangeWithDefaultPattern
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|CONTEXT
argument_list|)
decl_stmt|;
name|Exchange
name|builtExchange
init|=
name|ExchangeBuilder
operator|.
name|anExchange
argument_list|(
name|CONTEXT
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|exchange
operator|.
name|getPattern
argument_list|()
argument_list|,
name|builtExchange
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBuildAnExchangeWithBodyHeaderAndPattern ()
specifier|public
name|void
name|testBuildAnExchangeWithBodyHeaderAndPattern
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|ExchangeBuilder
operator|.
name|anExchange
argument_list|(
name|CONTEXT
argument_list|)
operator|.
name|withBody
argument_list|(
name|BODY
argument_list|)
operator|.
name|withHeader
argument_list|(
name|KEY
argument_list|,
name|VALUE
argument_list|)
operator|.
name|withProperty
argument_list|(
name|PROPERTY_KEY
argument_list|,
name|PROPERTY_VALUE
argument_list|)
operator|.
name|withPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
name|BODY
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KEY
argument_list|)
argument_list|,
name|VALUE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exchange
operator|.
name|getPattern
argument_list|()
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|PROPERTY_KEY
argument_list|)
argument_list|,
name|PROPERTY_VALUE
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

