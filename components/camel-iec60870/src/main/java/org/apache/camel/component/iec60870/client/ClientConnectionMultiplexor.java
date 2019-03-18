begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.iec60870.client
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|iec60870
operator|.
name|client
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
name|component
operator|.
name|iec60870
operator|.
name|AbstractConnectionMultiplexor
import|;
end_import

begin_class
DECL|class|ClientConnectionMultiplexor
specifier|public
class|class
name|ClientConnectionMultiplexor
extends|extends
name|AbstractConnectionMultiplexor
block|{
DECL|field|connection
specifier|private
specifier|final
name|ClientConnection
name|connection
decl_stmt|;
DECL|method|ClientConnectionMultiplexor (final ClientConnection connection)
specifier|public
name|ClientConnectionMultiplexor
parameter_list|(
specifier|final
name|ClientConnection
name|connection
parameter_list|)
block|{
name|this
operator|.
name|connection
operator|=
name|connection
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|performStart ()
specifier|protected
name|void
name|performStart
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|connection
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|performStop ()
specifier|protected
name|void
name|performStop
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|connection
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|getConnection ()
specifier|public
name|ClientConnection
name|getConnection
parameter_list|()
block|{
return|return
name|this
operator|.
name|connection
return|;
block|}
block|}
end_class

end_unit

