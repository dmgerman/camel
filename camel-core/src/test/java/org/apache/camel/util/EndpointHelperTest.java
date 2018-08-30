begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

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
name|List
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
name|CamelContext
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
name|ContextTestSupport
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
name|Endpoint
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
name|NoSuchBeanException
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
name|impl
operator|.
name|SimpleRegistry
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|EndpointHelperTest
specifier|public
class|class
name|EndpointHelperTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|foo
specifier|private
name|Endpoint
name|foo
decl_stmt|;
DECL|field|bar
specifier|private
name|Endpoint
name|bar
decl_stmt|;
annotation|@
name|Test
DECL|method|testPollEndpoint ()
specifier|public
name|void
name|testPollEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|bodies
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|// uses 1 sec default timeout
name|EndpointHelper
operator|.
name|pollEndpoint
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:foo"
argument_list|)
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
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
name|bodies
operator|.
name|add
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|bodies
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|bodies
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|bodies
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPollEndpointTimeout ()
specifier|public
name|void
name|testPollEndpointTimeout
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|bodies
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|EndpointHelper
operator|.
name|pollEndpoint
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:foo"
argument_list|)
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
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
name|bodies
operator|.
name|add
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|bodies
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|bodies
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|bodies
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleRegistry
name|reg
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|reg
argument_list|)
decl_stmt|;
name|foo
operator|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
expr_stmt|;
name|bar
operator|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
expr_stmt|;
name|reg
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
name|foo
argument_list|)
expr_stmt|;
name|reg
operator|.
name|put
argument_list|(
literal|"coolbar"
argument_list|,
name|bar
argument_list|)
expr_stmt|;
name|reg
operator|.
name|put
argument_list|(
literal|"numbar"
argument_list|,
literal|"12345"
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testLookupEndpointRegistryId ()
specifier|public
name|void
name|testLookupEndpointRegistryId
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|EndpointHelper
operator|.
name|lookupEndpointRegistryId
argument_list|(
name|foo
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"coolbar"
argument_list|,
name|EndpointHelper
operator|.
name|lookupEndpointRegistryId
argument_list|(
name|bar
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|EndpointHelper
operator|.
name|lookupEndpointRegistryId
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:cheese"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLookupEndpointRegistryIdUsingRef ()
specifier|public
name|void
name|testLookupEndpointRegistryIdUsingRef
parameter_list|()
throws|throws
name|Exception
block|{
name|foo
operator|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"ref:foo"
argument_list|)
expr_stmt|;
name|bar
operator|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"ref:coolbar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|EndpointHelper
operator|.
name|lookupEndpointRegistryId
argument_list|(
name|foo
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"coolbar"
argument_list|,
name|EndpointHelper
operator|.
name|lookupEndpointRegistryId
argument_list|(
name|bar
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|EndpointHelper
operator|.
name|lookupEndpointRegistryId
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:cheese"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testResolveReferenceParameter ()
specifier|public
name|void
name|testResolveReferenceParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|EndpointHelper
operator|.
name|resolveReferenceParameter
argument_list|(
name|context
argument_list|,
literal|"coolbar"
argument_list|,
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|bar
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testResolveAndConvertReferenceParameter ()
specifier|public
name|void
name|testResolveAndConvertReferenceParameter
parameter_list|()
throws|throws
name|Exception
block|{
comment|// The registry value is a java.lang.String
name|Integer
name|number
init|=
name|EndpointHelper
operator|.
name|resolveReferenceParameter
argument_list|(
name|context
argument_list|,
literal|"numbar"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|number
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|12345
argument_list|,
operator|(
name|int
operator|)
name|number
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testResolveAndConvertMissingReferenceParameter ()
specifier|public
name|void
name|testResolveAndConvertMissingReferenceParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|Integer
name|number
init|=
name|EndpointHelper
operator|.
name|resolveReferenceParameter
argument_list|(
name|context
argument_list|,
literal|"misbar"
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|number
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMandatoryResolveAndConvertMissingReferenceParameter ()
specifier|public
name|void
name|testMandatoryResolveAndConvertMissingReferenceParameter
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|EndpointHelper
operator|.
name|resolveReferenceParameter
argument_list|(
name|context
argument_list|,
literal|"misbar"
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchBeanException
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"No bean could be found in the registry for: misbar of type: java.lang.Integer"
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testResolveParameter ()
specifier|public
name|void
name|testResolveParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|EndpointHelper
operator|.
name|resolveParameter
argument_list|(
name|context
argument_list|,
literal|"#coolbar"
argument_list|,
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|bar
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|Integer
name|num
init|=
name|EndpointHelper
operator|.
name|resolveParameter
argument_list|(
name|context
argument_list|,
literal|"123"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|num
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|num
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

