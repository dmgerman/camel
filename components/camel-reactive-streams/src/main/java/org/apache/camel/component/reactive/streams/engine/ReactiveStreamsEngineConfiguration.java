begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|engine
package|;
end_package

begin_comment
comment|/**  * Configuration parameters for the Camel internal reactive-streams engine.  */
end_comment

begin_class
DECL|class|ReactiveStreamsEngineConfiguration
specifier|public
class|class
name|ReactiveStreamsEngineConfiguration
implements|implements
name|Cloneable
block|{
DECL|field|threadPoolName
specifier|private
name|String
name|threadPoolName
init|=
literal|"CamelReactiveStreamsWorker"
decl_stmt|;
DECL|field|threadPoolMinSize
specifier|private
name|int
name|threadPoolMinSize
decl_stmt|;
DECL|field|threadPoolMaxSize
specifier|private
name|int
name|threadPoolMaxSize
init|=
literal|10
decl_stmt|;
DECL|method|ReactiveStreamsEngineConfiguration ()
specifier|public
name|ReactiveStreamsEngineConfiguration
parameter_list|()
block|{     }
DECL|method|getThreadPoolName ()
specifier|public
name|String
name|getThreadPoolName
parameter_list|()
block|{
return|return
name|threadPoolName
return|;
block|}
comment|/**      * The name of the thread pool used by the reactive streams internal engine.      */
DECL|method|setThreadPoolName (String threadPoolName)
specifier|public
name|void
name|setThreadPoolName
parameter_list|(
name|String
name|threadPoolName
parameter_list|)
block|{
name|this
operator|.
name|threadPoolName
operator|=
name|threadPoolName
expr_stmt|;
block|}
DECL|method|getThreadPoolMinSize ()
specifier|public
name|int
name|getThreadPoolMinSize
parameter_list|()
block|{
return|return
name|threadPoolMinSize
return|;
block|}
comment|/**      * The minimum number of threads used by the reactive streams internal engine.      */
DECL|method|setThreadPoolMinSize (int threadPoolMinSize)
specifier|public
name|void
name|setThreadPoolMinSize
parameter_list|(
name|int
name|threadPoolMinSize
parameter_list|)
block|{
name|this
operator|.
name|threadPoolMinSize
operator|=
name|threadPoolMinSize
expr_stmt|;
block|}
DECL|method|getThreadPoolMaxSize ()
specifier|public
name|int
name|getThreadPoolMaxSize
parameter_list|()
block|{
return|return
name|threadPoolMaxSize
return|;
block|}
comment|/**      * The maximum number of threads used by the reactive streams internal engine.      */
DECL|method|setThreadPoolMaxSize (int threadPoolMaxSize)
specifier|public
name|void
name|setThreadPoolMaxSize
parameter_list|(
name|int
name|threadPoolMaxSize
parameter_list|)
block|{
name|this
operator|.
name|threadPoolMaxSize
operator|=
name|threadPoolMaxSize
expr_stmt|;
block|}
block|}
end_class

end_unit

