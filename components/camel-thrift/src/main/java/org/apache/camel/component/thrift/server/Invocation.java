begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.thrift.server
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|thrift
operator|.
name|server
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|thrift
operator|.
name|server
operator|.
name|AbstractNonblockingServer
operator|.
name|FrameBuffer
import|;
end_import

begin_comment
comment|/**  * Copy of the org.apache.thrift.server.Invocation  */
end_comment

begin_class
DECL|class|Invocation
class|class
name|Invocation
implements|implements
name|Runnable
block|{
DECL|field|frameBuffer
specifier|private
specifier|final
name|FrameBuffer
name|frameBuffer
decl_stmt|;
DECL|method|Invocation (final FrameBuffer frameBuffer)
specifier|public
name|Invocation
parameter_list|(
specifier|final
name|FrameBuffer
name|frameBuffer
parameter_list|)
block|{
name|this
operator|.
name|frameBuffer
operator|=
name|frameBuffer
expr_stmt|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|frameBuffer
operator|.
name|invoke
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

