begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servlet
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|MessageFormat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|arquillian
operator|.
name|container
operator|.
name|test
operator|.
name|api
operator|.
name|Deployment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|arquillian
operator|.
name|container
operator|.
name|test
operator|.
name|api
operator|.
name|RunAsClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|arquillian
operator|.
name|junit
operator|.
name|Arquillian
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|arquillian
operator|.
name|test
operator|.
name|api
operator|.
name|ArquillianResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|Archive
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|ShrinkWrap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|spec
operator|.
name|WebArchive
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
import|import static
name|com
operator|.
name|jayway
operator|.
name|restassured
operator|.
name|RestAssured
operator|.
name|given
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|equalTo
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|Arquillian
operator|.
name|class
argument_list|)
DECL|class|ServletAsyncArquillianTest
specifier|public
class|class
name|ServletAsyncArquillianTest
block|{
annotation|@
name|Deployment
DECL|method|createTestArchive ()
specifier|public
specifier|static
name|Archive
argument_list|<
name|?
argument_list|>
name|createTestArchive
parameter_list|()
block|{
comment|// this is a WAR project so use WebArchive
return|return
name|ShrinkWrap
operator|.
name|create
argument_list|(
name|WebArchive
operator|.
name|class
argument_list|)
comment|// add the web.xml
operator|.
name|setWebXML
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"src/test/resources/org/apache/camel/component/servlet/web-spring-async.xml"
argument_list|)
operator|.
name|toFile
argument_list|()
argument_list|)
return|;
block|}
comment|/**      *      * @param url the URL is the URL to the web application that was deployed      * @throws Exception      */
annotation|@
name|Test
annotation|@
name|RunAsClient
DECL|method|testHello (@rquillianResource URL url)
specifier|public
name|void
name|testHello
parameter_list|(
annotation|@
name|ArquillianResource
name|URL
name|url
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|String
name|name
init|=
literal|"Arnaud"
decl_stmt|;
name|given
argument_list|()
operator|.
name|baseUri
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|queryParam
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
operator|.
name|when
argument_list|()
operator|.
name|get
argument_list|(
literal|"/services/hello"
argument_list|)
operator|.
name|then
argument_list|()
operator|.
name|body
argument_list|(
name|equalTo
argument_list|(
name|MessageFormat
operator|.
name|format
argument_list|(
literal|"Hello {0} how are you?"
argument_list|,
name|name
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

