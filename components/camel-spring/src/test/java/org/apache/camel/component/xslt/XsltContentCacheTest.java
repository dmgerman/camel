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
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_comment
comment|/**  * Unit test hot reloading of XSL files (CAMEL-2737).  */
end_comment

begin_class
DECL|class|XsltContentCacheTest
specifier|public
class|class
name|XsltContentCacheTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|ORIGINAL_XSL
specifier|private
specifier|static
specifier|final
name|String
name|ORIGINAL_XSL
init|=
literal|"<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">"
operator|+
literal|"<xsl:template match=\"/\"><goodbye><xsl:value-of select=\"/hello\"/></goodbye></xsl:template>"
operator|+
literal|"</xsl:stylesheet>"
decl_stmt|;
DECL|field|NEW_XSL
specifier|private
specifier|static
specifier|final
name|String
name|NEW_XSL
init|=
literal|"<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">"
operator|+
literal|"<xsl:template match=\"/\"><goodnight><xsl:value-of select=\"/hello\"/></goodnight></xsl:template>"
operator|+
literal|"</xsl:stylesheet>"
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
comment|// create file with original XSL transformation
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/test-classes/org/apache/camel/component/xslt?fileExist=Override"
argument_list|,
name|ORIGINAL_XSL
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.xsl"
argument_list|)
expr_stmt|;
comment|// start the context AFTER we've created the hello.xsl file, otherwise the xslt routes will fail
name|super
operator|.
name|startCamelContext
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|startCamelContext ()
specifier|protected
name|void
name|startCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Override so we can start the context ourself in the setUp
block|}
DECL|method|testNotCached ()
specifier|public
name|void
name|testNotCached
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><goodbye>world!</goodbye>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// now replace the file with a new XSL transformation
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/test-classes/org/apache/camel/component/xslt?fileExist=Override"
argument_list|,
name|NEW_XSL
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.xsl"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// we expect the new output as the cache is not enabled, so it's "goodnight" and not "goodbye"
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><goodnight>world!</goodnight>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testCached ()
specifier|public
name|void
name|testCached
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><goodbye>world!</goodbye>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:b"
argument_list|,
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// now replace the file with a new XSL transformation
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/test-classes/org/apache/camel/component/xslt?fileExist=Override"
argument_list|,
name|NEW_XSL
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.xsl"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// we expect the original output as the cache is enabled, so it's "goodbye" and not "goodnight"
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><goodbye>world!</goodbye>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:b"
argument_list|,
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testCachedIsDefault ()
specifier|public
name|void
name|testCachedIsDefault
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><goodbye>world!</goodbye>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:c"
argument_list|,
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// now replace the file with a new XSL transformation
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/test-classes/org/apache/camel/component/xslt?fileExist=Override"
argument_list|,
name|NEW_XSL
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.xsl"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// we expect the original output as the cache is enabled, so it's "goodbye" and not "goodnight"
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><goodbye>world!</goodbye>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:c"
argument_list|,
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xslt://org/apache/camel/component/xslt/hello.xsl?output=string&contentCache=false"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:b"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xslt://org/apache/camel/component/xslt/hello.xsl?output=string&contentCache=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:c"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xslt://org/apache/camel/component/xslt/hello.xsl?output=string"
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
block|}
end_class

end_unit

