begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cdi.rest.servlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cdi
operator|.
name|rest
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|IOHelper
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
name|ArchivePaths
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
name|asset
operator|.
name|EmptyAsset
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
name|org
operator|.
name|hamcrest
operator|.
name|MatcherAssert
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|equalTo
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|is
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|startsWith
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
DECL|class|CdiRestServletTest
specifier|public
class|class
name|CdiRestServletTest
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
return|return
name|ShrinkWrap
operator|.
name|create
argument_list|(
name|WebArchive
operator|.
name|class
argument_list|)
operator|.
name|addClass
argument_list|(
name|Application
operator|.
name|class
argument_list|)
operator|.
name|addAsWebInfResource
argument_list|(
name|EmptyAsset
operator|.
name|INSTANCE
argument_list|,
name|ArchivePaths
operator|.
name|create
argument_list|(
literal|"beans.xml"
argument_list|)
argument_list|)
operator|.
name|setWebXML
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"src/main/webapp/WEB-INF/web.xml"
argument_list|)
operator|.
name|toFile
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Test
annotation|@
name|RunAsClient
DECL|method|testWithPath (@rquillianResource URL url)
specifier|public
name|void
name|testWithPath
parameter_list|(
annotation|@
name|ArquillianResource
name|URL
name|url
parameter_list|)
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|IOHelper
operator|.
name|loadText
argument_list|(
operator|new
name|URL
argument_list|(
name|url
argument_list|,
literal|"camel/say/hello"
argument_list|)
operator|.
name|openStream
argument_list|()
argument_list|)
argument_list|,
name|is
argument_list|(
name|equalTo
argument_list|(
literal|"Hello World!\n"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|RunAsClient
DECL|method|testWithUriTemplate (@rquillianResource URL url)
specifier|public
name|void
name|testWithUriTemplate
parameter_list|(
annotation|@
name|ArquillianResource
name|URL
name|url
parameter_list|)
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|IOHelper
operator|.
name|loadText
argument_list|(
operator|new
name|URL
argument_list|(
name|url
argument_list|,
literal|"camel/say/hello/Antonin"
argument_list|)
operator|.
name|openStream
argument_list|()
argument_list|)
argument_list|,
name|is
argument_list|(
name|startsWith
argument_list|(
literal|"Hello Antonin"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

