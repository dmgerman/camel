begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
package|;
end_package

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
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|string
operator|.
name|StringDecoder
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|string
operator|.
name|StringEncoder
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
name|BindToRegistry
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|NettySingleCodecTest
specifier|public
class|class
name|NettySingleCodecTest
extends|extends
name|BaseNettyTest
block|{
annotation|@
name|BindToRegistry
argument_list|(
literal|"decoder"
argument_list|)
DECL|field|stringDecoder
specifier|private
name|StringDecoder
name|stringDecoder
init|=
operator|new
name|StringDecoder
argument_list|()
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"encoder"
argument_list|)
DECL|field|stringEncoder
specifier|private
name|StringEncoder
name|stringEncoder
init|=
operator|new
name|StringEncoder
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|canSupplySingleCodecToEndpointPipeline ()
specifier|public
name|void
name|canSupplySingleCodecToEndpointPipeline
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|poem
init|=
operator|new
name|Poetry
argument_list|()
operator|.
name|getPoem
argument_list|()
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:single-codec"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|poem
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:single-codec"
argument_list|,
name|poem
argument_list|)
expr_stmt|;
name|mock
operator|.
name|await
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:single-codec"
argument_list|)
operator|.
name|to
argument_list|(
literal|"netty4:tcp://localhost:{{port}}?encoders=#encoder&sync=false"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"netty4:tcp://localhost:{{port}}?decoders=#decoder&sync=false"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:single-codec"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

