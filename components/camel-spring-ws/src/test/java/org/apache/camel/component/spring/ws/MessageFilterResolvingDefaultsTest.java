begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.ws
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|ws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|net
operator|.
name|javacrumbs
operator|.
name|smock
operator|.
name|springws
operator|.
name|client
operator|.
name|AbstractSmockClientTest
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
name|junit
operator|.
name|After
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
name|context
operator|.
name|ApplicationContext
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
name|SpringJUnit4ClassRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|WebServiceMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|test
operator|.
name|client
operator|.
name|RequestMatcher
import|;
end_import

begin_comment
comment|/**  * Check if the MessageFilter is used and resolved from endpoint uri or global  * context configuration.  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|SpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"classpath:org/apache/camel/component/spring/ws/DefaultMessageFilter-context.xml"
block|}
argument_list|)
DECL|class|MessageFilterResolvingDefaultsTest
specifier|public
class|class
name|MessageFilterResolvingDefaultsTest
extends|extends
name|AbstractSmockClientTest
block|{
annotation|@
name|Autowired
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
DECL|field|body
specifier|private
name|String
name|body
init|=
literal|"<customerCountRequest xmlns='http://springframework.org/spring-ws'>"
operator|+
literal|"<customerName>John Doe</customerName>"
operator|+
literal|"</customerCountRequest>"
decl_stmt|;
annotation|@
name|Test
DECL|method|isUsedDefaultFilter ()
specifier|public
name|void
name|isUsedDefaultFilter
parameter_list|()
block|{
name|expect
argument_list|(
name|soapHeader
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://newHeaderSupport/"
argument_list|,
literal|"testHeaderValue1"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andExpect
argument_list|(
name|doesntContains
argument_list|(
name|soapHeader
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://virtualCheck/"
argument_list|,
literal|"localFilter"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andExpect
argument_list|(
name|doesntContains
argument_list|(
name|soapHeader
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://virtualCheck/"
argument_list|,
literal|"globalFilter"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:sendDefault"
argument_list|,
name|body
argument_list|,
literal|"headerKey"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://newHeaderSupport/"
argument_list|,
literal|"testHeaderValue1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|doesntContains (final RequestMatcher soapHeader)
specifier|private
name|RequestMatcher
name|doesntContains
parameter_list|(
specifier|final
name|RequestMatcher
name|soapHeader
parameter_list|)
block|{
return|return
operator|new
name|RequestMatcher
argument_list|()
block|{
specifier|public
name|void
name|match
parameter_list|(
name|URI
name|uri
parameter_list|,
name|WebServiceMessage
name|request
parameter_list|)
throws|throws
name|IOException
throws|,
name|AssertionError
block|{
try|try
block|{
name|soapHeader
operator|.
name|match
argument_list|(
name|uri
argument_list|,
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionError
name|e
parameter_list|)
block|{
comment|// ok
return|return;
block|}
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"Should failed!"
argument_list|)
throw|;
block|}
block|}
return|;
block|}
annotation|@
name|Autowired
DECL|method|setApplicationContext (ApplicationContext applicationContext)
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
block|{
name|createServer
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|verify ()
specifier|public
name|void
name|verify
parameter_list|()
block|{
name|super
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

