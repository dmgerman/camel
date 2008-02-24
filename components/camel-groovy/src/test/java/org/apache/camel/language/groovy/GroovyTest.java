begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.groovy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|groovy
package|;
end_package

begin_import
import|import
name|groovy
operator|.
name|lang
operator|.
name|GroovyClassLoader
import|;
end_import

begin_import
import|import
name|groovy
operator|.
name|lang
operator|.
name|MetaClassRegistry
import|;
end_import

begin_import
import|import
name|groovy
operator|.
name|lang
operator|.
name|MetaClass
import|;
end_import

begin_import
import|import
name|groovy
operator|.
name|lang
operator|.
name|Closure
import|;
end_import

begin_import
import|import
name|groovy
operator|.
name|lang
operator|.
name|ProxyMetaClass
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
name|model
operator|.
name|ProcessorType
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|GroovyTest
specifier|public
class|class
name|GroovyTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|expected
specifier|protected
name|String
name|expected
init|=
literal|"<hello>world!</hello>"
decl_stmt|;
DECL|field|groovyBuilderClass
specifier|protected
name|String
name|groovyBuilderClass
init|=
literal|"org.apache.camel.language.groovy.example.GroovyRoutes"
decl_stmt|;
DECL|method|testSendMatchingMessage ()
specifier|public
name|void
name|testSendMatchingMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:results"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:a"
argument_list|,
name|expected
argument_list|,
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisifed
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Should have received one exchange: "
operator|+
name|resultEndpoint
operator|.
name|getReceivedExchanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSendNotMatchingMessage ()
specifier|public
name|void
name|testSendNotMatchingMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:results"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:a"
argument_list|,
name|expected
argument_list|,
literal|"foo"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisifed
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Should not have received any messages: "
operator|+
name|resultEndpoint
operator|.
name|getReceivedExchanges
argument_list|()
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
name|CamelContext
name|answer
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|GroovyClassLoader
name|classLoader
init|=
operator|new
name|GroovyClassLoader
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|classLoader
operator|.
name|loadClass
argument_list|(
name|groovyBuilderClass
argument_list|)
decl_stmt|;
name|Object
name|object
init|=
name|answer
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|RouteBuilder
name|builder
init|=
name|assertIsInstanceOf
argument_list|(
name|RouteBuilder
operator|.
name|class
argument_list|,
name|object
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Loaded builder: "
operator|+
name|builder
argument_list|)
expr_stmt|;
name|answer
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

