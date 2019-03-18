begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
package|;
end_package

begin_comment
comment|/**  * Options to configure an Undertow host.  */
end_comment

begin_class
DECL|class|UndertowHostOptions
specifier|public
specifier|final
class|class
name|UndertowHostOptions
block|{
comment|/**      * The number of worker threads to use in a Undertow host.      */
DECL|field|workerThreads
specifier|private
name|Integer
name|workerThreads
decl_stmt|;
comment|/**      * The number of io threads to use in a Undertow host.      */
DECL|field|ioThreads
specifier|private
name|Integer
name|ioThreads
decl_stmt|;
comment|/**      * The buffer size of the Undertow host.      */
DECL|field|bufferSize
specifier|private
name|Integer
name|bufferSize
decl_stmt|;
comment|/**      * Set if the Undertow host should use direct buffers.      */
DECL|field|directBuffers
specifier|private
name|Boolean
name|directBuffers
decl_stmt|;
comment|/**      * Set if the Undertow host should use http2 protocol.      */
DECL|field|http2Enabled
specifier|private
name|Boolean
name|http2Enabled
decl_stmt|;
DECL|method|UndertowHostOptions ()
specifier|public
name|UndertowHostOptions
parameter_list|()
block|{     }
DECL|method|getWorkerThreads ()
specifier|public
name|Integer
name|getWorkerThreads
parameter_list|()
block|{
return|return
name|workerThreads
return|;
block|}
DECL|method|setWorkerThreads (Integer workerThreads)
specifier|public
name|void
name|setWorkerThreads
parameter_list|(
name|Integer
name|workerThreads
parameter_list|)
block|{
name|this
operator|.
name|workerThreads
operator|=
name|workerThreads
expr_stmt|;
block|}
DECL|method|getIoThreads ()
specifier|public
name|Integer
name|getIoThreads
parameter_list|()
block|{
return|return
name|ioThreads
return|;
block|}
DECL|method|setIoThreads (Integer ioThreads)
specifier|public
name|void
name|setIoThreads
parameter_list|(
name|Integer
name|ioThreads
parameter_list|)
block|{
name|this
operator|.
name|ioThreads
operator|=
name|ioThreads
expr_stmt|;
block|}
DECL|method|getBufferSize ()
specifier|public
name|Integer
name|getBufferSize
parameter_list|()
block|{
return|return
name|bufferSize
return|;
block|}
DECL|method|setBufferSize (Integer bufferSize)
specifier|public
name|void
name|setBufferSize
parameter_list|(
name|Integer
name|bufferSize
parameter_list|)
block|{
name|this
operator|.
name|bufferSize
operator|=
name|bufferSize
expr_stmt|;
block|}
DECL|method|getDirectBuffers ()
specifier|public
name|Boolean
name|getDirectBuffers
parameter_list|()
block|{
return|return
name|directBuffers
return|;
block|}
DECL|method|setDirectBuffers (Boolean directBuffers)
specifier|public
name|void
name|setDirectBuffers
parameter_list|(
name|Boolean
name|directBuffers
parameter_list|)
block|{
name|this
operator|.
name|directBuffers
operator|=
name|directBuffers
expr_stmt|;
block|}
DECL|method|getHttp2Enabled ()
specifier|public
name|Boolean
name|getHttp2Enabled
parameter_list|()
block|{
return|return
name|http2Enabled
return|;
block|}
DECL|method|setHttp2Enabled (Boolean http2Enabled)
specifier|public
name|void
name|setHttp2Enabled
parameter_list|(
name|Boolean
name|http2Enabled
parameter_list|)
block|{
name|this
operator|.
name|http2Enabled
operator|=
name|http2Enabled
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"UndertowHostOptions{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"workerThreads="
argument_list|)
operator|.
name|append
argument_list|(
name|workerThreads
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", ioThreads="
argument_list|)
operator|.
name|append
argument_list|(
name|ioThreads
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", bufferSize="
argument_list|)
operator|.
name|append
argument_list|(
name|bufferSize
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", directBuffers="
argument_list|)
operator|.
name|append
argument_list|(
name|directBuffers
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", http2Enabled="
argument_list|)
operator|.
name|append
argument_list|(
name|http2Enabled
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

