begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.event
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|event
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
name|Exchange
import|;
end_import

begin_comment
comment|/**  * Base class for step events.  */
end_comment

begin_class
DECL|class|AbstractStepEvent
specifier|public
specifier|abstract
class|class
name|AbstractStepEvent
extends|extends
name|AbstractExchangeEvent
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|stepId
specifier|private
specifier|final
name|String
name|stepId
decl_stmt|;
DECL|method|AbstractStepEvent (Exchange source, String stepId)
specifier|public
name|AbstractStepEvent
parameter_list|(
name|Exchange
name|source
parameter_list|,
name|String
name|stepId
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|this
operator|.
name|stepId
operator|=
name|stepId
expr_stmt|;
block|}
DECL|method|getStepId ()
specifier|public
name|String
name|getStepId
parameter_list|()
block|{
return|return
name|stepId
return|;
block|}
block|}
end_class

end_unit

