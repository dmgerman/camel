begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.resequencer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|resequencer
package|;
end_package

begin_class
DECL|class|ResequencerRunner
specifier|public
class|class
name|ResequencerRunner
parameter_list|<
name|E
parameter_list|>
extends|extends
name|Thread
block|{
DECL|field|resequencer
specifier|private
name|ResequencerEngineSync
argument_list|<
name|E
argument_list|>
name|resequencer
decl_stmt|;
DECL|field|interval
specifier|private
name|long
name|interval
decl_stmt|;
DECL|field|cancelRequested
specifier|private
name|boolean
name|cancelRequested
decl_stmt|;
DECL|method|ResequencerRunner (ResequencerEngineSync<E> resequencer, long interval)
specifier|public
name|ResequencerRunner
parameter_list|(
name|ResequencerEngineSync
argument_list|<
name|E
argument_list|>
name|resequencer
parameter_list|,
name|long
name|interval
parameter_list|)
block|{
name|this
operator|.
name|resequencer
operator|=
name|resequencer
expr_stmt|;
name|this
operator|.
name|interval
operator|=
name|interval
expr_stmt|;
name|this
operator|.
name|cancelRequested
operator|=
literal|false
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
while|while
condition|(
operator|!
name|cancelRequested
argument_list|()
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|interval
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|resequencer
operator|.
name|deliver
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
name|super
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
DECL|method|cancel ()
specifier|public
specifier|synchronized
name|void
name|cancel
parameter_list|()
block|{
name|this
operator|.
name|cancelRequested
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|cancelRequested ()
specifier|private
specifier|synchronized
name|boolean
name|cancelRequested
parameter_list|()
block|{
return|return
name|cancelRequested
return|;
block|}
block|}
end_class

end_unit

