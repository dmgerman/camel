begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot.routefilter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|routefilter
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
name|ModelCamelContext
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
name|test
operator|.
name|spring
operator|.
name|CamelSpringBootRunner
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
name|test
operator|.
name|spring
operator|.
name|ExcludeRoutes
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|SpringBootApplication
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|test
operator|.
name|context
operator|.
name|SpringBootTest
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|CamelSpringBootRunner
operator|.
name|class
argument_list|)
annotation|@
name|SpringBootApplication
argument_list|()
annotation|@
name|SpringBootTest
argument_list|(
name|classes
operator|=
name|FooTest
operator|.
name|class
argument_list|)
annotation|@
name|ExcludeRoutes
argument_list|(
block|{
name|BarRoute
operator|.
name|class
block|,
name|DrinkRoute
operator|.
name|class
block|}
argument_list|)
DECL|class|FooExcludeRouteAnnotationTest
specifier|public
class|class
name|FooExcludeRouteAnnotationTest
block|{
annotation|@
name|Autowired
DECL|field|producerTemplate
name|ProducerTemplate
name|producerTemplate
decl_stmt|;
annotation|@
name|Autowired
DECL|field|camelContext
name|ModelCamelContext
name|camelContext
decl_stmt|;
annotation|@
name|Test
DECL|method|shouldSendToFoo ()
specifier|public
name|void
name|shouldSendToFoo
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given
name|MockEndpoint
name|mock
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"mock:foo"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Foo"
argument_list|)
expr_stmt|;
comment|// When
name|producerTemplate
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello Foo"
argument_list|)
expr_stmt|;
comment|// Then
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit
