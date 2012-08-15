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
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|URIResolver
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|XsltCustomizeURIResolverTest
specifier|public
class|class
name|XsltCustomizeURIResolverTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|XsltCustomizeURIResolverTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|EXPECTED_XML_CONSTANT
specifier|private
specifier|final
name|String
name|EXPECTED_XML_CONSTANT
init|=
literal|"<data>FOO DATA</data>"
decl_stmt|;
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
literal|"file:src/test/data/?fileName=staff.xml&noop=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xslt:org/apache/camel/component/xslt/include_not_existing_resource.xsl?uriResolver=#customURIResolver"
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
DECL|method|getCustomURIResolver ()
specifier|private
name|URIResolver
name|getCustomURIResolver
parameter_list|()
block|{
return|return
operator|new
name|URIResolver
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Source
name|resolve
parameter_list|(
name|String
name|href
parameter_list|,
name|String
name|base
parameter_list|)
throws|throws
name|TransformerException
block|{
name|Source
name|constantResult
init|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|EXPECTED_XML_CONSTANT
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|constantResult
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
name|URIResolver
name|customURIResolver
init|=
name|getCustomURIResolver
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"customURIResolver"
argument_list|,
name|customURIResolver
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
block|}
end_class

end_unit

