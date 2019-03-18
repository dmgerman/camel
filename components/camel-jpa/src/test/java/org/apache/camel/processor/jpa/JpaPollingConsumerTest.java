begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.jpa
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|jpa
package|;
end_package

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
name|examples
operator|.
name|Customer
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
name|spring
operator|.
name|SpringRouteBuilder
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
DECL|class|JpaPollingConsumerTest
specifier|public
class|class
name|JpaPollingConsumerTest
extends|extends
name|AbstractJpaTest
block|{
DECL|field|SELECT_ALL_STRING
specifier|protected
specifier|static
specifier|final
name|String
name|SELECT_ALL_STRING
init|=
literal|"select x from "
operator|+
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" x"
decl_stmt|;
DECL|method|assertEntitiesInDatabase (int count, String entity)
specifier|protected
name|void
name|assertEntitiesInDatabase
parameter_list|(
name|int
name|count
parameter_list|,
name|String
name|entity
parameter_list|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|entityManager
operator|.
name|createQuery
argument_list|(
literal|"select o from "
operator|+
name|entity
operator|+
literal|" o"
argument_list|)
operator|.
name|getResultList
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|count
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPollingConsumer ()
specifier|public
name|void
name|testPollingConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|Customer
name|customer
init|=
operator|new
name|Customer
argument_list|()
decl_stmt|;
name|customer
operator|.
name|setName
argument_list|(
literal|"Donald Duck"
argument_list|)
expr_stmt|;
name|saveEntityInDB
argument_list|(
name|customer
argument_list|)
expr_stmt|;
name|Customer
name|customer2
init|=
operator|new
name|Customer
argument_list|()
decl_stmt|;
name|customer2
operator|.
name|setName
argument_list|(
literal|"Goofy"
argument_list|)
expr_stmt|;
name|saveEntityInDB
argument_list|(
name|customer2
argument_list|)
expr_stmt|;
name|assertEntitiesInDatabase
argument_list|(
literal|2
argument_list|,
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Donald Duck how are you today?"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello NAME how are you today?"
argument_list|,
literal|"name"
argument_list|,
literal|"Donald%"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|SpringRouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|pollEnrich
argument_list|()
operator|.
name|simple
argument_list|(
literal|"jpa://"
operator|+
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"?query=select c from Customer c where c.name like '${header.name}'"
argument_list|)
operator|.
name|aggregationStrategy
argument_list|(
parameter_list|(
name|a
parameter_list|,
name|b
parameter_list|)
lambda|->
block|{
name|String
name|name
init|=
name|b
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Customer
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|phrase
init|=
name|a
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
operator|.
name|replace
argument_list|(
literal|"NAME"
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|a
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|phrase
argument_list|)
expr_stmt|;
return|return
name|a
return|;
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|routeXml ()
specifier|protected
name|String
name|routeXml
parameter_list|()
block|{
return|return
literal|"org/apache/camel/processor/jpa/springJpaRouteTest.xml"
return|;
block|}
annotation|@
name|Override
DECL|method|selectAllString ()
specifier|protected
name|String
name|selectAllString
parameter_list|()
block|{
return|return
name|SELECT_ALL_STRING
return|;
block|}
block|}
end_class

end_unit

