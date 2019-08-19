begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|jetty
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
name|test
operator|.
name|AvailablePortFinder
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
name|junit4
operator|.
name|CamelTestSupport
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|JettyXsltHttpTemplateTest
specifier|public
class|class
name|JettyXsltHttpTemplateTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
annotation|@
name|Test
DECL|method|testXsltHttpTemplate ()
specifier|public
name|void
name|testXsltHttpTemplate
parameter_list|()
throws|throws
name|Exception
block|{
comment|// give Jetty a bit time to startup and be ready
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|String
name|xml
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"xslt:http://0.0.0.0:"
operator|+
name|port
operator|+
literal|"/myxslt"
argument_list|,
literal|"<mail><subject>Hey</subject><body>Hello world!</body></mail>"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The transformed XML should not be null"
argument_list|,
name|xml
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|indexOf
argument_list|(
literal|"transformed"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
comment|// the cheese tag is in the transform.xsl
name|assertTrue
argument_list|(
name|xml
operator|.
name|indexOf
argument_list|(
literal|"cheese"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|indexOf
argument_list|(
literal|"<subject>Hey</subject>"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|indexOf
argument_list|(
literal|"<body>Hello world!</body>"
argument_list|)
operator|>
operator|-
literal|1
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
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
expr_stmt|;
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
literal|"jetty:http://0.0.0.0:"
operator|+
name|port
operator|+
literal|"/myxslt"
argument_list|)
operator|.
name|pollEnrich
argument_list|(
literal|"file://src/test/resources/org/apache/camel/itest/jetty/?fileName=transform.xsl&noop=true&readLock=none"
argument_list|,
literal|2000
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:transform"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

