begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xslt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xslt
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
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|EntityResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
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
name|impl
operator|.
name|JndiRegistry
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
DECL|class|XsltCustomizeEntityResolverTest
specifier|public
class|class
name|XsltCustomizeEntityResolverTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|EXPECTED_XML_CONSTANT
specifier|private
specifier|static
specifier|final
name|String
name|EXPECTED_XML_CONSTANT
init|=
literal|"<A>1</A>"
decl_stmt|;
annotation|@
name|Test
DECL|method|testXsltCustomURIResolverDirectInRouteUri ()
specifier|public
name|void
name|testXsltCustomURIResolverDirectInRouteUri
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:resultURIResolverDirect"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|contains
argument_list|(
name|EXPECTED_XML_CONSTANT
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
name|from
argument_list|(
literal|"file:src/test/data/?fileName=xml_with_entity.xml&noop=true&initialDelay=0&delay=10"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xslt:xslt/common/copy.xsl?allowStAX=false&output=string&entityResolver=#customEntityResolver"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:resultURIResolverDirect"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|getCustomEntityResolver ()
specifier|private
name|EntityResolver
name|getCustomEntityResolver
parameter_list|()
block|{
return|return
operator|new
name|EntityResolver
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|InputSource
name|resolveEntity
parameter_list|(
name|String
name|publicId
parameter_list|,
name|String
name|systemId
parameter_list|)
throws|throws
name|SAXException
throws|,
name|IOException
block|{
return|return
operator|new
name|InputSource
argument_list|(
operator|new
name|StringReader
argument_list|(
literal|"<!ELEMENT A (#PCDATA)>"
argument_list|)
argument_list|)
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|EntityResolver
name|customEntityResolver
init|=
name|getCustomEntityResolver
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"customEntityResolver"
argument_list|,
name|customEntityResolver
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
block|}
end_class

end_unit
