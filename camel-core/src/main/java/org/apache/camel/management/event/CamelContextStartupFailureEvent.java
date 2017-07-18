begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.event
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
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
name|CamelContext
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|CamelContextStartupFailureEvent
specifier|public
class|class
name|CamelContextStartupFailureEvent
extends|extends
name|AbstractContextEvent
implements|implements
name|FailureEvent
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|4271899927507894567L
decl_stmt|;
DECL|field|cause
specifier|private
name|Throwable
name|cause
decl_stmt|;
DECL|method|CamelContextStartupFailureEvent (CamelContext context, Throwable cause)
specifier|public
name|CamelContextStartupFailureEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|cause
operator|=
name|cause
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCause ()
specifier|public
name|Throwable
name|getCause
parameter_list|()
block|{
return|return
name|cause
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Failed to start Camel: "
operator|+
name|getContext
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" due to "
operator|+
name|cause
operator|.
name|getMessage
argument_list|()
return|;
block|}
block|}
end_class

end_unit

