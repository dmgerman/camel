begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|impl
operator|.
name|JndiRegistry
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
name|language
operator|.
name|ConstantExpression
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
name|language
operator|.
name|SimpleExpression
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
import|import static
name|org
operator|.
name|awaitility
operator|.
name|Awaitility
operator|.
name|await
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|XsltUriResolverFactoryTest
specifier|public
class|class
name|XsltUriResolverFactoryTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|registry
specifier|private
name|JndiRegistry
name|registry
decl_stmt|;
annotation|@
name|Test
DECL|method|testConfigurationOnEndpoint ()
specifier|public
name|void
name|testConfigurationOnEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointUri
init|=
literal|"xslt:xslt/staff/staff.xsl?uriResolverFactory=#uriResolverFactory"
decl_stmt|;
name|String
name|directStart
init|=
literal|"direct:start"
decl_stmt|;
comment|// ensure that the URI resolver factory is not set on the component by
comment|// the method "testConfigurationOnComponent"
name|registry
operator|.
name|getContext
argument_list|()
operator|.
name|unbind
argument_list|(
literal|"xslt"
argument_list|)
expr_stmt|;
name|execute
argument_list|(
name|endpointUri
argument_list|,
name|directStart
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConfigurationOnComponent ()
specifier|public
name|void
name|testConfigurationOnComponent
parameter_list|()
throws|throws
name|Exception
block|{
name|XsltComponent
name|xsltComponent
init|=
operator|new
name|XsltComponent
argument_list|()
decl_stmt|;
name|xsltComponent
operator|.
name|setUriResolverFactory
argument_list|(
operator|new
name|CustomXsltUriResolverFactory
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"xslt"
argument_list|,
name|xsltComponent
argument_list|)
expr_stmt|;
name|String
name|endpointUri
init|=
literal|"xslt:xslt/staff/staff.xsl"
decl_stmt|;
name|String
name|directStart
init|=
literal|"direct:startComponent"
decl_stmt|;
name|execute
argument_list|(
name|endpointUri
argument_list|,
name|directStart
argument_list|)
expr_stmt|;
block|}
DECL|method|execute (String endpointUri, String directStart)
name|void
name|execute
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|directStart
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|InputStream
name|payloud
init|=
name|XsltUriResolverFactoryTest
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"xslt/staff/staff.xml"
argument_list|)
decl_stmt|;
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
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
name|directStart
argument_list|,
name|payloud
argument_list|)
expr_stmt|;
comment|// wait until endpoint is resolved
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|until
argument_list|(
parameter_list|()
lambda|->
name|resolveMandatoryEndpoint
argument_list|(
name|endpointUri
argument_list|,
name|XsltEndpoint
operator|.
name|class
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|XsltEndpoint
name|xsltEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
name|endpointUri
argument_list|,
name|XsltEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|xsltEndpoint
argument_list|)
expr_stmt|;
name|CustomXsltUriResolver
name|resolver
init|=
operator|(
name|CustomXsltUriResolver
operator|)
name|xsltEndpoint
operator|.
name|getUriResolver
argument_list|()
decl_stmt|;
name|checkResourceUri
argument_list|(
name|resolver
operator|.
name|resolvedResourceUris
argument_list|,
literal|"xslt/staff/staff.xsl"
argument_list|)
expr_stmt|;
name|checkResourceUri
argument_list|(
name|resolver
operator|.
name|resolvedResourceUris
argument_list|,
literal|"../common/staff_template.xsl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilders ()
specifier|protected
name|RouteBuilder
index|[]
name|createRouteBuilders
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
index|[]
block|{
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
literal|"direct:start"
argument_list|)
comment|//
operator|.
name|setHeader
argument_list|(
literal|"xslt_file"
argument_list|,
operator|new
name|ConstantExpression
argument_list|(
literal|"xslt/staff/staff.xsl"
argument_list|)
argument_list|)
comment|//
operator|.
name|recipientList
argument_list|(
operator|new
name|SimpleExpression
argument_list|(
literal|"xslt:${header.xslt_file}?uriResolverFactory=#uriResolverFactory"
argument_list|)
argument_list|)
comment|//
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
operator|,
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
literal|"direct:startComponent"
argument_list|)
comment|//
operator|.
name|setHeader
argument_list|(
literal|"xslt_file"
argument_list|,
operator|new
name|ConstantExpression
argument_list|(
literal|"xslt/staff/staff.xsl"
argument_list|)
argument_list|)
comment|//
operator|.
name|recipientList
argument_list|(
operator|new
name|SimpleExpression
argument_list|(
literal|"xslt:${header.xslt_file}"
argument_list|)
argument_list|)
comment|//
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

begin_empty_stmt
empty_stmt|;
end_empty_stmt

begin_function
unit|}      @
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|registry
operator|=
name|super
operator|.
name|createRegistry
argument_list|()
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"uriResolverFactory"
argument_list|,
operator|new
name|CustomXsltUriResolverFactory
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
end_function

begin_function
DECL|method|checkResourceUri (Set<String> uris, String resourceUri)
name|void
name|checkResourceUri
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|uris
parameter_list|,
name|String
name|resourceUri
parameter_list|)
block|{
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Missing resource uri "
operator|+
name|resourceUri
operator|+
literal|" in resolved resource URI set"
argument_list|,
name|uris
operator|.
name|contains
argument_list|(
name|resourceUri
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_class
DECL|class|CustomXsltUriResolverFactory
specifier|static
class|class
name|CustomXsltUriResolverFactory
implements|implements
name|XsltUriResolverFactory
block|{
annotation|@
name|Override
DECL|method|createUriResolver (CamelContext camelContext, String resourceUri)
specifier|public
name|URIResolver
name|createUriResolver
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|resourceUri
parameter_list|)
block|{
return|return
operator|new
name|CustomXsltUriResolver
argument_list|(
name|camelContext
argument_list|,
name|resourceUri
argument_list|)
return|;
block|}
block|}
end_class

begin_class
DECL|class|CustomXsltUriResolver
specifier|static
class|class
name|CustomXsltUriResolver
extends|extends
name|XsltUriResolver
block|{
DECL|field|resolvedResourceUris
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|resolvedResourceUris
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|CustomXsltUriResolver (CamelContext context, String location)
name|CustomXsltUriResolver
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|location
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|location
argument_list|)
expr_stmt|;
block|}
DECL|method|resolve (String href, String base)
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
name|result
init|=
name|super
operator|.
name|resolve
argument_list|(
name|href
argument_list|,
name|base
argument_list|)
decl_stmt|;
name|resolvedResourceUris
operator|.
name|add
argument_list|(
name|href
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

unit|}
end_unit

