begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.xpath
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|xpath
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathFactory
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
name|test
operator|.
name|spring
operator|.
name|CamelSpringTestSupport
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
name|Before
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
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
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
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  * Tests that verify the usage of default settings in the XPath language by declaring a bean called xpath in the registry  */
end_comment

begin_class
DECL|class|XPathLanguageDefaultSettingsTest
specifier|public
class|class
name|XPathLanguageDefaultSettingsTest
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|KEY
specifier|private
specifier|static
specifier|final
name|String
name|KEY
init|=
name|XPathFactory
operator|.
name|DEFAULT_PROPERTY_NAME
operator|+
literal|":"
operator|+
literal|"http://java.sun.com/jaxp/xpath/dom"
decl_stmt|;
DECL|field|jvmAdequate
specifier|private
name|boolean
name|jvmAdequate
init|=
literal|true
decl_stmt|;
DECL|field|oldPropertyValue
specifier|private
name|String
name|oldPropertyValue
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|isJavaVendor
argument_list|(
literal|"ibm"
argument_list|)
condition|)
block|{
comment|// Force using the JAXP default implementation, because having Saxon in the classpath will automatically make JAXP use it
comment|// because of Service Provider discovery (this does not happen in OSGi because the META-INF/services package is not exported
name|oldPropertyValue
operator|=
name|System
operator|.
name|setProperty
argument_list|(
name|KEY
argument_list|,
literal|"com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|jvmAdequate
operator|=
literal|false
expr_stmt|;
block|}
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|oldPropertyValue
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|KEY
argument_list|,
name|oldPropertyValue
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|clearProperty
argument_list|(
name|KEY
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/language/xpath/XPathLanguageDefaultSettingsTest.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testSpringDSLXPathLanguageDefaultSettings ()
specifier|public
name|void
name|testSpringDSLXPathLanguageDefaultSettings
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|jvmAdequate
condition|)
block|{
return|return;
block|}
name|MockEndpoint
name|mockEndpointResult
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:testDefaultXPathSettingsResult"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mockEndpointException
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:testDefaultXPathSettingsResultException"
argument_list|)
decl_stmt|;
name|mockEndpointResult
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockEndpointException
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:testDefaultXPathSettings"
argument_list|,
literal|"<a>Hello|there|Camel</a>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

