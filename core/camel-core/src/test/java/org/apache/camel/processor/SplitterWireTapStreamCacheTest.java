begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|engine
operator|.
name|DefaultStreamCachingStrategy
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
name|spi
operator|.
name|StreamCachingStrategy
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

begin_class
DECL|class|SplitterWireTapStreamCacheTest
specifier|public
class|class
name|SplitterWireTapStreamCacheTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|startEnd
specifier|private
name|MockEndpoint
name|startEnd
decl_stmt|;
DECL|field|splitEnd
specifier|private
name|MockEndpoint
name|splitEnd
decl_stmt|;
DECL|field|wiretapEnd
specifier|private
name|MockEndpoint
name|wiretapEnd
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|startEnd
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:startEnd"
argument_list|)
expr_stmt|;
name|splitEnd
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:splitEnd"
argument_list|)
expr_stmt|;
name|wiretapEnd
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:wireTapEnd"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWireTapAfterSplitDeletesStreamCacheFileWhenSplitFinishes ()
specifier|public
name|void
name|testWireTapAfterSplitDeletesStreamCacheFileWhenSplitFinishes
parameter_list|()
throws|throws
name|Exception
block|{
name|startEnd
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|splitEnd
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|wiretapEnd
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// test.txt should contain more than one character
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"text"
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
name|StreamCachingStrategy
name|streamCachingStrategy
init|=
operator|new
name|DefaultStreamCachingStrategy
argument_list|()
decl_stmt|;
name|streamCachingStrategy
operator|.
name|setSpoolThreshold
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
name|context
operator|.
name|setStreamCachingStrategy
argument_list|(
name|streamCachingStrategy
argument_list|)
expr_stmt|;
name|context
operator|.
name|setStreamCaching
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|split
argument_list|(
name|bodyAs
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|tokenize
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:split"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:startEnd"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:split"
argument_list|)
operator|.
name|wireTap
argument_list|(
literal|"direct:wireTap"
argument_list|)
comment|// wait for the streamcache to be created in the wireTap
comment|// route
operator|.
name|delay
argument_list|(
literal|1000
argument_list|)
comment|// spool file is deleted when this route ends
operator|.
name|to
argument_list|(
literal|"mock:splitEnd"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:wireTap"
argument_list|)
comment|// create streamcache
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/log4j2.properties"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|delay
argument_list|(
literal|3000
argument_list|)
comment|// spool file is deleted by the split route
operator|.
name|to
argument_list|(
literal|"mock:wireTapEnd"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

