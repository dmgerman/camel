begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p>  * http://www.apache.org/licenses/LICENSE-2.0  *<p>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty.rest.producer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
operator|.
name|rest
operator|.
name|producer
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
name|jetty
operator|.
name|BaseJettyTest
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
name|jetty
operator|.
name|rest
operator|.
name|CountryPojo
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
name|jetty
operator|.
name|rest
operator|.
name|UserPojo
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
name|jetty
operator|.
name|rest
operator|.
name|UserService
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
name|rest
operator|.
name|RestBindingMode
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
DECL|class|JettyRestProducerPojoInOutTest
specifier|public
class|class
name|JettyRestProducerPojoInOutTest
extends|extends
name|BaseJettyTest
block|{
annotation|@
name|Test
DECL|method|testJettyEmptyBody ()
specifier|public
name|void
name|testJettyEmptyBody
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|fluentTemplate
operator|.
name|to
argument_list|(
literal|"rest:get:users/lives"
argument_list|)
operator|.
name|withHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/json"
argument_list|)
operator|.
name|request
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{\"iso\":\"EN\",\"country\":\"England\"}"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testJettyJSonBody ()
specifier|public
name|void
name|testJettyJSonBody
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|body
init|=
literal|"{\"id\": 123, \"name\": \"Donald Duck\"}"
decl_stmt|;
name|String
name|out
init|=
name|fluentTemplate
operator|.
name|to
argument_list|(
literal|"rest:post:users/lives"
argument_list|)
operator|.
name|withHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/json"
argument_list|)
operator|.
name|withBody
argument_list|(
name|body
argument_list|)
operator|.
name|request
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{\"iso\":\"EN\",\"country\":\"England\"}"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testJettyPojoIn ()
specifier|public
name|void
name|testJettyPojoIn
parameter_list|()
throws|throws
name|Exception
block|{
name|UserPojo
name|user
init|=
operator|new
name|UserPojo
argument_list|()
decl_stmt|;
name|user
operator|.
name|setId
argument_list|(
literal|123
argument_list|)
expr_stmt|;
name|user
operator|.
name|setName
argument_list|(
literal|"Donald Duck"
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|fluentTemplate
operator|.
name|to
argument_list|(
literal|"rest:post:users/lives"
argument_list|)
operator|.
name|withHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/json"
argument_list|)
operator|.
name|withBody
argument_list|(
name|user
argument_list|)
operator|.
name|request
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{\"iso\":\"EN\",\"country\":\"England\"}"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testJettyPojoInOut ()
specifier|public
name|void
name|testJettyPojoInOut
parameter_list|()
throws|throws
name|Exception
block|{
name|UserPojo
name|user
init|=
operator|new
name|UserPojo
argument_list|()
decl_stmt|;
name|user
operator|.
name|setId
argument_list|(
literal|123
argument_list|)
expr_stmt|;
name|user
operator|.
name|setName
argument_list|(
literal|"Donald Duck"
argument_list|)
expr_stmt|;
name|CountryPojo
name|pojo
init|=
name|fluentTemplate
operator|.
name|to
argument_list|(
literal|"rest:post:users/lives"
argument_list|)
operator|.
name|withHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/json"
argument_list|)
operator|.
name|withBody
argument_list|(
name|user
argument_list|)
operator|.
name|request
argument_list|(
name|CountryPojo
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pojo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"EN"
argument_list|,
name|pojo
operator|.
name|getIso
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"England"
argument_list|,
name|pojo
operator|.
name|getCountry
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// configure to use jetty on localhost with the given port
comment|// and enable auto binding mode
name|restConfiguration
argument_list|()
operator|.
name|component
argument_list|(
literal|"jetty"
argument_list|)
operator|.
name|host
argument_list|(
literal|"localhost"
argument_list|)
operator|.
name|port
argument_list|(
name|getPort
argument_list|()
argument_list|)
operator|.
name|bindingMode
argument_list|(
name|RestBindingMode
operator|.
name|auto
argument_list|)
expr_stmt|;
comment|// use the rest DSL to define the rest services
name|rest
argument_list|(
literal|"/users/"
argument_list|)
comment|// just return the default country here
operator|.
name|get
argument_list|(
literal|"lives"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|post
argument_list|(
literal|"lives"
argument_list|)
operator|.
name|type
argument_list|(
name|UserPojo
operator|.
name|class
argument_list|)
operator|.
name|outType
argument_list|(
name|CountryPojo
operator|.
name|class
argument_list|)
operator|.
name|route
argument_list|()
operator|.
name|log
argument_list|(
literal|"Lives where"
argument_list|)
operator|.
name|bean
argument_list|(
operator|new
name|UserService
argument_list|()
argument_list|,
literal|"livesWhere"
argument_list|)
expr_stmt|;
name|CountryPojo
name|country
init|=
operator|new
name|CountryPojo
argument_list|()
decl_stmt|;
name|country
operator|.
name|setIso
argument_list|(
literal|"EN"
argument_list|)
expr_stmt|;
name|country
operator|.
name|setCountry
argument_list|(
literal|"England"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
name|country
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

